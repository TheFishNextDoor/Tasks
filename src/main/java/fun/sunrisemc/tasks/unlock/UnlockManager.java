package fun.sunrisemc.tasks.unlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.file.ConfigFile;

public class UnlockManager {

    private static HashMap<String, Unlock> unlocksLookup = new HashMap<>();

    private static List<Unlock> unlocksSorted = Collections.unmodifiableList(new ArrayList<>());

    public static Optional<Unlock> get(@NonNull String id) {
        return Optional.ofNullable(unlocksLookup.get(id));
    }

    public static List<Unlock> getSorted() {
        return unlocksSorted;
    }

    public static ArrayList<String> getIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Unlock unlock : unlocksSorted) {
            ids.add(unlock.getId());
        }
        return ids;
    }

    public static void loadConfig() {
        unlocksLookup = new HashMap<>();
        ArrayList<Unlock> tempUnlocksSorted = new ArrayList<>();

        YamlConfiguration config = ConfigFile.get("unlocks", false);
        for (String id : config.getKeys(false)) {
            Unlock unlock = new Unlock(config, id);
            unlocksLookup.put(id, unlock);
            tempUnlocksSorted.add(unlock);
        }

        Collections.sort(tempUnlocksSorted);
        unlocksSorted = Collections.unmodifiableList(tempUnlocksSorted);

        TasksPlugin.logInfo("Loaded " + unlocksSorted.size() + " unlocks");
    }
}