package org.kharisov.in.servlets.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kharisov.configs.UserContextHolder;
import org.kharisov.dtos.in.*;
import org.kharisov.domains.*;
import org.kharisov.enums.Role;
import org.kharisov.mappers.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.utils.*;
import org.kharisov.validators.DtoInValidator;

import java.io.IOException;
import java.util.*;

/**
 * Сервлет AdminServlet обрабатывает запросы по пути "/api/v1/admin/*".
 * Этот сервлет наследуется от HttpServlet и предоставляет функциональность администратора.
 *
 * <p>Сервлет использует следующие сервисы:</p>
 * <ul>
 *   <li>AuthService: Сервис аутентификации.</li>
 *   <li>AuditService: Сервис аудита.</li>
 *   <li>ReadingTypeService: Сервис типов показаний.</li>
 *   <li>ReadingService: Сервис показаний.</li>
 * </ul>
 *
 * @see HttpServlet
 */
@WebServlet("/api/v1/admin/*")
public class AdminServlet extends HttpServlet {

    private AuthService authService;
    private AuditService auditService;
    private ReadingTypeService readingTypeService;
    private ReadingService readingService;

    /**
     * Инициализирует сервлет и получает сервисы из контекста сервлета.
     */
    @Override
    public void init() {
        authService = (AuthService) getServletContext().getAttribute("authService");
        auditService = (AuditService) getServletContext().getAttribute("auditService");
        readingTypeService = (ReadingTypeService) getServletContext().getAttribute("readingTypeService");
        readingService = (ReadingService) getServletContext().getAttribute("readingService");
    }

    /**
     * Обрабатывает GET-запросы. Выполняет различные действия в зависимости от роли пользователя и пути запроса.
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

        if (Objects.equals(user.getRole().toString(), "ADMIN")) {
            if (pathInfo.equals("/audit")) {
                Map<String, List<String>> allAudits = auditService.getAllEntries();
                ResponseUtils.sendSuccessResponse(resp, allAudits);
            } else if (pathInfo.equals("/readings")) {
                Map<String, List<ReadingRecord>> allReadings = readingService.getAllReadings();
                ResponseUtils.sendSuccessResponse(resp, allReadings);
            } else {
                ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            }
        } else {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }

    /**
     * Обрабатывает POST-запросы. Выполняет различные действия в зависимости от роли пользователя и пути запроса.
     *
     * @param req  объект HttpServletRequest, который содержит запрос клиента
     * @param resp объект HttpServletResponse, который содержит ответ сервера
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jwt = AuthUtils.extractJwtFromRequest(req);
        String subject = AuthUtils.getSubjectFromJwt(jwt);
        Role role = Role.valueOf(AuthUtils.getRoleFromJwt(jwt));

        User user = User.builder()
                .accountNum(subject)
                .role(role)
                .build();
        UserContextHolder.setUser(user);

        String pathInfo = req.getPathInfo();

        if (Objects.equals(user.getRole().toString(), "ADMIN")) {
            if (pathInfo.equals("/makeAdmin")) {
                AdminDtoIn adminDto = DtoUtils.parseDtoFromRequest(req, AdminDtoIn.class);
                if (!DtoInValidator.isValid(adminDto).isEmpty()) {
                    ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
                    return;
                }
                User newAdmin = AdminMapper.INSTANCE.toEntity(adminDto);
                boolean isSuccess = authService.changeUserRole(newAdmin, Role.ADMIN);
                if (isSuccess) {
                    ResponseUtils.sendSuccessResponse(resp, "User promoted to admin successfully");
                } else {
                    ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to promote user");
                }
            } else if (pathInfo.equals("/addReadingType")) {
                ReadingTypeDtoIn readingTypeDto = DtoUtils.parseDtoFromRequest(req, ReadingTypeDtoIn.class);
                if (!DtoInValidator.isValid(readingTypeDto).isEmpty()) {
                    ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
                    return;
                }
                ReadingType readingType = ReadingTypeMapper.INSTANCE.toEntity(readingTypeDto);
                boolean isSuccess = readingTypeService.addReadingType(readingType.getValue());
                if (isSuccess) {
                    ResponseUtils.sendSuccessResponse(resp, "Reading type added successfully");
                } else {
                    ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add reading type");
                }
            } else {
                ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid request data");
            }
        } else {
            ResponseUtils.sendErrorResponse(resp, HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }
}

