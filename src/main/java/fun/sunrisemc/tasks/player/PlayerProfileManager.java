package fun.sunrisemc.tasks.player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fun.sunrisemc.tasks.TasksPlugin;

public class PlayerProfileManager {

    static ConcurrentHashMap<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    public static void load(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        PlayerProfileManager.load(player.getUniqueId());
    }

    public static void load(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        Bukkit.getScheduler().runTaskAsynchronously(TasksPlugin.getInstance(), () -> {
            PlayerProfileManager.get(uuid);
        });
    }

    public static PlayerProfile get(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        return PlayerProfileManager.get(player.getUniqueId());
    }

    public static PlayerProfile get(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
    
        PlayerProfile playerProfile = playerProfiles.get(uuid);
        if (playerProfile == null) {
            playerProfile = new PlayerProfile(uuid);
        }
        return playerProfile;
    }

    public static void refreshAllTasks() {
        playerProfiles.values().forEach(PlayerProfile::refreshTasks);
    }

    public static void reload() {
        PlayerProfileManager.saveAll();
        playerProfiles.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            load(player);
        }
    }

    public static void saveAll() {
        playerProfiles.values().forEach(PlayerProfile::save);
    }
    
}
