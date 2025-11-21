package fun.sunrisemc.tasks.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.config.MainConfig;
import fun.sunrisemc.tasks.permission.Permissions;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.PlayerTask;
import fun.sunrisemc.tasks.task.TaskConfiguration;
import fun.sunrisemc.tasks.task.TaskConfigurationManager;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.unlock.UnlockManager;
import fun.sunrisemc.tasks.utils.PlayerUtils;
import fun.sunrisemc.tasks.utils.StringUtils;

public class TasksAdminCommand implements CommandExecutor, TabCompleter {

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String @NotNull [] args) {
        // Get plugin config
        MainConfig config = TasksPlugin.getMainConfig();

        // /tasksadmin <subcommand1>
        if (args.length == 1) {
            ArrayList<String> subcommands = new ArrayList<String>();
            subcommands.add("help");
            if (sender.hasPermission(Permissions.RELOAD_PERMISSION)) {
                subcommands.add("reload");
            }
            if (sender.hasPermission(Permissions.TASK_PERMISSION)) {
                subcommands.add("task");
            }
            if (config.ENABLE_LEVELLING && sender.hasPermission(Permissions.XP_PERMISSION)) {
                subcommands.add("xp");
            }
            if (sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
                subcommands.add("unlock");
            }
            if (config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
                subcommands.add("skips");
            }
            return subcommands;
        }
        else if (args.length == 2) {
            String subcommand1 = args[0];

            // /tasksadmin task <subcommand2>
            if (subcommand1.equalsIgnoreCase("task") && sender.hasPermission(Permissions.TASK_PERMISSION)) {
                return List.of("list", "give", "remove", "addprogress");
            }
            // /tasksadmin xp <subcommand2>
            else if (subcommand1.equalsIgnoreCase("xp") && config.ENABLE_LEVELLING && sender.hasPermission(Permissions.XP_PERMISSION)) {
                return List.of("give", "take", "set");
            }
            // /tasksadmin unlock <subcommand2>
            else if (subcommand1.equalsIgnoreCase("unlock") && sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
                return List.of("list", "give");
            }
            // /tasksadmin skips <subcommand2>
            else if (subcommand1.equalsIgnoreCase("skips") && config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
                return List.of("give", "take", "set");
            }
        }
        else if (args.length == 3) {
            // /tasksadmin <subcommand1> <subcommand2> <player>
            return PlayerUtils.getOnlinePlayerNames();
        }
        else if (args.length == 4) {
            // Get player
            Optional<Player> player = PlayerUtils.getPlayerByName(args[2]);
            if (player.isEmpty()) {
                return null;
            }

            // Get player profile
            PlayerProfile playerProfile = PlayerProfileManager.get(player.get());

            // Get subcommands
            String subcommand1 = args[0];
            String subcommand2 = args[1];

            if (subcommand1.equalsIgnoreCase("task") && sender.hasPermission(Permissions.TASK_PERMISSION)) {
                // /tasksadmin task give <player> <task>
                if (subcommand2.equalsIgnoreCase("give")) {
                    return TaskConfigurationManager.getIds();
                }
                // /tasksadmin task remove <player> <task>
                else if (subcommand2.equalsIgnoreCase("remove")) {
                    return playerProfile.getTaskIds();
                }
                // /tasksadmin task addprogress <player> <task>
                else if (subcommand2.equalsIgnoreCase("addprogress")) {
                    return playerProfile.getTaskIds();
                }
            }
            else if (subcommand1.equalsIgnoreCase("xp") && config.ENABLE_LEVELLING && sender.hasPermission(Permissions.XP_PERMISSION)) {
                // /tasksadmin xp set <player> <amount>
                if (subcommand2.equalsIgnoreCase("set")) {
                    return List.of(playerProfile.getTotalXp() + "");
                }
            }
            else if (subcommand1.equalsIgnoreCase("unlock") && sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
                // /tasksadmin unlock give <player> <unlock>
                if (subcommand2.equalsIgnoreCase("give")) {
                    return UnlockManager.getIds();
                }
            }
            else if (subcommand1.equalsIgnoreCase("skips") && config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
                // /tasksadmin skips set <player> <amount>
                if (subcommand2.equalsIgnoreCase("set")) {
                    return List.of(playerProfile.getSkips() + "");
                }
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        // Get plugin config
        MainConfig config = TasksPlugin.getMainConfig();

        // Get subcommand1
        String subcommand1 = args.length > 0 ? args[0] : "help";
        
        // Reload //
        if (subcommand1.equalsIgnoreCase("reload") && sender.hasPermission(Permissions.RELOAD_PERMISSION)) {
            // Reload the plugin configuration
            TasksPlugin.reload();

            // Notify sender
            sender.sendMessage(ChatColor.BLUE + "Plugin reloaded.");

            return true;
        }
        // Task //
        else if (subcommand1.equalsIgnoreCase("task") && sender.hasPermission(Permissions.TASK_PERMISSION)) {
            // Ensure sender provided enough arguments
            if (args.length < 3) {
                sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin task <subcommand> <player>");
                return true;
            }

            // Get subcommand2
            String subcommand2 = args[1];

            // Get player
            Optional<Player> player = PlayerUtils.getPlayerByName(args[2]);
            if (player.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            // Get player profile
            PlayerProfile playerProfile = PlayerProfileManager.get(player.get());

            // Task List //
            if (subcommand2.equalsIgnoreCase("list")) {
                // List player tasks
                sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Tasks for " + player.get().getName());

                boolean hasTasks = false;
                for (PlayerTask task : playerProfile.getTasks()) {
                    hasTasks = true;
                    sender.sendMessage(ChatColor.BLUE + task.getTaskConfiguration().getId() + ":" + ChatColor.WHITE + " " + task.toString());
                }
                
                if (!hasTasks) {
                    sender.sendMessage(ChatColor.WHITE + "None");
                }

                return true;
            }
            // Task Give //
            else if (subcommand2.equalsIgnoreCase("give")) {
                // Ensure the sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin task give <player> <task>");
                    return true;
                }

                // Get task configuration
                Optional<TaskConfiguration> taskConfiguration = TaskConfigurationManager.get(args[3]);
                if (taskConfiguration.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Task not found.");
                    return true;
                }
                
                // Create the player task
                PlayerTask playerTask = new PlayerTask(taskConfiguration.get(), playerProfile);

                // Add the task to the player profile
                if (playerProfile.addTask(playerTask)) {
                    sender.sendMessage(ChatColor.BLUE + "Task given.");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Failed to give task.");
                }

                return true;
            }
            // Task Remove //
            else if (subcommand2.equalsIgnoreCase("remove")) {
                // Ensure the sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin task remove <player> <task>");
                    return true;
                }

                // Get the player task
                Optional<PlayerTask> task = playerProfile.getTask(args[3]);
                if (task.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Task not found");
                    return true;
                }

                // Get the task configuration ID
                String taskConfigurationId = task.get().getTaskConfiguration().getId();

                // Remove the task from the player profile
                if (playerProfile.removeTask(taskConfigurationId)) {
                    sender.sendMessage(ChatColor.BLUE + "Task removed.");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Failed to remove task.");
                }

                return true;
            }
            // Task AddProgress //
            else if (subcommand2.equalsIgnoreCase("addprogress")) {
                // Ensure the sender provided enough arguments
                if (args.length < 5) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin task addprogress <player> <task> <amount>");
                    return true;
                }

                // Get the player task
                Optional<PlayerTask> task = playerProfile.getTask(args[3]);
                if (task.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Task not found.");
                    return true;
                }

                // Get the amount to add
                Optional<Integer> amount = StringUtils.parseInteger(args[4]);
                if (amount.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount.");
                    return true;
                }

                // Add progress to the task
                task.get().addProgress(amount.get());

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "Progress added.");
            }
        }
        // XP //
        else if (subcommand1.equalsIgnoreCase("xp") && config.ENABLE_LEVELLING && sender.hasPermission(Permissions.XP_PERMISSION)) {
            // Ensure sender provided enough arguments
            if (args.length < 3) {
                sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin xp <subcommand> <player>");
                return true;
            }

            // Get subcommand2
            String subcommand2 = args[1];

            // Get player
            Optional<Player> player = PlayerUtils.getPlayerByName(args[2]);
            if (player.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            // Get player profile
            PlayerProfile playerProfile = PlayerProfileManager.get(player.get());

            // XP Give //
            if (subcommand2.equalsIgnoreCase("give")) {
                // Ensure sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin xp give <player> <amount>");
                    return true;
                }

                // Get amount to give
                Optional<Integer> amount = StringUtils.parseInteger(args[3]);
                if (amount.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount.");
                    return true;
                }

                // Give XP to player
                playerProfile.addXp(amount.get());

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "XP given.");

                return true;
            }
            // XP Take //
            else if (subcommand2.equalsIgnoreCase("take")) {
                // Ensure sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin xp take <player> <amount>");
                    return true;
                }

                // Get amount to take
                Optional<Integer> amount = StringUtils.parseInteger(args[3]);
                if (amount.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount.");
                    return true;
                }

                // Take XP from player
                playerProfile.removeXp(amount.get());

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "XP taken.");

                return true;
            }
            // XP Set //
            else if (subcommand2.equalsIgnoreCase("set")) {
                // Ensure sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin xp set <player> <amount>");
                    return true;
                }

                // Get amount to set
                Optional<Integer> amount = StringUtils.parseInteger(args[3]);
                if (amount.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount.");
                    return true;
                }

                // Set player XP
                playerProfile.setXp(amount.get());

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "XP set.");

                return true;
            }
        }
        // Unlock //
        else if (subcommand1.equalsIgnoreCase("unlock") && sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
            // Ensure sender provided enough arguments
            if (args.length < 3) {
                sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin unlock <subcommand> <player>");
                return true;
            }

            // Get subcommand2
            String subcommand2 = args[1];

            // Get player
            Optional<Player> player = PlayerUtils.getPlayerByName(args[2]);
            if (player.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            // Get player profile
            PlayerProfile playerProfile = PlayerProfileManager.get(player.get());

            // Unlock List //
            if (subcommand2.equalsIgnoreCase("list")) {
                // List player unlocks
                sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Unlocks for " + player.get().getName());
                boolean hasUnlocks = false;
                for (Unlock unlock : UnlockManager.getSorted()) {
                    if (playerProfile.hasCompletedUnlock(unlock.getId())) {
                        hasUnlocks = true;
                        sender.sendMessage(ChatColor.BLUE + unlock.getId() + ":" + ChatColor.WHITE + " " + unlock.toString());
                    }
                }
                if (!hasUnlocks) {
                    sender.sendMessage(ChatColor.WHITE + "None");
                }

                return true;
            }
            // Unlock Give //
            else if (subcommand2.equalsIgnoreCase("give")) {
                // Ensure sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin unlock give <player> <unlock>");
                    return true;
                }

                // Get unlock
                Optional<Unlock> unlock = UnlockManager.get(args[3]);
                if (unlock.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Unlock not found.");
                    return true;
                }

                // Give unlock to player
                unlock.get().giveTo(playerProfile);

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "Unlock given.");

                return true;
            }
        }
        // Skips //
        else if (subcommand1.equalsIgnoreCase("skips") && config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
            // Ensure sender provided enough arguments
            if (args.length < 3) {
                sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin skips <subcommand> <player>");
                return true;
            }

            // Get subcommand2
            String subcommand2 = args[1];

            // Get player
            Optional<Player> player = PlayerUtils.getPlayerByName(args[2]);
            if (player.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }

            // Get player profile
            PlayerProfile playerProfile = PlayerProfileManager.get(player.get());

            // Skips Give //
            if (subcommand2.equalsIgnoreCase("give")) {
                // Ensure sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin skips give <player> <amount>");
                    return true;
                }

                // Get amount
                Optional<Integer> amount = StringUtils.parseInteger(args[3]);
                if (amount.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount.");
                    return true;
                }

                // Give skips to player
                playerProfile.addSkips(amount.get());

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "Skips given.");

                return true;
            }
            // Skips Take //
            else if (subcommand2.equalsIgnoreCase("take")) {
                // Ensure sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin skips take <player> <amount>");
                    return true;
                }

                // Get amount
                Optional<Integer> amount = StringUtils.parseInteger(args[3]);
                if (amount.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount.");
                    return true;
                }

                // Take skips from player
                playerProfile.removeSkips(amount.get());

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "Skips taken.");

                return true;
            }
            // Skips Set //
            else if (subcommand2.equalsIgnoreCase("set")) {
                // Ensure sender provided enough arguments
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.BLUE + "Usage: " + ChatColor.WHITE + "/tasksadmin skips set <player> <amount>");
                    return true;
                }

                // Get amount
                Optional<Integer> amount = StringUtils.parseInteger(args[3]);
                if (amount.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount.");
                    return true;
                }

                // Set player skips
                playerProfile.setSkips(amount.get());

                // Notify sender
                sender.sendMessage(ChatColor.BLUE + "Skips set.");

                return true;
            }
        }

        sendHelpMessage(sender);
        return true;
    }

    public static void sendHelpMessage(CommandSender sender) {
        // Get plugin config
        MainConfig mainConfig = TasksPlugin.getMainConfig();
        
        // Send help message
        sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Tasks Admin Help");
        sender.sendMessage(ChatColor.BLUE + "/tasksadmin help " + ChatColor.WHITE + "Show this help message");
        if (sender.hasPermission(Permissions.RELOAD_PERMISSION)) {
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin reload " + ChatColor.WHITE + "Reload the plugin");
        }
        if (sender.hasPermission(Permissions.TASK_PERMISSION)) {
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin task list <player> " + ChatColor.WHITE + "List a player's tasks");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin task give <player> <task> " + ChatColor.WHITE + "Give a player a task");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin task remove <player> <task> " + ChatColor.WHITE + "Remove a task from a player");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin task addprogress <player> <task> <amount> " + ChatColor.WHITE + "Add progress to a task");
        }
        if (mainConfig.ENABLE_LEVELLING &&sender.hasPermission(Permissions.XP_PERMISSION)) {
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin xp give <player> <amount> " + ChatColor.WHITE + "Give a player XP");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin xp take <player> <amount> " + ChatColor.WHITE + "Take XP from a player");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin xp set <player> <amount> " + ChatColor.WHITE + "Set a player's XP");
        }
        if (sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin unlock list <player> " + ChatColor.WHITE + "List a player's unlocks");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin unlock give <player> <unlock> " + ChatColor.WHITE + "Give a player an unlock");
        }
        if (mainConfig.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin skips give <player> <amount> " + ChatColor.WHITE + "Give a player skips");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin skips take <player> <amount> " + ChatColor.WHITE + "Take skips from a player");
            sender.sendMessage(ChatColor.BLUE + "/tasksadmin skips set <player> <amount> " + ChatColor.WHITE + "Set a player's skips");
        }
    }
}