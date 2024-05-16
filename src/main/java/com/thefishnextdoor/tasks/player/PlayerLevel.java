package com.thefishnextdoor.tasks.player;

import com.thefishnextdoor.tasks.Settings;
import com.thefishnextdoor.tasks.TasksPlugin;

public class PlayerLevel {

    public static int getXpFor(int level) {
        Settings settings = TasksPlugin.getSettings();
        level -= 1;
        double b = settings.LEVEL_BASE;
        int total = 0;
        for (int i = 1; i <= level; i++) {
            total += (int) (b);
            b *= settings.LEVEL_MULTIPLIER;
        }
        return total;
    }

    public static int getLevel(int totalXp) {
        Settings settings = TasksPlugin.getSettings();
        double b = settings.LEVEL_BASE;
        int currentXp = 0;
        int level = 0;
        while (currentXp <= totalXp) {
            level++;
            currentXp += (int) b;
            b *= settings.LEVEL_MULTIPLIER;
            if (currentXp > totalXp) {
                break;
            }
        }
        return level;
    }

    public static boolean verify() {
        int level = 1;
        while(level <= 200) {
            int xp = getXpFor(level);
            if (getLevel(xp) != level) {
                return false;
            }
            level++;
        }
        return true;
    }
}