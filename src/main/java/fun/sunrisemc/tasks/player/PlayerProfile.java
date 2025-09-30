package fun.sunrisemc.tasks.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.config.MainConfig;
import fun.sunrisemc.tasks.file.DataFile;
import fun.sunrisemc.tasks.hook.Vault;
import fun.sunrisemc.tasks.task.PlayerTask;
import fun.sunrisemc.tasks.task.TaskConfiguration;
import fun.sunrisemc.tasks.task.TaskConfigurationManager;
import fun.sunrisemc.tasks.task.TriggerType;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.unlock.UnlockManager;
import fun.sunrisemc.tasks.utils.Money;
import net.md_5.bungee.api.ChatColor;

public class PlayerProfile {

    private final UUID uuid;

    // Save data

    private int xp;

    private int skips;

    private ChatColor color;

    private HashSet<String> completedUnlocks = new HashSet<>();

    private HashSet<String> completedTasks = new HashSet<>();

    private ArrayList<PlayerTask> tasks = new ArrayList<>();

    // Cache data

    private int level;

    PlayerProfile(@NonNull UUID uuid) {
        this.uuid = uuid;

        String id = uuid.toString();
        YamlConfiguration playerData = DataFile.get(id);

        xp = playerData.getInt("xp", 0);
        skips = playerData.getInt("skips", 0);

        for (String unlockId : playerData.getStringList("completed-unlocks")) {
            completedUnlocks.add(unlockId);
        }

        for (String taskId : playerData.getStringList("completed-tasks")) {
            completedTasks.add(taskId);
        }

        if (playerData.contains("tasks")) {
            for (String taskKey : playerData.getConfigurationSection("tasks").getKeys(false)) {
                Optional<TaskConfiguration> taskConfiguration = TaskConfigurationManager.get(taskKey);
                if (!taskConfiguration.isPresent()) {
                    TasksPlugin.logWarning("Removing invalid task " + taskKey + " for player " + id);
                    continue;
                }
                int progress = playerData.getInt("tasks." + taskKey + ".progress");
                long expires = playerData.getLong("tasks." + taskKey + ".expires");
                PlayerTask task = new PlayerTask(taskConfiguration.get(), this, progress, expires);
                if (!task.isExpired()) {
                    tasks.add(task);
                }
            }
        }

        try {
            this.color = ChatColor.of(playerData.getString("color"));
        }
        catch (IllegalArgumentException e) {
            this.color = ChatColor.BLUE;
        }

        this.level = PlayerLevel.getLevel(xp);
    }

    public void save() {
        String id = uuid.toString();
        YamlConfiguration playerData = DataFile.get(id);

        playerData.set("xp", xp);
        playerData.set("skips", skips);

        playerData.set("completed-unlocks", new ArrayList<>(completedUnlocks));

        playerData.set("tasks", null);
        for (PlayerTask task : tasks) {
            if (task.isCompleted()) {
                continue;
            }
            String taskId = task.getTaskConfiguration().getId();
            playerData.set("tasks." + taskId + ".progress", task.getProgress());
            playerData.set("tasks." + taskId + ".expires", task.getExpires());
        }

        playerData.set("completed-tasks", new ArrayList<>(completedTasks));

        playerData.set("color", color.getName());

        DataFile.save(id, playerData);

        if (!isOnline()) {
            PlayerProfileManager.unload(uuid);
        }
    }

    public boolean isOnline() {
        return getPlayer().isPresent();
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public void addMoney(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount == 0) {
            return;
        }
        if (!Vault.isUsingVault()) {
            return;
        }
        Vault.getEconomy().depositPlayer(getPlayer().get(), amount);
        getPlayer().ifPresent(player -> player.sendMessage(ChatColor.GOLD + "+" + Money.format(amount)));
    }

    public int getTotalXp() {
        return xp;
    }

    public int getXpSinceLastLevel() {
        return xp - PlayerLevel.getXpFor(level);
    }

    public int getXpToNextLevel() {
        return PlayerLevel.getXpFor(level + 1) - xp;
    }

    public void addXp(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("Xp must be positive");
        }
        if (xp == 0) {
            return;
        }
        MainConfig config = TasksPlugin.getMainConfig();
        if (!config.ENABLE_LEVELLING) {
            return;
        }
        this.xp += xp;
        getPlayer().ifPresent(player -> player.sendMessage(ChatColor.GRAY + "+" + xp + " Xp"));
        checkLevelUp();
    }

    public void removeXp(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("Xp must be positive");
        }
        if (xp == 0) {
            return;
        }
        MainConfig config = TasksPlugin.getMainConfig();
        if (!config.ENABLE_LEVELLING) {
            return;
        }
        this.xp -= xp;
        if (this.xp < 0) {
            this.xp = 0;
        }
        getPlayer().ifPresent(player -> player.sendMessage(ChatColor.RED + "-" + xp + " Xp"));
        checkLevelUp();
    }

    public void setXp(int xp) {
        if (xp < 0) {
            throw new IllegalArgumentException("Xp must be positive");
        }
        if (this.xp == xp) {
            return;
        }
        MainConfig config = TasksPlugin.getMainConfig();
        if (!config.ENABLE_LEVELLING) {
            return;
        }
        this.xp = xp;
        getPlayer().ifPresent(player -> player.sendMessage(getColor() + "Your xp has been set to " + xp));
        checkLevelUp();
    }

    public int getLevel() {
        return level;
    }

    public int getSkips() {
        return skips;
    }

    public void addSkips(int skips) {
        if (skips < 0) {
            throw new IllegalArgumentException("Skips must be positive");
        }
        if (skips == 0) {
            return;
        }
        this.skips += skips;
        getPlayer().ifPresent(player -> player.sendMessage(ChatColor.LIGHT_PURPLE + "+" + skips + " Skip(s)"));
    }

    public void removeSkips(int skips) {
        if (skips < 0) {
            throw new IllegalArgumentException("Skips must be positive");
        }
        if (skips == 0) {
            return;
        }
        this.skips -= skips;
        if (this.skips < 0) {
            this.skips = 0;
        }
        getPlayer().ifPresent(player -> player.sendMessage(ChatColor.RED + "-" + skips + " Skip(s)"));
    }

    public void setSkips(int skips) {
        if (skips < 0) {
            throw new IllegalArgumentException("Skips must be positive");
        }
        if (this.skips == skips) {
            return;
        }
        this.skips = skips;
        getPlayer().ifPresent(player -> player.sendMessage(ChatColor.LIGHT_PURPLE + "Your skips have been set to " + skips));
    }

    public boolean skip(@NonNull PlayerTask playerTask) {
        if (skips <= 0) {
            return false;
        }
        TaskConfiguration taskConfiguration = playerTask.getTaskConfiguration();
        if (!taskConfiguration.isSkippable()) {
            return false;
        }
        if (!removeTask(taskConfiguration.getId())) {
            return false;
        }
        skips--;
        return true;
    }

    public String getColor() {
        return color + "";
    }

    public void setColor(@NonNull ChatColor color) {
        if (color == ChatColor.STRIKETHROUGH || color == ChatColor.MAGIC || color == ChatColor.BOLD || color == ChatColor.ITALIC || color == ChatColor.UNDERLINE || color == ChatColor.RESET) {
            throw new IllegalArgumentException("Invalid color");
        }
        this.color = color;
    }

    public boolean addCompletedUnlock(@NonNull String id) {
        return completedUnlocks.add(id);
    }

    public boolean hasCompletedUnlock(@NonNull String id) {
        return completedUnlocks.contains(id);
    }

    public boolean addCompletedTask(@NonNull String id) {
        return completedTasks.add(id);
    }

    public boolean hasCompletedTask(@NonNull String id) {
        return completedTasks.contains(id);
    }

    public boolean addTask(@NonNull PlayerTask task) {
        if (hasTask(task.getTaskConfiguration().getId())) {
            return false;
        }
        tasks.add(task);
        sendNotification("New Task", task.toString());
        return true;
    }

    public boolean removeTask(@NonNull String id) {
        Optional<PlayerTask> task = getTask(id);
        if (!task.isPresent()) {
            return false;
        }
        if (tasks.remove(task.get())) {
            sendNotification("Task Removed", task.get().toString());
            return true;
        }
        return false;
    }

    public boolean hasTask(@NonNull String id) {
        return tasks.stream().anyMatch(task -> task.getTaskConfiguration().getId().equals(id));
    }

    public Optional<PlayerTask> getTask(@NonNull String id) {
        return tasks.stream().filter(task -> task.getTaskConfiguration().getId().equals(id)).findFirst();
    }

    public List<PlayerTask> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public ArrayList<String> getTaskIds() {
        ArrayList<String> taskIds = new ArrayList<>();
        tasks.forEach(task -> taskIds.add(task.getTaskConfiguration().getId()));
        return taskIds;
    }

    public void triggerTasks(@NonNull TriggerType triggerType, @NonNull Location location, @Nullable Entity entity, @Nullable ItemStack item, @Nullable Block block, int amount) {
        tasks.forEach(task -> task.trigger(triggerType, location, entity, item, block, amount));
    }

    public void refreshTasks() {
        checkExpiredTasks();
        populateTasks();
    }

    public void sendNotification(@NonNull String title) {
        sendNotification(title, null);
    }

    public void sendNotification(@NonNull String title, @Nullable String info) {
        Optional<Player> optionalPlayer = getPlayer();
        if (!optionalPlayer.isPresent()) {
            return;
        }
        Player player = optionalPlayer.get();

        if (info == null) {
            player.sendMessage(getColor() + "" + ChatColor.BOLD + title);
        }
        else {
            player.sendMessage(getColor() + "" + ChatColor.BOLD + title + ": " + ChatColor.WHITE + info);
        }
        
        player.playSound(player.getLocation(), "block.sniffer_egg.plop", 1, 1);
    }

    private void checkLevelUp() {
        Optional<Player> player = getPlayer();
        if (!player.isPresent()) {
            return;
        }

        int newLevel = PlayerLevel.getLevel(xp);
        boolean levelUp = newLevel > level;
        if (levelUp) {
            for (int i = level + 1; i <= newLevel; i++) {
                sendNotification("Level Up", String.valueOf(i));
            }
            checkUnlocks();
        }
        this.level = newLevel;
        if (levelUp) {
            checkUnlocks();
        }
    }

    private void checkUnlocks() {
        for (Unlock unlock : UnlockManager.getSorted()) {
            if (unlock.isValidFor(this)) {
                unlock.giveTo(this);
            }
        }
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
                sendNotification("Task Expired", task.toString());
            }
        }
    }

    private void populateTasks() {
        int maxTasks = TasksPlugin.getMainConfig().MAX_TASKS;
        if (tasks.size() >= maxTasks) {
            return;
        }

        while (tasks.size() < maxTasks) {
            Optional<TaskConfiguration> optionalTask = TaskConfigurationManager.getNewTask(this);
            if (!optionalTask.isPresent()) {
                break;
            }
            TaskConfiguration task = optionalTask.get();
            addTask(new PlayerTask(task, this));
        }
    }
}