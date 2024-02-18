package org.kharisov.configs;

import java.io.*;
import java.util.Properties;

/**
 * Класс Config предназначен для управления конфигурационными параметрами приложения.
 * Он загружает свойства из файла 'application.properties' при инициализации.
 */
public class Config {
    private static final Properties properties = new Properties();

    // Статический блок инициализации для загрузки свойств из файла 'application.properties'
    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Возвращает значение свойства по заданному ключу.
     *
     * @param key ключ свойства, значение которого требуется получить.
     * @return значение свойства, соответствующее заданному ключу, или null, если такого ключа нет.
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}