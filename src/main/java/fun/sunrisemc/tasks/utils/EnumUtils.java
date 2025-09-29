package fun.sunrisemc.tasks.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

public class EnumUtils {

    public static <E extends Enum<E>> E fromString(@NonNull Class<E> enumClass, @NonNull String name) {
        name = name.trim().replace(" ", "_").replace("-", "_");

        try {
            for (E constant : enumClass.getEnumConstants()) {
                if (constant.name().equalsIgnoreCase(name)) {
                    return constant;
                }
            }
        }
        catch (Exception e) {
            Log.severe("Failed to parse enum: " + enumClass.getSimpleName() + " - " + name);
        }

        return null;
    }

    public static String allStrings(@NonNull Class<? extends Enum<?>> enumClass) {
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