package org.kharisov.in.controllers;

import org.kharisov.entities.*;
import org.kharisov.services.IndicatorService;
import org.kharisov.storages.IndicatorType;

import java.time.LocalDate;
import java.util.*;

public class IndicatorController {
    private final IndicatorService indicatorService;

    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    public void addIndicator(User user, IndicatorType indicator, int value) {
        indicatorService.addIndicator(user, indicator, value);
    }

    public Optional<IndicatorRecord> getCurrentIndicator(User user, IndicatorType type) {
        return indicatorService.getCurrentIndicator(user, type);
    }

    public List<IndicatorRecord> getIndicatorsByMonth(User user, int month, int year) {
        return indicatorService.getIndicatorsByMonth(user, month, year);
    }

    public List<IndicatorRecord> getHistory(User user) {
        return indicatorService.getHistory(user);
    }

    public Map<String, List<IndicatorRecord>> getAllIndicators() {
        return indicatorService.getAllIndicators();
    }

    public boolean indicatorExists(User user, IndicatorType indicator, LocalDate now) {
        return indicatorService.indicatorExists(user, indicator, now);
    }


}
