package fun.sunrisemc.tasks.player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.TasksPlugin;

public class PlayerProfileManager {

    private static @NotNull ConcurrentHashMap<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    // Getting

    @NotNull
    public static PlayerProfile get(@NotNull Player player) {
        UUID uuid = player.getUniqueId();
        PlayerProfile playerProfile = playerProfiles.get(uuid);
        if (playerProfile == null) {
            playerProfile = new PlayerProfile(uuid);
            playerProfiles.put(uuid, playerProfile);
        }

        return playerProfile;
    }

    // Task Refresh

    public static void refreshAllTasks() {
        playerProfiles.values().forEach(PlayerProfile::refreshTasks);
    }

    // Loading and Saving

    public static void preload(@NotNull Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(TasksPlugin.getInstance(), () -> PlayerProfileManager.get(player));
    }

    public static void reload() {
        PlayerProfileManager.saveAll();

        playerProfiles.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == null) {
                continue;
            }

            preload(player);
        }
    }

    public static void saveAll() {
        playerProfiles.values().forEach(PlayerProfile::save);
    }

    protected static boolean unload(@NotNull UUID uuid) {
        return playerProfiles.remove(uuid) != null;
    }
}