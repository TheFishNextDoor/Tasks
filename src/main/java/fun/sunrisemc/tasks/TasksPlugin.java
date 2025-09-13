package fun.sunrisemc.tasks;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
import fun.sunrisemc.tasks.hook.Vault;
import fun.sunrisemc.tasks.player.PlayerLevel;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.scheduler.AutoSave;
import fun.sunrisemc.tasks.scheduler.TaskRefresh;
import fun.sunrisemc.tasks.scheduler.TimerTrigger;
import fun.sunrisemc.tasks.task.TaskConfigurationManager;
import fun.sunrisemc.tasks.unlock.UnlockManager;
import fun.sunrisemc.tasks.utils.CommandUtils;
import fun.sunrisemc.tasks.utils.Log;

public class TasksPlugin extends JavaPlugin {

    private static TasksPlugin instance;

    private static MainConfig mainConfig;

    public void onEnable() {
        instance = this;

        if (Vault.hook(this)) {
            Log.info("Vault hooked");
        } 
        else {
            Log.warning("Vault not found");
        }

        loadConfigs();

        CommandUtils.register(this, "tasksadmin", new TasksAdminCommand());
        CommandUtils.register(this, "tasks", new TasksCommand());
        CommandUtils.register(this, "level", new LevelCommand());
        CommandUtils.register(this, "unlocks", new UnlocksCommand());

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

        AutoSave.start();
        TaskRefresh.start();
        TimerTrigger.start();

        Log.info("Plugin enabled");
    }

    public void onDisable() {
        AutoSave.stop();
        TaskRefresh.stop();
        TimerTrigger.stop();
        PlayerProfileManager.saveAll();
        Log.info("Plugin disabled");
    }

    public static TasksPlugin getInstance() {
        return instance;
    }

    public static MainConfig getMainConfig() {
        return mainConfig;
    }

    public static void loadConfigs() {
        mainConfig = new MainConfig();
        UnlockManager.loadConfig();
        TaskConfigurationManager.loadConfig();
        PlayerProfileManager.reload();
        if (mainConfig.ENABLE_LEVELLING && !PlayerLevel.verify()) {
            Log.severe("PlayerLevel verification failed");
        }
    }
}