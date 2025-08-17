package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.task.TriggerType;

public class DropItem implements Listener {

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Item entity = event.getItemDrop();
        ItemStack item = entity.getItemStack();
        playerProfile.triggerTasks(TriggerType.DROP_ITEM, entity.getLocation(), entity, item, null, item.getAmount());
    }
}