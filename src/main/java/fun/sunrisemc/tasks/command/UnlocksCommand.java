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

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.unlock.UnlockManager;

public class UnlocksCommand implements CommandExecutor, TabCompleter {

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String @NotNull [] args) {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        // Ensure the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Get player profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get unlocks
        List<Unlock> unlocks = UnlockManager.getSorted();

        // Display unlocks
        player.sendMessage(playerProfile.getColor() + ChatColor.BOLD + "Unlocks");

        boolean hasUnlocks = false;
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