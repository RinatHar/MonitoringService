package org.kharisov.configs;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * Класс конфигурации, который загружает настройки из YAML файла.
 */
public class YamlTestConfig {
    /**
     * Свойства, загруженные из YAML файла.
     */
    private final Map<String, Object> yamlProps;

    /**
     * Конструктор класса. Загружает настройки из указанного YAML файла.
     *
     * @param yamlFile имя YAML файла.
     */
    public YamlTestConfig(String yamlFile) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(yamlFile);
        yamlProps = yaml.load(inputStream);
    }

    /**
     * Возвращает значение свойства, указанного в параметре.
     *
     * @param property имя свойства.
     * @return значение свойства.
     */
    public String getProperty(String property) {
        String[] parts = property.split("\\.");
        Map<String, Object> currentLevel = yamlProps;
        for (int i = 0; i < parts.length - 1; i++) {
            currentLevel = (Map<String, Object>) currentLevel.get(parts[i]);
        }
        return String.valueOf(currentLevel.get(parts[parts.length - 1]));
    }
}
