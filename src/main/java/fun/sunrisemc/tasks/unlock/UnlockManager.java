package fun.sunrisemc.tasks.unlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.file.ConfigFile;

public class UnlockManager {

    private static @NotNull HashMap<String, Unlock> unlocksLookup = new HashMap<>();

    private static @NotNull List<Unlock> unlocksSorted = Collections.unmodifiableList(new ArrayList<>());

    public static Optional<Unlock> get(@NotNull String id) {
        return Optional.ofNullable(unlocksLookup.get(id));
    }

    @NotNull
    public static List<Unlock> getSorted() {
        return unlocksSorted;
    }

    @NotNull
    public static ArrayList<String> getIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Unlock unlock : unlocksSorted) {
            ids.add(unlock.getId());
        }
        return ids;
    }

    public static void loadConfig() {
        TasksPlugin.logInfo("Loading unlocks...");

        ConfigFile config = ConfigFile.get("unlocks", false);

        unlocksLookup = new HashMap<>();
        ArrayList<Unlock> tempUnlocksSorted = new ArrayList<>();
        
        for (String id : config.getKeys()) {
            Unlock unlock = new Unlock(config, id);
            unlocksLookup.put(id, unlock);
            tempUnlocksSorted.add(unlock);
        }

        Collections.sort(tempUnlocksSorted);
        unlocksSorted = Collections.unmodifiableList(tempUnlocksSorted);

        TasksPlugin.logInfo("Loaded " + unlocksSorted.size() + " unlocks.");
    }
}