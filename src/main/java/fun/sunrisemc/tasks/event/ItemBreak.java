package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ItemBreak implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemBreak(@NotNull PlayerItemBreakEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the player's location
        Location playerLocation = player.getLocation();

        // Get the broken item
        ItemStack itemBroken = event.getBrokenItem();

        // Get the amount broken (should always be 1)
        int amountBroken = itemBroken.getAmount();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.BREAK_ITEM, playerLocation, player, itemBroken, null, amountBroken);
    }
}