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

public class TaskConfiguration {

    private static HashMap<String, TaskConfiguration> taskConfigurations = new HashMap<>();

    private static List<String> settings = List.of(
        "amount",
        "min-level",
        "max-level",
        "reward-money",
        "reward-xp",
        "triggers",
        "min-x",
        "max-x",
        "min-y",
        "max-y",
        "min-z",
        "max-z",
        "worlds",
        "environments",
        "biomes",
        "entity-names",
        "entity-types",
        "spawn-categories",
        "item-names",
        "item-materials",
        "block-materials"
    );

    private final String id;

    private int amount;

    // Requirements
    private Integer minLevel = null;
    private Integer maxLevel = null;

    // Rewards
    private double rewardMoney;
    private int rewardXp;

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

    private HashSet<String> entityNames = new HashSet<>();
    private HashSet<EntityType> entityTypes = new HashSet<>();
    private HashSet<SpawnCategory> spawnCategories = new HashSet<>();

    private HashSet<String> itemNames = new HashSet<>();
    private HashSet<Material> itemMaterials = new HashSet<>();

    private HashSet<Material> blockMaterials = new HashSet<>();

    public TaskConfiguration(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        this.id = id;

        Logger logger = TasksPlugin.getInstance().getLogger();
        YamlConfiguration config = ConfigFile.get("tasks");

        for (String setting : config.getConfigurationSection(id).getKeys(false)) {
            if (!settings.contains(setting)) {
                logger.warning("Invalid setting for task " + id + ": " + setting);
                String possibleSettings = String.join(", ", settings);
                logger.warning("Valid settings are: " + possibleSettings);
            }
        }

        amount = Math.max(config.getInt(id + ".amount"), 1);

        if (config.contains(id + ".min-level")) {
            minLevel = config.getInt(id + ".min-level");
        }
        if (config.contains(id + ".max-level")) {
            maxLevel = config.getInt(id + ".max-level");
        }
        
        rewardMoney = config.getDouble(id + ".reward-money");
        rewardXp = config.getInt(id + ".reward-xp");

        for (String triggerName : config.getStringList(id + ".triggers")) {
            TriggerType trigger = EnumTools.fromString(TriggerType.class, triggerName);
            if (trigger == null) {
                logger.warning("Invalid trigger for task " + id + ": " + triggerName);
                logger.warning("Valid triggers are: " + EnumTools.allStrings(TriggerType.class));
                continue;
            }
            triggers.add(trigger);
        }
        if (triggers.isEmpty()) {
            logger.warning("No triggers for task " + id);
        }

        for (String worldName : config.getStringList(id + ".worlds")) {
            worlds.add(worldName);
        }

        for (String environmentName : config.getStringList(id + ".environments")) {
            Environment environment = EnumTools.fromString(Environment.class, environmentName);
            if (environment == null) {
                logger.warning("Invalid environment for task " + id + ": " + environmentName);
                logger.warning("Valid environments are: " + EnumTools.allStrings(Environment.class));
                continue;
            }
            environments.add(environment);
        }

        for (String biomeName : config.getStringList(id + ".biomes")) {
            Biome biome = EnumTools.fromString(Biome.class, biomeName);
            if (biome == null) {
                logger.warning("Invalid biome for task " + id + ": " + biomeName);
                logger.warning("Valid biomes are: " + EnumTools.allStrings(Biome.class));
                continue;
            }
            biomes.add(biome);
        }

        if (config.contains(id + ".min-x")) {
            minX = config.getInt(id + ".min-x");
        }
        if (config.contains(id + ".max-x")) {
            maxX = config.getInt(id + ".max-x");
        }
        if (config.contains(id + ".min-y")) {
            minY = config.getInt(id + ".min-y");
        }
        if (config.contains(id + ".max-y")) {
            maxY = config.getInt(id + ".max-y");
        }
        if (config.contains(id + ".min-z")) {
            minZ = config.getInt(id + ".min-z");
        }
        if (config.contains(id + ".max-z")) {
            maxZ = config.getInt(id + ".max-z");
        }

        for (String entityName : config.getStringList(id + ".entity-names")) {
            entityNames.add(entityName);
        }

        for (String entityTypeName : config.getStringList(id + ".entity-types")) {
            EntityType entityType = EnumTools.fromString(EntityType.class, entityTypeName);
            if (entityType == null) {
                logger.warning("Invalid entity type for task " + id + ": " + entityTypeName);
                logger.warning("Valid entity types are: " + EnumTools.allStrings(EntityType.class));
                continue;
            }
            entityTypes.add(entityType);
        }

        for (String spawnCategoryName : config.getStringList(id + ".spawn-categories")) {
            SpawnCategory spawnCategory = EnumTools.fromString(SpawnCategory.class, spawnCategoryName);
            if (spawnCategory == null) {
                logger.warning("Invalid spawn category for task " + id + ": " + spawnCategoryName);
                logger.warning("Valid spawn categories are: " + EnumTools.allStrings(SpawnCategory.class));
                continue;
            }
            spawnCategories.add(spawnCategory);
        }

        for (String itemName : config.getStringList(id + ".item-names")) {
            itemNames.add(itemName);
        }

        for (String itemMaterialName : config.getStringList(id + ".item-materials")) {
            Material itemMaterial = EnumTools.fromString(Material.class, itemMaterialName);
            if (itemMaterial == null) {
                logger.warning("Invalid item material for task " + id + ": " + itemMaterialName);
                logger.warning("Valid item materials are: " + EnumTools.allStrings(Material.class));
                continue;
            }
            itemMaterials.add(itemMaterial);
        }

        for (String blockMaterialName : config.getStringList(id + ".block-materials")) {
            Material blockMaterial = EnumTools.fromString(Material.class, blockMaterialName);
            if (blockMaterial == null) {
                logger.warning("Invalid block material for task " + id + ": " + blockMaterialName);
                logger.warning("Valid block materials are: " + EnumTools.allStrings(Material.class));
                continue;
            }
            blockMaterials.add(blockMaterial);
        }

        taskConfigurations.put(id, this);
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public double getRewardMoney() {
        return rewardMoney;
    }

    public int getRewardXp() {
        return rewardXp;
    }

    public boolean meetsRequirements(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }
        if (playerProfile.hasTask(id)) {
            return false;
        }
        if (minLevel != null && playerProfile.getLevel() < minLevel) {
            return false;
        }
        if (maxLevel != null && playerProfile.getLevel() > maxLevel) {
            return false;
        }
        return true;
    }

    public boolean isValidFor(TriggerType trigger, Player player, Entity entity, ItemStack item, Block block) {
        if (trigger == null) {
            throw new IllegalArgumentException("Trigger cannot be null");
        }
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        if (!triggers.contains(trigger)) {
            return false;
        }

        Location location;
        if (block != null) {
            location = block.getLocation();
        } 
        else if (entity != null) {
            location = entity.getLocation();
        }
        else {
            location = player.getLocation();
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

        if (!spawnCategories.isEmpty()) {
            if (entity == null) {
                return false;
            }
            if (!spawnCategories.contains(entity.getSpawnCategory())) {
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
        return Optional.ofNullable(taskConfigurations.get(id));
    }

    public static ArrayList<TaskConfiguration> getPossibleTasks(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }
        ArrayList<TaskConfiguration> possibleTasks = new ArrayList<>();
        for (TaskConfiguration taskConfiguration : taskConfigurations.values()) {
            if (taskConfiguration.meetsRequirements(playerProfile)) {
                possibleTasks.add(taskConfiguration);
            }
        }
        return possibleTasks;
    }

    public static void loadConfig() {
        taskConfigurations.clear();
        YamlConfiguration config = ConfigFile.get("tasks");
        for (String id : config.getKeys(false)) {
            new TaskConfiguration(id);
        }
    }
}
