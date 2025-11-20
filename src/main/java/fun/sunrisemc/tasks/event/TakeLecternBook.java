package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class TakeLecternBook implements Listener {

    @EventHandler
    public void onTakeLecternBook(@NotNull PlayerTakeLecternBookEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the book taken
        ItemStack bookTaken = event.getBook();

        // Get the lectern block
        Block lectern = event.getLectern().getBlock();

        // Get the lectern location
        Location lecternLocation = lectern.getLocation();

        // Get the amount of the book taken (default to 1)
        int amount = 1;
        if (bookTaken != null) {
            amount = bookTaken.getAmount();
        }

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.TAKE_LECTERN_BOOK, lecternLocation, player, bookTaken, lectern, amount);
    }
}