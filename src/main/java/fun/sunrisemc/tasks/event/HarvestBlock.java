package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.InventoryUtils;

public class HarvestBlock implements Listener {

    @EventHandler
    public void onPlayerHarvest(PlayerHarvestBlockEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        Block block = event.getHarvestedBlock();
        ItemStack hand = InventoryUtils.getItemInHand(player);
        Location location = block.getLocation();
        playerProfile.triggerTasks(TriggerType.HARVEST_BLOCK, location, player, hand, block, 1);
        for (ItemStack item : event.getItemsHarvested()) {
            playerProfile.triggerTasks(TriggerType.HARVEST_ITEM, location, player, item, block, item.getAmount());
        }
    }
}