package org.kharisov.storages;

import java.util.HashMap;

public class IndicatorTypeStorageMemory {
    private final HashMap<String, IndicatorType> indicatorTypes;

    public IndicatorTypeStorageMemory() {
        indicatorTypes = new HashMap<>();
        indicatorTypes.put("Горячая вода", IndicatorType.Create("Горячая вода"));
        indicatorTypes.put("Холодная вода", IndicatorType.Create("Холодная вода"));
        indicatorTypes.put("Отопление", IndicatorType.Create("Отопление"));
    }

    public HashMap<String, IndicatorType> getStorage() {
        return indicatorTypes;
    }
}
