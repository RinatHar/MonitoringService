package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.kharisov.domains.*;
import org.kharisov.enums.Role;
import org.kharisov.in.servlets.admin.AdminServlet;
import org.kharisov.services.interfaces.*;
import org.kharisov.utils.AuthUtils;
import org.mockito.*;

import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminServletTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ReadingService readingService;

    @Mock
    private AuditService auditService;

    @Mock
    private AuthService authService;

    @Mock
    private ReadingTypeService readingTypeService;

    @Mock
    private ServletContext servletContext;

    private AdminServlet adminServlet;

    private ByteArrayOutputStream outputStream;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        adminServlet = new AdminServlet();

        User user = User.builder()
                .accountNum("0000000000000000")
                .role(Role.ADMIN)
                .build();

        Map<String, String> tokens = AuthUtils.createJwtForUser(user);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokens.get("accessToken"));

        ServletConfig mockConfig = mock(ServletConfig.class);
        when(mockConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("readingService")).thenReturn(readingService);
        when(servletContext.getAttribute("auditService")).thenReturn(auditService);
        when(servletContext.getAttribute("authService")).thenReturn(authService);
        when(servletContext.getAttribute("readingTypeService")).thenReturn(readingTypeService);

        adminServlet.init(mockConfig);

        outputStream = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
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
    @DisplayName("Тестирование doGet с путем audit")
    public void testDoGetWithAuditPath() throws Exception {
        when(request.getPathInfo()).thenReturn("/audit");

        Map<String, List<String>> allAudits = new HashMap<>();
        when(auditService.getAllEntries()).thenReturn(allAudits);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains(objectMapper.writeValueAsString(allAudits));
    }

    @Test
    @DisplayName("Тестирование doGet с путем readings")
    public void testDoGetWithReadingsPath() throws Exception {
        when(request.getPathInfo()).thenReturn("/readings");

        Map<String, List<ReadingRecord>> allReadings = new HashMap<>();
        when(readingService.getAllReadings()).thenReturn(allReadings);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains(objectMapper.writeValueAsString(allReadings));
    }

    @Test
    @DisplayName("Тестирование doGet с недействительным путем")
    public void testDoGetWithInvalidPath() throws Exception {
        when(request.getPathInfo()).thenReturn("/invalid");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Invalid request data");
    }

    @Test
    @DisplayName("Тестирование doGet с ролью не администратора")
    public void testDoGetWithNonAdminRole() throws Exception {
        when(request.getPathInfo()).thenReturn("/readings");
        User user = User.builder()
                .accountNum("0000000000000000")
                .role(Role.USER)
                .build();

        Map<String, String> tokens = AuthUtils.createJwtForUser(user);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokens.get("accessToken"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doGet(request, response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);

        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Access denied");
    }

    @Test
    @DisplayName("Тестирование doPost с путем makeAdmin")
    public void testDoPostWithMakeAdminPath() throws Exception {
        // Мокайте request.getPathInfo()
        when(request.getPathInfo()).thenReturn("/makeAdmin");

        // Создайте JSON-строку, представляющую AdminDtoIn
        String adminDtoJson = "{\"accountNum\":\"0000000000000001\"}";

        // Мокайте request.getReader()
        BufferedReader reader = new BufferedReader(new StringReader(adminDtoJson));
        when(request.getReader()).thenReturn(reader);

        // Мокайте authService.changeUserRole()
        when(authService.changeUserRole(any(User.class), any(Role.class))).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doPost(request, response);

        // Проверьте, что статус ответа установлен в SC_OK
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

        // Проверьте, что содержимое ответа содержит ожидаемое сообщение
        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("User promoted to admin successfully");
    }

    @Test
    @DisplayName("Тестирование doPost с путем addReadingType")
    public void testDoPostWithAddReadingTypePath() throws Exception {
        // Мокайте request.getPathInfo()
        when(request.getPathInfo()).thenReturn("/addReadingType");

        // Создайте JSON-строку, представляющую ReadingTypeDtoIn
        String readingTypeDtoJson = "{\"name\":\"NewType\"}";

        // Мокайте request.getReader()
        BufferedReader reader = new BufferedReader(new StringReader(readingTypeDtoJson));
        when(request.getReader()).thenReturn(reader);

        // Мокайте readingTypeService.addReadingType()
        when(readingTypeService.addReadingType(anyString())).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doPost(request, response);

        // Проверьте, что статус ответа установлен в SC_OK
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);

        // Проверьте, что содержимое ответа содержит ожидаемое сообщение
        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Reading type added successfully");
    }

    @Test
    @DisplayName("Тестирование doPost с недействительным путем")
    public void testDoPostWithInvalidPath() throws Exception {
        // Мокайте request.getPathInfo()
        when(request.getPathInfo()).thenReturn("/invalid");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doPost(request, response);

        // Проверьте, что статус ответа установлен в SC_BAD_REQUEST
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);

        // Проверьте, что содержимое ответа содержит ожидаемое сообщение об ошибке
        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Invalid request data");
    }

    @Test
    @DisplayName("Тестирование doPost с ролью не администратора")
    public void testDoPostWithNonAdminRole() throws Exception {
        when(request.getPathInfo()).thenReturn("/");
        // Мокайте UserContextHolder.getUser()
        User user = User.builder()
                .accountNum("0000000000000000")
                .role(Role.USER)  // Non-admin role
                .build();
        Map<String, String> tokens = AuthUtils.createJwtForUser(user);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokens.get("accessToken"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        adminServlet.doPost(request, response);

        // Проверьте, что статус ответа установлен в SC_FORBIDDEN
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);

        // Проверьте, что содержимое ответа содержит ожидаемое сообщение об ошибке
        String responseContent = outputStream.toString();
        assertThat(responseContent).contains("Access denied");
    }

}
