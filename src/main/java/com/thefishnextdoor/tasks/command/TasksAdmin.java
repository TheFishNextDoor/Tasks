package com.thefishnextdoor.tasks.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.thefishnextdoor.tasks.TasksPlugin;
import net.md_5.bungee.api.ChatColor;

public class TasksAdmin implements CommandExecutor, TabCompleter {

    private final String RELOAD_PERMISSION = "tasks.admin.reload";

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommands = new ArrayList<String>();
            subcommands.add("help");
            if (sender.hasPermission(RELOAD_PERMISSION)) {
                subcommands.add("reload");
            }
            return subcommands;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String subCommand = args.length > 0 ? args[0] : "";
        
        // Reload //
        if (subCommand.equalsIgnoreCase("reload") && sender.hasPermission(RELOAD_PERMISSION)) {
            TasksPlugin.loadConfigs();
            sender.sendMessage(ChatColor.BLUE + "Plugin reloaded");
            return true;
        }
        // Help (default) //
        else {
            sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Tasks Admin Help");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin help" + ChatColor.WHITE + " - Show this help message");
            if (sender.hasPermission(RELOAD_PERMISSION)) {
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin reload" + ChatColor.WHITE + " - Reload the plugin");
            }
            return true;
        }
    }
}
