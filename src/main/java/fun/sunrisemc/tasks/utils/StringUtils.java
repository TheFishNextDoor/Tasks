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
        String normalizedNameA = normalize(name);
        for (org.bukkit.Material material : org.bukkit.Material.values()) {
            String normalizedNameB = normalize(material.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(material);
            }
        }
        return Optional.empty();
    }

    public static Optional<SpawnCategory> parseSpawnCategory(@NotNull String name) {
        String normalizedNameA = normalize(name);
        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            String normalizedNameB = normalize(spawnCategory.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(spawnCategory);
            }
        }
        return Optional.empty();
    }

    public static Optional<EntityType> parseEntityType(@NotNull String name) {
        String normalizedNameA = normalize(name);
        for (EntityType entityType : EntityType.values()) {
            String normalizedNameB = normalize(entityType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(entityType);
            }
        }
        return Optional.empty();
    }

    public static Optional<Environment> parseEnvironment(@NotNull String name) {
        String normalizedNameA = normalize(name);
        for (Environment environment : Environment.values()) {
            String normalizedNameB = normalize(environment.name());
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
        String normalizedNameA = normalize(name);
        for (ProgressDisplayType progressDisplayType : ProgressDisplayType.values()) {
            String normalizedNameB = normalize(progressDisplayType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(progressDisplayType);
            }
        }
        return Optional.empty();
    }

    public static Optional<TriggerType> parseTriggerType(@NotNull String name) {
        String normalizedNameA = normalize(name);
        for (TriggerType triggerType : TriggerType.values()) {
            String normalizedNameB = normalize(triggerType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(triggerType);
            }
        }
        return Optional.empty();
    }

    // Formatting

    @NotNull
    public static String formatPercent(int seconds, int totalSeconds) {
        return String.valueOf((int) (((double) seconds / totalSeconds) * 100)) + "%";
    }

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

    public static String formatMoneyAbbreviated(double money) {
        String prefix = money < 0 ? ChatColor.RED + "-$" : ChatColor.GOLD + "$";
        money = Math.abs(money);
        if (money >= 1000000000) {
            double billion = money / 1000000000;
            String moneyFormatted = billion % 1 == 0 ? String.format("%.0f", billion) : String.format("%.1f", billion);
            return prefix + moneyFormatted + "b";
        }
        else if (money >= 1000000) {
            double million = money / 1000000;
            String moneyFormatted = million % 1 == 0 ? String.format("%.0f", million) : String.format("%.1f", million);
            return prefix + moneyFormatted + "m";
        }
        else if (money >= 1000) {
            double thousand = money / 1000;
            String moneyFormatted = thousand % 1 == 0 ? String.format("%.0f", thousand) : String.format("%.1f", thousand);
            return prefix + moneyFormatted + "k";
        }
        else {
            String moneyFormatted = money % 1 == 0 ? String.format("%.0f", money) : String.format("%.2f", money);
            return prefix + moneyFormatted;
        }
    }

    // Normalization

    public static String normalize(@NotNull String string) {
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