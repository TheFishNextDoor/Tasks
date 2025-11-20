package fun.sunrisemc.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class BedLeave implements Listener {

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        // Get player
        Player player = event.getPlayer();

        // Get player profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get bed
        Block bed = event.getBed();

        // Get item in players hand
        ItemStack item = PlayerUtils.getItemInHand(player);

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.LEAVE_BED, bed.getLocation(), player, item, bed, 1);
    }
}