package com.thefishnextdoor.tasks;

import java.util.HashMap;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

public class TaskConfiguration {

    private static HashMap<String, TaskConfiguration> taskConfigurations = new HashMap<>();

    private final String id;

    private TaskType type;

    public TaskConfiguration(@NotNull String id) {
        this.id = id;
        this.type = TaskType.PLACE_BLOCK; // Fix later
        taskConfigurations.put(id, this);
    }

    public boolean isValidFor(@NotNull TaskType type) {
        return this.type == type;
    }

    public static Optional<TaskConfiguration> get(@NotNull String id) {
        return Optional.ofNullable(taskConfigurations.get(id));
    }
}
