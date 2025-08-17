package fun.sunrisemc.tasks.player;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.config.MainConfig;

public class PlayerLevel {

    public static int getXpFor(int level) {
        MainConfig settings = TasksPlugin.getMainConfig();
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
        MainConfig settings = TasksPlugin.getMainConfig();
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