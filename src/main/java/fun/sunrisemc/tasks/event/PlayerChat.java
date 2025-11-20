package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.config.MainConfig;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;

public class PlayerChat implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        // Get the plugin config
        MainConfig config = TasksPlugin.getMainConfig();

        // Check if we should show the level in chat
        if (!config.SHOW_LEVEL) {
            return;
        }
        
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        
        // Get the chat prefix format
        String prefixFormat = config.CHAT_PREFIX_FORMAT;

        // Get the player's color and level
        String color = playerProfile.getColor();
        String level = String.valueOf(playerProfile.getLevel());
        
        // Create the prefix
        String prefix = ChatColor.translateAlternateColorCodes('&', prefixFormat.replace("{color}", color).replace("{level}", level));

        // Apply the prefix
        event.setFormat(prefix + " " + event.getFormat());
    }
}