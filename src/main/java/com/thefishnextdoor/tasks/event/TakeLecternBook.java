package com.thefishnextdoor.tasks.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class TakeLecternBook implements Listener {

    @EventHandler
    public void onTakeLecternBook(PlayerTakeLecternBookEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        ItemStack book = event.getBook();
        Block lectern = event.getLectern().getBlock();
        playerProfile.triggerTasks(TriggerType.TAKE_LECTERN_BOOK, lectern.getLocation(), player, book, lectern, book.getAmount());
    }
}