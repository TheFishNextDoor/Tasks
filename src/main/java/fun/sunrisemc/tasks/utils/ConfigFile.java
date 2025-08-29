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
            try {
                TasksPlugin.getInstance().saveResource(name + ".yml", false);
            } catch (Exception e) {
                Log.warning("Failed to save default configuration for " + name + ".yml");
                e.printStackTrace();
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (copyDefaults(config, getDefault(name))) {
            try {
                config.save(configFile);
            } catch (Exception e) {
                Log.warning("Failed to copy defaults to " + configFile.getName());
                e.printStackTrace();
            }
        }

        return config;
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
