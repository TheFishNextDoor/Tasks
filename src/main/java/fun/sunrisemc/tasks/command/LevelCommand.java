package fun.sunrisemc.tasks.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.unlock.Unlock;
import net.md_5.bungee.api.ChatColor;

public class LevelCommand implements CommandExecutor, TabCompleter {

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
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        int level = playerProfile.getLevel();
        int xp = playerProfile.getXpSinceLastLevel();
        int nextLevel = xp + playerProfile.getXpToNextLevel();
        player.sendMessage(playerProfile.getColor() + ChatColor.BOLD + "Level " + level + ChatColor.WHITE + " (" + xp + "/" + nextLevel + ")");
        if (TasksPlugin.getMainConfig().ALLOW_TASK_SKIPPING) {
            player.sendMessage(ChatColor.WHITE + "You have " + playerProfile.getSkips() + " skip(s) remaining");
        }
        player.sendMessage(playerProfile.getColor() + ChatColor.BOLD + "Upcoming Unlocks");
        int shownUnlocks = 0;
        for (Unlock unlock : Unlock.getSorted()) {
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