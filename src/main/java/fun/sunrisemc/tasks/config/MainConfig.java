package fun.sunrisemc.tasks.config;

import org.bukkit.configuration.file.YamlConfiguration;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.file.ConfigFile;
import fun.sunrisemc.tasks.utils.YAMLUtils;

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
        updateConfig();
        
        YamlConfiguration config = ConfigFile.get("config", true);

        // Levels settings

        this.ENABLE_LEVELLING = config.getBoolean("levels.enable", true);
        this.SHOW_LEVEL = config.getBoolean("levels.show-level", true);
        this.CHAT_PREFIX_FORMAT = YAMLUtils.getStringOrDefault(config, "levels.chat-prefix-format", "&f[{color}{level}&f] ");
        this.LEVEL_BASE = YAMLUtils.getDoubleClamped(config, "levels.xp-curve.base", 1.0, 1_000_000.0);
        this.LEVEL_MULTIPLIER = YAMLUtils.getDoubleClamped(config, "levels.xp-curve.multiplier", 1.0, 10.0);

        // Tasks settings

        this.ALLOW_TASK_SKIPPING = config.getBoolean("tasks.allow-skipping", true);
        this.MAX_TASKS = YAMLUtils.getIntClamped(config, "tasks.max-tasks", 0, 20);
        this.TASK_XP_MULTIPLIER = YAMLUtils.getDoubleClamped(config, "tasks.reward-xp-multiplier", 0.0, 1_000_000.0);
        this.TASK_MONEY_MULTIPLIER = YAMLUtils.getDoubleClamped(config, "tasks.reward-money-multiplier", 0.0, 1_000_000.0);
    }

    private void updateConfig() {
        YamlConfiguration config = ConfigFile.get("config", true);

        if (YAMLUtils.moveKey(config, "xp-curve", "levels.xp-curve")) {
            ConfigFile.save("config", config);
        }
    }
}