package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.thefishnextdoor.tasks.player.PlayerProfile;

import net.md_5.bungee.api.ChatColor;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        String level = ChatColor.WHITE + "[" + ChatColor.BLUE + playerProfile.getLevel() + ChatColor.WHITE + "]";
        event.setFormat(level + " " + event.getFormat());
    }
}