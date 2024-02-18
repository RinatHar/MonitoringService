package org.kharisov.entities;

import lombok.*;

import java.util.Objects;

/**
 * Сущность ReadingTypeRecord представляет тип показания.
 * Эта сущность содержит название типа показания.
 */
public record ReadingTypeRecord (Long id, String name) {

}
