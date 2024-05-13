package com.thefishnextdoor.tasks.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.TasksPlugin;
import com.thefishnextdoor.tasks.file.DataFile;
import com.thefishnextdoor.tasks.task.PlayerTask;
import com.thefishnextdoor.tasks.task.TaskConfiguration;
import com.thefishnextdoor.tasks.task.TriggerType;
import com.thefishnextdoor.tasks.unlock.Unlock;

public class PlayerProfile {

    private static ConcurrentHashMap<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    private final UUID uuid;

    // Save data

    private int xp;

    private HashSet<String> completedUnlocks = new HashSet<>();

    private HashSet<String> completedTasks = new HashSet<>();

    private ArrayList<PlayerTask> tasks = new ArrayList<>();

    // Cache data

    private int cachedLevel;

    private PlayerProfile(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }

        this.uuid = uuid;

        String id = uuid.toString();
        Logger logger = TasksPlugin.getInstance().getLogger();
        YamlConfiguration playerData = DataFile.get(id);

        xp = playerData.getInt("xp", 0);

        for (String unlockId : playerData.getStringList(".completed-unlocks")) {
            completedUnlocks.add(unlockId);
        }

        for (String taskId : playerData.getStringList(".completed-tasks")) {
            completedTasks.add(taskId);
        }

        if (playerData.contains("tasks")) {
            for (String taskKey : playerData.getConfigurationSection("tasks").getKeys(false)) {
                Optional<TaskConfiguration> taskConfiguration = TaskConfiguration.get(taskKey);
                if (!taskConfiguration.isPresent()) {
                    logger.warning("Removing invalid task " + taskKey + " for player " + id);
                    continue;
                }
                int progress = playerData.getInt("tasks." + taskKey + ".progress");
                long expires = playerData.getLong("tasks." + taskKey + ".expires");
                tasks.add(new PlayerTask(taskConfiguration.get(), this, progress, expires));
            }
        }

        cachedLevel = calcLevel();

        refreshTasks();
        
        playerProfiles.putIfAbsent(uuid, this);
    }

    public void save() {
        String id = uuid.toString();
        YamlConfiguration playerData = DataFile.get(id);

        playerData.set("xp", xp);

        playerData.set(".completed-unlocks", new ArrayList<>(completedUnlocks));

        playerData.set("tasks", null);
        for (PlayerTask task : tasks) {
            if (task.isCompleted()) {
                continue;
            }
            String taskId = task.getTaskConfiguration().getId();
            playerData.set("tasks." + taskId + ".progress", task.getProgress());
            playerData.set("tasks." + taskId + ".expires", task.getExpires());
        }

        playerData.set(".completed-tasks", new ArrayList<>(completedTasks));

        DataFile.save(id, playerData);

        if (!isOnline()) {
            playerProfiles.remove(uuid);
        }
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public int getTotalXp() {
        return xp;
    }

    public int getXp() {
        return xp % 100;
    }

    public int getXpToNextLevel() {
        return getLevel() * 100 - xp;
    }

    public void addXp(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("Xp must be positive");
        }
        this.xp += xp;
        checkLevelUp();
    }

    public int getLevel() {
        return cachedLevel;
    }

    public boolean isOnline() {
        return getPlayer().isPresent();
    }

    public void addCompletedUnlock(String id) {
        completedUnlocks.add(id);
    }

    public boolean hasCompletedUnlock(String id) {
        return completedUnlocks.contains(id);
    }

    public void addCompletedTask(String id) {
        completedTasks.add(id);
    }

    public boolean hasCompletedTask(String id) {
        return completedTasks.contains(id);
    }

    public boolean hasTask(String id) {
        return tasks.stream().anyMatch(task -> task.getTaskConfiguration().getId().equals(id));
    }

    public ArrayList<PlayerTask> getTasks() {
        return tasks;
    }

    public void triggerTasks(TriggerType triggerType, Location location, Entity entity, ItemStack item, Block block, int amount) {
        tasks.forEach(task -> task.trigger(triggerType, location, entity, item, block, amount));
    }

    public void refreshTasks() {
        checkExpiredTasks();
        populateTasks();
    }

    private void checkLevelUp() {
        Optional<Player> player = getPlayer();
        if (!player.isPresent()) {
            return;
        }

        int level = calcLevel();
        if (level > cachedLevel) {
            for (int i = cachedLevel + 1; i <= level; i++) {
                TasksMessage.send(player.get(), "Level Up", String.valueOf(i));
            }
            cachedLevel = level;
            Unlock.checkUnlocks(this);
        }
    }

    private int calcLevel() {
        return (xp + 100) / 100;
    }

    private void checkExpiredTasks() {
        Iterator<PlayerTask> taskIter = tasks.iterator();
        while (taskIter.hasNext()) {
            PlayerTask task = taskIter.next();
            if (task.isCompleted()) {
                taskIter.remove();
            } 
            else if (task.isExpired()) {
                taskIter.remove();
                getPlayer().ifPresent(player -> TasksMessage.send(player, "Task Expired", task.getTaskConfiguration().toString()));
            }
        }
    }

    private void populateTasks() {
        int maxTasks = 8;
        if (tasks.size() >= maxTasks) {
            return;
        }

        while (tasks.size() < maxTasks) {
            Optional<TaskConfiguration> optionalTask = TaskConfiguration.getNewTask(this);
            if (!optionalTask.isPresent()) {
                break;
            }
            TaskConfiguration task = optionalTask.get();
            long timeLimitMS = task.getTimeLimitMS();
            long expireTime = timeLimitMS == 0 ? 0 : System.currentTimeMillis() + timeLimitMS;
            tasks.add(new PlayerTask(task, this, 0, expireTime));
            getPlayer().ifPresent(player -> TasksMessage.send(player, "New Task", task.toString()));
        }
    }

    public static void load(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        load(player.getUniqueId());
    }

    public static void load(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        Bukkit.getScheduler().runTaskAsynchronously(TasksPlugin.getInstance(), () -> {
            get(uuid);
        });
    }

    public static PlayerProfile get(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        return get(player.getUniqueId());
    }

    public static PlayerProfile get(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }

        PlayerProfile playerProfile = playerProfiles.get(uuid);
        if (playerProfile == null) {
            playerProfile = new PlayerProfile(uuid);
        }
        return playerProfile;
    }

    public static void refreshAllTasks() {
        playerProfiles.values().forEach(PlayerProfile::refreshTasks);
    }

    public static void reload() {
        saveAll();
        playerProfiles.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            load(player);
        }
    }

    public static void saveAll() {
        playerProfiles.values().forEach(PlayerProfile::save);
    }
}
