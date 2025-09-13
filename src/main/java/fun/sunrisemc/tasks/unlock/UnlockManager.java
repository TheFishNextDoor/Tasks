package fun.sunrisemc.tasks.unlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.configuration.file.YamlConfiguration;

import fun.sunrisemc.tasks.utils.ConfigFile;
import fun.sunrisemc.tasks.utils.Log;

public class UnlockManager {

    static HashMap<String, Unlock> unlocksLookup = new HashMap<>();

    static ArrayList<Unlock> unlocksSorted = new ArrayList<>();

    public static Optional<Unlock> get(String id) {
        return Optional.ofNullable(unlocksLookup.get(id));
    }

    public static List<Unlock> getSorted() {
        return Collections.unmodifiableList(unlocksSorted);
    }

    public static ArrayList<String> getIds() {
        ArrayList<String> ids = new ArrayList<>();
        for (Unlock unlock : unlocksSorted) {
            ids.add(unlock.getId());
        }
        return ids;
    }

    public static void loadConfig() {
        unlocksSorted.clear();
        unlocksLookup.clear();
        YamlConfiguration config = ConfigFile.get("unlocks", false);
        for (String id : config.getKeys(false)) {
            new Unlock(config, id);
        }
        Collections.sort(unlocksSorted);
        Log.info("Loaded " + unlocksSorted.size() + " unlocks");
    }
}