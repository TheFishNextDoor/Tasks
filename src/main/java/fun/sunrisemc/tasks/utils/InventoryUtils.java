package fun.sunrisemc.tasks.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;

public class InventoryUtils {

    public static ItemStack getItemInHand(@NonNull Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = inventory.getItemInMainHand();
        if (mainHand != null) {
            return mainHand;
        }
        return inventory.getItemInOffHand();
    }
}