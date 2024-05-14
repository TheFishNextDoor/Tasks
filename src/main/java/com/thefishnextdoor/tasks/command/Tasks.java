package com.thefishnextdoor.tasks.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.player.TasksMessage;
import com.thefishnextdoor.tasks.task.PlayerTask;

import net.md_5.bungee.api.ChatColor;

public class Tasks implements CommandExecutor, TabCompleter {

    private final String COLOR_PERMISSION = "tasks.color";

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommands = new ArrayList<String>();
            if (sender.hasPermission(COLOR_PERMISSION)) {
                subcommands.add("color");
            }
            return subcommands;
        }
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
        String subCommand = args.length > 0 ? args[0] : "";

        // Color //
        if (subCommand.equalsIgnoreCase("color") && sender.hasPermission(COLOR_PERMISSION)) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /tasks color <color>");
                return true;
            }
            ChatColor color;
            try {
                color = ChatColor.of(args[1]);
                playerProfile.setColor(color);
            }
            catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Invalid color");
                return true;
            }
            TasksMessage.send(player, playerProfile, "Color Changed", color.getName());
            return true;
        }
        // List (default) //
        else {
            player.sendMessage(playerProfile.getColor() + ChatColor.BOLD + "Tasks");
            Integer i = 1;
            for (PlayerTask task : playerProfile.getTasks()) {
                player.sendMessage(i.toString() + ". " + task.toString());
                i++;
            }
            return true;
        }
    }
}
