package org.kharisov.configs;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

/**
 * Класс MainWebAppInitializer реализует интерфейс WebApplicationInitializer и используется для настройки контекста веб-приложения и сервлетов при запуске приложения.
 * Он сканирует указанный пакет для поиска компонентов Spring, добавляет слушателя контекста, настраивает главный сервлет DispatcherServlet и добавляет фильтр JWT.
 *
 * @see org.springframework.web.WebApplicationInitializer
 */
public class MainWebAppInitializer implements WebApplicationInitializer {
    /**
     * Метод, вызываемый при запуске веб-приложения. Он настраивает контекст веб-приложения и сервлеты.
     *
     * @param container контекст сервлета для инициализации.
     */
    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("org.kharisov");
        container.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = container.addServlet("mvc", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        container.addFilter("jwtFilter", new DelegatingFilterProxy("jwtFilter"))
                .addMappingForUrlPatterns(null, false, "/api/v1/admin/*", "/api/v1/readings/*");
    }
}
