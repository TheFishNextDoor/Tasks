package fun.sunrisemc.tasks.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.task.ProgressDisplayType;
import fun.sunrisemc.tasks.task.TriggerType;

public class Names {

    @NotNull
    public static ArrayList<String> getOnlinePlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == null) {
                continue;
            }
            playerNames.add(player.getName());
        }
        return playerNames;
    }

    @NotNull
    public static ArrayList<String> getProgressDisplayTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (ProgressDisplayType commandType : ProgressDisplayType.values()) {
            String formattedName = StringUtils.formatName(commandType.name());
            names.add(formattedName);
        }
        return names;
    }

    @NotNull
    public static ArrayList<String> getTriggerTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (TriggerType triggerType : TriggerType.values()) {
            String formattedName = StringUtils.formatName(triggerType.name());
            names.add(formattedName);
        }
        return names;
    }

    @NotNull
    public static ArrayList<String> getEnvironmentNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Environment environment : Environment.values()) {
            String formattedName = StringUtils.formatName(environment.name());
            names.add(formattedName);
        }
        return names;
    }

    @NotNull
    public static ArrayList<String> getBiomeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Biome biome : Biome.values()) {
            String formattedName = StringUtils.formatName(biome.name());
            names.add(formattedName);
        }
        return names;
    }

    @NotNull
    public static ArrayList<String> getEntityTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (EntityType entityType : EntityType.values()) {
            String formattedName = StringUtils.formatName(entityType.name());
            names.add(formattedName);
        }
        return names;
    }

    @NotNull
    public static ArrayList<String> getSpawnCategoryNames() {
        ArrayList<String> names = new ArrayList<>();
        for (org.bukkit.entity.SpawnCategory spawnCategory : org.bukkit.entity.SpawnCategory.values()) {
            String formattedName = StringUtils.formatName(spawnCategory.name());
            names.add(formattedName);
        }
        return names;
    }
}