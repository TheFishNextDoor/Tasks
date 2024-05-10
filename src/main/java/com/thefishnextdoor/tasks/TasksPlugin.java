package com.thefishnextdoor.tasks;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.thefishnextdoor.tasks.command.Level;
import com.thefishnextdoor.tasks.command.Tasks;
import com.thefishnextdoor.tasks.event.BlockBreak;
import com.thefishnextdoor.tasks.event.BlockPlace;
import com.thefishnextdoor.tasks.event.BucketEntity;
import com.thefishnextdoor.tasks.event.EnchantItem;
import com.thefishnextdoor.tasks.event.EntityDamageByEntity;
import com.thefishnextdoor.tasks.event.EntityDeath;
import com.thefishnextdoor.tasks.event.HarvestBlock;
import com.thefishnextdoor.tasks.event.InteractEntity;
import com.thefishnextdoor.tasks.event.ItemBreak;
import com.thefishnextdoor.tasks.event.ItemConsume;
import com.thefishnextdoor.tasks.event.PlayerJoin;
import com.thefishnextdoor.tasks.event.ShearEntity;
import com.thefishnextdoor.tasks.event.TakeLecternBook;
import com.thefishnextdoor.tasks.player.AutoSave;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TaskConfiguration;
import com.thefishnextdoor.tasks.task.TaskRefresh;
import com.thefishnextdoor.tasks.task.TimerTrigger;

public class TasksPlugin extends JavaPlugin {

    private static TasksPlugin instance;

    public void onEnable() {
        instance = this;

        loadConfigs();

        registerCommand("tasks", new Tasks());
        registerCommand("level", new Level());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new InteractEntity(), this);
        pluginManager.registerEvents(new EntityDamageByEntity(), this);
        pluginManager.registerEvents(new EntityDeath(), this);
        pluginManager.registerEvents(new BucketEntity(), this);
        pluginManager.registerEvents(new ShearEntity(), this);
        pluginManager.registerEvents(new EnchantItem(), this);
        pluginManager.registerEvents(new ItemConsume(), this);
        pluginManager.registerEvents(new ItemBreak(), this);
        pluginManager.registerEvents(new BlockBreak(), this);
        pluginManager.registerEvents(new BlockPlace(), this);
        pluginManager.registerEvents(new HarvestBlock(), this);
        pluginManager.registerEvents(new TakeLecternBook(), this);

        AutoSave.start();
        TaskRefresh.start();
        TimerTrigger.start();

        getLogger().info("Plugin enabled");
    }

    public void onDisable() {
        AutoSave.stop();
        TaskRefresh.stop();
        TimerTrigger.stop();
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
