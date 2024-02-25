package org.kharisov.configs;

import jakarta.validation.constraints.NotNull;
import liquibase.exception.*;
import lombok.RequiredArgsConstructor;
import org.kharisov.liquibase.LiquibaseExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Класс, который слушает события запуска приложения.
 * Этот класс автоматически вызывает миграцию базы данных при запуске приложения.
 */
@RequiredArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private final LiquibaseExample liquibaseExample;

    /**
     * Обрабатывает событие после инициализации контекста приложения.
     * Запускает миграцию базы данных.
     *
     * @param event событие обновления контекста.
     */
    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        try {
            liquibaseExample.runMigration();
        } catch (SQLException | DatabaseException | CommandExecutionException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
    }
}
