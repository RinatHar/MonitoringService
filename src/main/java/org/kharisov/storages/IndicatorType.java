package org.kharisov.storages;

import java.util.Objects;

public class IndicatorType {
    private final String value;

    private IndicatorType(String value) {
        this.value = value;
    }

    public static IndicatorType Create(String value) {
        // Здесь вы можете добавить проверки на допустимость значения
        return new IndicatorType(value);
    }

    public String getValue() {
        return value;
    }

    // Переопределите метод equals для сравнения типов показаний
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndicatorType that = (IndicatorType) obj;
        return Objects.equals(value, that.value);
    }

    // Переопределите метод hashCode для корректной работы с коллекциями
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
