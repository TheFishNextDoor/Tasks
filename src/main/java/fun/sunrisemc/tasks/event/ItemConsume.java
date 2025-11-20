package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ItemConsume implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onConsume(@NotNull PlayerItemConsumeEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the players location
        Location playerLocation = player.getLocation();

        // Get the item consumed
        ItemStack itemConsumed = event.getItem();

        // Get the amount consumed (should always be 1)
        int amountConsumed = itemConsumed.getAmount();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.CONSUME_ITEM, playerLocation, player, itemConsumed, null, amountConsumed);
    }
}