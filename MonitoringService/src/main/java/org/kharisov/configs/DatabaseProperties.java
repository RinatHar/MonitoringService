package org.kharisov.configs;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "database")
@Getter
@Setter
public class DatabaseProperties {
    private String url;
    private String schema;
    private String username;
    private String password;
    private int sizePool;

    public String getUrlAndSchema() {
        return url + "?currentSchema=" + schema;
    }
}
