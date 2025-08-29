package fun.sunrisemc.tasks.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fun.sunrisemc.tasks.player.PlayerProfileManager;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerProfileManager.load(event.getPlayer());
    }
}