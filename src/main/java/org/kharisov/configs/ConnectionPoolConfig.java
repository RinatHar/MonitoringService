package org.kharisov.configs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionPoolConfig {
    private String url = Config.get("database.url") + "?currentSchema=" + Config.get("database.schema");
    private String user = Config.get("database.username");
    private String password = Config.get("database.password");
    private int size = Integer.parseInt(Config.get("database.sizePool"));
}