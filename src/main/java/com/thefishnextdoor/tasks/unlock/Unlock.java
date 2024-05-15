package com.thefishnextdoor.tasks.unlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.thefishnextdoor.tasks.TasksPlugin;
import com.thefishnextdoor.tasks.file.ConfigFile;
import com.thefishnextdoor.tasks.hook.VaultHook;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.player.TasksMessage;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;

public class Unlock implements Comparable<Unlock> {

    private static ArrayList<Unlock> unlocksSorted = new ArrayList<>();
    private static HashMap<String, Unlock> unlocksLookup = new HashMap<>();


    private static List<String> settings = List.of(
        "level",
        "name",
        "permissions",
        "console-commands",
        "player-commands",
        "messages",
        "run-once"
    );

    private final String id;

    private int level;

    private String name;

    private ArrayList<String> permissions = new ArrayList<String>();
    private ArrayList<String> console_commands = new ArrayList<String>();
    private ArrayList<String> player_commands = new ArrayList<String>();
    private ArrayList<String> messages = new ArrayList<String>();

    private boolean runOnce = true;

    public Unlock(YamlConfiguration config, String id) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        this.id = id;

        Logger logger = TasksPlugin.getInstance().getLogger();
        for (String setting : config.getConfigurationSection(id).getKeys(false)) {
            if (!settings.contains(setting)) {
                logger.warning("Invalid setting for unlock " + id + ": " + setting);
                String possibleSettings = String.join(", ", settings);
                logger.warning("Valid settings are: " + possibleSettings);
            }
        }

        level = config.getInt(id + ".level");

        name = config.getString(id + ".name");
        
        for (String permission : config.getStringList(id + ".permissions")) {
            permissions.add(permission);
        }

        for (String command : config.getStringList(id + ".console-commands")) {
            console_commands.add(command);
        }

        for (String command : config.getStringList(id + ".player-commands")) {
            player_commands.add(command);
        }

        for (String message : config.getStringList(id + ".messages")) {
            messages.add(ChatColor.translateAlternateColorCodes('&', message));
        }

        if (config.contains(id + ".run-once")) {
            runOnce = config.getBoolean(id + ".run-once");
        }

        unlocksSorted.add(this);
        unlocksLookup.put(id, this);
    }

    @Override
    public String toString() {
        String string = name != null ? name : id;
        if (level > 0) {
            string = string + " (Level " + level + ")";
        }
        return string;
    }

    @Override
    public int compareTo(Unlock other) {
        if (other == null) {
            throw new IllegalArgumentException("Other unlock cannot be null");
        }
        return Integer.compare(this.getLevel(), other.getLevel());
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public boolean isValidFor(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }
        if (level < 1 || level > playerProfile.getLevel()) {
            return false;
        }
        if (runOnce && playerProfile.hasCompletedUnlock(id)) {
            return false;
        }
        return true;
    }

    public void giveTo(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }

        Optional<Player> optionalPlayer = playerProfile.getPlayer();
        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();
        boolean unlocked = playerProfile.hasCompletedUnlock(id);
        if (!unlocked) {
            TasksMessage.send(player, playerProfile, "Unlocked", name);
        }

        if (runOnce && unlocked) {
            return;
        }

        if (VaultHook.isUsingVault()) {
            Permission permissionsProvider = VaultHook.getPermissions();
            for (String permission : permissions) {
                if (!player.hasPermission(permission)) {
                    permissionsProvider.playerAdd(player, permission);
                }
            }
        }

        for (String command : console_commands) {
            Server server = Bukkit.getServer();
            command = command.replace("{player}", player.getName());
            server.dispatchCommand(server.getConsoleSender(), command);
        }

        for (String command : player_commands) {
            command = command.replace("{player}", player.getName());
            player.performCommand(command);
        }
        
        for (String message : messages) {
            player.sendMessage(message);
        }
        
        playerProfile.addCompletedUnlock(id);
        return;
    }

    public static Optional<Unlock> get(String id) {
        return Optional.ofNullable(unlocksLookup.get(id));
    }

    public static List<Unlock> getSorted() {
        return Collections.unmodifiableList(unlocksSorted);
    }

    public static ArrayList<String> getIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Unlock unlock : unlocksSorted) {
            ids.add(unlock.getId());
        }
        return ids;
    }

    public static void loadConfig() {
        unlocksSorted.clear();
        unlocksLookup.clear();
        YamlConfiguration config = ConfigFile.get("unlocks");
        for (String id : config.getKeys(false)) {
            new Unlock(config, id);
        }
        Collections.sort(unlocksSorted);
        TasksPlugin.getInstance().getLogger().info("Loaded " + unlocksSorted.size() + " unlocks");
    }
}