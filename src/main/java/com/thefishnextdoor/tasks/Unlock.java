package com.thefishnextdoor.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.thefishnextdoor.tasks.file.ConfigFile;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;

public class Unlock implements Comparable<Unlock> {

    private static ArrayList<Unlock> unlocks = new ArrayList<>();

    private static List<String> settings = List.of(
        "level",
        "message",
        "permissions",
        "commands"
    );

    private final String id;

    private int level;

    private String message;

    private ArrayList<String> permissions = new ArrayList<String>();
    private ArrayList<String> console_commands = new ArrayList<String>();
    private ArrayList<String> player_commands = new ArrayList<String>();

    public Unlock(YamlConfiguration config, String id) {
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

        message = config.getString(id + ".message");
        
        for (String permission : config.getStringList(id + ".permissions")) {
            permissions.add(permission);
        }

        for (String command : config.getStringList(id + ".console-commands")) {
            console_commands.add(command);
        }

        for (String command : config.getStringList(id + ".player-commands")) {
            player_commands.add(command);
        }

        unlocks.add(this);
    }

    @Override
    public String toString() {
        return ChatColor.BLUE + "" + getLevel() + "" + ChatColor.WHITE + ": " + message;
    }

    @Override
    public int compareTo(Unlock other) {
        return Integer.compare(this.getLevel(), other.getLevel());
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void giveTo(PlayerProfile playerProfile) {
        Optional<Player> optionalPlayer = playerProfile.getPlayer();
        if (!optionalPlayer.isPresent()) {
            return;
        }

        Player player = optionalPlayer.get();
        player.sendMessage(ChatColor.BLUE + "" +  ChatColor.BOLD + "Unlocked: " + ChatColor.WHITE + message);

        Optional<Permission> permissionsProvider = TasksPlugin.getPermissions();
        if (permissionsProvider.isPresent()) {
            for (String permission : permissions) {
                if (!player.hasPermission(permission)) {
                    permissionsProvider.get().playerAdd(player, permission);
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
        
        playerProfile.addCompletedUnlock(id);
        return;
    }

    public static List<Unlock> getAll() {
        return Collections.unmodifiableList(unlocks);
    }

    public static void checkUnlocks(PlayerProfile playerProfile) {
        for (Unlock unlock : unlocks) {
            if (unlock.getLevel() > playerProfile.getLevel() ) {
                continue;
            }
            if (playerProfile.hasCompletedUnlock(unlock.getId())) {
                continue;
            }
            unlock.giveTo(playerProfile);
        }
    }

    public static void loadConfig() {
        unlocks.clear();
        YamlConfiguration config = ConfigFile.get("unlocks");
        for (String id : config.getKeys(false)) {
            new Unlock(config, id);
        }
        Collections.sort(unlocks);
        TasksPlugin.getInstance().getLogger().info("Loaded " + unlocks.size() + " unlocks");
    }
}