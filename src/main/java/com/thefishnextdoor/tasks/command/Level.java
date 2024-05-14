package com.thefishnextdoor.tasks.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thefishnextdoor.tasks.player.PlayerProfile;

import net.md_5.bungee.api.ChatColor;

public class Level implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }
        
        Player player = (Player) sender;
        PlayerProfile playerProfile = PlayerProfile.get(player);
        int level = playerProfile.getLevel();
        int xp = playerProfile.getXpSinceLastLevel();
        int nextLevel = xp + playerProfile.getXpToNextLevel();
        player.sendMessage(playerProfile.getColor() + ChatColor.BOLD + "Level " + level + ChatColor.WHITE + " (" + xp + "/" + nextLevel + ")");
        return true;
    }
}
