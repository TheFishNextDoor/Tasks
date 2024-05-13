package com.thefishnextdoor.tasks.player;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class TasksMessage {

    public static void send(Player player, String title, String info) {
        PlayerProfile playerProfile = PlayerProfile.get(player);
        player.sendMessage(playerProfile.getColor() + "" + ChatColor.BOLD + title + ": " + ChatColor.WHITE + info);
        player.playSound(player.getLocation(), "block.sniffer_egg.plop", 1, 1);
    }
}
