package org.kharisov.entities;

import java.util.Objects;

/**
 * Сущность IndicatorType представляет тип показания.
 * Эта сущность содержит название типа показания.
 */
public class ReadingType {
    /**
     * Название типа показания.
     */
    private final String name;

    /**
     * Конструктор для класса.
     * @param name Название типа показания.
     */
    private ReadingType(String name) {
        this.name = name;
    }

    /**
     * Создает новый объект типа показания по названию типа.
     * @param name Название типа показания.
     * @return Новый объект типа показания.
     */
    public static ReadingType Create(String name) {
        return new ReadingType(name);
    }

    /**
     * Получить название типа показания.
     * @return Название типа показания.
     */
    public String getValue() {
        return name;
    }

    /**
     * Переопределение метода equals для сравнения объектов IndicatorType.
     * @param obj Объект, с которым сравнивается текущий объект.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReadingType that = (ReadingType) obj;
        return Objects.equals(name, that.name);
    }

    /**
     * Переопределение метода hashCode для получения хеш-кода объекта IndicatorType по названию.
     * @return Хеш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
