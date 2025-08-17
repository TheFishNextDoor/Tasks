package fun.sunrisemc.tasks.utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import fun.sunrisemc.tasks.TasksPlugin;

public class ConfigUtils {

    public static YamlConfiguration get(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        
        File configFile = new File(getFolder(), name + ".yml");
        if (!configFile.exists()) {
            try {
                TasksPlugin.getInstance().saveResource(name + ".yml", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    private static File getFolder() {
        File pluginFolder = TasksPlugin.getInstance().getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
        return pluginFolder;
    }   
}
