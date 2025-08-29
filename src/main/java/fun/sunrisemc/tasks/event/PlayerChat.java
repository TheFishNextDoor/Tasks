package fun.sunrisemc.tasks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.config.MainConfig;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfileManager.get(player);
        MainConfig config = TasksPlugin.getMainConfig();
        if (config.SHOW_LEVEL) {
            String color = playerProfile.getColor();
            String level = String.valueOf(playerProfile.getLevel());
            String message = event.getFormat();
            String format = config.CHAT_FORMAT;
            String newFormat = format.replace("{color}", color).replace("{level}", level).replace("{message}", message);
            event.setFormat(newFormat);
        }
    }
}