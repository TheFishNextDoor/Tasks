package fun.sunrisemc.tasks.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.utils.ConfigFile;
import fun.sunrisemc.tasks.utils.Log;

public class TaskConfigurationManager {

    static HashMap<String, TaskConfiguration> taskConfigurations = new HashMap<>();

    public static Optional<TaskConfiguration> get(@NonNull String id) {
        return Optional.ofNullable(taskConfigurations.get(id));
    }

    public static Optional<TaskConfiguration> getNewTask(@NonNull PlayerProfile playerProfile) {
        ArrayList<TaskConfiguration> possibleTasks = new ArrayList<>();
        for (TaskConfiguration taskConfiguration : taskConfigurations.values()) {
            if (taskConfiguration.meetsRequirements(playerProfile)) {
                possibleTasks.add(taskConfiguration);
            }
        }
        
        if (possibleTasks.isEmpty()) {
            return Optional.empty();
        }
    
        return Optional.of(possibleTasks.get((int) (Math.random() * possibleTasks.size())));
    }

    public static List<String> getIds() {
        return new ArrayList<>(taskConfigurations.keySet());
    }

    public static void loadConfig() {
        taskConfigurations.clear();
        YamlConfiguration config = ConfigFile.get("tasks", false);
        for (String id : config.getKeys(false)) {
            new TaskConfiguration(config, id);
        }
        Log.info("Loaded " + taskConfigurations.size() + " tasks");
    }
}