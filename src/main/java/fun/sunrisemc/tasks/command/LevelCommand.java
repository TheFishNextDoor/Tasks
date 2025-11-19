package fun.sunrisemc.tasks.command;

import java.util.List;

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
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.unlock.UnlockManager;

public class LevelCommand implements CommandExecutor, TabCompleter {

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
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

        // Get plugin config
        MainConfig config = TasksPlugin.getMainConfig();

        // Check if leveling is enabled
        if (!config.ENABLE_LEVELLING) {
            player.sendMessage(ChatColor.RED + "Leveling is disabled on this server.");
            return true;
        }
        
        // Get player profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get player info
        int level = playerProfile.getLevel();
        int xpSinceLastLevel = playerProfile.getXpSinceLastLevel();
        int xpToNextLevel = xpSinceLastLevel + playerProfile.getXpToNextLevel();
        String color = playerProfile.getColor();

        // Send level
        player.sendMessage(color + ChatColor.BOLD + "Level " + level + ChatColor.WHITE + " (" + xpSinceLastLevel + "/" + xpToNextLevel + ")");

        // Send skips info
        if (TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING) {
            int skips = playerProfile.getSkips();
            if (skips == 1) {
                player.sendMessage(ChatColor.WHITE + "You have 1 skip remaining.");
            }
            else {
                player.sendMessage(ChatColor.WHITE + "You have " + skips + " skips remaining.");
            }
        }

        // Send upcoming unlocks
        player.sendMessage(color + ChatColor.BOLD + "Upcoming Unlocks");
        int shownUnlocks = 0;
        for (Unlock unlock : UnlockManager.getSorted()) {
            if (shownUnlocks >= 5) {
                break;
            }
            if (unlock.getLevel() > level) {
                player.sendMessage(unlock.toString());
                shownUnlocks++;
            }
        }
        if (shownUnlocks == 0) {
            player.sendMessage(ChatColor.WHITE + "No upcoming unlocks");
        }
        
        return true;
    }
}