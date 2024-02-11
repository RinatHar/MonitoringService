package servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.kharisov.domains.User;
import org.kharisov.in.servlets.auth.RegisterServlet;
import org.kharisov.services.interfaces.AuthService;
import org.mockito.*;

import java.io.*;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class RegisterServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthService authService;

    @Mock
    private ServletContext servletContext;

    private RegisterServlet registerServlet;

    @BeforeEach
    public void setUp() throws ServletException, IOException {
        MockitoAnnotations.openMocks(this);
        registerServlet = new RegisterServlet();

        ServletConfig mockConfig = mock(ServletConfig.class);
        when(mockConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("authService")).thenReturn(authService);

        registerServlet.init(mockConfig);

        ServletOutputStream servletOutputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(servletOutputStream);
    }

    @Test
    @DisplayName("Тестирование doPost с успешной регистрацией")
    public void testDoPostSuccess() throws Exception {
        when(request.getReader())
                .thenReturn(
                        new BufferedReader(
                                new StringReader("{\"accountNum\":\"0000000000000000\",\"password\":\"12345678\"}")));
        when(authService.addUser(any(User.class))).thenReturn(Optional.of(new User()));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        registerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Тестирование doPost с неудачной регистрацией")
    public void testDoPostFailure() throws Exception {
        when(request.getReader())
                .thenReturn(
                        new BufferedReader(
                                new StringReader("{\"accountNum\":\"0000000000000000\",\"password\":\"12345678\"}")));
        when(authService.addUser(any(User.class))).thenReturn(Optional.empty());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        registerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CONFLICT);
    }
}

