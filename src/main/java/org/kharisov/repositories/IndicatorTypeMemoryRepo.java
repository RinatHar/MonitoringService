package org.kharisov.repositories;

import org.kharisov.storages.IndicatorType;
import org.kharisov.storages.IndicatorTypeStorageMemory;

import java.util.Optional;
import java.util.Set;

public class IndicatorTypeMemoryRepo extends IndicatorTypeRepo{
    private final IndicatorTypeStorageMemory storage;

    public IndicatorTypeMemoryRepo(IndicatorTypeStorageMemory storage) {
        this.storage = storage;
    }

    public void addIndicatorType(String name) {
        storage.getStorage().put(name, IndicatorType.Create(name));
    }

    public Optional<IndicatorType> getIndicatorType(String name) {
        IndicatorType indicatorType = storage.getStorage().get(name);
        if (indicatorType != null) {
            return Optional.of(indicatorType);
        } else {
            return Optional.empty();
        }
    }

    public Set<String> getIndicatorNames() {
        return storage.getStorage().keySet();
    }
}
