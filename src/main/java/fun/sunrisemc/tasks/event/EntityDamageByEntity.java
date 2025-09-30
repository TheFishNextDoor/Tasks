package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class EntityDamageByEntity implements Listener {
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            PlayerProfile playerProfile = PlayerProfileManager.get(player);
            ItemStack item = PlayerUtils.getItemInHand(player);
            int damage = (int) event.getDamage();
            playerProfile.triggerTasks(TriggerType.DAMAGE_ENTITY, damaged.getLocation(), damaged, item, null, damage);
        }
    }
}