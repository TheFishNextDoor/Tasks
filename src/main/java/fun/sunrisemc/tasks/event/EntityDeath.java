package fun.sunrisemc.tasks.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.InventoryUtils;

public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        ItemStack hand = null;

        if (killer != null) {
            hand = InventoryUtils.getItemInHand(killer);
            PlayerProfile playerProfile = PlayerProfileManager.get(killer);
            playerProfile.triggerTasks(TriggerType.KILL_ENTITY, entity.getLocation(), entity, hand, null, 1);
            for (ItemStack item : event.getDrops()) {
                playerProfile.triggerTasks(TriggerType.KILL_ENTITY_DROP_ITEM, entity.getLocation(), entity, item, null, item.getAmount());
            }
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            PlayerProfile playerProfile = PlayerProfileManager.get(player);
            if (killer != null) {
                hand = InventoryUtils.getItemInHand(killer);
            }
            playerProfile.triggerTasks(TriggerType.DEATH, player.getLocation(), player, hand, null, 1);
        }
    }
}