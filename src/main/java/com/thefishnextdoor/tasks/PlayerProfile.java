package com.thefishnextdoor.tasks;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

public class PlayerProfile {

    private static HashMap<UUID, PlayerProfile> playerProfiles = new HashMap<>();

    private final UUID uuid;

    private PlayerProfile(@NotNull UUID uuid) {
        this.uuid = uuid;
        playerProfiles.put(uuid, this);
    }

    @NotNull
    public static PlayerProfile get(@NotNull Player player) {
        return get(player.getUniqueId());
    }

    @NotNull
    public static PlayerProfile get(@NotNull UUID uuid) {
        PlayerProfile playerProfile = playerProfiles.get(uuid);
        if (playerProfile == null) {
            playerProfile = new PlayerProfile(uuid);
        }
        return playerProfile;
    }
}
