package org.kharisov.configs;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "liquibase")
@Getter
@Setter
public class LiquibaseProperties {
    private String changeLog;
    private String schema;
}
