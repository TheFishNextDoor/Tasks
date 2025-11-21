package fun.sunrisemc.tasks.utils;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.SpawnCategory;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.task.ProgressDisplayType;
import fun.sunrisemc.tasks.task.TriggerType;

public class StringUtils {

    // Parsing

    public static Optional<Integer> parseInteger(@NotNull String str) {
        try {
            int value = Integer.parseInt(str);
            return Optional.of(value);
        } 
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> parseDouble(@NotNull String str) {
        try {
            double value = Double.parseDouble(str);
            return Optional.of(value);
        } 
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> parseLong(@NotNull String str) {
        try {
            long value = Long.parseLong(str);
            return Optional.of(value);
        } 
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<org.bukkit.Material> parseMaterial(@NotNull String name) {
        String normalizedNameA = normalizeString(name);
        for (org.bukkit.Material material : org.bukkit.Material.values()) {
            String normalizedNameB = normalizeString(material.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(material);
            }
        }
        return Optional.empty();
    }

    public static Optional<SpawnCategory> parseSpawnCategory(@NotNull String name) {
        String normalizedNameA = normalizeString(name);
        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            String normalizedNameB = normalizeString(spawnCategory.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(spawnCategory);
            }
        }
        return Optional.empty();
    }

    public static Optional<EntityType> parseEntityType(@NotNull String name) {
        String normalizedNameA = normalizeString(name);
        for (EntityType entityType : EntityType.values()) {
            String normalizedNameB = normalizeString(entityType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(entityType);
            }
        }
        return Optional.empty();
    }

    public static Optional<Environment> parseEnvironment(@NotNull String name) {
        String normalizedNameA = normalizeString(name);
        for (Environment environment : Environment.values()) {
            String normalizedNameB = normalizeString(environment.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(environment);
            }
        }
        return Optional.empty();
    }

    public static Optional<ChatColor> parseChatColor(@NotNull String color) {
        try {
            ChatColor chatColor = ChatColor.of(color);
            return Optional.of(chatColor);
        } 
        catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<ProgressDisplayType> parseProgressDisplayType(@NotNull String name) {
        String normalizedNameA = normalizeString(name);
        for (ProgressDisplayType progressDisplayType : ProgressDisplayType.values()) {
            String normalizedNameB = normalizeString(progressDisplayType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(progressDisplayType);
            }
        }
        return Optional.empty();
    }

    public static Optional<TriggerType> parseTriggerType(@NotNull String name) {
        String normalizedNameA = normalizeString(name);
        for (TriggerType triggerType : TriggerType.values()) {
            String normalizedNameB = normalizeString(triggerType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(triggerType);
            }
        }
        return Optional.empty();
    }

    // Formatting

    @NotNull
    public static String formatSecondsAbbreviated(int seconds) {
        if (seconds <= 0) {
            return "0s";
        }
        else if (seconds < 60) {
            return seconds + "s";
        }
        else if (seconds < 3600) {
            return seconds / 60 + "m";
        }
        else if (seconds < 86400) {
            return seconds / 3600 + "h";
        }
        else {
            return seconds / 86400 + "d";
        }
    }

    @NotNull
    public static String formatPercent(int seconds, int totalSeconds) {
        return String.valueOf((int) (((double) seconds / totalSeconds) * 100)) + "%";
    }

    // Normalization

    public static String normalizeString(@NotNull String string) {
        return string.trim().toLowerCase().replace(" ", "").replace("_", "").replace("-", "");
    }

    // Lists

    @NotNull
    public static String commaSeparatedList(@NotNull Object[] items) {
        ArrayList<String> itemsStringList = new ArrayList<>();
        for (Object item : items) {
            itemsStringList.add(item.toString());
        }
        return String.join(", ", itemsStringList);
    }
}