package com.thefishnextdoor.tasks;

import org.bukkit.configuration.file.YamlConfiguration;

import com.thefishnextdoor.tasks.file.ConfigFile;
import com.thefishnextdoor.tasks.toolkit.NumberTools;

public class Settings {

    public final int MAX_TASKS;

    public final double TASK_XP_MULTIPLIER;
    public final double TASK_MONEY_MULTIPLIER;

    public Settings() {
        YamlConfiguration config = ConfigFile.get("config");
        this.MAX_TASKS = NumberTools.clamp(config.getInt("max-tasks"), 0, 20);
        this.TASK_XP_MULTIPLIER = NumberTools.clamp(config.getDouble("task-xp-multiplier"), 0.0, 1_000_000.0);
        this.TASK_MONEY_MULTIPLIER = NumberTools.clamp(config.getDouble("task-money-multiplier"), 0.0, 1_000_000.0);
    }
}