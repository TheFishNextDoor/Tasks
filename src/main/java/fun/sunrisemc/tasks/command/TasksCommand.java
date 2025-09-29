package fun.sunrisemc.tasks.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.permission.Permissions;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.PlayerTask;
import net.md_5.bungee.api.ChatColor;

public class TasksCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommands = new ArrayList<String>();
            if (sender.hasPermission(Permissions.COLOR_PERMISSION)) {
                subcommands.add("color");
            }
            if (TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING) {
                subcommands.add("skip");
                subcommands.add("skips");
            }
            return subcommands;
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("color") && sender.hasPermission(Permissions.COLOR_PERMISSION)) {
                return List.of("#RRGGBB", "black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white");
            }
            else if (args[0].equalsIgnoreCase("skip") && TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING && sender instanceof Player) {
                PlayerProfile playerProfile = PlayerProfileManager.get((Player) sender);
                int i = 1;
                ArrayList<String> taskNumbers = new ArrayList<String>();
                for (PlayerTask task : playerProfile.getTasks()) {
                    if (task.getTaskConfiguration().isSkippable()) {
                        taskNumbers.add(Integer.toString(i));
                    }
                    i++;
                }
                return taskNumbers;
            }
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
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        String subCommand = args.length > 0 ? args[0] : "";

        // Color //
        if (subCommand.equalsIgnoreCase("color") && sender.hasPermission(Permissions.COLOR_PERMISSION)) {
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
            playerProfile.sendNotification("Color Changed");
            return true;
        }
        // Skip //
        else if (subCommand.equalsIgnoreCase("skip") && TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /tasks skip <number>");
                return true;
            }
            if (playerProfile.getSkips() <= 0) {
                sender.sendMessage(ChatColor.RED + "You do not have any skips");
                return true;
            }
            Integer taskNumber;
            try {
                taskNumber = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid task number");
                return true;
            }
            if (taskNumber < 1 || taskNumber > playerProfile.getTasks().size()) {
                sender.sendMessage(ChatColor.RED + "Invalid task number");
                return true;
            }
            PlayerTask task = playerProfile.getTasks().get(taskNumber - 1);
            if (playerProfile.skip(task)) {
                sender.sendMessage(playerProfile.getColor() + "You have " + playerProfile.getSkips() + " skip(s) remaining");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Cannot skip task");
            }
            return true;
        }
        else if (subCommand.equalsIgnoreCase("skips") && TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING) {
            int skips = playerProfile.getSkips();
            if (skips == 1) {
                sender.sendMessage(playerProfile.getColor() + "You have 1 skip remaining");
            }
            else {
                sender.sendMessage(playerProfile.getColor() + "You have " + skips + " skips remaining");
            }
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
