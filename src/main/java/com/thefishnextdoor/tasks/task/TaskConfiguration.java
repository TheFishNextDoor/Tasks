package com.thefishnextdoor.tasks.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.TasksPlugin;
import com.thefishnextdoor.tasks.file.ConfigFile;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.toolkit.EnumTools;
import com.thefishnextdoor.tasks.unlock.Unlock;

import net.md_5.bungee.api.ChatColor;

public class TaskConfiguration {

    private static HashMap<String, TaskConfiguration> taskConfigurations = new HashMap<>();

    private static List<String> settings = List.of(
        "amount",
        "message",
        "time-limit-minutes",
        "reset-on-death",
        "repeatable",
        "min-level",
        "max-level",
        "prerequisite-tasks",
        "incompatible-tasks",
        "permission",
        "reward-money",
        "reward-xp",
        "reward-unlocks",
        "reward-console-commands",
        "reward-player-commands",
        "reward-messages",
        "triggers",
        "worlds",
        "environments",
        "biomes",
        "min-x",
        "max-x",
        "min-y",
        "max-y",
        "min-z",
        "max-z",
        "entity-in-water",
        "entity-on-ground",
        "entity-names",
        "entity-types",
        "entity-categories",
        "item-names",
        "item-materials",
        "block-materials"
    );

    private final String id;

    // Behavior

    private int amount;

    private String message = null;

    private long timeLimitMS = 0;

    private boolean resetOnDeath = false;

    // Requirements
    private boolean repeatable = true;

    private Integer minLevel = null;
    private Integer maxLevel = null;

    private String permission = null;

    private ArrayList<String> prerequisiteTasks = new ArrayList<>();
    private HashSet<String> incompatibleTasks = new HashSet<>();

    // Rewards
    private double rewardMoney;
    private int rewardXp;

    private ArrayList<Unlock> rewardUnlocks = new ArrayList<>();

    private ArrayList<String> rewardConsoleCommands = new ArrayList<>();
    private ArrayList<String> rewardPlayerCommands = new ArrayList<>();
    private ArrayList<String> rewardMessages = new ArrayList<>();

    // Conditions
    private HashSet<TriggerType> triggers = new HashSet<>();

    private HashSet<String> worlds = new HashSet<>();
    private HashSet<Environment> environments = new HashSet<>();
    private HashSet<Biome> biomes = new HashSet<>();

    private Integer minX = null;
    private Integer maxX = null;
    private Integer minY = null;
    private Integer maxY = null;
    private Integer minZ = null;
    private Integer maxZ = null;

    private Boolean entityIsInWater = null;
    private Boolean entityIsOnGround = null;

    private HashSet<String> entityNames = new HashSet<>();
    private HashSet<EntityType> entityTypes = new HashSet<>();
    private HashSet<SpawnCategory> entityCategories = new HashSet<>();

    private HashSet<String> itemNames = new HashSet<>();
    private HashSet<Material> itemMaterials = new HashSet<>();

    private HashSet<Material> blockMaterials = new HashSet<>();

    public TaskConfiguration(YamlConfiguration config, String id) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        this.id = id;

        Logger logger = TasksPlugin.getInstance().getLogger();

        for (String setting : config.getConfigurationSection(id).getKeys(false)) {
            if (!settings.contains(setting)) {
                logger.warning("Invalid setting for task " + id + ": " + setting);
                String possibleSettings = String.join(", ", settings);
                logger.warning("Valid settings are: " + possibleSettings);
            }
        }

        this.amount = Math.max(config.getInt(id + ".amount"), 1);

        this.message = config.getString(id + ".message");

        this.timeLimitMS = Math.max(config.getInt(id + ".time-limit-minutes") * 60000, 0);

        this.resetOnDeath = config.getBoolean(id + ".reset-on-death");

        if (config.contains(id + ".repeatable")) {
            this.repeatable = config.getBoolean(id + ".repeatable");
        }

        if (config.contains(id + ".min-level")) {
            this.minLevel = config.getInt(id + ".min-level");
        }
        if (config.contains(id + ".max-level")) {
            this.maxLevel = config.getInt(id + ".max-level");
        }

        this.permission = config.getString(id + ".permission");

        this.prerequisiteTasks.addAll(config.getStringList(id + ".prerequisite-tasks"));
        this.incompatibleTasks.addAll(config.getStringList(id + ".incompatible-tasks"));
        
        this.rewardMoney = config.getDouble(id + ".reward-money");
        this.rewardXp = config.getInt(id + ".reward-xp");

        for (String unlockName : config.getStringList(id + ".reward-unlocks")) {
            Optional<Unlock> unlock = Unlock.get(unlockName);
            if (!unlock.isPresent()) {
                logger.warning("Invalid reward unlock for task " + id + ": " + unlockName);
                continue;
            }
            this.rewardUnlocks.add(unlock.get());
        }

        for (String consoleCommand : config.getStringList(id + ".reward-console-commands")) {
            this.rewardConsoleCommands.add(consoleCommand);
        }

        for (String playerCommand : config.getStringList(id + ".reward-player-commands")) {
            this.rewardPlayerCommands.add(playerCommand);
        }

        for (String rewardMessage : config.getStringList(id + ".reward-messages")) {
            this.rewardMessages.add(ChatColor.translateAlternateColorCodes('&', rewardMessage));
        }

        for (String triggerName : config.getStringList(id + ".triggers")) {
            TriggerType trigger = EnumTools.fromString(TriggerType.class, triggerName);
            if (trigger == null) {
                logger.warning("Invalid trigger for task " + id + ": " + triggerName);
                logger.warning("Valid triggers are: " + EnumTools.allStrings(TriggerType.class));
                continue;
            }
            this.triggers.add(trigger);
        }
        if (this.triggers.isEmpty()) {
            logger.warning("No triggers for task " + id);
        }

        for (String worldName : config.getStringList(id + ".worlds")) {
            this.worlds.add(worldName);
        }

        for (String environmentName : config.getStringList(id + ".environments")) {
            Environment environment = EnumTools.fromString(Environment.class, environmentName);
            if (environment == null) {
                logger.warning("Invalid environment for task " + id + ": " + environmentName);
                logger.warning("Valid environments are: " + EnumTools.allStrings(Environment.class));
                continue;
            }
            this.environments.add(environment);
        }

        for (String biomeName : config.getStringList(id + ".biomes")) {
            Biome biome = EnumTools.fromString(Biome.class, biomeName);
            if (biome == null) {
                logger.warning("Invalid biome for task " + id + ": " + biomeName);
                logger.warning("Valid biomes are: " + EnumTools.allStrings(Biome.class));
                continue;
            }
            this.biomes.add(biome);
        }

        if (config.contains(id + ".min-x")) {
            this.minX = config.getInt(id + ".min-x");
        }
        if (config.contains(id + ".max-x")) {
            this.maxX = config.getInt(id + ".max-x");
        }
        if (config.contains(id + ".min-y")) {
            this.minY = config.getInt(id + ".min-y");
        }
        if (config.contains(id + ".max-y")) {
            this.maxY = config.getInt(id + ".max-y");
        }
        if (config.contains(id + ".min-z")) {
            this.minZ = config.getInt(id + ".min-z");
        }
        if (config.contains(id + ".max-z")) {
            this.maxZ = config.getInt(id + ".max-z");
        }

        if (config.contains(id + ".entity-in-water")) {
            this.entityIsInWater = config.getBoolean(id + ".entity-in-water");
        }

        if (config.contains(id + ".entity-on-ground")) {
            this.entityIsOnGround = config.getBoolean(id + ".entity-on-ground");
        }

        for (String entityName : config.getStringList(id + ".entity-names")) {
            this.entityNames.add(entityName);
        }

        for (String entityTypeName : config.getStringList(id + ".entity-types")) {
            EntityType entityType = EnumTools.fromString(EntityType.class, entityTypeName);
            if (entityType == null) {
                logger.warning("Invalid entity type for task " + id + ": " + entityTypeName);
                logger.warning("Valid entity types are: " + EnumTools.allStrings(EntityType.class));
                continue;
            }
            this.entityTypes.add(entityType);
        }

        for (String categoryName : config.getStringList(id + ".entity-categories")) {
            SpawnCategory category = EnumTools.fromString(SpawnCategory.class, categoryName);
            if (category == null) {
                logger.warning("Invalid entity category for task " + id + ": " + categoryName);
                logger.warning("Valid entity categories are: " + EnumTools.allStrings(SpawnCategory.class));
                continue;
            }
            this.entityCategories.add(category);
        }

        for (String itemName : config.getStringList(id + ".item-names")) {
            this.itemNames.add(itemName);
        }

        for (String itemMaterialName : config.getStringList(id + ".item-materials")) {
            Material itemMaterial = EnumTools.fromString(Material.class, itemMaterialName);
            if (itemMaterial == null) {
                logger.warning("Invalid item material for task " + id + ": " + itemMaterialName);
                continue;
            }
            this.itemMaterials.add(itemMaterial);
        }

        for (String blockMaterialName : config.getStringList(id + ".block-materials")) {
            Material blockMaterial = EnumTools.fromString(Material.class, blockMaterialName);
            if (blockMaterial == null) {
                logger.warning("Invalid block material for task " + id + ": " + blockMaterialName);
                continue;
            }
            this.blockMaterials.add(blockMaterial);
        }

        taskConfigurations.put(id, this);
    }

    @Override
    public String toString() {
        if (message != null) {
            String formattedMessage = message;
            formattedMessage = message.replace("{total}", String.valueOf(amount));
            return formattedMessage;
        }
        else {
            return id;
        }
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public long getTimeLimitMS() {
        return timeLimitMS;
    }

    public boolean resetOnDeath() {
        return resetOnDeath;
    }

    public boolean conflictsWith(String otherTaskId) {
        if (otherTaskId == null) {
            throw new IllegalArgumentException("Other task id cannot be null");
        }
        return incompatibleTasks.contains(otherTaskId);
    }

    public double getRewardMoney() {
        return rewardMoney * TasksPlugin.getSettings().TASK_MONEY_MULTIPLIER;
    }

    public int getRewardXp() {
        return (int) (rewardXp * TasksPlugin.getSettings().TASK_XP_MULTIPLIER);
    }

    public ArrayList<Unlock> getRewardUnlocks() {
        return rewardUnlocks;
    }

    public ArrayList<String> getRewardConsoleCommands() {
        return rewardConsoleCommands;
    }

    public ArrayList<String> getRewardPlayerCommands() {
        return rewardPlayerCommands;
    }

    public ArrayList<String> getRewardMessages() {
        return rewardMessages;
    }

    public boolean meetsRequirements(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }
        if (playerProfile.hasTask(id)) {
            return false;
        }
        if (!repeatable && playerProfile.hasCompletedTask(id)) {
            return false;
        }
        Optional<Player> player = playerProfile.getPlayer();
        if (!player.isPresent()) {
            return false;
        }
        if (minLevel != null && playerProfile.getLevel() < minLevel) {
            return false;
        }
        if (maxLevel != null && playerProfile.getLevel() > maxLevel) {
            return false;
        }
        if (permission != null && !player.get().hasPermission(permission)) {
            return false;
        }
        for (String prerequisiteTask : prerequisiteTasks) {
            if (!playerProfile.hasCompletedTask(prerequisiteTask)) {
                return false;
            }
        }
        for (PlayerTask task : playerProfile.getTasks()) {
            if (conflictsWith(task.getTaskConfiguration().getId())) {
                return false;
            }
            if (task.getTaskConfiguration().conflictsWith(id)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidFor(TriggerType trigger, Player player, Location location, Entity entity, ItemStack item, Block block) {
        if (trigger == null) {
            throw new IllegalArgumentException("Trigger cannot be null");
        }
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        if (!triggers.contains(trigger)) {
            return false;
        }

        if (!worlds.isEmpty() && !worlds.contains(location.getWorld().getName())) {
            return false;
        }

        if (!environments.isEmpty() && !environments.contains(location.getWorld().getEnvironment())) {
            return false;
        }

        if (!biomes.isEmpty() && !biomes.contains(location.getBlock().getBiome())) {
            return false;
        }

        if (minX != null && location.getBlockX() < minX) {
            return false;
        }
        if (maxX != null && location.getBlockX() > maxX) {
            return false;
        }
        if (minY != null && location.getBlockY() < minY) {
            return false;
        }
        if (maxY != null && location.getBlockY() > maxY) {
            return false;
        }
        if (minZ != null && location.getBlockZ() < minZ) {
            return false;
        }
        if (maxZ != null && location.getBlockZ() > maxZ) {
            return false;
        }

        if (entityIsInWater != null) {
            if (entity == null) {
                return false;
            }
            if (entityIsInWater != entity.isInWater()) {
                return false;
            }
        }

        if (entityIsOnGround != null) {
            if (entity == null) {
                return false;
            }
            if (entityIsOnGround != entity.isOnGround()) {
                return false;
            }
        }

        if (!entityNames.isEmpty()) {
            if (entity == null) {
                return false;
            }
            if (!entityNames.contains(entity.getName())) {
                return false;
            }
        }

        if (!entityTypes.isEmpty()) {
            if (entity == null) {
                return false;
            }
            if (!entityTypes.contains(entity.getType())) {
                return false;
            }
        }

        if (!entityCategories.isEmpty()) {
            if (entity == null) {
                return false;
            }
            if (!entityCategories.contains(entity.getSpawnCategory())) {
                return false;
            }
        }

        if (!itemNames.isEmpty()) {
            if (item == null) {
                return false;
            }
            if (!item.hasItemMeta()) {
                return false;
            }
            if (!itemNames.contains(item.getItemMeta().getDisplayName())) {
                return false;
            }
        }

        if (!itemMaterials.isEmpty()) {
            if (item == null) {
                return false;
            }
            if (!itemMaterials.contains(item.getType())) {
                return false;
            }
        }

        if (!blockMaterials.isEmpty()) {
            if (block == null) {
                return false;
            }
            if (!blockMaterials.contains(block.getType())) {
                return false;
            }
        }

        return true;
    }

    public static Optional<TaskConfiguration> get(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return Optional.ofNullable(taskConfigurations.get(id));
    }

    public static Optional<TaskConfiguration> getNewTask(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }
        ArrayList<TaskConfiguration> possibleTasks = new ArrayList<>();
        for (TaskConfiguration taskConfiguration : taskConfigurations.values()) {
            if (taskConfiguration.meetsRequirements(playerProfile)) {
                possibleTasks.add(taskConfiguration);
            }
        }
        
        if (possibleTasks.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(possibleTasks.get((int) (Math.random() * possibleTasks.size())));
    }

    public static List<String> getIds() {
        return new ArrayList<>(taskConfigurations.keySet());
    }

    public static void loadConfig() {
        taskConfigurations.clear();
        YamlConfiguration config = ConfigFile.get("tasks");
        for (String id : config.getKeys(false)) {
            new TaskConfiguration(config, id);
        }
        TasksPlugin.getInstance().getLogger().info("Loaded " + taskConfigurations.size() + " tasks");
    }
}
