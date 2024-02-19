package org.kharisov.configs;

import org.kharisov.aspects.LoggingAspect;
import org.kharisov.in.controllers.*;
import org.kharisov.in.filters.JwtFilter;
import org.kharisov.liquibase.LiquibaseExample;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.repos.interfaces.*;
import org.kharisov.services.databaseImpls.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.utils.JwtUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.*;

import java.util.*;

/**
 * Класс AppConfig представляет собой конфигурационный класс Spring, который используется для настройки приложения.
 * Он содержит методы для создания и настройки различных компонентов приложения,
 * таких как JwtUtils, ConnectionPool, Liquibase, Repos, Services, Controllers и Aspects.
 */
@Configuration
@ComponentScan(basePackages = {"org.kharisov"})
@EnableWebMvc
@EnableMethodSecurity
@EnableAspectJAutoProxy
@PropertySource("classpath:application.yml")
@DependsOn("yamlConfig")
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    @Bean
    public JwtUtils jwtUtils(YamlPropertiesFactoryBean yaml) {
        Properties myProperties = yaml.getObject();
        String secretKey = myProperties.getProperty("jwt.secretKey");
        String accessTokenDuration = myProperties.getProperty("jwt.duration.access");
        String refreshTokenDuration = myProperties.getProperty("jwt.duration.refresh");
        JwtUtils jwtUtils = new JwtUtils(
                secretKey,
                Integer.parseInt(accessTokenDuration),
                Integer.parseInt(refreshTokenDuration));
        jwtUtils.init();
        return jwtUtils;
    }
    @Bean
    @Scope("prototype")
    public ConnectionPool connectionPool(YamlPropertiesFactoryBean yaml) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Properties myProperties = yaml.getObject();
        String dbUrl = myProperties.getProperty("database.url");
        String dbSchema = myProperties.getProperty("database.schema");
        String urlAndSchema = dbUrl + "?currentSchema=" + dbSchema;
        String dbUsername = myProperties.getProperty("database.username");
        String dbPassword = myProperties.getProperty("database.password");
        String sizePool = myProperties.getProperty("database.sizePool");
        return new ConnectionPool(urlAndSchema, dbUsername, dbPassword, Integer.parseInt(sizePool));
    }

    @Bean
    public LiquibaseExample liquibaseExample(YamlPropertiesFactoryBean yaml) {
        Properties myProperties = yaml.getObject();
        String dbUrl = myProperties.getProperty("database.url");
        String dbUsername = myProperties.getProperty("database.username");
        String dbPassword = myProperties.getProperty("database.password");
        String dbSchema = myProperties.getProperty("database.schema");
        String lbSchema = myProperties.getProperty("liquibase.schema");
        String lbChangeLog = myProperties.getProperty("liquibase.change_log");
        return new LiquibaseExample(dbUrl, dbUsername, dbPassword, dbSchema, lbSchema, lbChangeLog);
    }

    @Bean
    public StartupApplicationListener startupApplicationListener(LiquibaseExample liquibaseExample) {
        return new StartupApplicationListener(liquibaseExample);
    }

    @Bean
    public AuditDbRepo auditRepo(ConnectionPool connectionPool) {
        return new AuditDbRepo(connectionPool);
    }

    @Bean
    public AuthDbRepo authRepo(ConnectionPool connectionPool) {
        return new AuthDbRepo(connectionPool);
    }

    @Bean
    public ReadingDbRepo readingRepo(ConnectionPool connectionPool) {
        return new ReadingDbRepo(connectionPool);
    }

    @Bean
    public ReadingTypeDbRepo readingTypeRepo(ConnectionPool connectionPool) {
        return new ReadingTypeDbRepo(connectionPool);
    }

    @Bean
    public AuditDbService auditService(AuthRepo authRepo, AuditRepo auditRepo) {
        return new AuditDbService(authRepo, auditRepo);
    }

    @Bean
    public AuthDbService authService(AuthRepo authRepo) {
        return new AuthDbService(authRepo);
    }

    @Bean
    public ReadingDbService readingService(ReadingRepo readingRepo, AuthRepo authRepo, ReadingTypeRepo readingTypeRepo) {
        return new ReadingDbService(readingRepo, authRepo, readingTypeRepo);
    }

    @Bean
    public ReadingTypeDbService readingTypeService(ReadingTypeRepo readingTypeRepo) {
        return new ReadingTypeDbService(readingTypeRepo);
    }

    @Bean
    public JwtFilter jwtFilter(AuthService authService, JwtUtils jwtUtils) {
        return new JwtFilter(authService, jwtUtils);
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    public AuthController authController(AuthService authService, JwtUtils jwtUtils) {
        return new AuthController(authService, jwtUtils);
    }

    @Bean
    public AdminController adminController(AuthService authService,
                                           AuditService auditService,
                                           ReadingTypeService readingTypeService,
                                           ReadingService readingService) {
        return new AdminController(authService, auditService, readingTypeService, readingService);
    }

    @Bean
    public ReadingController readingController(ReadingService readingService, ReadingTypeService readingTypeService) {
        return new ReadingController(readingService, readingTypeService);
    }
}
