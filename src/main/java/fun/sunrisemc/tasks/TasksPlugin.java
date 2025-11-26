package fun.sunrisemc.tasks;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fun.sunrisemc.tasks.command.LevelCommand;
import fun.sunrisemc.tasks.command.TasksCommand;
import fun.sunrisemc.tasks.command.TasksAdminCommand;
import fun.sunrisemc.tasks.command.UnlocksCommand;
import fun.sunrisemc.tasks.config.MainConfig;
import fun.sunrisemc.tasks.event.BedEnter;
import fun.sunrisemc.tasks.event.BedLeave;
import fun.sunrisemc.tasks.event.BlockBreak;
import fun.sunrisemc.tasks.event.BlockCook;
import fun.sunrisemc.tasks.event.BlockDropItem;
import fun.sunrisemc.tasks.event.BlockPlace;
import fun.sunrisemc.tasks.event.BucketEmpty;
import fun.sunrisemc.tasks.event.BucketEntity;
import fun.sunrisemc.tasks.event.BucketFill;
import fun.sunrisemc.tasks.event.CraftItem;
import fun.sunrisemc.tasks.event.DropItem;
import fun.sunrisemc.tasks.event.EnchantItem;
import fun.sunrisemc.tasks.event.EntityDamageByEntity;
import fun.sunrisemc.tasks.event.EntityDeath;
import fun.sunrisemc.tasks.event.EntityTame;
import fun.sunrisemc.tasks.event.FurnaceExtract;
import fun.sunrisemc.tasks.event.HarvestBlock;
import fun.sunrisemc.tasks.event.InteractEntity;
import fun.sunrisemc.tasks.event.ItemBreak;
import fun.sunrisemc.tasks.event.ItemConsume;
import fun.sunrisemc.tasks.event.ItemDamage;
import fun.sunrisemc.tasks.event.ItemMend;
import fun.sunrisemc.tasks.event.ManipulateArmorstand;
import fun.sunrisemc.tasks.event.PickupArrow;
import fun.sunrisemc.tasks.event.PlayerChat;
import fun.sunrisemc.tasks.event.PlayerJoin;
import fun.sunrisemc.tasks.event.Portal;
import fun.sunrisemc.tasks.event.Respawn;
import fun.sunrisemc.tasks.event.Riptide;
import fun.sunrisemc.tasks.event.ShearEntity;
import fun.sunrisemc.tasks.event.TakeLecternBook;
import fun.sunrisemc.tasks.event.Teleport;
import fun.sunrisemc.tasks.event.ThrowEgg;
import fun.sunrisemc.tasks.file.ConfigFile;
import fun.sunrisemc.tasks.file.DataFile;
import fun.sunrisemc.tasks.file.PlayerDataFile;
import fun.sunrisemc.tasks.hook.Vault;
import fun.sunrisemc.tasks.player.PlayerLevel;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.scheduler.AutoSaveTask;
import fun.sunrisemc.tasks.scheduler.TaskRefreshTask;
import fun.sunrisemc.tasks.scheduler.TimerTriggerTask;
import fun.sunrisemc.tasks.task.TaskConfigurationManager;
import fun.sunrisemc.tasks.unlock.UnlockManager;
import fun.sunrisemc.tasks.utils.YAMLUtils;

public class TasksPlugin extends JavaPlugin {

    // Plugin Instance

    private static @Nullable TasksPlugin instance;

    // Configs

    private static @Nullable MainConfig mainConfig;

    // Java Plugin

    public void onEnable() {
        instance = this;

        applyUpdates();

        mainConfig = new MainConfig();

        if (Vault.hook(this)) {
            TasksPlugin.logInfo("Vault hooked.");
        } 
        else {
            TasksPlugin.logWarning("Vault not found. Economy and Permissions features will be disabled.");
        }

        UnlockManager.loadConfig();

        TaskConfigurationManager.loadConfig();

        PlayerProfileManager.reload();
        
        if (getMainConfig().ENABLE_LEVELLING && !PlayerLevel.verify()) {
            TasksPlugin.logSevere("Player level verification failed.");
        }

        registerCommand("tasks", new TasksCommand());
        registerCommand("level", new LevelCommand());
        registerCommand("unlocks", new UnlocksCommand());
        registerCommand("tasksadmin", new TasksAdminCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new PlayerChat(), this);
        pluginManager.registerEvents(new InteractEntity(), this);
        pluginManager.registerEvents(new EntityDamageByEntity(), this);
        pluginManager.registerEvents(new EntityDeath(), this);
        pluginManager.registerEvents(new BucketEntity(), this);
        pluginManager.registerEvents(new ShearEntity(), this);
        pluginManager.registerEvents(new EntityTame(), this);
        pluginManager.registerEvents(new ManipulateArmorstand(), this);
        pluginManager.registerEvents(new ThrowEgg(), this);
        pluginManager.registerEvents(new CraftItem(), this);
        pluginManager.registerEvents(new EnchantItem(), this);
        pluginManager.registerEvents(new ItemConsume(), this);
        pluginManager.registerEvents(new FurnaceExtract(), this);
        pluginManager.registerEvents(new BlockCook(), this);
        pluginManager.registerEvents(new ItemBreak(), this);
        pluginManager.registerEvents(new DropItem(), this);
        pluginManager.registerEvents(new ItemDamage(), this);
        pluginManager.registerEvents(new ItemMend(), this);
        pluginManager.registerEvents(new PickupArrow(), this);
        pluginManager.registerEvents(new BlockBreak(), this);
        pluginManager.registerEvents(new BlockDropItem(), this);
        pluginManager.registerEvents(new BlockPlace(), this);
        pluginManager.registerEvents(new HarvestBlock(), this);
        pluginManager.registerEvents(new TakeLecternBook(), this);
        pluginManager.registerEvents(new BucketFill(), this);
        pluginManager.registerEvents(new BucketEmpty(), this);
        pluginManager.registerEvents(new BedEnter(), this);
        pluginManager.registerEvents(new BedLeave(), this);
        pluginManager.registerEvents(new Riptide(), this);
        pluginManager.registerEvents(new Teleport(), this);
        pluginManager.registerEvents(new Portal(), this);
        pluginManager.registerEvents(new Respawn(), this);

        AutoSaveTask.start();
        TaskRefreshTask.start();
        TimerTriggerTask.start();

        TasksPlugin.logInfo("Plugin enabled.");
    }

    public void onDisable() {
        AutoSaveTask.stop();
        TaskRefreshTask.stop();
        TimerTriggerTask.stop();

        PlayerProfileManager.saveAll();

        TasksPlugin.logInfo("Plugin disabled.");
    }

    // Plugin Instance

    @NotNull
    public static TasksPlugin getInstance() {
        if (instance != null) {
            return instance;
        }
        else {
            throw new IllegalStateException("Plugin instance not initialized.");
        }
    }

    // Configs

    @NotNull
    public static MainConfig getMainConfig() {
        if (mainConfig != null) {
            return mainConfig;
        }
        else {
            throw new IllegalStateException("Main config not loaded.");
        }
    }

    // Reloading

    public static void reload() {
        mainConfig = new MainConfig();

        UnlockManager.loadConfig();

        TaskConfigurationManager.loadConfig();

        PlayerProfileManager.reload();

        if (getMainConfig().ENABLE_LEVELLING && !PlayerLevel.verify()) {
            TasksPlugin.logSevere("PlayerLevel verification failed.");
        }
    }

    // Logging

    public static void logInfo(@NotNull String message) {
        getInstance().getLogger().info(message);
    }

    public static void logWarning(@NotNull String message) {
        getInstance().getLogger().warning(message);
    }

    public static void logSevere(@NotNull String message) {
        getInstance().getLogger().severe(message);
    }

    // Command Registration

    private boolean registerCommand(@NotNull String commandName, @NotNull CommandExecutor commandExecutor) {
        PluginCommand command = getCommand(commandName);
        if (command == null) {
            TasksPlugin.logSevere("Command '" + commandName + "' not found in plugin.yml.");
            return false;
        }

        command.setExecutor(commandExecutor);

        if (commandExecutor instanceof TabCompleter) {
            command.setTabCompleter((TabCompleter) commandExecutor);
        }

        return true;
    }

    // Updates

    private void applyUpdates() {
        // 1.0.0 -> 1.1.0: Move "xp-curve" to "levels.xp-curve"
        YamlConfiguration config = ConfigFile.get("config", true);

        if (YAMLUtils.moveKey(config, "xp-curve", "levels.xp-curve")) {
            ConfigFile.save("config", config);

            logInfo("Config file updated to 1.1.0 format.");
        }

        // 1.1.0 -> 1.2.0: Player data files moved to new folder
        ArrayList<String> playerDataFileNames = new ArrayList<>();
        for (String name : DataFile.getNames()) {
            // Check that it is a player uuid (correct length)
            if (name.length() != 36) {
                continue;
            }

            playerDataFileNames.add(name);
        }

        if (!playerDataFileNames.isEmpty()) {
            logInfo("Updating player data files to new 1.2.0 format...");

            for (String playerDataFileName : playerDataFileNames) {
                YamlConfiguration playerData = DataFile.get(playerDataFileName);

                UUID uuid = UUID.fromString(playerDataFileName);

                // Save as a player data file rather than a generic data file
                if (PlayerDataFile.save(uuid, playerData)) {
                    DataFile.delete(playerDataFileName);
                }
            }

            logInfo("Player data files updated to 1.2.0 format.");
        }
    }
}