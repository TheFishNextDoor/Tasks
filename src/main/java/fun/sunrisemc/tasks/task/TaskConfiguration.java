package fun.sunrisemc.tasks.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.unlock.UnlockManager;
import fun.sunrisemc.tasks.utils.EnumUtils;
import fun.sunrisemc.tasks.utils.Log;
import fun.sunrisemc.tasks.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

public class TaskConfiguration {

    private final String id;

    // Behavior

    private int amount;

    private String message = null;

    private long timeLimitMS = 0;

    private boolean resetOnDeath = false;

    private boolean skippable = true;

    private boolean actionbar = true;

    private ProgressDisplayType progressDisplayType = ProgressDisplayType.STANDARD;

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
    private int rewardSkips;

    private ArrayList<Unlock> rewardUnlocks = new ArrayList<>();

    private ArrayList<String> rewardConsoleCommands = new ArrayList<>();
    private ArrayList<String> rewardPlayerCommands = new ArrayList<>();
    private ArrayList<String> rewardMessages = new ArrayList<>();

    // Conditions
    private HashSet<TriggerType> triggers = new HashSet<>();

    private HashSet<String> worlds = new HashSet<>();
    private HashSet<Environment> environments = new HashSet<>();
    private HashSet<String> biomes = new HashSet<>();

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

    TaskConfiguration(YamlConfiguration config, String id) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        this.id = id;

        for (String setting : config.getConfigurationSection(id).getKeys(false)) {
            if (!TaskConfigurationManager.settings.contains(setting)) {
                Log.warning("Invalid setting for task " + id + ": " + setting);
                String possibleSettings = String.join(", ", TaskConfigurationManager.settings);
                Log.warning("Valid settings are: " + possibleSettings);
            }
        }

        this.amount = Math.max(config.getInt(id + ".amount"), 1);

        this.message = config.getString(id + ".message");

        this.timeLimitMS = Math.max(config.getInt(id + ".time-limit-minutes") * 60000, 0);

        if (config.contains(id + ".reset-on-death")) {
            this.resetOnDeath = config.getBoolean(id + ".reset-on-death");
        }

        if (config.contains(id + ".skippable")) {
            this.skippable = config.getBoolean(id + ".skippable");
        }

        if (config.contains(id + ".actionbar")) {
            this.actionbar = config.getBoolean(id + ".actionbar");
        }

        if (config.contains(id + ".progress-display")) {
            String progressDisplayName = config.getString(id + ".progress-display");
            ProgressDisplayType potentialProgressDisplayType = EnumUtils.fromString(ProgressDisplayType.class, progressDisplayName);
            if (potentialProgressDisplayType == null) {
                Log.warning("Invalid progress display for task " + id + ": " + progressDisplayName);
                Log.warning("Valid progress displays are: " + EnumUtils.allStrings(ProgressDisplayType.class));
            }
            else {
                this.progressDisplayType = potentialProgressDisplayType;
            }
        }

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
        this.rewardSkips = config.getInt(id + ".reward-skips");

        for (String unlockName : config.getStringList(id + ".reward-unlocks")) {
            Optional<Unlock> unlock = UnlockManager.get(unlockName);
            if (!unlock.isPresent()) {
                Log.warning("Invalid reward unlock for task " + id + ": " + unlockName);
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
            TriggerType trigger = EnumUtils.fromString(TriggerType.class, triggerName);
            if (trigger == null) {
                Log.warning("Invalid trigger for task " + id + ": " + triggerName);
                Log.warning("Valid triggers are: " + EnumUtils.allStrings(TriggerType.class));
                continue;
            }
            this.triggers.add(trigger);
        }
        if (this.triggers.isEmpty()) {
            Log.warning("No triggers for task " + id);
        }

        for (String worldName : config.getStringList(id + ".worlds")) {
            this.worlds.add(worldName);
        }

        for (String environmentName : config.getStringList(id + ".environments")) {
            Environment environment = EnumUtils.fromString(Environment.class, environmentName);
            if (environment == null) {
                Log.warning("Invalid environment for task " + id + ": " + environmentName);
                Log.warning("Valid environments are: " + EnumUtils.allStrings(Environment.class));
                continue;
            }
            this.environments.add(environment);
        }

        for (String biomeName : config.getStringList(id + ".biomes")) {
            this.biomes.add(StringUtils.normalizeString(biomeName));
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
            EntityType entityType = EnumUtils.fromString(EntityType.class, entityTypeName);
            if (entityType == null) {
                Log.warning("Invalid entity type for task " + id + ": " + entityTypeName);
                Log.warning("Valid entity types are: " + EnumUtils.allStrings(EntityType.class));
                continue;
            }
            this.entityTypes.add(entityType);
        }

        for (String categoryName : config.getStringList(id + ".entity-categories")) {
            SpawnCategory category = EnumUtils.fromString(SpawnCategory.class, categoryName);
            if (category == null) {
                Log.warning("Invalid entity category for task " + id + ": " + categoryName);
                Log.warning("Valid entity categories are: " + EnumUtils.allStrings(SpawnCategory.class));
                continue;
            }
            this.entityCategories.add(category);
        }

        for (String itemName : config.getStringList(id + ".item-names")) {
            this.itemNames.add(itemName);
        }

        for (String itemMaterialName : config.getStringList(id + ".item-materials")) {
            Material itemMaterial = EnumUtils.fromString(Material.class, itemMaterialName);
            if (itemMaterial == null) {
                Log.warning("Invalid item material for task " + id + ": " + itemMaterialName);
                continue;
            }
            this.itemMaterials.add(itemMaterial);
        }

        for (String blockMaterialName : config.getStringList(id + ".block-materials")) {
            Material blockMaterial = EnumUtils.fromString(Material.class, blockMaterialName);
            if (blockMaterial == null) {
                Log.warning("Invalid block material for task " + id + ": " + blockMaterialName);
                continue;
            }
            this.blockMaterials.add(blockMaterial);
        }

        TaskConfigurationManager.taskConfigurations.put(id, this);
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

    public boolean isSkippable() {
        return skippable;
    }

    public boolean showActionbar() {
        return actionbar;
    }

    public ProgressDisplayType getProgressDisplayType() {
        return progressDisplayType;
    }

    public boolean conflictsWith(String otherTaskId) {
        if (otherTaskId == null) {
            throw new IllegalArgumentException("Other task id cannot be null");
        }
        return incompatibleTasks.contains(otherTaskId);
    }

    public double getRewardMoney() {
        return rewardMoney * TasksPlugin.getMainConfig().TASK_MONEY_MULTIPLIER;
    }

    public int getRewardXp() {
        return (int) (rewardXp * TasksPlugin.getMainConfig().TASK_XP_MULTIPLIER);
    }

    public int getRewardSkips() {
        return rewardSkips;
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

        Optional<Player> optionalPlayer = playerProfile.getPlayer();
        if (!optionalPlayer.isPresent()) {
            return false;
        }
        Player player = optionalPlayer.get();

        if (playerProfile.hasTask(id)) {
            return false;
        }

        if (!repeatable && playerProfile.hasCompletedTask(id)) {
            return false;
        }

        if (minLevel != null && playerProfile.getLevel() < minLevel) {
            return false;
        }

        if (maxLevel != null && playerProfile.getLevel() > maxLevel) {
            return false;
        }

        if (permission != null && !player.hasPermission(permission)) {
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

        if (!biomes.isEmpty() && !biomes.contains(StringUtils.normalizeString(location.getBlock().getBiome().name()))) {
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
}