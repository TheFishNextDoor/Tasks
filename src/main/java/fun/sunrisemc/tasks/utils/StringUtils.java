package fun.sunrisemc.tasks.utils;

import org.checkerframework.checker.nullness.qual.NonNull;

public class StringUtils {

    public static String normalizeString(@NonNull String string) {
        return string.trim().toLowerCase().replace(" ", "_").replace("-", "_");
    }
}