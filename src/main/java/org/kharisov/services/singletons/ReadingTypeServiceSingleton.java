package org.kharisov.services.singletons;

import jakarta.servlet.ServletContext;
import org.kharisov.services.interfaces.ReadingTypeService;

public class ReadingTypeServiceSingleton {
    private static ReadingTypeService instance;

    private ReadingTypeServiceSingleton() {}

    public static void initialize(ServletContext context) {
        instance = (ReadingTypeService) context.getAttribute("readingTypeService");
    }

    public static ReadingTypeService getInstance() {
        return instance;
    }
}
