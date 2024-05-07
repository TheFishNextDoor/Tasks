package com.thefishnextdoor.tasks.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.thefishnextdoor.tasks.player.PlayerProfile;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerProfile.load(event.getPlayer());
    }
}