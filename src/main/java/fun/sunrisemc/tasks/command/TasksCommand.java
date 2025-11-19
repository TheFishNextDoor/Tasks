package fun.sunrisemc.tasks.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.config.MainConfig;
import fun.sunrisemc.tasks.permission.Permissions;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.PlayerTask;
import fun.sunrisemc.tasks.utils.StringUtils;

public class TasksCommand implements CommandExecutor, TabCompleter {

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String @NotNull [] args) {
        // /tasks <subcommand>
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
            // Get plugin config
            MainConfig mainConfig = TasksPlugin.getMainConfig();

            // Get subcommand
            String subcommand = args[0];

            // /tasks color <color>
            if (subcommand.equalsIgnoreCase("color") && sender.hasPermission(Permissions.COLOR_PERMISSION)) {
                return List.of("#RRGGBB", "black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white");
            }
            // /tasks skip <number>
            else if (subcommand.equalsIgnoreCase("skip") && mainConfig.ALLOW_TASK_SKIPPING && sender instanceof Player) {
                // Get player profile
                PlayerProfile playerProfile = PlayerProfileManager.get((Player) sender);

                // Build task numbers list
                ArrayList<String> taskNumbers = new ArrayList<String>();

                int i = 1;
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        // Ensure sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get subcommand
        String subCommand = args.length > 0 ? args[0] : "";

        // Color //
        if (subCommand.equalsIgnoreCase("color") && sender.hasPermission(Permissions.COLOR_PERMISSION)) {
            // Ensure the player provided enough arguments
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /tasks color <color>");
                return true;
            }

            // Get color
            Optional<ChatColor> color = StringUtils.parseChatColor(args[1]);
            if (color.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Invalid color.");
                return true;
            }

            // Set color
            playerProfile.setColor(color.get());

            // Notify player
            playerProfile.sendNotification("Color Changed.");

            return true;
        }
        // Skip //
        else if (subCommand.equalsIgnoreCase("skip") && TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING) {
            // Ensure the player provided enough arguments
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Usage: /tasks skip <number>");
                return true;
            }

            // Ensure the player has skips remaining
            if (playerProfile.getSkips() <= 0) {
                player.sendMessage(ChatColor.RED + "You do not have any skips.");
                return true;
            }

            // Get task number
            Optional<Integer> taskNumber = StringUtils.parseInteger(args[1]);
            if (taskNumber.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Invalid task number.");
                return true;
            }

            // Validate task number
            if (taskNumber.get() < 1 || taskNumber.get() > playerProfile.getTasks().size()) {
                player.sendMessage(ChatColor.RED + "Invalid task number.");
                return true;
            }

            // Get task to skip
            PlayerTask task = playerProfile.getTasks().get(taskNumber.get() - 1);

            // Attempt to skip task
            if (playerProfile.skip(task)) {
                player.sendMessage(playerProfile.getColor() + "You have " + playerProfile.getSkips() + " skip(s) remaining.");
            }
            else {
                player.sendMessage(ChatColor.RED + "Cannot skip task.");
            }

            return true;
        }
        // Skips //
        else if (subCommand.equalsIgnoreCase("skips") && TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING) {
            // Get skips remaining
            int skips = playerProfile.getSkips();

            // Notify player
            if (skips == 1) {
                sender.sendMessage(playerProfile.getColor() + "You have 1 skip remaining.");
            }
            else {
                sender.sendMessage(playerProfile.getColor() + "You have " + skips + " skips remaining.");
            }

            return true;
        }
        // List (default) //
        else {
            // List tasks
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