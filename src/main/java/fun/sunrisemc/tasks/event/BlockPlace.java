package fun.sunrisemc.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        Block block = event.getBlock();
        ItemStack item = PlayerUtils.getItemInHand(player);
        playerProfile.triggerTasks(TriggerType.PLACE_BLOCK, block.getLocation(), player, item, block, 1);
    }
}