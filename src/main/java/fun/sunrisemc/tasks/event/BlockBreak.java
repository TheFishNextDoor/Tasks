package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class BlockBreak implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get player profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get block broken
        Block blockBroken = event.getBlock();

        // Get block location
        Location blockLocation = blockBroken.getLocation();

        // Get item in player's hand
        ItemStack itemInHand = PlayerUtils.getItemInHand(player).orElse(null);

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.BREAK_BLOCK, blockLocation, player, itemInHand, blockBroken, 1);
    }
}