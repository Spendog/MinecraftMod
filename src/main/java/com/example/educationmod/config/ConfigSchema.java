package com.example.educationmod.config;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigSchema<T> {
    public final String key;
    public final String label;
    public final ConfigType type;
    public final T defaultValue;
    public final Supplier<T> getter;
    public final Consumer<T> setter;
    public final Number min; // Optional
    public final Number max; // Optional

    public ConfigSchema(String key, String label, ConfigType type, T defaultValue, Supplier<T> getter,
            Consumer<T> setter) {
        this(key, label, type, defaultValue, getter, setter, null, null);
    }

    public ConfigSchema(String key, String label, ConfigType type, T defaultValue, Supplier<T> getter,
            Consumer<T> setter, Number min, Number max) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.defaultValue = defaultValue;
        this.getter = getter;
        this.setter = setter;
        this.min = min;
        this.max = max;
    }

    public enum ConfigType {
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING
    }
}
