package com.thefishnextdoor.tasks.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
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

public class PlayerProfile {

    private static ConcurrentHashMap<UUID, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

    private final UUID uuid;

    private int xp;

    private int maxTasks = 8;

    private ArrayList<PlayerTask> tasks = new ArrayList<>();

    private HashSet<String> completedTasks = new HashSet<>();

    private PlayerProfile(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }

        this.uuid = uuid;

        String id = uuid.toString();
        Logger logger = TasksPlugin.getInstance().getLogger();
        YamlConfiguration playerData = DataFile.get(id);

        xp = playerData.getInt("xp", 0);

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

        for (String taskId : playerData.getStringList(id + ".completed-tasks")) {
            completedTasks.add(taskId);
        }

        refreshTasks();
        
        playerProfiles.putIfAbsent(uuid, this);
    }

    public void save() {
        String id = uuid.toString();
        YamlConfiguration playerData = DataFile.get(id);

        playerData.set("xp", xp);

        playerData.set("tasks", null);
        for (int i = 0; i < tasks.size(); i++) {
            PlayerTask task = tasks.get(i);
            String taskId = task.getTaskConfiguration().getId();
            playerData.set("tasks." + taskId + ".progress", task.getProgress());
            playerData.set("tasks." + taskId + ".expires", task.getExpires());
        }

        playerData.set(id + ".completed-tasks", new ArrayList<>(completedTasks));

        DataFile.save(id, playerData);

        if (!isOnline()) {
            playerProfiles.remove(uuid);
        }
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public int getXp() {
        return xp;
    }

    public void addXp(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("Xp must be positive");
        }
        this.xp += xp;
    }

    public int getLevel() {
        return (xp + 100) / 100;
    }

    public boolean isOnline() {
        return getPlayer().isPresent();
    }

    public ArrayList<PlayerTask> getTasks() {
        return tasks;
    }

    public HashSet<String> getCompletedTasks() {
        return completedTasks;
    }

    public boolean hasTask(String id) {
        return tasks.stream().anyMatch(task -> task.getTaskConfiguration().getId().equals(id));
    }

    public boolean hasCompletedTask(String id) {
        return completedTasks.contains(id);
    }

    public void triggerTasks(TriggerType triggerType, Entity entity, ItemStack item, Block block, int amount) {
        tasks.forEach(task -> task.trigger(triggerType, entity, item, block, amount));
    }

    public void refreshTasks() {
        checkExpiredTasks();
        populateTasks();
    }

    private void checkExpiredTasks() {
        Iterator<PlayerTask> taskIter = tasks.iterator();
        while (taskIter.hasNext()) {
            PlayerTask task = taskIter.next();
            if (task.isExpired()) {
                taskIter.remove();
            }
        }
    }

    private void populateTasks() {
        if (tasks.size() >= maxTasks) {
            return;
        }

        ArrayList<TaskConfiguration> possibleTasks = TaskConfiguration.getPossibleTasks(this);
        if (possibleTasks.isEmpty()) {
            return;
        }
        
        int i = 0;
        while (tasks.size() < maxTasks && i++ < maxTasks * 2) {
            TaskConfiguration task = possibleTasks.get((int) (Math.random() * possibleTasks.size()));
            if (!hasTask(task.getId())) {
                tasks.add(new PlayerTask(task, this, 0, System.currentTimeMillis() + 1000 * 60 * 60 * 24));
            }
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

    public static void saveAll() {
        playerProfiles.values().forEach(PlayerProfile::save);
    }
}
