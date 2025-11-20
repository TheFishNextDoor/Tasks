package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ManipulateArmorstand implements Listener {
        
    @EventHandler(ignoreCancelled = true)
    public void onManipulateArmorstand(@NotNull PlayerArmorStandManipulateEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the armorstand
        Entity armorstand = event.getRightClicked();

        // Get the location of the armorstand
        Location armorstandLocation = armorstand.getLocation();

        // Get the item involved in the manipulation (if both are present, prefer armorstand item)
        ItemStack armorStandItem = event.getArmorStandItem();
        ItemStack playerItem = event.getPlayerItem();
        ItemStack item = armorStandItem != null ? armorStandItem : playerItem;

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.MANIPULATE_ARMORSTAND, armorstandLocation, armorstand, item, null, 1);
    }
}