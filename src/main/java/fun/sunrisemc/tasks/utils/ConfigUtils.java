package fun.sunrisemc.tasks.utils;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUtils {

    public static int getIntClamped(YamlConfiguration config, String path, int min, int max) {
        int value = config.getInt(path);
        return Math.clamp(value, min, max);
    }

    public static double getDoubleClamped(YamlConfiguration config, String path, double min, double max) {
        double value = config.getDouble(path);
        return Math.clamp(value, min, max);
    }
}