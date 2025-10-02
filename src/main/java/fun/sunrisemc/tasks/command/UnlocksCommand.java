package fun.sunrisemc.tasks.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.unlock.UnlockManager;

public class UnlocksCommand implements CommandExecutor, TabCompleter {

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
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        List<Unlock> unlocks = UnlockManager.getSorted();
        boolean hasUnlocks = false;
        player.sendMessage(playerProfile.getColor() + ChatColor.BOLD + "Unlocks");
        for (Unlock unlock : unlocks) {
            if (playerProfile.hasCompletedUnlock(unlock.getId())) {
                player.sendMessage(unlock.toString());
                hasUnlocks = true;
            }
        }
        if (!hasUnlocks) {
            player.sendMessage(ChatColor.WHITE + "You have not unlocked anything yet.");
        }

        return true;
    }
}