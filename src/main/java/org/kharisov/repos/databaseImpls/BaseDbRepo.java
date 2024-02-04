package org.kharisov.repos.databaseImpls;

import java.util.*;

/**
 * Абстрактный класс BaseDbRepo представляет собой базовый репозиторий для работы с базой данных.
 * Он содержит общие методы и свойства, которые могут быть использованы во всех репозиториях, наследующихся от этого класса.
 *
 * @param <K> Тип ключа, используемого в репозитории.
 * @param <V> Тип значения, используемого в репозитории.
 */
public abstract class BaseDbRepo<K, V> {
    /**
     * Пул соединений с базой данных, используемый в репозитории.
     */
    protected final ConnectionPool connectionPool;

    /**
     * Конструктор класса BaseDbRepo.
     *
     * @param connectionPool Пул соединений с базой данных.
     */
    public BaseDbRepo(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Записывает объект DTO в хранилище и возвращает его.
     *
     * @param dto Объект DTO для добавления в хранилище.
     * @return Объект DTO, добавленный в хранилище, или пустой Optional, если добавление не удалось.
     */
    public abstract Optional<V> add(V dto);

    /**
     * Возвращает все объекты DTO из хранилища.
     *
     * @return Список всех объектов DTO из хранилища.
     */
    public abstract List<V> getAll();
}
