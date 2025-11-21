package fun.sunrisemc.tasks.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.player.PlayerProfileManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.utils.PlayerUtils;

public class Respawn implements Listener {

    @EventHandler
    public void onRespawn(@NotNull PlayerRespawnEvent event) {
        // Get the player
        Player player = event.getPlayer();

        // Get the player's profile
        PlayerProfile playerProfile = PlayerProfileManager.get(player);

        // Get the item in the player's hand
        ItemStack itemInHand = PlayerUtils.getItemInHand(player);

        // Get the respawn location
        Location respawnLocation = event.getRespawnLocation();

        // Trigger tasks
        playerProfile.triggerTasks(TriggerType.RESPAWN, respawnLocation, player, itemInHand, null, 1);
    }
}