package com.thefishnextdoor.tasks;

import org.jetbrains.annotations.NotNull;

public class PlayerTask {

    private TaskConfiguration taskConfiguration;

    private int level;

    private int progress;

    public PlayerTask(@NotNull TaskConfiguration taskConfiguration, int level, int progress) {
        this.taskConfiguration = taskConfiguration;
        this.level = level;
        this.progress = progress;
    }
}
