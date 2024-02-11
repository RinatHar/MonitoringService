package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.kharisov.in.servlets.auth.LoginServlet;
import org.kharisov.services.interfaces.AuthService;
import org.mockito.*;

import java.io.*;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthService authService;

    @Mock
    private ServletContext servletContext;

    private LoginServlet loginServlet;

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        loginServlet = new LoginServlet();

        ServletConfig mockConfig = mock(ServletConfig.class);
        when(mockConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("authService")).thenReturn(authService);

        loginServlet.init(mockConfig);

        // Создайте мок ServletOutputStream
        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
    }

    @Test
    @DisplayName("Тестирование doPost с успешным входом")
    public void testDoPostSuccess() throws Exception {
        when(request.getReader())
                .thenReturn(
                        new BufferedReader(
                                new StringReader("{\"accountNum\":\"0000000000000000\",\"password\":\"12345678\"}")));
        when(authService.logIn(anyString(), anyString())).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Тестирование doPost с неудачным входом")
    public void testDoPostFailure() throws Exception {
        when(request.getReader())
                .thenReturn(
                        new BufferedReader(
                                new StringReader("{\"accountNum\":\"0000000000000000\",\"password\":\"12345678\"}")));
        when(authService.logIn(anyString(), anyString())).thenReturn(false);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

