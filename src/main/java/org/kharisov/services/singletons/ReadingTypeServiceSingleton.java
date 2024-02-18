package org.kharisov.services.singletons;

import jakarta.servlet.ServletContext;
import org.kharisov.services.interfaces.ReadingTypeService;

public class ReadingTypeServiceSingleton {
    /**
     * Класс ReadingTypeServiceSingleton представляет собой синглтон, который обеспечивает доступ к экземпляру ReadingTypeService.
     * Этот класс использует ленивую инициализацию для создания единственного экземпляра ReadingTypeService.
     *
     * <p>Этот класс содержит следующие методы:</p>
     * <ul>
     *   <li>initialize(ServletContext context): Инициализирует экземпляр ReadingTypeService, используя контекст сервлета.</li>
     *   <li>getInstance(): Возвращает экземпляр ReadingTypeService.</li>
     * </ul>
     */
    public static ReadingTypeService instance;

    /**
     * Конструктор ReadingTypeServiceSingleton является приватным, чтобы предотвратить создание экземпляра класса.
     */
    private ReadingTypeServiceSingleton() {}

    /**
     * Инициализирует экземпляр ReadingTypeService, используя контекст сервлета.
     *
     * @param context контекст сервлета, из которого извлекается ReadingTypeService
     */
    public static void initialize(ServletContext context) {
        instance = (ReadingTypeService) context.getAttribute("readingTypeService");
    }

    /**
     * Возвращает экземпляр ReadingTypeService.
     *
     * @return экземпляр ReadingTypeService
     */
    public static ReadingTypeService getInstance() {
        return instance;
    }
}
