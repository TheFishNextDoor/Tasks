package fun.sunrisemc.tasks.unlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fun.sunrisemc.tasks.hook.Vault;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.utils.Log;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.permission.Permission;

public class Unlock implements Comparable<Unlock> {

    private final List<String> SETTINGS = List.of(
        "level",
        "name",
        "permissions",
        "console-commands",
        "player-commands",
        "messages"
    );

    private final String id;

    private int level;

    private String name;

    private ArrayList<String> permissions = new ArrayList<String>();
    private ArrayList<String> console_commands = new ArrayList<String>();
    private ArrayList<String> player_commands = new ArrayList<String>();
    private ArrayList<String> messages = new ArrayList<String>();



    Unlock(YamlConfiguration config, String id) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        this.id = id;

        for (String setting : config.getConfigurationSection(id).getKeys(false)) {
            if (!SETTINGS.contains(setting)) {
                Log.warning("Invalid setting for unlock " + id + ": " + setting);
                String possibleSettings = String.join(", ", SETTINGS);
                Log.warning("Valid settings are: " + possibleSettings);
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

        UnlockManager.unlocksSorted.add(this);
        UnlockManager.unlocksLookup.put(id, this);
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

        givePermissions(player);

        if (playerProfile.hasCompletedUnlock(id)) {
            return;
        }

        playerProfile.sendNotification("Unlocked", name);

        String playerName = player.getName();
        for (String command : console_commands) {
            Server server = Bukkit.getServer();
            command = command.replace("{player}", playerName);
            server.dispatchCommand(server.getConsoleSender(), command);
        }
        for (String command : player_commands) {
            command = command.replace("{player}", playerName);
            player.performCommand(command);
        }
        for (String message : messages) {
            message = message.replace("{player}", playerName);
            player.sendMessage(message);
        }
        
        playerProfile.addCompletedUnlock(id);
        return;
    }

    public void givePermissions(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (Vault.isUsingVault()) {
            Permission permissionsProvider = Vault.getPermissions();
            for (String permission : permissions) {
                if (!player.hasPermission(permission)) {
                    permissionsProvider.playerAdd(player, permission);
                }
            }
        }
    }
}