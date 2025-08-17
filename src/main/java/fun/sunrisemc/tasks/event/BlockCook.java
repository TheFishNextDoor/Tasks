package fun.sunrisemc.tasks.event;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class BlockCook implements Listener {

    @EventHandler
    public void onBlockCook(BlockCookEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof Campfire)) {
            return;
        }
        Location location = block.getLocation();
        Optional<Player> player = PlayerUtils.getNearestPlayer(location);
        if (!player.isPresent()) {
            return;
        }
        PlayerProfile playerProfile = PlayerProfile.get(player.get());
        ItemStack result = event.getResult();
        playerProfile.triggerTasks(TriggerType.SMELT_ITEM, location, player.get(), result, block, result.getAmount());
    }
}
