package fun.sunrisemc.tasks.utils;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import fun.sunrisemc.tasks.TasksPlugin;

public class DataFile {

    public static YamlConfiguration get(@NonNull String name) {
        File dataFile = new File(getFolder(), name + ".yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(dataFile);
    }

    public static void save(@NonNull String name, @NonNull YamlConfiguration data) {
        File dataFile = new File(getFolder(), name + ".yml");
        try {
            data.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getFolder() {
        File pluginFolder = TasksPlugin.getInstance().getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        File dataFolder = new File(pluginFolder, "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        return dataFolder;
    }   
}