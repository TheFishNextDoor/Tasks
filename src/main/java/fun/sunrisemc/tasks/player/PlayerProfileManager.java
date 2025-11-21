package fun.sunrisemc.tasks.player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.TasksPlugin;

public class PlayerProfileManager {

    private static @NotNull ConcurrentHashMap<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    public static void load(@NotNull Player player) {
        PlayerProfileManager.load(player.getUniqueId());
    }

    public static void load(@NotNull UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(TasksPlugin.getInstance(), () -> PlayerProfileManager.get(uuid));
    }

    public static boolean unload(@NotNull UUID uuid) {
        return playerProfiles.remove(uuid) != null;
    }

    public static PlayerProfile get(@NotNull Player player) {
        return PlayerProfileManager.get(player.getUniqueId());
    }

    @NotNull
    public static PlayerProfile get(@NotNull UUID uuid) {
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
            if (player == null) {
                continue;
            }

            load(player);
        }
    }

    public static void saveAll() {
        playerProfiles.values().forEach(PlayerProfile::save);
    }
}