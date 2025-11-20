package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ItemDamage implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemDamage(@NotNull PlayerItemDamageEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the players location
        Location playerLocation = player.getLocation();

        // Get the item being damaged
        ItemStack itemDamaged = event.getItem();

        // Get the amount of damage
        int damage = event.getDamage();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.DAMAGE_ITEM, playerLocation, player, itemDamaged, null, damage);
    }
}