package org.kharisov.configs;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * Класс конфигурации, который загружает настройки из файла application.yml.
 */
@Configuration
@PropertySource("classpath:application.yml")
public class YamlConfig {

    /**
     * Создает новый экземпляр YamlPropertiesFactoryBean, который загружает настройки из файла application.yml.
     *
     * @return новый экземпляр YamlPropertiesFactoryBean.
     */
    @Bean
    @Primary
    public YamlPropertiesFactoryBean yamlPropertiesFactoryBean() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        return yaml;
    }

    /**
     * Создает новый экземпляр PropertySourcesPlaceholderConfigurer, который использует настройки,
     * загруженные YamlPropertiesFactoryBean.
     *
     * @param yaml экземпляр YamlPropertiesFactoryBean, который загружает настройки.
     * @return новый экземпляр PropertySourcesPlaceholderConfigurer.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(YamlPropertiesFactoryBean yaml) {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}
