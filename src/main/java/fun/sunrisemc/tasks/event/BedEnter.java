package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class BedEnter implements Listener {
    
    @EventHandler(ignoreCancelled = true)
    public void onBedEnter(@NotNull PlayerBedEnterEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get bed entered
        Block enteredBed = event.getBed();

        // Get bed location
        Location bedLocation = enteredBed.getLocation();

        // Get item in player's hand
        ItemStack itemInHand = PlayerUtils.getItemInHand(player);

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.ENTER_BED, bedLocation, player, itemInHand, enteredBed, 1);
    }
}