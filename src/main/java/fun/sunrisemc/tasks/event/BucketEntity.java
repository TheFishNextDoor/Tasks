package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.task.TriggerType;

public class BucketEntity implements Listener {

    @EventHandler
    public void onBucketEntity(PlayerBucketEntityEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Entity entity = event.getEntity();
        ItemStack item = event.getEntityBucket();
        playerProfile.triggerTasks(TriggerType.BUCKET_ENTITY, entity.getLocation(), entity, item, null, 1);
    }
}