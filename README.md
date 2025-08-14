# Tasks Bukkit Plugin

## Trigger Types

### INTERACT_ENTITY
- **Description:** Triggered when a player right clicks an entity.
- **Associated Data:**
    - Player: The player who interacted with the entity
    - Location: The location of the entity
    - Entity: The entity interacted with
    - Item: The item in the players hand
    - Amount: 1

### DAMAGE_ENTITY
- **Description:** Triggered when a player damages an entity.
- **Associated Data:**
    - Player: The player who caused the damage
    - Location: The location of the entity
    - Entity: The entity damaged
    - Item: The item in the players hand
    - Amount: The damage amount

### KILL_ENTITY
- **Description:** Triggered when an entity is killed by a player.
- **Associated Data:**
    - Player: The player who killed the entity
    - Location: The location of the entity
    - Entity: The entity killed
    - Item: The item in the players hand
    - Amount: 1

### BUCKET_ENTITY
- **Description:** Triggered when a player buckets an entity.
- **Associated Data:**
    - Player: The player using the bucket
    - Location: The location of the entity
    - Entity: The entity targeted
    - Item: The bucket
    - Amount: 1

### SHEAR_ENTITY
- **Description:** Triggered when a player shears an entity.
- **Associated Data:**
    - Player: The player using the shears
    - Location: The location of the entity
    - Entity: The entity sheared
    - Item: The item used
    - Amount: 1

### TAME_ENTITY
- **Description:** Triggered when a player tames an entity.
- **Associated Data:**
    - Player: The player taming the entity
    - Location: The location of the entity
    - Entity: The entity tamed
    - Item: The item in the players hand
    - Amount: 1

### MANIPULATE_ARMORSTAND
- **Description:** Triggered when a player interacts with an armor stand.
- **Associated Data:**
    - Player: The player manipulating the armor stand
    - Location: The location of the armorstand
    - Entity: The armor stand
    - Item: The item held by the player
    - Amount: 1

### THROW_EGG
- **Description:** Triggered when a player throws an egg.
- **Associated Data:**
    - Player: The player throwing the egg
    - Location: The location of the egg entity
    - Entity: The egg entity
    - Item: The egg item
    - Amount: 1

### PICKUP_ARROW
- **Description:** Triggered when a player picks up an arrow.
- **Associated Data:**
    - Player: The player picking up the arrow
    - Location: The location of the arrow entity
    - Entity: The arrow entity
    - Amount: 1

### CRAFT_ITEM
- **Description:** Triggered when a player crafts an item.
- **Associated Data:**
    - Player: The player crafting the item
    - Location: The location of the block
    - Entity: The player crafting the item
    - Item: The crafted item
    - Block: The block used to craft the item
    - Amount: The amount crafted

### ENCHANT_ITEM
- **Description:** Triggered when a player enchants an item.
- **Associated Data:**
    - Player: The player enchanting the item
    - Location: The location of the enchanting table
    - Entity: The player enchanting the item
    - Item: The enchanted item
    - Block: The enchanting table
    - Amount: 1

### CONSUME_ITEM
- **Description:** Triggered when a player consumes an item.
- **Associated Data:**
    - Player: The player consuming the item
    - Location: The location of the player
    - Entity: The player
    - Item: The consumed item
    - Amount: The quantity of the item consumed

### HARVEST_ITEM
- **Description:** Triggered when a player harvests an item from a block.
- **Associated Data:**
    - Player: The player harvesting
    - Location: The location of the block
    - Entity: The player
    - Item: The harvested item
    - Block: The block harvested from
    - Amount: The amount harvested

### SMELT_ITEM
- **Description:** Triggered when a player smelts an item.
- **Associated Data:**
    - Player: The player smelting the item
    - Location: The location of the block
    - Item: The smelted item
    - Block: The block used to smelt
    - Amount: The amount smelted

### BREAK_ITEM
- **Description:** Triggered when a player breaks an item.
- **Associated Data:**
    - Player: The player breaking the item
    - Location: The location of the player
    - Entity: The player breaking the item
    - Item: The broken item
    - Amount: The stack size of the broken item

### DAMAGE_ITEM
- **Description:** Triggered when a player's item takes damage.
- **Associated Data:**
    - Player: The player whose item is damaged
    - Location: The location of the player
    - Entity: The player whose item is damaged
    - Item: The damaged item
    - Amount: The damage amount

### MEND_ITEM
- **Description:** Triggered when a player's item is mended.
- **Associated Data:**
    - Player: The player whose item is mended
    - Location: The location of the player
    - Entity: The player whose item is mended
    - Item: The mended item
    - Amount: The amount mended

### DROP_ITEM
- **Description:** Triggered when a player drops an item.
- **Associated Data:**
    - Player: The player dropping the item
    - Location: The location of the item entity
    - Entity: The dropped item entity
    - Item: The dropped item
    - Amount: The amount dropped

### BREAK_BLOCK_DROP_ITEM
- **Description:** Triggered when a player breaks a block and it drops an item.
- **Associated Data:**
    - Player: The player breaking the block
    - Location: The location of the block
    - Entity: The player breaking the block
    - Item: The dropped item
    - Block: The broken block
    - Amount: The amount dropped

### KILL_ENTITY_DROP_ITEM
- **Description:** Triggered when a player kills an entity and it drops an item.
- **Associated Data:**
    - Player: The player killing the entity
    - Location: The location of the killed entity
    - Entity: The killed entity
    - Item: The dropped item
    - Amount: The amount dropped

### BREAK_BLOCK
- **Description:** Triggered when a player breaks a block.
- **Associated Data:**
    - Player: The player breaking the block
    - Location: The location of the block
    - Entity: The player breaking the block
    - Item: The item held by the player
    - Block: The broken block
    - Amount: 1

### PLACE_BLOCK
- **Description:** Triggered when a player places a block.
- **Associated Data:**
    - Player: The player placing the block
    - Location: The location of the block
    - Entity: The player placing the block
    - Item: The item in the players hand
    - Block: The placed block
    - Amount: 1

### HARVEST_BLOCK
- **Description:** Triggered when a player harvests a block (e.g. berries).
- **Associated Data:**
    - Player: The player harvesting the block
    - Location: The location of the block
    - Entity: The player harvesting the block
    - Item: The item in the players hand
    - Block: The harvested block
    - Amount: 1

### TAKE_LECTERN_BOOK
- **Description:** Triggered when a player takes a book from a lectern.
- **Associated Data:**
    - Player: The player taking the book
    - Location: The location of the lectern
    - Entity: The player taking the book
    - Item: The book
    - Block: The lectern
    - Amount: The amount of the book

### FILL_BUCKET
- **Description:** Triggered when a player fills a bucket.
- **Associated Data:**
    - Player: The player filling the bucket
    - Location: The location of the source block
    - Entity: The player filling the bucket
    - Item: The filled bucket
    - Block: The source block
    - Amount: 1

### EMPTY_BUCKET
- **Description:** Triggered when a player empties a bucket.
- **Associated Data:**
    - Player: The player emptying the bucket
    - Location: The location of the target block
    - Entity: The player emptying the bucket
    - Item: The bucket
    - Block: The target block
    - Amount: 1

### ENTER_BED
- **Description:** Triggered when a player enters a bed.
- **Associated Data:**
    - Player: The player entering the bed
    - Location: The location of the bed
    - Entity: The player entering the bed
    - Block: The bed
    - Amount: 1

### LEAVE_BED
- **Description:** Triggered when a player leaves a bed.
- **Associated Data:**
    - Player: The player leaving the bed
    - Location: The location of the bed
    - Entity: The player leaving the bed
    - Block: The bed
    - Amount: 1

### RIPTIDE
- **Description:** Triggered when a player laucnhes themself with riptide.
- **Associated Data:**
    - Player: The player using the trident
    - Location: The location of the player
    - Entity: The player using the trident
    - Item: The trident
    - Amount: 1

### TELEPORT_TO
- **Description:** Triggered when a player teleports to a location.
- **Associated Data:**
    - Player: The player teleporting
    - Location: The destination
    - Entity: The player teleporting
    - Item: The item in the players hand
    - Amount: 1

### TELEPORT_FROM
- **Description:** Triggered when a player teleports from a location.
- **Associated Data:**
    - Player: The player teleporting
    - Location: The origin
    - Entity: The player teleporting
    - Item: The item in the players hand
    - Amount: 1

### PORTAL_TO
- **Description:** Triggered when a player enters a portal to a location.
- **Associated Data:**
    - Player: The player entering the portal
    - Location: The destination
    - Entity: The player entering the portal
    - Item: The item in the players hand
    - Amount: 1

### PORTAL_FROM
- **Description:** Triggered when a player leaves a portal from a location.
- **Associated Data:**
    - Player: The player leaving the portal
    - Location: The origin
    - Entity: The player entering the portal
    - Item: The item in the players hand
    - Amount: 1

### DEATH
- **Description:** Triggered when a player dies.
- **Associated Data:**
    - Player: The player who died
    - Location: The location of the player who died
    - Entity: The player who died
    - Item: The item in hand
    - Amount: 1

### RESPAWN
- **Description:** Triggered when a player respawns.
- **Associated Data:**
    - Player: The player respawning
    - Location: The respawn location
    - Entity: The player respawning
    - Item: The item in the players hand
    - Amount: 1

### TIMER
- **Description:** Executes once per second.
- **Associated Data:**
    - Player: The player with the task
    - Location: The location of the player
    - Entity: The player with the task
    - Item: The item in the players hand
    - Amount: 1
