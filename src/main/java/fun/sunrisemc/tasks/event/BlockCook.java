package fun.sunrisemc.tasks.event;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class BlockCook implements Listener {

    @EventHandler
    public void onBlockCook(BlockCookEvent event) {
        // Get block
        Block block = event.getBlock();

        // Check if block is a campfire
        if (!(block.getState() instanceof Campfire)) {
            return;
        }

        // Get location
        Location location = block.getLocation();

        // Get nearest player
        Optional<Player> player = getNearestPlayer(location);
        if (!player.isPresent()) {
            return;
        }

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player.get());

        // Get the resulting item
        ItemStack result = event.getResult();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.SMELT_ITEM, location, player.get(), result, block, result.getAmount());
    }

    private Optional<Player> getNearestPlayer(@NonNull Location location) {
        // Get world
        World world = location.getWorld();
        if (world == null) {
            return Optional.empty();
        }

        // Find nearest player in world
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Player player : world.getPlayers()) {
            double distance = player.getLocation().distance(location);
            if (distance < nearestDistance) {
                nearestPlayer = player;
                nearestDistance = distance;
            }
        }
        
        return Optional.ofNullable(nearestPlayer);
    }
}
