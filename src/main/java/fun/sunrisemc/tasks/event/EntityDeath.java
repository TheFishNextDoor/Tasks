package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class EntityDeath implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        // Get the entity that died
        LivingEntity killedEntity = event.getEntity();

        // Get the player who killed the entity, if any
        Player killer = killedEntity.getKiller();

        // Player kill entity
        if (killer != null) {
            // Get the player's profile
            PlayerProfile playerProfile = PlayerProfileManager.get(killer);

            // Get item in the player's hand
            ItemStack itemInKillersHand = PlayerUtils.getItemInHand(killer);

            // Get the killed entities location
            Location entityLocation = killedEntity.getLocation();
            
            // Trigger kill entity tasks
            playerProfile.triggerTasks(TriggerType.KILL_ENTITY, entityLocation, killedEntity, itemInKillersHand, null, 1);

            // Trigger kill entity drop item tasks for each dropped item
            for (ItemStack droppedItem : event.getDrops()) {
                // Get the amount of the dropped item
                int droppedItemAmount = droppedItem.getAmount();

                // Trigger tasks
                playerProfile.triggerTasks(TriggerType.KILL_ENTITY_DROP_ITEM, entityLocation, killedEntity, droppedItem, null, droppedItemAmount);
            }
        }

        // Player death
        if (killedEntity instanceof Player) {
            // Get the killed player
            Player killedPlayer = (Player) killedEntity;

            // Get the killed player's profile
            PlayerProfile playerProfile = PlayerProfileManager.get(killedPlayer);

            // Get the location of the killed player
            Location deathLocation = killedPlayer.getLocation();

            // Get item in killer's hand, if any
            ItemStack itemInKillersHand = null;
            if (killer != null) {
                itemInKillersHand = PlayerUtils.getItemInHand(killer);
            }

            // Trigger death tasks
            playerProfile.triggerTasks(TriggerType.DEATH, deathLocation, killedPlayer, itemInKillersHand, null, 1);
        }
    }
}