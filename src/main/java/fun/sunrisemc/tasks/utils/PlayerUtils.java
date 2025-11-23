package fun.sunrisemc.tasks.utils;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import org.jetbrains.annotations.NotNull;

public class PlayerUtils {

    @NotNull
    public static ArrayList<String> getOnlinePlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == null) {
                continue;
            }
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    public static Optional<ItemStack> getItemInHand(@NotNull Player player) {
        PlayerInventory inventory = player.getInventory();

        // Check main hand
        ItemStack mainHandMaterial = inventory.getItemInMainHand();
        if (!mainHandMaterial.getType().isAir()) {
            return Optional.of(mainHandMaterial);
        }

        // Check off hand
        ItemStack offHandMaterial = inventory.getItemInOffHand();
        if (!offHandMaterial.getType().isAir()) {
            return Optional.of(offHandMaterial);
        }

        // No item in either hand
        return Optional.empty();
    }

    public static Optional<Player> getPlayerByName(@NotNull String name) {
        Player player = org.bukkit.Bukkit.getPlayerExact(name);
        return Optional.ofNullable(player);
    }
}