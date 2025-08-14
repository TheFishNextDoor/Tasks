# Tasks Bukkit Plugin

## Trigger Types

### INTERACT_ENTITY
- **Description:** Triggered when a player interacts with an entity.
- **Associated Data:**
    - Player: The player who interacted with the entity
    - Entity: The entity interacted with
    - Location: The location of the entity
    - Item: The item in the players hand

### DAMAGE_ENTITY
- **Description:** Triggered when an entity is damaged.
- **Associated Data:**
    - Player: The player who caused the damage
    - Entity: The entity damaged
    - Location: The location of the entity
    - Item: The item in the players hand
    - Amount: The damage amount

### KILL_ENTITY
- **Description:** Triggered when an entity is killed.
- **Associated Data:**
    - Player: The player who killed the entity (if applicable)
    - Entity: The entity killed
    - Location: The location of the kill
    - Item: The weapon used

### BUCKET_ENTITY
- **Description:** Triggered when a player uses a bucket on an entity.
- **Associated Data:**
    - Player: The player using the bucket
    - Entity: The entity targeted
    - Location: The location of the interaction
    - Item: The bucket used

### SHEAR_ENTITY
- **Description:** Triggered when a player shears an entity.
- **Associated Data:**
    - Player: The player using the shears
    - Entity: The entity sheared
    - Location: The location of the shearing
    - Item: The shears used

### TAME_ENTITY
- **Description:** Triggered when a player tames an entity.
- **Associated Data:**
    - Player: The player taming the entity
    - Entity: The entity tamed
    - Location: The location of the taming
    - Item: The item used to tame

### MANIPULATE_ARMORSTAND
- **Description:** Triggered when a player manipulates an armor stand.
- **Associated Data:**
    - Player: The player manipulating the armor stand
    - Entity: The armor stand
    - Location: The location of the manipulation
    - Item: The item used

### THROW_EGG
- **Description:** Triggered when a player throws an egg.
- **Associated Data:**
    - Player: The player throwing the egg
    - Entity: The egg entity
    - Location: The location of the throw
    - Item: The egg

### PICKUP_ARROW
- **Description:** Triggered when a player picks up an arrow.
- **Associated Data:**
    - Player: The player picking up the arrow
    - Entity: The arrow entity
    - Location: The location of the pickup
    - Item: The arrow
    - Amount: The number of arrows picked up

### CRAFT_ITEM
- **Description:** Triggered when a player crafts an item.
- **Associated Data:**
    - Player: The player crafting the item
    - Location: The location of crafting
    - Item: The crafted item
    - Amount: The amount crafted

### ENCHANT_ITEM
- **Description:** Triggered when a player enchants an item.
- **Associated Data:**
    - Player: The player enchanting the item
    - Location: The location of enchanting
    - Item: The enchanted item

### CONSUME_ITEM
- **Description:** Triggered when a player consumes an item.
- **Associated Data:**
    - Player: The player consuming the item
    - Location: The location of consumption
    - Item: The consumed item

### HARVEST_ITEM
- **Description:** Triggered when a player harvests an item from a block or entity.
- **Associated Data:**
    - Player: The player harvesting
    - Entity: The entity harvested from (if any)
    - Location: The location of harvesting
    - Item: The harvested item
    - Block: The block harvested from (if any)
    - Amount: The amount harvested

### SMELT_ITEM
- **Description:** Triggered when a player smelts an item.
- **Associated Data:**
    - Player: The player smelting the item
    - Location: The location of smelting
    - Item: The smelted item
    - Block: The furnace used
    - Amount: The amount smelted

### BREAK_ITEM
- **Description:** Triggered when a player breaks an item (item durability reaches zero).
- **Associated Data:**
    - Player: The player breaking the item
    - Location: The location of the break
    - Item: The broken item

### DAMAGE_ITEM
- **Description:** Triggered when a player's item takes damage.
- **Associated Data:**
    - Player: The player whose item is damaged
    - Location: The location of the damage
    - Item: The damaged item
    - Amount: The damage amount

### MEND_ITEM
- **Description:** Triggered when a player's item is mended.
- **Associated Data:**
    - Player: The player whose item is mended
    - Location: The location of mending
    - Item: The mended item
    - Amount: The amount mended

### DROP_ITEM
- **Description:** Triggered when a player drops an item.
- **Associated Data:**
    - Player: The player dropping the item
    - Location: The location of the drop
    - Item: The dropped item
    - Amount: The amount dropped

### BREAK_BLOCK_DROP_ITEM
- **Description:** Triggered when a player breaks a block and it drops an item.
- **Associated Data:**
    - Player: The player breaking the block
    - Location: The location of the block
    - Item: The dropped item
    - Block: The broken block
    - Amount: The amount dropped

### KILL_ENTITY_DROP_ITEM
- **Description:** Triggered when a player kills an entity and it drops an item.
- **Associated Data:**
    - Player: The player killing the entity
    - Entity: The killed entity
    - Location: The location of the kill
    - Item: The dropped item
    - Amount: The amount dropped

### BREAK_BLOCK
- **Description:** Triggered when a player breaks a block.
- **Associated Data:**
    - Player: The player breaking the block
    - Location: The location of the block
    - Item: The tool used
    - Block: The broken block

### PLACE_BLOCK
- **Description:** Triggered when a player places a block.
- **Associated Data:**
    - Player: The player placing the block
    - Location: The location of the block
    - Item: The block placed
    - Block: The placed block

### HARVEST_BLOCK
- **Description:** Triggered when a player harvests a block (e.g., crops).
- **Associated Data:**
    - Player: The player harvesting the block
    - Location: The location of the block
    - Item: The harvested item
    - Block: The harvested block
    - Amount: The amount harvested

### TAKE_LECTERN_BOOK
- **Description:** Triggered when a player takes a book from a lectern.
- **Associated Data:**
    - Player: The player taking the book
    - Location: The location of the lectern
    - Item: The book
    - Block: The lectern

### FILL_BUCKET
- **Description:** Triggered when a player fills a bucket.
- **Associated Data:**
    - Player: The player filling the bucket
    - Location: The location of the source
    - Item: The filled bucket
    - Block: The source block

### EMPTY_BUCKET
- **Description:** Triggered when a player empties a bucket.
- **Associated Data:**
    - Player: The player emptying the bucket
    - Location: The location of the target
    - Item: The bucket
    - Block: The target block

### ENTER_BED
- **Description:** Triggered when a player enters a bed.
- **Associated Data:**
    - Player: The player entering the bed
    - Location: The location of the bed
    - Block: The bed

### LEAVE_BED
- **Description:** Triggered when a player leaves a bed.
- **Associated Data:**
    - Player: The player leaving the bed
    - Location: The location of the bed
    - Block: The bed

### RIPTIDE
- **Description:** Triggered when a player uses a trident with Riptide.
- **Associated Data:**
    - Player: The player using the trident
    - Location: The location of use
    - Item: The trident

### TELEPORT_TO
- **Description:** Triggered when a player teleports to a location.
- **Associated Data:**
    - Player: The player teleporting
    - Location: The destination

### TELEPORT_FROM
- **Description:** Triggered when a player teleports from a location.
- **Associated Data:**
    - Player: The player teleporting
    - Location: The origin

### PORTAL_TO
- **Description:** Triggered when a player enters a portal to a location.
- **Associated Data:**
    - Player: The player entering the portal
    - Location: The destination
    - Block: The portal block

### PORTAL_FROM
- **Description:** Triggered when a player leaves a portal from a location.
- **Associated Data:**
    - Player: The player leaving the portal
    - Location: The origin
    - Block: The portal block

### DEATH
- **Description:** Triggered when a player dies.
- **Associated Data:**
    - Player: The player who died
    - Location: The location of death
    - Item: The item in hand

### RESPAWN
- **Description:** Triggered when a player respawns.
- **Associated Data:**
    - Player: The player respawning
    - Location: The respawn location

### TIMER
- **Description:** Triggered by a timer event.
- **Associated Data:**
    - Player: The player associated with the timer (if any)
    - Location: The location associated with the timer (if any)
    - Amount: The timer value (if any)
