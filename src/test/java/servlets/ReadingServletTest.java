package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.kharisov.domains.*;
import org.kharisov.enums.Role;
import org.kharisov.in.servlets.reading.ReadingServlet;
import org.kharisov.services.interfaces.*;
import org.kharisov.services.singletons.ReadingTypeServiceSingleton;
import org.kharisov.utils.AuthUtils;
import org.mockito.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReadingServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ReadingService readingService;

    @Mock
    private ReadingTypeService readingTypeService;

    @Mock
    private ServletContext servletContext;

    private ReadingServlet readingServlet;

    private ByteArrayOutputStream outputStream;

    private ServletOutputStream servletOutputStream;

    private ReadingRecord mockReadingRecord;

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        readingServlet = new ReadingServlet();

        User user = User.builder()
                .accountNum("0000000000000000")
                .role(Role.USER)
                .build();

        mockReadingRecord = ReadingRecord.builder()
                .type(ReadingType.Create("Gas"))
                .value(10)
                .accountNum("0000000000000000")
                .date(LocalDate.now())
                .build();

        Map<String, String> tokens = AuthUtils.createJwtForUser(user);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokens.get("accessToken"));

        ServletConfig mockConfig = mock(ServletConfig.class);
        when(mockConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("readingService")).thenReturn(readingService);
        when(servletContext.getAttribute("readingTypeService")).thenReturn(readingTypeService);
        when(readingTypeService.getReadingType(anyString())).thenReturn(Optional.of(new ReadingType("Gas")));
        ReadingTypeServiceSingleton.initialize(mock(ServletContext.class));
        ReadingTypeServiceSingleton.instance = readingTypeService;

        readingServlet.init(mockConfig);

        outputStream = new ByteArrayOutputStream();
        servletOutputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) {
                outputStream.write(b);
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        doAnswer(invocation -> {
            Integer statusCode = invocation.getArgument(0);
            when(response.getStatus()).thenReturn(statusCode);
            return null;
        }).when(response).setStatus(anyInt());
    }

    @Test
    @DisplayName("Проверка doGet без параметров")
    public void testDoGetWithNothing() throws Exception {
        when(readingService.getCurrentReading(any(User.class), any(ReadingType.class)))
                .thenReturn(Optional.of(mockReadingRecord));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        readingServlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Проверка doGet с типом показания")
    public void testDoGetWithType() throws Exception {

        when(request.getParameter("type")).thenReturn("Gas");

        when(readingService.getCurrentReading(any(User.class), any(ReadingType.class)))
                .thenReturn(Optional.of(mockReadingRecord));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        readingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Проверка doGet с месяцем и годом")
    public void testDoGetWithMonthAndYear() throws Exception {

        when(request.getParameter("month")).thenReturn("1");
        when(request.getParameter("year")).thenReturn("2022");

        when(readingService.getReadingsByMonth(any(User.class), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(mockReadingRecord));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        readingServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Проверка doGet с недействительным месяцем и годом")
    public void testDoGetWithInvalidMonthAndYear() throws Exception {

        when(request.getParameter("month")).thenReturn("invalid_month");
        when(request.getParameter("year")).thenReturn("invalid_year");

        when(response.getOutputStream()).thenReturn(servletOutputStream);

        readingServlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Invalid year or month. They should be numbers.");
    }

    @Test
    @DisplayName("Проверка doGet с недействительными данными запроса")
    public void testDoGetWithInvalidRequestData() throws Exception {
        when(request.getPathInfo()).thenReturn("/invalid");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        readingServlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Invalid request data");
    }

    @Test
    @DisplayName("Проверка doPost с действительным показанием")
    public void testDoPostWithValidReading() throws Exception {
        String readingDtoJson = "{\"type\":\"TYPE\",\"value\":100}";

        BufferedReader reader = new BufferedReader(new StringReader(readingDtoJson));
        when(request.getReader()).thenReturn(reader);

        when(readingService.readingExists(any(User.class), any(ReadingType.class), any(LocalDate.class))).thenReturn(false);

        when(readingService.addReading(any(User.class), any(ReadingType.class), anyInt())).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        readingServlet.doPost(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Reading saved successfully");
    }

    @Test
    @DisplayName("Проверка doPost с существующим показанием")
    public void testDoPostWithExistingReading() throws Exception {
        String readingDtoJson = "{\"type\":\"TYPE\",\"value\":100}";

        BufferedReader reader = new BufferedReader(new StringReader(readingDtoJson));
        when(request.getReader()).thenReturn(reader);

        when(readingService.readingExists(any(User.class), any(ReadingType.class), any(LocalDate.class))).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        readingServlet.doPost(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Reading already saved");
    }

}
