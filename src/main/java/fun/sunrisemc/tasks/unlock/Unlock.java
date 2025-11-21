package fun.sunrisemc.tasks.unlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;

import net.milkbowl.vault.permission.Permission;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.hook.Vault;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.utils.YAMLUtils;

public class Unlock implements Comparable<Unlock> {

    private final @NotNull List<String> SETTINGS = List.of(
        "level",
        "name",
        "permissions",
        "console-commands",
        "player-commands",
        "messages"
    );

    private final @NotNull String id;

    private int level;

    private Optional<String> name;

    private @NotNull ArrayList<String> permissions = new ArrayList<String>();
    private @NotNull ArrayList<String> consoleCommands = new ArrayList<String>();
    private @NotNull ArrayList<String> playerCommands = new ArrayList<String>();
    private @NotNull ArrayList<String> messages = new ArrayList<String>();

    Unlock(@NotNull YamlConfiguration config, @NotNull String id) {
        this.id = id;

        for (String setting : YAMLUtils.getKeys(config, id)) {
            if (!SETTINGS.contains(setting)) {
                TasksPlugin.logWarning("Invalid setting for unlock " + id + ": " + setting + ".");
                String possibleSettings = String.join(", ", SETTINGS);
                TasksPlugin.logWarning("Valid settings are: " + possibleSettings + ".");
            }
        }

        level = config.getInt(id + ".level");

        name = Optional.ofNullable(config.getString(id + ".name"));
        
        for (String permission : config.getStringList(id + ".permissions")) {
            permissions.add(permission);
        }

        for (String command : config.getStringList(id + ".console-commands")) {
            consoleCommands.add(command);
        }

        for (String command : config.getStringList(id + ".player-commands")) {
            playerCommands.add(command);
        }

        for (String message : config.getStringList(id + ".messages")) {
            String messageFormatted = ChatColor.translateAlternateColorCodes('&', message);
            messages.add(messageFormatted);
        }
    }

    @Override
    @NotNull
    public String toString() {
        String string = name.isPresent() ? name.get() : id;
        if (level > 0) {
            string = string + " (Level " + level + ")";
        }
        return string;
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

    public boolean isValidFor(@NotNull PlayerProfile playerProfile) {
        if (level < 1 || level > playerProfile.getLevel()) {
            return false;
        }
        return true;
    }

    public void giveTo(@NotNull PlayerProfile playerProfile) {
        Optional<Player> optionalPlayer = playerProfile.getPlayer();
        if (!optionalPlayer.isPresent()) {
            return;
        }
        Player player = optionalPlayer.get();

        givePermissions(player);

        if (playerProfile.hasCompletedUnlock(id)) {
            return;
        }

        if (name.isPresent()) {
            playerProfile.sendNotification("Unlocked", name.get());
        }

        String playerName = player.getName();
        for (String command : consoleCommands) {
            Server server = Bukkit.getServer();
            command = command.replace("{player}", playerName);
            server.dispatchCommand(server.getConsoleSender(), command);
        }
        for (String command : playerCommands) {
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

    public void givePermissions(@NotNull Player player) {
        Optional<Permission> permissionsProvider = Vault.getPermissions();
        if (!permissionsProvider.isPresent()) {
            return;
        }

        for (String permission : permissions) {
            if (!player.hasPermission(permission)) {
                permissionsProvider.get().playerAdd(player, permission);
            }
        }
    }
}