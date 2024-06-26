package com.thefishnextdoor.tasks.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.unlock.Unlock;

public class Unlocks implements CommandExecutor, TabCompleter {

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
        PlayerProfile playerProfile = PlayerProfile.get(player);

        List<Unlock> unlocks = Unlock.getSorted();
        boolean hasUnlocks = false;
        player.sendMessage(playerProfile.getColor() + ChatColor.BOLD + "Unlocks");
        for (Unlock unlock : unlocks) {
            if (playerProfile.hasCompletedUnlock(unlock.getId())) {
                player.sendMessage(unlock.toString());
                hasUnlocks = true;
            }
        }
        if (!hasUnlocks) {
            player.sendMessage(ChatColor.WHITE + "You have not unlocked anything yet");
        }

        return true;
    }
}