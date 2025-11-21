package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class EntityTame implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityTame(@NotNull EntityTameEvent event) {
        // Check if the tamer is a player
        AnimalTamer tamer = event.getOwner();
        if (!(tamer instanceof Player)) {
            return;
        }

        // Get the player
        Player player = (Player) tamer;

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the entity being tamed
        LivingEntity tamedEntity = event.getEntity();

        // Get the item in the player's hand
        ItemStack itemInHand = PlayerUtils.getItemInHand(player).orElse(null);

        // Get the location of the tamed entity
        Location locationOfTamedEntity = tamedEntity.getLocation();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.TAME_ENTITY, locationOfTamedEntity, tamedEntity, itemInHand, null, 1);
    }
}