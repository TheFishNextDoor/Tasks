package com.thefishnextdoor.tasks;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.thefishnextdoor.tasks.command.Tasks;
import com.thefishnextdoor.tasks.event.BlockBreak;
import com.thefishnextdoor.tasks.event.BlockPlace;
import com.thefishnextdoor.tasks.player.AutoSave;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TaskConfiguration;
import com.thefishnextdoor.tasks.task.TaskRefresh;

public class TasksPlugin extends JavaPlugin {

    private static TasksPlugin instance;

    public void onEnable() {
        instance = this;

        loadConfigs();

        registerCommand("tasks", new Tasks());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BlockBreak(), this);
        pluginManager.registerEvents(new BlockPlace(), this);

        AutoSave.start();
        TaskRefresh.start();

        getLogger().info("Plugin enabled");
    }

    public void onDisable() {
        AutoSave.stop();
        TaskRefresh.stop();
        PlayerProfile.saveAll();
        getLogger().info("Plugin disabled");
    }

    public static TasksPlugin getInstance() {
        return instance;
    }

    public static void loadConfigs() {
        TaskConfiguration.loadConfig();
    }

    private void registerCommand(String commandName, CommandExecutor commandHandler) {
        PluginCommand command = getCommand(commandName);
        if (command == null) {
            getLogger().warning("Failed to register command: " + commandName);
            return;
        }
        command.setExecutor(commandHandler);
        if (commandHandler instanceof TabCompleter) {
            command.setTabCompleter((TabCompleter) commandHandler);
        }
    }
}
