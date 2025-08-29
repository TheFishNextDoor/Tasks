package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;

public class ShearEntity implements Listener {
    
    @EventHandler
    public void onShearEntity(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        Entity entity = event.getEntity();
        ItemStack item = event.getItem();
        playerProfile.triggerTasks(TriggerType.SHEAR_ENTITY, entity.getLocation(), entity, item, null, 1);
    }
}