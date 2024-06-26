package com.thefishnextdoor.tasks.event;

import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.task.TriggerType;

public class ThrowEgg implements Listener {

    @EventHandler
    public void onThrowEgg(PlayerEggThrowEvent event) {
        Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerProfile.get(player);
        Egg egg = event.getEgg();
        ItemStack item = egg.getItem();
        playerProfile.triggerTasks(TriggerType.THROW_EGG, egg.getLocation(), egg, item, null, 1);
    }
}