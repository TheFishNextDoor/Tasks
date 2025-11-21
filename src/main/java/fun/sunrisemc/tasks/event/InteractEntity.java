package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class InteractEntity implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(@NotNull PlayerInteractEntityEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the entity being interacted with
        Entity entity = event.getRightClicked();

        // Get the location of the entity
        Location entityLocation = entity.getLocation();

        // Get the item in the player's hand
        ItemStack itemInHand = PlayerUtils.getItemInHand(player).orElse(null);

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.INTERACT_ENTITY, entityLocation, entity, itemInHand, null, 1);
    }
}