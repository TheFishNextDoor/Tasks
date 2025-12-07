package fun.sunrisemc.tasks.config;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.file.ConfigFile;

public class MainConfig {

    // Levels settings

    public final boolean ENABLE_LEVELLING;
    public final boolean SHOW_LEVEL;
    public final @NotNull String CHAT_PREFIX_FORMAT;
    public final double LEVEL_BASE;
    public final double LEVEL_MULTIPLIER;

    // Tasks settings

    public final boolean ALLOW_TASK_SKIPPING;
    public final int MAX_TASKS;
    public final double TASK_XP_MULTIPLIER;
    public final double TASK_MONEY_MULTIPLIER;

    public MainConfig() {
        TasksPlugin.logInfo("Loading main config...");

        ConfigFile config = ConfigFile.get("config", true);

        // Levels settings
        this.ENABLE_LEVELLING = config.getBoolean("levels.enable").orElse(true);
        this.SHOW_LEVEL = config.getBoolean("levels.show-level").orElse(true);
        this.CHAT_PREFIX_FORMAT = config.getString("levels.chat-prefix-format").orElse("&f[{color}{level}&f] ");
        this.LEVEL_BASE = config.getDoubleClamped("levels.xp-curve.base", 1.0, 1_000_000.0).orElse(10.0);
        this.LEVEL_MULTIPLIER = config.getDoubleClamped("levels.xp-curve.multiplier", 1.0, 10.0).orElse(1.02);
        // Tasks settings
        this.ALLOW_TASK_SKIPPING = config.getBoolean("tasks.allow-skipping").orElse(true);
        this.MAX_TASKS = config.getIntClamped("tasks.max-tasks", 0, 20).orElse(8);
        this.TASK_XP_MULTIPLIER = config.getDoubleClamped("tasks.reward-xp-multiplier", 0.0, 1_000_000.0).orElse(1.0);
        this.TASK_MONEY_MULTIPLIER = config.getDoubleClamped("tasks.reward-money-multiplier", 0.0, 1_000_000.0).orElse(1.0);

        TasksPlugin.logInfo("Main config loaded.");
    }
}