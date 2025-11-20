package fun.sunrisemc.tasks.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfileManager;

public class PlayerJoin implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        PlayerProfileManager.load(event.getPlayer());
    }
}