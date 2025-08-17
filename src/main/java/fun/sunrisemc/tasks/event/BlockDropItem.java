package fun.sunrisemc.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.task.TriggerType;

public class BlockDropItem implements Listener {
        
    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Block block = event.getBlock();
        event.getItems().forEach(item -> {
            ItemStack itemStack = item.getItemStack();
            playerProfile.triggerTasks(TriggerType.BREAK_BLOCK_DROP_ITEM, block.getLocation(), player, itemStack, block, itemStack.getAmount());
        });
    }
}