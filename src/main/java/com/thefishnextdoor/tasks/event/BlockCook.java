package com.thefishnextdoor.tasks.event;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.toolkit.PlayerTools;

public class BlockCook implements Listener {

    @EventHandler
    public void onBlockCook(BlockCookEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof Campfire)) {
            return;
        }
        Location location = block.getLocation();
        Optional<Player> player = PlayerTools.getNearestPlayer(location);
        if (!player.isPresent()) {
            return;
        }
        PlayerProfile playerProfile = PlayerProfile.get(player.get());
        ItemStack result = event.getResult();
        playerProfile.triggerTasks(TriggerType.SMELT_ITEM, location, player.get(), result, block, result.getAmount());
    }
}
