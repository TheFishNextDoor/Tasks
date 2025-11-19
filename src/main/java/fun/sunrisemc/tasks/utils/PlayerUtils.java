package fun.sunrisemc.tasks.utils;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class PlayerUtils {

    public static ArrayList<String> getOnlinePlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public static ItemStack getItemInHand(@NonNull Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = inventory.getItemInMainHand();
        if (mainHand != null) {
            return mainHand;
        }
        return inventory.getItemInOffHand();
    }

    public static Optional<Player> getPlayerByName(@NotNull String name) {
        Player player = org.bukkit.Bukkit.getPlayerExact(name);
        return Optional.ofNullable(player);
    }
}