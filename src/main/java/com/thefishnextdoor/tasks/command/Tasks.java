package com.thefishnextdoor.tasks.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.PlayerTask;

import net.md_5.bungee.api.ChatColor;

public class Tasks implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;
        PlayerProfile profile = PlayerProfile.get(player);

        player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Tasks");
        Integer i = 1;
        for (PlayerTask task : profile.getTasks()) {
            player.sendMessage(i.toString() + ". " + task.toString());
        }
        return true;
    }
}
