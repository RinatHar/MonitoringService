package org.kharisov.services;

import org.kharisov.repositories.IndicatorTypeRepo;
import org.kharisov.storages.IndicatorType;

import java.util.Optional;
import java.util.Set;

public class IndicatorTypeService {
    private final IndicatorTypeRepo indicatorTypeRepo;

    public IndicatorTypeService(IndicatorTypeRepo indicatorTypeRepo) {
        this.indicatorTypeRepo = indicatorTypeRepo;
    }

    public void addIndicatorType(String name) {
        indicatorTypeRepo.addIndicatorType(name);
    }

    public Optional<IndicatorType> getIndicatorType(String name) {
        return indicatorTypeRepo.getIndicatorType(name);
    }

    public Set<String> getIndicatorNames() {
        return indicatorTypeRepo.getIndicatorNames();
    }

}
