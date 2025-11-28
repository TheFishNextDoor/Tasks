package fun.sunrisemc.tasks.utils;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import org.jetbrains.annotations.NotNull;

public class PlayerUtils {

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
}