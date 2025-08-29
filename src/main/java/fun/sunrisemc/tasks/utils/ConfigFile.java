package fun.sunrisemc.tasks.utils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.YamlConfiguration;

import fun.sunrisemc.tasks.TasksPlugin;

public class ConfigFile {

    public static YamlConfiguration get(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        File configFile = new File(getFolder(), name + ".yml");
        if (!configFile.exists()) {
            create(name);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (copyDefaults(config, getDefault(name))) {
            save(name, config);
        }

        return config;
    }

    public static boolean save(String filename, YamlConfiguration config) {
        File configFile = new File(getFolder(), filename + ".yml");
        try {
            config.save(configFile);
            return true;
        } catch (Exception e) {
            Log.warning("Failed to save configuration for " + filename + ".yml");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean create(String filename) {
        try {
            TasksPlugin.getInstance().saveResource(filename + ".yml", false);
        } catch (Exception e) {
            Log.warning("Failed to save default configuration for " + filename + ".yml");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean moveKeyIfExists(String filename, String fromKey, String toKey) {
        YamlConfiguration config = get(filename);
        if (config.contains(fromKey)) {
            config.set(toKey, config.get(fromKey));
            config.set(fromKey, null);
            save(filename, config);
            return true;
        }
        return false;
    }

    private static File getFolder() {
        File pluginFolder = TasksPlugin.getInstance().getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
        return pluginFolder;
    }

    private static YamlConfiguration getDefault(String name) {
        YamlConfiguration defaultConfig = new YamlConfiguration();
        try {
            InputStream resourceStream = TasksPlugin.getInstance().getResource(name + ".yml");
            if (resourceStream != null) {
                InputStreamReader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8);
                defaultConfig.load(reader);
            }
        } catch (Exception e) {
            Log.warning("Failed to load default configuration for " + name + ".yml");
            e.printStackTrace();
        }
        return defaultConfig;
    }

    private static boolean copyDefaults(YamlConfiguration config, YamlConfiguration defaultConfig) {
        boolean changed = false;
        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, defaultConfig.get(key));
                changed = true;
            }
        }
        return changed;
    }
}
