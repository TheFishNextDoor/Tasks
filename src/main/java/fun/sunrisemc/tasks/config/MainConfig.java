package fun.sunrisemc.tasks.config;

import org.bukkit.configuration.file.YamlConfiguration;

import fun.sunrisemc.tasks.utils.ConfigFile;
import fun.sunrisemc.tasks.utils.NumberUtils;

public class MainConfig {

    public final boolean ENABLE_LEVELLING;
    public final boolean SHOW_LEVEL;
    public final double LEVEL_BASE;
    public final double LEVEL_MULTIPLIER;

    public final boolean ALLOW_TASK_SKIPPING;
    public final int MAX_TASKS;
    public final double TASK_XP_MULTIPLIER;
    public final double TASK_MONEY_MULTIPLIER;

    public MainConfig() {
        YamlConfiguration config = ConfigFile.get("config");

        this.ENABLE_LEVELLING = config.getBoolean("levels.enable", true);
        this.SHOW_LEVEL = config.getBoolean("levels.show-level", true);
        this.LEVEL_BASE = NumberUtils.clamp(config.getDouble("levels.xp-curve.base"), 1.0, 1_000_000.0);
        this.LEVEL_MULTIPLIER = NumberUtils.clamp(config.getDouble("levels.xp-curve.multiplier"), 1.0, 10.0);

        this.ALLOW_TASK_SKIPPING = config.getBoolean("tasks.allow-skipping", true);
        this.MAX_TASKS = NumberUtils.clamp(config.getInt("tasks.max-tasks"), 0, 20);
        this.TASK_XP_MULTIPLIER = NumberUtils.clamp(config.getDouble("tasks.reward-xp-multiplier"), 0.0, 1_000_000.0);
        this.TASK_MONEY_MULTIPLIER = NumberUtils.clamp(config.getDouble("tasks.reward-money-multiplier"), 0.0, 1_000_000.0);
    }
}