package org.kharisov;

import org.kharisov.loggingstarter.annotations.EnableLogging;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableMethodSecurity
@EnableAspectJAutoProxy
@EnableLogging
public class MainApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MainApplication.class);
        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run(args);
    }
}
