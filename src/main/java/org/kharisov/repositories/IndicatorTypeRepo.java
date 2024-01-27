package org.kharisov.repositories;

import org.kharisov.storages.IndicatorType;

import java.util.Optional;
import java.util.Set;

public abstract class IndicatorTypeRepo {
    public abstract void addIndicatorType(String name);

    public abstract Optional<IndicatorType> getIndicatorType(String name);
    public abstract Set<String> getIndicatorNames();
}
