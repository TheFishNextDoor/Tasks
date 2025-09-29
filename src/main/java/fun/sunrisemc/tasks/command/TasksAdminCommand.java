package fun.sunrisemc.tasks.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
import net.md_5.bungee.api.ChatColor;

public class TasksAdminCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        MainConfig config = TasksPlugin.getMainConfig();
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
            String subcommand = args[0];
            if (subcommand.equalsIgnoreCase("task") && sender.hasPermission(Permissions.TASK_PERMISSION)) {
                return List.of("list", "give", "remove", "addprogress");
            }
            else if (subcommand.equalsIgnoreCase("xp") && config.ENABLE_LEVELLING && sender.hasPermission(Permissions.XP_PERMISSION)) {
                return List.of("give", "take", "set");
            }
            else if (subcommand.equalsIgnoreCase("unlock") && sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
                return List.of("list", "give");
            }
            else if (subcommand.equalsIgnoreCase("skips") && config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
                return List.of("give", "take", "set");
            }
            else {
                return null;
            }
        }
        else if (args.length == 3) {
            return PlayerUtils.getPlayerNames();
        }
        else if (args.length == 4) {
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                return null;
            }

            PlayerProfile playerProfile = PlayerProfileManager.get(player);
            String subCommand = args[0];
            String subSubCommand = args[1];
            if (subCommand.equalsIgnoreCase("task") && sender.hasPermission(Permissions.TASK_PERMISSION)) {
                if (subSubCommand.equalsIgnoreCase("give")) {
                    return TaskConfigurationManager.getIds();
                }
                else if (subSubCommand.equalsIgnoreCase("remove")) {
                    return playerProfile.getTaskIds();
                }
                else if (subSubCommand.equalsIgnoreCase("addprogress")) {
                    return playerProfile.getTaskIds();
                }
                else {
                    return null;
                }
            }
            else if (subCommand.equalsIgnoreCase("xp") && config.ENABLE_LEVELLING && sender.hasPermission(Permissions.XP_PERMISSION)) {
                if (subSubCommand.equalsIgnoreCase("set")) {
                    return List.of(playerProfile.getTotalXp() + "");
                }
                else {
                    return null;
                }
            }
            else if (subCommand.equalsIgnoreCase("unlock") && sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
                if (subSubCommand.equalsIgnoreCase("give")) {
                    return UnlockManager.getIds();
                }
                else {
                    return null;
                }
            }
            else if (subCommand.equalsIgnoreCase("skips") && config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
                if (subSubCommand.equalsIgnoreCase("set")) {
                    return List.of(playerProfile.getSkips() + "");
                }
                else {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MainConfig config = TasksPlugin.getMainConfig();
        String subCommand = args.length > 0 ? args[0] : "";
        
        // Reload //
        if (subCommand.equalsIgnoreCase("reload") && sender.hasPermission(Permissions.RELOAD_PERMISSION)) {
            TasksPlugin.loadConfigs();
            sender.sendMessage(ChatColor.BLUE + "Plugin reloaded");
            return true;
        }

        // Task //
        else if (subCommand.equalsIgnoreCase("task") && sender.hasPermission(Permissions.TASK_PERMISSION)) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "You must specify a subcommand");
                return true;
            }
            String subSubCommand = args[1];
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "You must specify a player");
                return true;
            }
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found");
                return true;
            }
            PlayerProfile playerProfile = PlayerProfileManager.get(player);

            // Task List //
            if (subSubCommand.equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Tasks for " + player.getName());
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
            else if (subSubCommand.equalsIgnoreCase("give")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify a task");
                    return true;
                }
                Optional<TaskConfiguration> taskConfiguration = TaskConfigurationManager.get(args[3]);
                if (taskConfiguration.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Task not found");
                    return true;
                }
                if (playerProfile.addTask(new PlayerTask(taskConfiguration.get(), playerProfile))) {
                    sender.sendMessage(ChatColor.BLUE + "Task given");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Failed to give task");
                }
                return true;
            }
            // Task Remove //
            else if (subSubCommand.equalsIgnoreCase("remove")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify a task");
                    return true;
                }
                Optional<PlayerTask> task = playerProfile.getTask(args[3]);
                if (task.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Task not found");
                    return true;
                }
                if (playerProfile.removeTask(task.get().getTaskConfiguration().getId())) {
                    sender.sendMessage(ChatColor.BLUE + "Task removed");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Failed to remove task");
                }
                return true;
            }
            // Task AddProgress //
            else if (subSubCommand.equalsIgnoreCase("addprogress")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify a task");
                    return true;
                }
                Optional<PlayerTask> task = playerProfile.getTask(args[3]);
                if (task.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Task not found");
                    return true;
                }
                if (args.length < 5) {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[4]);
                    task.get().addProgress(amount);
                    sender.sendMessage(ChatColor.BLUE + "Progress added");
                    return true;
                }
                catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount");
                    return true;
                }
            }
            // Task Invalid Subcommand //
            else {
                sender.sendMessage(ChatColor.RED + "Invalid subcommand");
                return true;
            }
        }

        // XP //
        else if (subCommand.equalsIgnoreCase("xp") && config.ENABLE_LEVELLING && sender.hasPermission(Permissions.XP_PERMISSION)) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "You must specify a subcommand");
                return true;
            }
            String subSubCommand = args[1];
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "You must specify a player");
                return true;
            }
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found");
                return true;
            }
            PlayerProfile playerProfile = PlayerProfileManager.get(player);

            // XP Give //
            if (subSubCommand.equalsIgnoreCase("give")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[3]);
                    playerProfile.addXp(amount);
                    sender.sendMessage(ChatColor.BLUE + "XP given");
                    return true;
                }
                catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount");
                    return true;
                }
            }
            // XP Take //
            else if (subSubCommand.equalsIgnoreCase("take")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[3]);
                    playerProfile.removeXp(amount);
                    sender.sendMessage(ChatColor.BLUE + "XP taken");
                    return true;
                }
                catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount");
                    return true;
                }
            }
            // XP Set //
            else if (subSubCommand.equalsIgnoreCase("set")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[3]);
                    playerProfile.setXp(amount);
                    sender.sendMessage(ChatColor.BLUE + "XP set");
                    return true;
                }
                catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount");
                    return true;
                }
            }
            // XP Invalid Subcommand //
            else {
                sender.sendMessage(ChatColor.RED + "Invalid subcommand");
                return true;
            }
        }

        // Unlock //
        else if (subCommand.equalsIgnoreCase("unlock") && sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "You must specify a subcommand");
                return true;
            }
            String subSubCommand = args[1];
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "You must specify a player");
                return true;
            }
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found");
                return true;
            }
            PlayerProfile playerProfile = PlayerProfileManager.get(player);

            // Unlock List //
            if (subSubCommand.equalsIgnoreCase("list")) {
                sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Unlocks for " + player.getName());
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
            else if (subSubCommand.equalsIgnoreCase("give")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify an unlock");
                    return true;
                }
                Optional<Unlock> unlock = UnlockManager.get(args[3]);
                if (unlock.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "Unlock not found");
                    return true;
                }
                unlock.get().giveTo(playerProfile);
                sender.sendMessage(ChatColor.BLUE + "Unlock given");
                return true;
            }
            // Unlock Invalid Subcommand //
            else {
                sender.sendMessage(ChatColor.RED + "Invalid subcommand");
                return true;
            }
        }

        // Skips //
        else if (subCommand.equalsIgnoreCase("skips") && config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "You must specify a subcommand");
                return true;
            }
            String subSubCommand = args[1];
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "You must specify a player");
                return true;
            }
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found");
                return true;
            }
            PlayerProfile playerProfile = PlayerProfileManager.get(player);

            // Skips Give //
            if (subSubCommand.equalsIgnoreCase("give")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[3]);
                    playerProfile.addSkips(amount);
                    sender.sendMessage(ChatColor.BLUE + "Skips given");
                    return true;
                }
                catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount");
                    return true;
                }
            }
            // Skips Take //
            else if (subSubCommand.equalsIgnoreCase("take")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[3]);
                    playerProfile.removeSkips(amount);
                    sender.sendMessage(ChatColor.BLUE + "Skips taken");
                    return true;
                }
                catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount");
                    return true;
                }
            }
            // Skips Set //
            else if (subSubCommand.equalsIgnoreCase("set")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "You must specify an amount");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[3]);
                    playerProfile.setSkips(amount);
                    sender.sendMessage(ChatColor.BLUE + "Skips set");
                    return true;
                }
                catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Invalid amount");
                    return true;
                }
            }
            // Skips Invalid Subcommand //
            else {
                sender.sendMessage(ChatColor.RED + "Invalid subcommand");
                return true;
            }
        }

        // Help (default) //
        else {
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
            if (config.ENABLE_LEVELLING &&sender.hasPermission(Permissions.XP_PERMISSION)) {
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin xp give <player> <amount> " + ChatColor.WHITE + "Give a player XP");
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin xp take <player> <amount> " + ChatColor.WHITE + "Take XP from a player");
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin xp set <player> <amount> " + ChatColor.WHITE + "Set a player's XP");
            }
            if (sender.hasPermission(Permissions.UNLOCK_PERMISSION)) {
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin unlock list <player> " + ChatColor.WHITE + "List a player's unlocks");
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin unlock give <player> <unlock> " + ChatColor.WHITE + "Give a player an unlock");
            }
            if (config.ALLOW_TASK_SKIPPING && sender.hasPermission(Permissions.SKIPS_PERMISSION)) {
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin skips give <player> <amount> " + ChatColor.WHITE + "Give a player skips");
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin skips take <player> <amount> " + ChatColor.WHITE + "Take skips from a player");
                sender.sendMessage(ChatColor.BLUE + "/tasksadmin skips set <player> <amount> " + ChatColor.WHITE + "Set a player's skips");
            }
            return true;
        }
    }
}
