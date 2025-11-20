package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ThrowEgg implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onThrowEgg(@NotNull PlayerEggThrowEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the egg
        Egg egg = event.getEgg();

        // Get the egg's location
        Location eggLocation = egg.getLocation();

        // Get the block at the egg's location
        Block blockAtEggLocation = eggLocation.getBlock();

        // Get the egg item
        ItemStack eggItem = egg.getItem();
        playerProfile.triggerTasks(TriggerType.THROW_EGG, eggLocation, egg, eggItem, blockAtEggLocation, 1);
    }
}