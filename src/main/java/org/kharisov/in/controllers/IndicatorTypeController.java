package org.kharisov.in.controllers;

import org.kharisov.services.IndicatorTypeService;
import org.kharisov.storages.IndicatorType;

import java.util.Optional;
import java.util.Set;

public class IndicatorTypeController {
    private final IndicatorTypeService indicatorTypeService;

    public IndicatorTypeController(IndicatorTypeService indicatorTypeService) {
        this.indicatorTypeService = indicatorTypeService;
    }

    public void addIndicatorType(String name) {
        indicatorTypeService.addIndicatorType(name);
    }

    public Optional<IndicatorType> getIndicatorType(String name) {
        return indicatorTypeService.getIndicatorType(name);
    }

    public Set<String> getIndicatorNames() {
        return indicatorTypeService.getIndicatorNames();
    }
}
