package com.thefishnextdoor.tasks;

import org.bukkit.configuration.file.YamlConfiguration;

import com.thefishnextdoor.tasks.file.ConfigFile;
import com.thefishnextdoor.tasks.toolkit.NumberTools;

public class Settings {

    public final double LEVEL_BASE;
    public final double LEVEL_MULTIPLIER;

    public final boolean ALLOW_TASK_SKIPPING;
    public final int MAX_TASKS;
    public final double TASK_XP_MULTIPLIER;
    public final double TASK_MONEY_MULTIPLIER;

    public Settings() {
        YamlConfiguration config = ConfigFile.get("config");
        this.LEVEL_BASE = NumberTools.clamp(config.getDouble("xp-curve.base"), 1.0, 1_000_000.0);
        this.LEVEL_MULTIPLIER = NumberTools.clamp(config.getDouble("xp-curve.multiplier"), 1.0, 10.0);
        this.ALLOW_TASK_SKIPPING = config.getBoolean("tasks.allow-skipping");
        this.MAX_TASKS = NumberTools.clamp(config.getInt("tasks.max-tasks"), 0, 20);
        this.TASK_XP_MULTIPLIER = NumberTools.clamp(config.getDouble("tasks.reward-xp-multiplier"), 0.0, 1_000_000.0);
        this.TASK_MONEY_MULTIPLIER = NumberTools.clamp(config.getDouble("tasks.reward-money-multiplier"), 0.0, 1_000_000.0);
    }
}