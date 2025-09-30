package fun.sunrisemc.tasks.utils;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static ArrayList<String> getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return playerNames;
    }
}