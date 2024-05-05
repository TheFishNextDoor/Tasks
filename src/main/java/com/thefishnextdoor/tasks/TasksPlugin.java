package com.thefishnextdoor.tasks;

import org.bukkit.plugin.java.JavaPlugin;

public class TasksPlugin extends JavaPlugin {

    public void onEnable() {
        getLogger().info("Plugin enabled");
    }

    public void onDisable() {
        getLogger().info("Plugin disabled");
    }
}
