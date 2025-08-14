package com.thefishnextdoor.tasks.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtils {

    public static ItemStack getItemInHand(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = inventory.getItemInMainHand();
        if (mainHand != null) {
            return mainHand;
        }
        return inventory.getItemInOffHand();
    }
}
