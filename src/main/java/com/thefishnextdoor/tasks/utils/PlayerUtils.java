package com.thefishnextdoor.tasks.utils;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static Optional<Player> getNearestPlayer(Location location) {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Player player : location.getWorld().getPlayers()) {
            double distance = player.getLocation().distance(location);
            if (distance < nearestDistance) {
                nearestPlayer = player;
                nearestDistance = distance;
            }
        }
        return Optional.ofNullable(nearestPlayer);
    }
}