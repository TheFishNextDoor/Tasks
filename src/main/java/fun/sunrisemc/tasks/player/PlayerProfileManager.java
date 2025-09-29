package fun.sunrisemc.tasks.player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import fun.sunrisemc.tasks.TasksPlugin;

public class PlayerProfileManager {

    private static ConcurrentHashMap<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    public static void load(@NonNull Player player) {
        PlayerProfileManager.load(player.getUniqueId());
    }

    public static void load(@NonNull UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(TasksPlugin.getInstance(), () -> PlayerProfileManager.get(uuid));
    }

    public static boolean unload(@NonNull UUID uuid) {
        return playerProfiles.remove(uuid) != null;
    }

    public static PlayerProfile get(@NonNull Player player) {
        return PlayerProfileManager.get(player.getUniqueId());
    }

    public static PlayerProfile get(@NonNull UUID uuid) {
        PlayerProfile playerProfile = playerProfiles.get(uuid);
        if (playerProfile == null) {
            playerProfile = new PlayerProfile(uuid);
            playerProfiles.put(uuid, playerProfile);
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