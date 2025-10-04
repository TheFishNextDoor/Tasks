package fun.sunrisemc.tasks.config;

import org.bukkit.configuration.file.YamlConfiguration;

import fun.sunrisemc.tasks.file.ConfigFile;

public class MainConfig {

    public final boolean ENABLE_LEVELLING;
    public final boolean SHOW_LEVEL;
    public final String CHAT_PREFIX_FORMAT;
    public final double LEVEL_BASE;
    public final double LEVEL_MULTIPLIER;

    public final boolean ALLOW_TASK_SKIPPING;
    public final int MAX_TASKS;
    public final double TASK_XP_MULTIPLIER;
    public final double TASK_MONEY_MULTIPLIER;

    public MainConfig() {
        updateConfig();
        
        YamlConfiguration config = ConfigFile.get("config", true);

        this.ENABLE_LEVELLING = config.getBoolean("levels.enable", true);
        this.SHOW_LEVEL = config.getBoolean("levels.show-level", true);
        this.CHAT_PREFIX_FORMAT = config.getString("levels.chat-prefix-format", "&f[{color}{level}&f] ");
        this.LEVEL_BASE = getDoubleClamped(config, "levels.xp-curve.base", 1.0, 1_000_000.0);
        this.LEVEL_MULTIPLIER = getDoubleClamped(config, "levels.xp-curve.multiplier", 1.0, 10.0);
        this.ALLOW_TASK_SKIPPING = config.getBoolean("tasks.allow-skipping", true);
        this.MAX_TASKS = getIntClamped(config, "tasks.max-tasks", 0, 20);
        this.TASK_XP_MULTIPLIER = getDoubleClamped(config, "tasks.reward-xp-multiplier", 0.0, 1_000_000.0);
        this.TASK_MONEY_MULTIPLIER = getDoubleClamped(config, "tasks.reward-money-multiplier", 0.0, 1_000_000.0);
    }

    private void updateConfig() {
        ConfigFile.moveKeyIfExists("config", "xp-curve", "levels.xp-curve");
    }

    private int getIntClamped(YamlConfiguration config, String path, int min, int max) {
        int value = config.getInt(path);
        return Math.clamp(value, min, max);
    }

    private double getDoubleClamped(YamlConfiguration config, String path, double min, double max) {
        double value = config.getDouble(path);
        return Math.clamp(value, min, max);
    }
}