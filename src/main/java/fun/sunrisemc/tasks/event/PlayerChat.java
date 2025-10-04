package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.config.MainConfig;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import net.md_5.bungee.api.ChatColor;

public class PlayerChat implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        MainConfig config = TasksPlugin.getMainConfig();
        if (config.SHOW_LEVEL) {
            String color = playerProfile.getColor();
            String level = String.valueOf(playerProfile.getLevel());
            String prefix = config.CHAT_PREFIX_FORMAT;
            prefix = prefix.replace("{color}", color).replace("{level}", level);
            prefix = ChatColor.translateAlternateColorCodes('&', prefix);
            event.setFormat(prefix + " " + event.getFormat());
        }
    }
}