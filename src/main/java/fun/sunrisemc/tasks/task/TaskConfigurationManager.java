package fun.sunrisemc.tasks.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.configuration.file.YamlConfiguration;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.utils.ConfigFile;
import fun.sunrisemc.tasks.utils.Log;

public class TaskConfigurationManager {

    static HashMap<String, TaskConfiguration> taskConfigurations = new HashMap<>();

    static List<String> settings = List.of(
        "amount",
        "message",
        "time-limit-minutes",
        "reset-on-death",
        "skippable",
        "actionbar",
        "progress-display",
        "repeatable",
        "min-level",
        "max-level",
        "prerequisite-tasks",
        "incompatible-tasks",
        "permission",
        "reward-money",
        "reward-xp",
        "reward-skips",
        "reward-unlocks",
        "reward-console-commands",
        "reward-player-commands",
        "reward-messages",
        "triggers",
        "worlds",
        "environments",
        "biomes",
        "min-x",
        "max-x",
        "min-y",
        "max-y",
        "min-z",
        "max-z",
        "entity-in-water",
        "entity-on-ground",
        "entity-names",
        "entity-types",
        "entity-categories",
        "item-names",
        "item-materials",
        "block-materials"
    );

    public static Optional<TaskConfiguration> get(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return Optional.ofNullable(taskConfigurations.get(id));
    }

    public static Optional<TaskConfiguration> getNewTask(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }
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
        YamlConfiguration config = ConfigFile.get("tasks");
        for (String id : config.getKeys(false)) {
            new TaskConfiguration(config, id);
        }
        Log.info("Loaded " + taskConfigurations.size() + " tasks");
    }
}