package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class EntityDamageByEntity implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        // Check if damager is a player
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            return;
        }

        // Get the player who caused the damage
        Player playerWhoDamaged = (Player) damager;

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(playerWhoDamaged);

        // Get the damaged entity
        Entity damagedEntity = event.getEntity();

        // Get the location of the damaged entity
        Location damagedEntityLocation = damagedEntity.getLocation();
        
        // Get the item in the player's hand
        ItemStack itemInPlayersHand = PlayerUtils.getItemInHand(playerWhoDamaged);

        // Get the damage dealt
        int damage = (int) event.getDamage();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.DAMAGE_ENTITY, damagedEntityLocation, damagedEntity, itemInPlayersHand, null, damage);
    }
}