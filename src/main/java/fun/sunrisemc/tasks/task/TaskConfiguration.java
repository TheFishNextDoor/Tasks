package fun.sunrisemc.tasks.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fun.sunrisemc.tasks.TasksPlugin;
import fun.sunrisemc.tasks.file.ConfigFile;
import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.unlock.UnlockManager;
import fun.sunrisemc.tasks.utils.Names;
import fun.sunrisemc.tasks.utils.StringUtils;

public class TaskConfiguration {

    private final @NotNull List<String> SETTINGS = List.of(
        "amount",
        "message",
        "time-limit-minutes",
        "reset-on-death",
        "skippable",
        "actionbar",
        "progress-display",
        "repeatable",
        "min-level",
        "max-level",
        "prerequisite-tasks",
        "incompatible-tasks",
        "permission",
        "reward-money",
        "reward-xp",
        "reward-skips",
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

    private final @NotNull String id;

    // Behavior

    private int amount;

    private Optional<String> message = Optional.empty();

    private long timeLimitMS;

    private boolean resetOnDeath;

    private boolean skippable;

    private boolean actionbar;

    private @NotNull ProgressDisplayType progressDisplayType = ProgressDisplayType.STANDARD;

    // Requirements

    private boolean repeatable;

    private Optional<Integer> minLevel;
    private Optional<Integer> maxLevel;

    private Optional<String> permission;

    private @NotNull ArrayList<String> prerequisiteTasks = new ArrayList<>();
    private @NotNull HashSet<String> incompatibleTasks = new HashSet<>();

    // Rewards

    private double rewardMoney;
    private int rewardXp;
    private int rewardSkips;

    private @NotNull ArrayList<Unlock> rewardUnlocks = new ArrayList<>();

    private @NotNull ArrayList<String> rewardConsoleCommands = new ArrayList<>();
    private @NotNull ArrayList<String> rewardPlayerCommands = new ArrayList<>();
    private @NotNull ArrayList<String> rewardMessages = new ArrayList<>();

    // Conditions (Triggers)
    
    private @NotNull HashSet<TriggerType> triggers = new HashSet<>();

    // Conditions (Location)

    private @NotNull HashSet<String> worlds = new HashSet<>();
    private @NotNull HashSet<Environment> environments = new HashSet<>();
    private @NotNull HashSet<Biome> biomes = new HashSet<>();

    private Optional<Integer> minX;
    private Optional<Integer> maxX;
    private Optional<Integer> minY;
    private Optional<Integer> maxY;
    private Optional<Integer> minZ;
    private Optional<Integer> maxZ;

    // Conditions (Entity)

    private Optional<Boolean> entityIsInWater;
    private Optional<Boolean> entityIsOnGround;

    private @NotNull HashSet<String> entityNames = new HashSet<>();
    private @NotNull HashSet<EntityType> entityTypes = new HashSet<>();
    private @NotNull HashSet<SpawnCategory> entityCategories = new HashSet<>();

    // Conditions (Item)

    private @NotNull HashSet<String> itemNames = new HashSet<>();
    private @NotNull HashSet<Material> itemMaterials = new HashSet<>();

    // Conditions (Block)

    private @NotNull HashSet<Material> blockMaterials = new HashSet<>();

    // Constructor

    protected TaskConfiguration(@NotNull ConfigFile config, @NotNull String id) {
        this.id = id;

        for (String setting : config.getKeys(id)) {
            if (!SETTINGS.contains(setting)) {
                TasksPlugin.logWarning("Invalid setting for task " + id + ": " + setting + ".");
                String possibleSettings = String.join(", ", SETTINGS);
                TasksPlugin.logWarning("Valid settings are: " + possibleSettings + ".");
            }
        }

        this.amount = config.getIntClamped(id + ".amount", 1, Integer.MAX_VALUE).orElse(1);

        this.message = config.getString(id + ".message");

        this.timeLimitMS = config.getInt(id + ".time-limit-minutes").orElse(0) * 60000;

        this.resetOnDeath = config.getBoolean(id + ".reset-on-death").orElse(false);

        this.skippable = config.getBoolean(id + ".skippable").orElse(true);

        this.actionbar = config.getBoolean( id + ".actionbar").orElse(true);

        if (config.hasKey(id + ".progress-display")) {
            Optional<String> progressDisplayName = config.getString(id + ".progress-display");
            if (progressDisplayName.isPresent()) {
                Optional<ProgressDisplayType> progressDisplayType = StringUtils.parseProgressDisplayType(progressDisplayName.get());
                if (progressDisplayType.isEmpty()) {
                    TasksPlugin.logWarning("Invalid progress display for task " + id + ": " + progressDisplayName + ".");
                    TasksPlugin.logWarning("Valid progress displays are: " + String.join(", ", Names.getProgressDisplayTypeNames()) + ".");
                }
                else {
                    this.progressDisplayType = progressDisplayType.get();
                }
            }
        }

        this.repeatable = config.getBoolean(id + ".repeatable").orElse(true);

        this.minLevel = config.getInt(id + ".min-level");
        this.maxLevel = config.getInt(id + ".max-level");

        this.permission = config.getString(id + ".permission");

        this.prerequisiteTasks.addAll(config.getStringList(id + ".prerequisite-tasks").orElse(new ArrayList<>()));
        this.incompatibleTasks.addAll(config.getStringList(id + ".incompatible-tasks").orElse(new ArrayList<>()));
        
        this.rewardMoney = config.getDoubleClamped(id + ".reward-money", 0.0, Double.MAX_VALUE).orElse(0.0);
        this.rewardXp = config.getInt(id + ".reward-xp").orElse(0);
        this.rewardSkips = config.getInt( id + ".reward-skips").orElse(0);

        for (String unlockName : config.getStringList(id + ".reward-unlocks").orElse(new ArrayList<>())) {
            Optional<Unlock> unlock = UnlockManager.get(unlockName);
            if (!unlock.isPresent()) {
                TasksPlugin.logWarning("Invalid reward unlock for task " + id + ": " + unlockName + ".");
                continue;
            }
            this.rewardUnlocks.add(unlock.get());
        }

        for (String consoleCommand : config.getStringList(id + ".reward-console-commands").orElse(new ArrayList<>())) {
            this.rewardConsoleCommands.add(consoleCommand);
        }

        for (String playerCommand : config.getStringList(id + ".reward-player-commands").orElse(new ArrayList<>())) {
            this.rewardPlayerCommands.add(playerCommand);
        }

        for (String rewardMessage : config.getStringList(id + ".reward-messages").orElse(new ArrayList<>())) {
            this.rewardMessages.add(ChatColor.translateAlternateColorCodes('&', rewardMessage));
        }

        for (String triggerName : config.getStringList(id + ".triggers").orElse(new ArrayList<>())) {
            Optional<TriggerType> triggerType = StringUtils.parseTriggerType(triggerName);
            if (triggerType.isEmpty()) {
                TasksPlugin.logWarning("Invalid trigger for task " + id + ": " + triggerName + ".");
                TasksPlugin.logWarning("Valid triggers are: " + String.join(", ", Names.getTriggerTypeNames()) + ".");
                continue;
            }
            this.triggers.add(triggerType.get());
        }
        if (this.triggers.isEmpty()) {
            TasksPlugin.logWarning("No triggers for task " + id);
        }

        for (String worldName : config.getStringList(id + ".worlds").orElse(new ArrayList<>())) {
            this.worlds.add(worldName);
        }

        for (String environmentName : config.getStringList(id + ".environments").orElse(new ArrayList<>())) {
            Optional<Environment> environment = StringUtils.parseEnvironment(environmentName);
            if (environment.isEmpty()) {
                TasksPlugin.logWarning("Invalid environment for task " + id + ": " + environmentName + ".");
                TasksPlugin.logWarning("Valid environments are: " + String.join(", ", Names.getEnvironmentNames()) + ".");
                continue;
            }
            this.environments.add(environment.get());
        }

        for (String biomeName : config.getStringList(id + ".biomes").orElse(new ArrayList<>())) {
            Optional<Biome> biome = StringUtils.parseBiome(biomeName);
            if (biome.isEmpty()) {
                TasksPlugin.logWarning("Invalid biome for task " + id + ": " + biomeName + ".");
                TasksPlugin.logWarning("Valid biomes are: " + String.join(", ", Names.getBiomeNames()) + ".");
                continue;
            }
            this.biomes.add(biome.get());
        }

        this.minX = config.getInt(id + ".min-x");
        this.maxX = config.getInt(id + ".max-x");
        this.minY = config.getInt(id + ".min-y");
        this.maxY = config.getInt(id + ".max-y");
        this.minZ = config.getInt(id + ".min-z");
        this.maxZ = config.getInt(id + ".max-z");

        this.entityIsInWater = config.getBoolean(id + ".entity-in-water");

        this.entityIsOnGround = config.getBoolean(id + ".entity-on-ground");

        for (String entityName : config.getStringList(id + ".entity-names").orElse(new ArrayList<>())) {
            this.entityNames.add(entityName);
        }

        for (String entityTypeName : config.getStringList(id + ".entity-types").orElse(new ArrayList<>())) {
            Optional<EntityType> entityType = StringUtils.parseEntityType(entityTypeName);
            if (entityType.isEmpty()) {
                TasksPlugin.logWarning("Invalid entity type for task " + id + ": " + entityTypeName + ".");
                TasksPlugin.logWarning("Valid entity types are: " + String.join(", ", Names.getEntityTypeNames()) + ".");
                continue;
            }
            this.entityTypes.add(entityType.get());
        }

        for (String categoryName : config.getStringList(id + ".entity-categories").orElse(new ArrayList<>())) {
            Optional<SpawnCategory> entityCategory = StringUtils.parseSpawnCategory(categoryName);
            if (entityCategory.isEmpty()) {
                TasksPlugin.logWarning("Invalid entity category for task " + id + ": " + categoryName + ".");
                TasksPlugin.logWarning("Valid entity categories are: " + String.join(", ", Names.getSpawnCategoryNames()) + ".");
                continue;
            }
            this.entityCategories.add(entityCategory.get());
        }

        for (String itemName : config.getStringList(id + ".item-names").orElse(new ArrayList<>())) {
            this.itemNames.add(itemName);
        }

        for (String itemMaterialName : config.getStringList(id + ".item-materials").orElse(new ArrayList<>())) {
            Optional<Material> itemMaterial = StringUtils.parseMaterial(itemMaterialName);
            if (itemMaterial.isEmpty()) {
                TasksPlugin.logWarning("Invalid item material for task " + id + ": " + itemMaterialName + ".");
                continue;
            }
            this.itemMaterials.add(itemMaterial.get());
        }

        for (String blockMaterialName : config.getStringList(id + ".block-materials").orElse(new ArrayList<>())) {
            Optional<Material> blockMaterial = StringUtils.parseMaterial(blockMaterialName);
            if (blockMaterial.isEmpty()) {
                TasksPlugin.logWarning("Invalid block material for task " + id + ": " + blockMaterialName + ".");
                continue;
            }
            this.blockMaterials.add(blockMaterial.get());
        }
    }

    // Displaying

    @Override
    @NotNull
    public String toString() {
        if (message.isPresent()) {
            return message.get().replace("{total}", String.valueOf(amount));
        }
        else {
            return id;
        }
    }

    // Identifier

    @NotNull
    public String getId() {
        return id;
    }

    // Behavior

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

    @NotNull
    public ProgressDisplayType getProgressDisplayType() {
        return progressDisplayType;
    }

    // Requirements

    public boolean conflictsWith(@NotNull String otherTaskId) {
        return incompatibleTasks.contains(otherTaskId);
    }

    public boolean meetsRequirements(@NotNull PlayerProfile playerProfile) {
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

        if (minLevel.isPresent() && playerProfile.getLevel() < minLevel.get()) {
            return false;
        }

        if (maxLevel.isPresent() && playerProfile.getLevel() > maxLevel.get()) {
            return false;
        }

        if (permission.isPresent() && !player.hasPermission(permission.get())) {
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

    // Rewards

    public double getRewardMoney() {
        return rewardMoney * TasksPlugin.getMainConfig().TASK_MONEY_MULTIPLIER;
    }

    public int getRewardXp() {
        return (int) (rewardXp * TasksPlugin.getMainConfig().TASK_XP_MULTIPLIER);
    }

    public int getRewardSkips() {
        return rewardSkips;
    }

    @NotNull
    public ArrayList<Unlock> getRewardUnlocks() {
        return rewardUnlocks;
    }

    @NotNull
    public ArrayList<String> getRewardConsoleCommands() {
        return rewardConsoleCommands;
    }

    @NotNull
    public ArrayList<String> getRewardPlayerCommands() {
        return rewardPlayerCommands;
    }

    @NotNull
    public ArrayList<String> getRewardMessages() {
        return rewardMessages;
    }

    // Conditions

    public boolean isValidFor(@NotNull TriggerType trigger, @NotNull Player player, @NotNull Location location, @Nullable Entity entity, @Nullable ItemStack item, @Nullable Block block) {
        if (!triggers.contains(trigger)) {
            return false;
        }

        World world = location.getWorld();
        if (world == null) {
            return false;
        }

        if (!worlds.isEmpty() && !worlds.contains(world.getName())) {
            return false;
        }

        if (!environments.isEmpty() && !environments.contains(world.getEnvironment())) {
            return false;
        }

        if (!biomes.isEmpty() && !biomes.contains(location.getBlock().getBiome())) {
            return false;
        }

        if (minX.isPresent() && location.getBlockX() < minX.get()) {
            return false;
        }
        if (maxX.isPresent() && location.getBlockX() > maxX.get()) {
            return false;
        }
        if (minY.isPresent() && location.getBlockY() < minY.get()) {
            return false;
        }
        if (maxY.isPresent() && location.getBlockY() > maxY.get()) {
            return false;
        }
        if (minZ.isPresent() && location.getBlockZ() < minZ.get()) {
            return false;
        }
        if (maxZ.isPresent() && location.getBlockZ() > maxZ.get()) {
            return false;
        }

        if (entityIsInWater.isPresent()) {
            if (entity == null) {
                return false;
            }
            if (entityIsInWater.get() != entity.isInWater()) {
                return false;
            }
        }

        if (entityIsOnGround.isPresent()) {
            if (entity == null) {
                return false;
            }
            if (entityIsOnGround.get() != entity.isOnGround()) {
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

            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) {
                return false;
            }

            if (!itemNames.contains(itemMeta.getDisplayName())) {
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