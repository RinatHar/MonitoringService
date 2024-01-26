package org.kharisov.in.controllers;

import org.kharisov.entities.IndicatorRecord;
import org.kharisov.entities.User;
import org.kharisov.enums.IndicatorTypeEnum;
import org.kharisov.services.IndicatorService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class IndicatorController {
    private final IndicatorService indicatorService;

    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    public boolean addIndicator(User user, IndicatorTypeEnum indicator, int value) {
        return indicatorService.addIndicator(user, indicator, value);
    }

    public Optional<IndicatorRecord> getCurrentIndicator(User user, IndicatorTypeEnum type) {
        return indicatorService.getCurrentIndicator(user, type);
    }

    public List<IndicatorRecord> getHistory(User user) {
        return indicatorService.getHistory(user);
    }

    public Map<String, List<IndicatorRecord>> getAllIndicators() {
        return indicatorService.getAllIndicators();
    }


}
