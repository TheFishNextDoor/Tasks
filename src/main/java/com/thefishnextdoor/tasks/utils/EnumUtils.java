package com.thefishnextdoor.tasks.utils;

public class EnumUtils {

    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String name) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum class cannot be null");
        }
        
        if (name == null) {
            return null;
        }

        name = name.trim().replace(" ", "_").replace("-", "_");

        try {
            for (E constant : enumClass.getEnumConstants()) {
                if (constant.name().equalsIgnoreCase(name)) {
                    return constant;
                }
            }
        }
        catch (Exception e) {
            Debug.logSevere("Failed to parse enum: " + enumClass.getSimpleName() + " - " + name);
        }

        return null;
    }

    public static String allStrings(Class<? extends Enum<?>> enumClass) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum class cannot be null");
        }

        Enum<?>[] constants = enumClass.getEnumConstants();
        if (constants == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Enum<?> constant : constants) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(constant.name());
        }
        return builder.toString();
    }
}
