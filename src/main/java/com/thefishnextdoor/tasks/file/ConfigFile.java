package com.thefishnextdoor.tasks.file;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.thefishnextdoor.tasks.TasksPlugin;

public class ConfigFile {

    public static YamlConfiguration get(String name) {
        File configFile = new File(getFolder(), name + ".yml");
        if (!configFile.exists()) {
            try {
                TasksPlugin.getInstance().saveResource(name, false);
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
