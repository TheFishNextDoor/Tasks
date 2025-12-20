package fun.sunrisemc.tasks.utils;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.task.ProgressDisplayType;
import fun.sunrisemc.tasks.task.TriggerType;

public class StringUtils {

    // Number Parsing

    public static Optional<Integer> parseInteger(@NotNull String str) {
        try {
            int value = Integer.parseInt(str.trim());
            return Optional.of(value);
        } 
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> parseDouble(@NotNull String str) {
        try {
            double value = Double.parseDouble(str.trim());
            return Optional.of(value);
        } 
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> parseLong(@NotNull String str) {
        try {
            long value = Long.parseLong(str.trim());
            return Optional.of(value);
        } 
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    // Progress Display Type

    public static Optional<ProgressDisplayType> parseProgressDisplayType(@NotNull String progressDisplayTypeName) {
        String normalizedNameA = normalize(progressDisplayTypeName);
        for (ProgressDisplayType progressDisplayType : ProgressDisplayType.values()) {
            String normalizedNameB = normalize(progressDisplayType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(progressDisplayType);
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static ArrayList<String> getProgressDisplayTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (ProgressDisplayType commandType : ProgressDisplayType.values()) {
            String formattedName = kebabCase(commandType.name());
            names.add(formattedName);
        }
        return names;
    }

    // Trigger Type

    public static Optional<TriggerType> parseTriggerType(@NotNull String triggerTypeName) {
        String normalizedNameA = normalize(triggerTypeName);
        for (TriggerType triggerType : TriggerType.values()) {
            String normalizedNameB = normalize(triggerType.name());
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(triggerType);
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static ArrayList<String> getTriggerTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (TriggerType triggerType : TriggerType.values()) {
            String formattedName = kebabCase(triggerType.name());
            names.add(formattedName);
        }
        return names;
    }

    // Online Player

    public static Optional<Player> parseOnlinePlayer(@NotNull String name) {
        Player player = Bukkit.getPlayer(name.trim());
        return Optional.ofNullable(player);
    }

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

    // Material

    public static Optional<org.bukkit.Material> parseMaterial(@NotNull String materialName) {
        String normalizedNameA = normalize(stripMinecraftTag(materialName));
        for (org.bukkit.Material material : org.bukkit.Material.values()) {
            String normalizedNameB = normalize(stripMinecraftTag(material.name()));
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(material);
            }
        }
        return Optional.empty();
    }

    // ChatColor

    public static Optional<ChatColor> parseChatColor(@NotNull String color) {
        try {
            ChatColor chatColor = ChatColor.of(color.trim());
            return Optional.of(chatColor);
        } 
        catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    // Environment

    public static Optional<Environment> parseEnvironment(@NotNull String environmentName) {
        String normalizedNameA = normalize(stripMinecraftTag(environmentName));
        for (Environment environment : Environment.values()) {
            String normalizedNameB = normalize(stripMinecraftTag(environment.name()));
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(environment);
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static ArrayList<String> getEnvironmentNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Environment environment : Environment.values()) {
            String formattedName = kebabCase(stripMinecraftTag(environment.name()));
            names.add(formattedName);
        }
        return names;
    }

    // Biome

    public static Optional<Biome> parseBiome(@NotNull String biomeName) {
        String normalizedNameA = normalize(stripMinecraftTag(biomeName));
        for (Biome biome : Biome.values()) {
            String normalizedNameB = normalize(stripMinecraftTag(biome.name()));
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(biome);
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static ArrayList<String> getBiomeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Biome biome : Biome.values()) {
            String formattedName = kebabCase(stripMinecraftTag(biome.name()));
            names.add(formattedName);
        }
        return names;
    }

    // Entity Type

    public static Optional<EntityType> parseEntityType(@NotNull String entityTypeName) {
        String normalizedNameA = normalize(stripMinecraftTag(entityTypeName));
        for (EntityType entityType : EntityType.values()) {
            String normalizedNameB = normalize(stripMinecraftTag(entityType.name()));
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(entityType);
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static ArrayList<String> getEntityTypeNames() {
        ArrayList<String> names = new ArrayList<>();
        for (EntityType entityType : EntityType.values()) {
            String formattedName = kebabCase(stripMinecraftTag(entityType.name()));
            names.add(formattedName);
        }
        return names;
    }

    // Spawn Category

    public static Optional<SpawnCategory> parseSpawnCategory(@NotNull String spawnCategoryName) {
        String normalizedNameA = normalize(stripMinecraftTag(spawnCategoryName));
        for (SpawnCategory spawnCategory : SpawnCategory.values()) {
            String normalizedNameB = normalize(stripMinecraftTag(spawnCategory.name()));
            if (normalizedNameA.equals(normalizedNameB)) {
                return Optional.of(spawnCategory);
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static ArrayList<String> getSpawnCategoryNames() {
        ArrayList<String> names = new ArrayList<>();
        for (org.bukkit.entity.SpawnCategory spawnCategory : org.bukkit.entity.SpawnCategory.values()) {
            String formattedName = kebabCase(stripMinecraftTag(spawnCategory.name()));
            names.add(formattedName);
        }
        return names;
    }

    // Formatting

    @NotNull
    public static String formatPercent(int numerator, int denominator) {
        return String.valueOf((int) (((double) numerator / denominator) * 100)) + "%";
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

    // Casing

    @NotNull
    public static String titleCase(@NotNull String str) {
        str = str.replace("_", " ").replace("-", " ").replace(":", " ").replace(".", " ");
        String[] words = str.split(" ");
        String titleCase = "";
        for (String word : words) {
            titleCase += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        return titleCase.trim();
    }

    @NotNull
    public static String kebabCase(@NotNull String str) {
        str = str.toLowerCase().replace("_", " ").replace("-", " ").replace(":", " ").replace(".", " ").trim();
        String[] words = str.split(" ");
        String kebabCase = "";
        for (String word : words) {
            kebabCase += word + "-";
        }
        return kebabCase.substring(0, kebabCase.length() - 1);
    }

    // Normalization

    @NotNull
    public static String stripMinecraftTag(@NotNull String str) {
        if (str.toLowerCase().startsWith("minecraft:")) {
            return str.substring(10);
        }
        return str;
    }

    @NotNull
    public static String normalize(@NotNull String str) {
        return str.toLowerCase()
                  .replace(" ", "")
                  .replace("_", "")
                  .replace("-", "")
                  .replace(".", "")
                  .replace(":", "")
                  .trim();
    }
}