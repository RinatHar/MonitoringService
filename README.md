# Monitoring Service

Веб-сервис для подачи показаний счетчиков отопления, горячей и холодной воды.

## Предварительные требования

- Установленный Docker и Docker Compose
- Установленный Git

## Установка и запуск проекта

Следуйте этим шагам, чтобы запустить проект:

1. **Клонирование репозитория**

   Сначала склонируйте репозиторий с помощью Git. Откройте терминал и введите следующую команду:

    ```bash
    git clone https://github.com/RinatHar/MonitoringService.git
    ```

2. **Запуск Docker Compose**

   Перейдите в директорию проекта и запустите Docker Compose:

    ```bash
    cd MonitoringService
    docker-compose up
    ```

3. **Запуск миграций Liquibase**

   Запустите класс для миграций Liquibase, по адресу:

    ```bash
    ./src/main/java/org/kharisov/liquibase/LiquibaseExample.java
    ```

4. **Запуск главного файла проекта**

   Запустите файл `Main()` проекта, по адресу:

    ```bash
    ./src/main/java/org/kharisov/Main.java
    ```

Поздравляем, проект теперь запущен и готов к использованию!
