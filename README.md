# MoPets

> A fully configurable pets plugin for Spigot/Paper servers.

MoPets allows server owners to create completely customizable pets with their own levels, experience system, actions, rewards, variables, tags, upgrades and boosters.
Every pet can react to Minecraft events, execute commands, apply potion effects, level up, unlock new abilities and much more.

---

# Features

- Unlimited custom pets
- Level & Experience system
- Upgrade system
- Configurable actions and rewards
- PlaceholderAPI support
- Public API and Events
- Fully configurable

---

# Installation

## Requirements

| Plugin | Required |
|---------|----------|
| MoCore | ✅ |
| NBT-API | ✅ |
| PlaceholderAPI | Optional |
| MoBoosters | Optional |

---

# Commands

| Command | Description |
|---------|-------------|
| `/mopets give <code> <amount> <player> <args...>` | Gives one or more pets to a player. |
| `/mopets giveall <code> <amount> <args...>` | Gives a pet to every online player. |
| `/mopets addexp <amount> <player>` | Adds experience to the active pet. |
| `/mopets setexp <amount> <player>` | Sets the active pet's experience. |
| `/mopets addlevel <amount> <player>` | Adds levels to the active pet. |
| `/mopets setlevel <amount> <player>` | Sets the pet level. |
| `/mopets activepets` | Displays all active pets. |
| `/mopets check` | Shows plugin diagnostic information. |

---

# API

MoPets includes a public API that can be used by other plugins.

## Main Class -> `PetsAPI`

---

# Events

| Event | Description |
|--------|-------------|
| `PetChangeExpEvent` | Called whenever a pet gains or loses experience. |
| `PetChangeLevelEvent` | Called whenever a pet changes level. |
| `PlayerChangePetEvent` | Called whenever a player changes their active pet. |

---

# PlaceholderAPI

## Active Pet

| Placeholder | Description |
|-------------|-------------|
| `%mopets_active_pets%` | Returns the player's active pets. |
| `%mopets_active_level_<pet>%` | Returns the pet level. |
| `%mopets_active_exp_<pet>%` | Returns the pet experience. |
| `%mopets_active_cost_<pet>%` | Returns the upgrade cost. |
| `%mopets_active_max_level_<pet>%` | Returns the maximum level. |
| `%mopets_active_tags_<pet>%` | Returns the pet tags. |
| `%mopets_active_has_tag_<tag>_<pet>%` | Returns whether the pet has the specified tag. |
| `%mopets_active_boost_<type>_<applicator>_<boosted>_<pet>%` | Returns booster information. |
| `%mopets_active_has_boost_<type>_<applicator>_<boosted>_<pet>%` | Checks if the booster exists. |

### Variables

| Placeholder |
|-------------|
| `%mopets_active_variables_<pet>%` |
| `%mopets_active_variable_{variable}_<pet>%` |
| `%mopets_active_has_variable_{variable}_<pet>%` |

> **Single Pet Mode**
>
> If `multiple-pets` is `false`, the pet code can be omitted from the placeholders.

---

# Available Variables

## Player
```text
%player%
```
---
## Cooldown
```text
%cooldown%
%cooldown_formatted%
```
---
## Pet
```text
%level%
%exp%
%cost%
%max_level%
%tags%
%code%
%variable_{variable}%
```
---
## Entity
```text
%entity_type%
%entity_name%
%entity_x%
%entity_y%
%entity_z%
%entity_world%
```
---
## Block
```text
%block%
%block_data%
%block_x%
%block_y%
%block_z%
%block_world%
```
---
## ItemStack
```text
%itemstack_material%
%itemstack_data%
%itemstack_name%
%itemstack_lore%
%itemstack_amount%
%itemstack_durability%
```
### MoPets Item Variables
```text
%itemstack_is_mopet%
%itemstack_mopet_level%
%itemstack_mopet_exp%
%itemstack_mopet_cost%
%itemstack_mopet_max_level%
%itemstack_mopet_tags%
%itemstack_mopet_code%
%itemstack_mopet_variable_{variable}%
```
---

# Experience System
Pets can gain experience automatically through configurable Minecraft events.

## Supported Experience Types

| Type | Description |
|------|-------------|
| `BLOCK_BREAK` | Gain experience by breaking blocks. |
| `BLOCK_PLACE` | Gain experience by placing blocks. |
| `PLAYER_KILLS` | Gain experience by killing entities. |
| `PLAYER_FISH` | Gain experience while fishing. |

Example

```yaml
exp:
  PLAYER_KILLS:
    - 'ZOMBIE -> 2'
  BLOCK_BREAK:
    - 'STONE -> 1'
    - 'DIAMOND_ORE -> 10'
```
---

# Actions
Actions define what a pet does whenever a specific Minecraft event occurs. Every action may contain:

- Event
- Cooldown
- Cooldown Message
- Requirements
- Rewards
- Else Rewards
- Boosters

Example
```yaml
default:

  SpeedBoost:

    event: PLAYER_KILLS

    cooldown: 5

    requirements:
      - '[EVENT] ZOMBIE'
      - '[EVAL] %level% >= 5'

    rewards:
      - '[CHANCE -> 50] EFFECT -> SPEED:5:1'
```
---

# Event Types
Each event provides different variables depending on the context.

| Event Type | Available Variables |
|------------|---------------------|
| `BLOCK_BREAK` | Block Variables |
| `BLOCK_PLACE` | Block Variables |
| `BLOCK_INTERACT` | Block Variables |
| `PLAYER_ATTACK` | Entity Variables |
| `PLAYER_ATTACKED` | Entity Variables |
| `PLAYER_KILLS` | Entity Variables |
| `PLAYER_DIE` | Entity Variables |
| `PLAYER_CAUGHT_FISH` | Entity Variables |
| `PLAYER_CAUGHT_ENTITY` | Entity Variables |
| `PLAYER_BED_ENTER` | None |
| `PLAYER_BED_LEAVE` | None |
| `PLAYER_CHANGE_WORLD` | `%from_world%`, `%to_world%` |
| `PLAYER_COMMAND` | `%command%` |
| `PLAYER_JOIN` | None |
| `PLAYER_LEAVE` | None |
| `PLAYER_RESPAWN` | None |
| `PLAYER_FLY` | None |
| `PLAYER_UNFLY` | None |
| `PLAYER_SNEAK` | None |
| `PLAYER_UNSNEAK` | None |
| `PLAYER_SPRINT` | None |
| `PLAYER_UNSPRINT` | None |
| `PLAYER_EQUIP_PIECE` | `%piece_type%`, ItemStack Variables |
| `PLAYER_UNEQUIP_PIECE` | `%piece_type%`, ItemStack Variables |
| `PLAYER_LEVELUP` | `%old_level%`, `%new_level%` |
| `PLAYER_CHAT` | `%message%` |
| `ITEM_SELECT` | ItemStack Variables |
| `ITEM_UNSELECT` | ItemStack Variables |
| `ITEM_ENCHANT` | `%exp_cost%`, ItemStack Variables |
| `ITEM_CRAFT` | ItemStack Variables |
| `ITEM_INTERACT` | `%click_type%`, ItemStack Variables |
| `ITEM_CONSUME` | ItemStack Variables |
| `ITEM_BREAK` | ItemStack Variables |
| `ITEM_PICKUP` | ItemStack Variables |
| `ITEM_DROP` | ItemStack Variables |
| `ITEM_HELD` | ItemStack Variables |
| `ITEM_UNHELD` | ItemStack Variables |
| `ENTITY_INTERACT` | Entity Variables |

# Requirements
Requirements determine whether an action should be executed.
If every requirement succeeds, the configured rewards will run. Otherwise, the **Else** section will be executed (if present).

## Requirement Types
| Type | Description |
|------|-------------|
| `EVENT` | Checks information related to the current event. |
| `EVAL` | Evaluates expressions using placeholders and variables. |

### EVAL supports
- PlaceholderAPI
- Event Variables
- Local Variables
- Item Variables
- Cooldown Variables

### Supported Operators

#### Strings
- `equals`
- `!equals`
- `equalsIgnoreCase`
- `!equalsIgnoreCase`
- `startsWith`
- `!startsWith`
- `contains`
- `!contains`

#### Numbers
- `<`
- `<=`
- `>`
- `>=`
- `==`
- `!=`

Example

```yaml
requirements:
  - '[EVENT] ZOMBIE || CREEPER'
  - '[EVAL] %level% >= 10'
```
Multiple requirements can be separated using `||`.

---

# Reward Types
Rewards are executed after every requirement has been successfully completed. Each reward can also be executed using:

- `CHANCE`
- `CHANCE_PER_LEVEL`

---

## Commands
| Reward | Format |
|---------|--------|
| `CONSOLE_COMMAND` | `CONSOLE_COMMAND -> <command>` |
| `PLAYER_COMMAND` | `PLAYER_COMMAND -> <command>` |
| `PLAYER_COMMAND_AS_OP` | `PLAYER_COMMAND_AS_OP -> <command>` |

---

## Messages
| Reward | Format |
|---------|--------|
| `TITLE` | `TITLE -> <title>::<subtitle>` |
| `SOUND` | `SOUND -> <sound>::<volume>::<pitch>` |
| `BROADCAST_MESSAGE` | `BROADCAST_MESSAGE -> <message>` |
| `BROADCAST_TITLE` | `BROADCAST_TITLE -> <title>::<subtitle>` |
| `JSON` | `JSON -> <json>` |
| `JSON_BROADCAST` | `JSON_BROADCAST -> <json>` |

---

## Effects
| Reward | Format |
|---------|--------|
| `EFFECT` | `EFFECT -> <effect>::<duration>::<amplifier>` |
```text
Example: EFFECT -> SPEED:10:1
```

---

## Items
| Reward | Format |
|---------|--------|
| `WORLD_DROP` | `WORLD_DROP -> <pet>[<amount>]` |
| `GIVE_ITEM` | `GIVE_ITEM -> <pet>[<amount>]` |
> For this section, you can create items that aren't pets; they don't need to have rewards, a leveling system, etc. The item just needs to have a material in order to be registered by MoPets.

---

## Events
| Reward | Format |
|---------|--------|
| `CANCEL_EVENT` | `CANCEL_EVENT` |
| `CANCEL_DROPS` | `CANCEL_DROPS` |

---

## Actions
| Reward | Format |
|---------|--------|
| `EXECUTE_ACTION` | `EXECUTE_ACTION -> <action>` |
> This action is used to execute another action that you have registered in the same PET.

---

## Experience
| Reward | Format |
|---------|--------|
| `ADD_EXP` | `ADD_EXP -> <exp>` |
| `SET_EXP` | `SET_EXP -> <exp>` |
| `REMOVE_EXP` | `REMOVE_EXP -> <exp>` |

---

## Levels
| Reward | Format |
|---------|--------|
| `ADD_LEVEL` | `ADD_LEVEL -> <level>` |
| `SET_LEVEL` | `SET_LEVEL -> <level>` |
| `REMOVE_LEVEL` | `REMOVE_LEVEL -> <level>` |

---

## Variables
| Reward | Format |
|---------|--------|
| `SET_VARIABLE` | `SET_VARIABLE -> <variable>::<value>` |
| `REMOVE_VARIABLE` | `REMOVE_VARIABLE -> <variable>` |

---

# Reward Examples
```text
[CHANCE -> 100] CONSOLE_COMMAND -> say Hello World

[CHANCE -> 50] EFFECT -> SPEED:10:1

[CHANCE -> 100] GIVE_ITEM -> MagicApple[1]

[CHANCE -> 100] EXECUTE_ACTION -> SpeedBoost

[CHANCE_PER_LEVEL -> 2] ADD_EXP -> 50

[CHANCE -> 100] TITLE -> &aPet Level Up!::&7Congratulations!
```

Rewards can be chained together.

```text
Example: 
[CHANCE -> 100] EFFECT -> SPEED:5:1 && MESSAGE -> Speed activated!
[CHANCE -> 50] EFFECT -> SPEED:5:1 && MESSAGE -> Lucky! || MESSAGE -> Better luck next time!
```

---

# Boosters
MoPets supports **MoBoosters** integration. Every action or pet can provide boosters.

## Multiplier Types
| Type | Description |
|------|-------------|
| `BASE` | Always provides the configured boost. |
| `BOOST_PER_LEVEL` | The boost is multiplied by the pet level. |

Example
```yaml
boosters:
  ExampleBoostName:
    multiplier: BOOST_PER_LEVEL
    type: PERSONAL
    applicator: MINECRAFT
    boosted: EXPERIENCE
    boost: 3
```

---

# Pet Format

```
A custom code to identify the pet
Example: MagicApple
```
---

# Tags
Tags are used to categorize pets.
```yaml
tags:
  - Magic
  - Apple
```
> Tags can later be checked using PlaceholderAPI or action requirements.

---

# Booster Identifier
When using **MoBoosters**, a pet can define a booster identifier.
```yaml
booster-identifier: ExampleBoost
```
> This ensures only one booster with the same identifier is applied.

---

# Variables
Variables allow storing custom information inside each pet.
```yaml
variables:
  - damage -> 15
  - rarity -> legendary
```
> Variables are accessible through placeholders.

---

# Upgrade System
Each pet may define its own progression.
```yaml
upgrades:
  max-level: 10
  cost-per-level: 100
  message:
    progress:
      - '&7Progress: %progress%'
    maxed-progress:
      - '&aMAX LEVEL'
```

## Options
| Option | Description |
|---------|-------------|
| `max-level` | Maximum level the pet can reach. |
| `cost-per-level` | Upgrade cost formula. |
| `progress` | Displayed while leveling. |
| `maxed-progress` | Displayed once the maximum level is reached. |

---

# Item Information
Every pet is represented by an item.

```yaml
item-info:
  material: GOLDEN_APPLE
  data: 0
  name: '&6Magic Apple'
  lore:
    - '&7A magical pet.'
  unbreakable: true
```

## Available Options
| Option | Description |
|---------|-------------|
| `material` | Minecraft material or custom head. |
| `data` | Legacy durability/data value. |
| `name` | Item display name. |
| `lore` | Item description. |
| `unbreakable` | Makes the item unbreakable. |
| `enchantments` | Item enchantments. |
| `flags` | Item flags. |

```yaml
Example:
  enchantments:
    - LUCK:2
  flags:
    - HIDE_ENCHANTS
  ```

---

# Pet Actions
Every pet may define one or more actions.

```yaml
actions:
  default:
    SpeedBoost:
      event: PLAYER_KILLS
      cooldown: 5
      cooldown_bypass: false
      cooldown_message: "&cWait %cooldown_formatted%!"
      requirements:
        - '[EVENT] ZOMBIE'
      rewards:
        - '[CHANCE -> 50] EFFECT -> SPEED:5:1'
      else:
        - '[CHANCE -> 100] MESSAGE -> &cRequirements not met.'
```

## Available Default Options
| Option | Description |
|---------|-------------|
| `event` | Minecraft event that triggers the action. |
| `cooldown` | Cooldown in seconds. |
| `cooldown_bypass` | Executes rewards even if the cooldown is active. |
| `cooldown_message` | Message shown while on cooldown. |
| `requirements` | Conditions required before executing rewards. |
| `rewards` | Rewards executed when requirements succeed. |
| `else` | Rewards executed when requirements fail. |

---

# Example Pet

```yaml
GoldenApple:
  item-info:
    material: GOLDEN_APPLE
Mutton:
  item-info:
    material: MUTTON
KranPet:
  tags: Kran, Pet, Combat
  variables:
    - 'killed_sheeps -> 0'
  upgrades:
    max-level: 200
    cost-per-level: 100
    message:
      progress:
        - 'Exp:& %exp%/%cost%'
      maxed-progress:
        - '&b&lMAXED PET'
  exp:
    PLAYER_KILLS:
      - 'ALL -> 5'
  item-info:
    material: basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M1OWVhZDAxZDk3OTIxY2ZmODk3MjlhMjg0YTljNGY1MTcyZDZiZjZmNmRkYjUzMTg0NzMyODlmMGU3YmZkYyJ9fX0=
    data: 0
    name: '&cKran &8| &fPet &7[Lvl. %level%]'
    lore:
      - '&8Combat Pet'
      - ''
      - '&6Golden'
      - '&7When you kill a player, there is a 50%'
      - '&7to drop a golden apple.'
      - '&8Level 100 to secure an golden apple.'
      - ''
      - '&bVenom'
      - '&7Chance of 2.5% to apply poison effect'
      - '&7to your opponent when you''re attacking him.'
      - '&cRequires 1000 killed sheeps to use this ability.'
      - ''
      - '&dRegenerator'
      - '&7When you attack a Player you have &c%math_%level%*0.025%% &8to'
      - '&7to receive regeneration.'
      - ''
      - '&8Killing sheep give you double xp and extra mutton.'
      - '&cKilled Sheeps: %variable_{killed_sheeps}%'
      - ''
      - '%progress%'
  actions:
    default:
      KillerSheeps:
        event: PLAYER_KILLS
        requirements:
          - '[event] SHEEP'
        rewards:
          - '[CHANCE -> 100] ADD_EXP -> 5'
          - '[CHANCE -> 100] SET_VARIABLE -> killed_sheeps::%math_%variable_{killed_sheeps}%+1%'
          - '[CHANCE -> 100] CANCEL_DROPS'
          - '[CHANCE -> 100] WORLD_DROP:TARGET -> Mutton[%randomnumber_value_1-3%]'
      Golden:
        event: PLAYER_KILLS
        requirements:
          - '[event] PLAYER'
          - '[eval] %level% >= 100'
        rewards:
          - '[CHANCE -> 100] WORLD_DROP:TARGET -> GoldenApple[1]'
        else:
          - '[CHANCE -> 50] WORLD_DROP:TARGET -> GoldenApple[1]'
      Venom:
        event: PLAYER_ATTACK
        requirements:
          - '[event] PLAYER'
          - '[eval] %variable_{killed_sheeps}% >= 1000'
        rewards:
          - '[chance -> 2.5] EFFECT -> POISON::3::1'
      RegeneratorOne:
        event: PLAYER_ATTACK
        requirements:
          - '[event] PLAYER'
          - '[eval] %level% < 200'
        rewards:
          - '[CHANCE_PER_LEVEL -> 0.025] EFFECT -> REGENERATION::5::1'
        else:
          - '[CHANCE -> 100] EXECUTE_ACTION -> RegeneratorTwo'
      RegeneratorTwo:
        rewards:
          - '[CHANCE_PER_LEVEL -> 0.025] EFFECT -> REGENERATION::5::1'
```
