# Tasks Bukkit Plugin

## Config
```yaml
xp-curve:
  base: 10 # The base amount of XP required to level up
  multiplier: 1.02 # The xp increase each level (exponential)

tasks:
  allow-skipping: true # Allow players to skip tasks
  max-tasks: 8 # Maximum number of tasks to give a player at one time
  reward-xp-multiplier: 1.0 # Multiplier for task XP rewards, use this to rebalance xp without changing every task
  reward-money-multiplier: 500.0 # Multiplier for task money rewards, use this to rebalance money without changing every task. Set to 0 to disable money rewards
```

## Task Parameters
Task parameters are used to configure tasks in `tasks.yml`.

**Example:**
```yaml
task1:
    triggers:
        - break_block
    block-materials:
        - diamond_ore
    amount: 10
```
This example task (task1) will require the player to break 10 diamond ore blocks.

### Behavior
- **amount**: The number of times the task action must be completed.
- **time-limit-minutes**: The time the player has to complete the task.
- **reset-on-death**: If true, progress resets when the player dies.
- **skippable**: If true, the player can skip the task.

### Display
- **message**: The message shown to the player when the task is active.
- **actionbar**: If true, task progress is displayed in the action bar.
- **progress-display**: Controls how progress is formatted (STANDARD, PERCENT, TIME)

### Start Requirements
- **repeatable**: If true, the task can be completed multiple times.
- **min-level**: The minimum player level required to start the task.
- **max-level**: The maximum player level allowed to start the task.
- **prerequisite-tasks**: List of tasks that must be completed before this one.
- **incompatible-tasks**: List of tasks that cannot be active at the same time.
- **permission**: The permission node required to access the task.

### Rewards
- **reward-money**: Amount of in-game currency awarded on completion.
- **reward-xp**: Amount of experience points awarded on completion.
- **reward-skips**: Number of task skips awarded on completion.
- **reward-unlocks**: List of tasks unlocked upon completion.
- **reward-console-commands**: Console commands executed as a reward.
- **reward-player-commands**: Commands executed by the player as a reward.
- **reward-messages**: Messages sent to the player as a reward.

### Triggers
- **triggers**: List of trigger types that progress the task. For a full list of available triggers, see the [Trigger Types](#trigger-types) section below.

### Location Filters
- **worlds**: Worlds where the task can be completed.
- **environments**: Allowed environments (e.g., NETHER, OVERWORLD).
- **biomes**: Allowed biomes for task progress.
- **min-x**: Minimum X coordinate where the task can be completed.
- **max-x**: Maximum X coordinate where the task can be completed.
- **min-y**: Minimum Y coordinate where the task can be completed.
- **max-y**: Maximum Y coordinate where the task can be completed.
- **min-z**: Minimum Z coordinate where the task can be completed.
- **max-z**: Maximum Z coordinate where the task can be completed.

### Entity Filters
- **entity-in-water**: If true, only counts entities in water.
- **entity-on-ground**: If true, only counts entities on the ground.
- **entity-names**: List of entity names to match for the task.
- **entity-types**: List of entity types to match for the task.
- **entity-categories**: List of entity categories to match for the task.

### Item Filters
- **item-names**: List of item names to match for the task.
- **item-materials**: List of item materials to match for the task.

### Block Filters
- **block-materials**: List of block materials to match for the task.

## Trigger Types

### INTERACT_ENTITY
- **Description:** Triggered when a player right clicks an entity.
- **Associated Data:**
    - Location: The location of the entity
    - Entity: The entity interacted with
    - Item: The item in the players hand
    - Amount: 1

### DAMAGE_ENTITY
- **Description:** Triggered when a player damages an entity.
- **Associated Data:**
    - Location: The location of the entity
    - Entity: The entity damaged
    - Item: The item in the players hand
    - Amount: The damage amount

### KILL_ENTITY
- **Description:** Triggered when an entity is killed by a player.
- **Associated Data:**
    - Location: The location of the entity
    - Entity: The entity killed
    - Item: The item in the players hand
    - Amount: 1

### BUCKET_ENTITY
- **Description:** Triggered when a player buckets an entity.
- **Associated Data:**
    - Location: The location of the entity
    - Entity: The entity targeted
    - Item: The bucket
    - Amount: 1

### SHEAR_ENTITY
- **Description:** Triggered when a player shears an entity.
- **Associated Data:**
    - Location: The location of the entity
    - Entity: The entity sheared
    - Item: The item used
    - Amount: 1

### TAME_ENTITY
- **Description:** Triggered when a player tames an entity.
- **Associated Data:**
    - Location: The location of the entity
    - Entity: The entity tamed
    - Item: The item in the players hand
    - Amount: 1

### MANIPULATE_ARMORSTAND
- **Description:** Triggered when a player interacts with an armor stand.
- **Associated Data:**
    - Location: The location of the armorstand
    - Entity: The armor stand
    - Item: The item held by the player
    - Amount: 1

### THROW_EGG
- **Description:** Triggered when a player throws an egg.
- **Associated Data:**
    - Location: The location of the egg entity
    - Entity: The egg entity
    - Item: The egg item
    - Amount: 1

### PICKUP_ARROW
- **Description:** Triggered when a player picks up an arrow.
- **Associated Data:**
    - Location: The location of the arrow entity
    - Entity: The arrow entity
    - Amount: 1

### CRAFT_ITEM
- **Description:** Triggered when a player crafts an item.
- **Associated Data:**
    - Location: The location of the block
    - Entity: The player crafting the item
    - Item: The crafted item
    - Block: The block used to craft the item
    - Amount: The amount crafted

### ENCHANT_ITEM
- **Description:** Triggered when a player enchants an item.
- **Associated Data:**
    - Location: The location of the enchanting table
    - Entity: The player enchanting the item
    - Item: The enchanted item
    - Block: The enchanting table
    - Amount: 1

### CONSUME_ITEM
- **Description:** Triggered when a player consumes an item.
- **Associated Data:**
    - Location: The location of the player
    - Entity: The player
    - Item: The consumed item
    - Amount: The quantity of the item consumed

### HARVEST_ITEM
- **Description:** Triggered when a player harvests an item from a block.
- **Associated Data:**
    - Location: The location of the block
    - Entity: The player
    - Item: The harvested item
    - Block: The block harvested from
    - Amount: The amount harvested

### SMELT_ITEM
- **Description:** Triggered when a player smelts an item.
- **Associated Data:**
    - Location: The location of the block
    - Item: The smelted item
    - Block: The block used to smelt
    - Amount: The amount smelted

### BREAK_ITEM
- **Description:** Triggered when a player breaks an item.
- **Associated Data:**
    - Location: The location of the player
    - Entity: The player breaking the item
    - Item: The broken item
    - Amount: The stack size of the broken item

### DAMAGE_ITEM
- **Description:** Triggered when a player's item takes damage.
- **Associated Data:**
    - Location: The location of the player
    - Entity: The player whose item is damaged
    - Item: The damaged item
    - Amount: The damage amount

### MEND_ITEM
- **Description:** Triggered when a player's item is mended.
- **Associated Data:**
    - Location: The location of the player
    - Entity: The player whose item is mended
    - Item: The mended item
    - Amount: The amount mended

### DROP_ITEM
- **Description:** Triggered when a player drops an item.
- **Associated Data:**
    - Location: The location of the item entity
    - Entity: The dropped item entity
    - Item: The dropped item
    - Amount: The amount dropped

### BREAK_BLOCK_DROP_ITEM
- **Description:** Triggered when a player breaks a block and it drops an item.
- **Associated Data:**
    - Location: The location of the block
    - Entity: The player breaking the block
    - Item: The dropped item
    - Block: The broken block
    - Amount: The amount dropped

### KILL_ENTITY_DROP_ITEM
- **Description:** Triggered when a player kills an entity and it drops an item.
- **Associated Data:**
    - Location: The location of the killed entity
    - Entity: The killed entity
    - Item: The dropped item
    - Amount: The amount dropped

### BREAK_BLOCK
- **Description:** Triggered when a player breaks a block.
- **Associated Data:**
    - Location: The location of the block
    - Entity: The player breaking the block
    - Item: The item held by the player
    - Block: The broken block
    - Amount: 1

### PLACE_BLOCK
- **Description:** Triggered when a player places a block.
- **Associated Data:**
    - Location: The location of the block
    - Entity: The player placing the block
    - Item: The item in the players hand
    - Block: The placed block
    - Amount: 1

### HARVEST_BLOCK
- **Description:** Triggered when a player harvests a block (e.g. berries).
- **Associated Data:**
    - Location: The location of the block
    - Entity: The player harvesting the block
    - Item: The item in the players hand
    - Block: The harvested block
    - Amount: 1

### TAKE_LECTERN_BOOK
- **Description:** Triggered when a player takes a book from a lectern.
- **Associated Data:**
    - Location: The location of the lectern
    - Entity: The player taking the book
    - Item: The book
    - Block: The lectern
    - Amount: The amount of the book

### FILL_BUCKET
- **Description:** Triggered when a player fills a bucket.
- **Associated Data:**
    - Location: The location of the source block
    - Entity: The player filling the bucket
    - Item: The filled bucket
    - Block: The source block
    - Amount: 1

### EMPTY_BUCKET
- **Description:** Triggered when a player empties a bucket.
- **Associated Data:**
    - Location: The location of the target block
    - Entity: The player emptying the bucket
    - Item: The bucket
    - Block: The target block
    - Amount: 1

### ENTER_BED
- **Description:** Triggered when a player enters a bed.
- **Associated Data:**
    - Location: The location of the bed
    - Entity: The player entering the bed
    - Block: The bed
    - Amount: 1

### LEAVE_BED
- **Description:** Triggered when a player leaves a bed.
- **Associated Data:**
    - Location: The location of the bed
    - Entity: The player leaving the bed
    - Block: The bed
    - Amount: 1

### RIPTIDE
- **Description:** Triggered when a player laucnhes themself with riptide.
- **Associated Data:**
    - Location: The location of the player
    - Entity: The player using the trident
    - Item: The trident
    - Amount: 1

### TELEPORT_TO
- **Description:** Triggered when a player teleports to a location.
- **Associated Data:**
    - Location: The destination
    - Entity: The player teleporting
    - Item: The item in the players hand
    - Amount: 1

### TELEPORT_FROM
- **Description:** Triggered when a player teleports from a location.
- **Associated Data:**
    - Location: The origin
    - Entity: The player teleporting
    - Item: The item in the players hand
    - Amount: 1

### PORTAL_TO
- **Description:** Triggered when a player enters a portal to a location.
- **Associated Data:**
    - Location: The destination
    - Entity: The player entering the portal
    - Item: The item in the players hand
    - Amount: 1

### PORTAL_FROM
- **Description:** Triggered when a player leaves a portal from a location.
- **Associated Data:**
    - Location: The origin
    - Entity: The player entering the portal
    - Item: The item in the players hand
    - Amount: 1

### DEATH
- **Description:** Triggered when a player dies.
- **Associated Data:**
    - Location: The location of the player who died
    - Entity: The player who died
    - Item: The item in hand
    - Amount: 1

### RESPAWN
- **Description:** Triggered when a player respawns.
- **Associated Data:**
    - Location: The respawn location
    - Entity: The player respawning
    - Item: The item in the players hand
    - Amount: 1

### TIMER
- **Description:** Executes once per second.
- **Associated Data:**
    - Location: The location of the player
    - Entity: The player with the task
    - Item: The item in the players hand
    - Amount: 1

## Unlock Parameters

### Display
- **name"**: The display name of the unlock.

### Behavior
- **level"**: The level the unlock is given at.

### Rewards
- **permissions"**: Permissions given to the player when unlocked.
- **console-commands"**: Console commands to run when unlocked.
- **player-commands"**: Commands for the player to run when unlocked.
- **messages"**: Messages to send to the player when unlocked