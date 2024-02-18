package org.kharisov.in.servlets.reading;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.configs.UserContextHolder;
import org.kharisov.domains.*;
import org.kharisov.dtos.in.ReadingDtoIn;
import org.kharisov.enums.Role;
import org.kharisov.mappers.ReadingMapper;
import org.kharisov.services.interfaces.ReadingService;
import org.kharisov.utils.*;
import org.kharisov.validators.DtoInValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Сервлет ReadingServlet обрабатывает запросы по пути "/api/v1/readings/*".
 * Этот сервлет наследуется от HttpServlet и предоставляет функциональность работы с показаниями.
 *
 * <p>Сервлет использует следующие сервисы:</p>
 * <ul>
 *   <li>ReadingService: Сервис показаний.</li>
 * </ul>
 *
 * @see HttpServlet
 */
@WebServlet("/api/v1/readings/*")
public class ReadingServlet extends HttpServlet {

    private ReadingService readingService;

    /**
     * Инициализирует сервлет и получает сервис чтения из контекста сервлета.
     */
    @Override
    public void init() {
        readingService = (ReadingService) getServletContext().getAttribute("readingService");
    }

    /**
     * Обрабатывает GET-запросы. Выполняет различные действия в зависимости от параметров запроса.
     *
     * @param req  объект HttpServletRequest, который содержит запрос клиента
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jwt = AuthUtils.extractJwtFromRequest(req);
        String subject = AuthUtils.getSubjectFromJwt(jwt);
        Role role = Role.valueOf(AuthUtils.getRoleFromJwt(jwt));

        User user = User.builder()
                .accountNum(subject)
                .role(role)
                .build();
        UserContextHolder.setUser(user);

        String pathInfo = req.getPathInfo();

        String type = req.getParameter("type");
        String year = req.getParameter("year");
        String month = req.getParameter("month");

        if (pathInfo == null) {
            if (type != null && year == null && month == null) {
                Optional<ReadingRecord> reading = readingService.getCurrentReading(user, ReadingType.Create(type));
                if (reading.isPresent()) {
                    ResponseUtils.sendSuccessResponse(resp, reading.get());
                } else {
                    ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "No readings.");
                }
            } else if (type == null && year != null && month != null) {
                int yearInt, monthInt;
                try {
                    yearInt = Integer.parseInt(year);
                    monthInt = Integer.parseInt(month);
                } catch (NumberFormatException e) {
                    ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid year or month. They should be numbers.");
                    return;
                }
                List<ReadingRecord> readings = readingService.getReadingsByMonth(user, monthInt, yearInt);
                ResponseUtils.sendSuccessResponse(resp, readings);
            } else {
                List<ReadingRecord> readings = readingService.getHistory(user);
                ResponseUtils.sendSuccessResponse(resp, readings);
            }
        } else {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
        }
    }

    /**
     * Обрабатывает POST-запросы. Проверяет валидность данных чтения,
     * преобразует DTO в запись чтения и пытается добавить чтение.
     * Если чтение успешно добавлено, отправляет ответ "Reading saved successfully".
     * В противном случае отправляет ошибку.
     *
     * @param req  объект HttpServletRequest, который содержит запрос клиента
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jwt = AuthUtils.extractJwtFromRequest(req);
        String subject = AuthUtils.getSubjectFromJwt(jwt);

        User user = User.builder()
                .accountNum(subject)
                .build();
        UserContextHolder.setUser(user);

        ReadingDtoIn readingDto = DtoUtils.parseDtoFromRequest(req, ReadingDtoIn.class);
        if (!DtoInValidator.isValid(readingDto).isEmpty()) {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            return;
        }
        ReadingRecord readingRecord = ReadingMapper.INSTANCE.toEntity(readingDto);

        if (!readingService.readingExists(user, readingRecord.getType(), LocalDate.now())) {
            boolean isSuccess = readingService.addReading(user, readingRecord.getType(), readingRecord.getValue());
            if (isSuccess) {
                ResponseUtils.sendSuccessResponse(resp, "Reading saved successfully");
            } else {
                ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save reading");
            }
        } else {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Reading already saved");
        }

    }
}
