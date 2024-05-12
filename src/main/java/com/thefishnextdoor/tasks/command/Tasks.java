package com.thefishnextdoor.tasks.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thefishnextdoor.tasks.TasksPlugin;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.PlayerTask;

import net.md_5.bungee.api.ChatColor;

public class Tasks implements CommandExecutor, TabCompleter {

    private final String RELOAD_PERMISSION = "tasks.reload";

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommands = new ArrayList<String>();
            if (sender.hasPermission(RELOAD_PERMISSION)) {
                subcommands.add("reload");
            }
            return subcommands;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(RELOAD_PERMISSION)) {
                TasksPlugin.loadConfigs();
                sender.sendMessage(ChatColor.BLUE + "Plugin reloaded");
                return true;
            }
        }

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
