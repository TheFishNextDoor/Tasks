package fun.sunrisemc.tasks.utils;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;

public class StringUtils {

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

    public static Optional<ChatColor> parseChatColor(@NotNull String color) {
        try {
            ChatColor chatColor = ChatColor.of(color);
            return Optional.of(chatColor);
        } 
        catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String normalizeString(@NonNull String string) {
        return string.trim().toLowerCase().replace(" ", "_").replace("-", "_");
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

    @NotNull
    public static String formatPercent(int seconds, int totalSeconds) {
        return String.valueOf((int) (((double) seconds / totalSeconds) * 100)) + "%";
    }
}