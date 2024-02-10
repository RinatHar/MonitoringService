package org.kharisov.in.servlets.reading;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kharisov.dtos.in.ReadingDtoIn;
import org.kharisov.entities.ReadingRecord;
import org.kharisov.entities.ReadingType;
import org.kharisov.entities.User;
import org.kharisov.enums.Role;
import org.kharisov.mappers.ReadingMapper;
import org.kharisov.services.interfaces.ReadingService;
import org.kharisov.utils.AuthUtils;
import org.kharisov.utils.DtoUtils;
import org.kharisov.utils.ResponseUtils;
import org.kharisov.validators.DtosInValidator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/readings/*")
public class ReadingServlet extends HttpServlet {

    private ReadingService readingService;

    @Override
    public void init() {
        readingService = (ReadingService) getServletContext().getAttribute("readingService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jwt = AuthUtils.extractJwtFromRequest(req);
        String subject = AuthUtils.getSubjectFromJwt(jwt);
        Role role = Role.valueOf(AuthUtils.getRoleFromJwt(jwt));

        User user = User.builder()
                .accountNum(subject)
                .role(role)
                .build();

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jwt = AuthUtils.extractJwtFromRequest(req);
        String subject = AuthUtils.getSubjectFromJwt(jwt);

        User user = User.builder()
                .accountNum(subject)
                .build();

        ReadingDtoIn readingDto = DtoUtils.parseDtoFromRequest(req, ReadingDtoIn.class);
        if (!DtosInValidator.isValid(readingDto).isEmpty()) {
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
