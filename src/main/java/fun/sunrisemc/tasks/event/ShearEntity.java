package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ShearEntity implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onShearEntity(@NotNull PlayerShearEntityEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the entity being sheared
        Entity entitySheared = event.getEntity();

        // Get the location of the entity being sheared
        Location entityLocation = entitySheared.getLocation();

        // Get the item used to shear
        ItemStack itemUsed = event.getItem();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.SHEAR_ENTITY, entityLocation, entitySheared, itemUsed, null, 1);
    }
}