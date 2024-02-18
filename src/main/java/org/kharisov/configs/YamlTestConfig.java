package org.kharisov.configs;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlTestConfig {
    private final Map<String, Object> yamlProps;

    public YamlTestConfig(String yamlFile) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(yamlFile);
        yamlProps = yaml.load(inputStream);
    }

    public String getProperty(String property) {
        String[] parts = property.split("\\.");
        Map<String, Object> currentLevel = yamlProps;
        for (int i = 0; i < parts.length - 1; i++) {
            currentLevel = (Map<String, Object>) currentLevel.get(parts[i]);
        }
        return String.valueOf(currentLevel.get(parts[parts.length - 1]));
    }
}
