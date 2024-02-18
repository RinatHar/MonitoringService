package org.kharisov.configs;

import jakarta.validation.constraints.NotNull;
import liquibase.exception.*;
import lombok.RequiredArgsConstructor;
import org.kharisov.liquibase.LiquibaseExample;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.sql.SQLException;

@RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final LiquibaseExample liquibaseExample;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        try {
            liquibaseExample.runMigration();
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }
}
