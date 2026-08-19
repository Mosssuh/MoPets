package mv.mossuh.mopets.ACTIONS.RewardsUtils;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.ENUMS.ExecuteType;
import mv.mossuh.mopets.ENUMS.ReceiveType;
import mv.mossuh.mopets.API.Events.PetChangeExpEvent;
import mv.mossuh.mopets.API.Events.PetChangeLevelEvent;
import mv.mossuh.mopets.PETS.Creator.PetCreator;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetUpdater;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class RewardMethods {

    public static void consoleCommand(String command) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
    }

    public static void playerCommand(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.performCommand(command);
        }
    }

    public static void playerCommandAsOP(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isOp()) {
                player.performCommand(command);
            } else {
                player.setOp(true);
                player.performCommand(command);
                player.setOp(false);
            }
        }
    }

    public static void playerMessage(LivingEntity entity, String message) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.sendMessage(message);
        }
    }

    public static void playerTitle(LivingEntity entity, String titleSubtitle) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = titleSubtitle.split("::");
            String title = space[0];
            String subtitle = space[1];
            player.sendTitle(title, subtitle);
        }
    }

    public static void playerSound(LivingEntity entity, String soundString) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = soundString.split("::", 3);
            String sound = space[0];
            float volume = Float.parseFloat(space[1]);
            float pitch = Float.parseFloat(space[2].replace(" ", ""));
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    public static void broadcastTitle(String titleSubtitle) {
        String title = "";
        String subtitle = "";
        String[] space;
        space = titleSubtitle.split("::");
        title = space[0];
        subtitle = space[1];

        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            playerOnline.sendTitle(title, subtitle);
        }
    }

    public static void json(LivingEntity entity, String json) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.dispatchCommand(console, ("tellraw " + player.getName() + " " + json));
        }
    }

    public static void jsonBroadcast(String json) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            Bukkit.dispatchCommand(console, ("tellraw " + playerOnline.getName() + " " + json));
        }
    }

    public static void effect(LivingEntity entity, String effectString) {
        if (entity != null) {
            String[] effectSeparate = effectString.replace(" ", "").split("::", 3);
            String effect = effectSeparate[0];
            int duration = Integer.parseInt(effectSeparate[1]) * 20;
            int amplifier = Integer.parseInt(effectSeparate[2]);
            PotionEffect poison = new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(effect)), duration, amplifier);
            entity.addPotionEffect(poison, true);
        }
    }

    public static void worldDrop(LivingEntity entity, Location location, String reward) {
        if (location != null) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                UUID uuid = player.getUniqueId();
                World world = location.getWorld();
                ItemStack itemStack = PetCreator.fromReward(reward, uuid);
                world.dropItemNaturally(location, itemStack);
            } else {
                World world = location.getWorld();
                ItemStack itemStack = PetCreator.fromReward(reward, null);
                world.dropItemNaturally(location, itemStack);
            }
        }
    }

    public static void giveItem(LivingEntity entity, String reward) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();
            ItemStack itemStack = PetCreator.fromReward(reward, uuid);
            int amountToGive = itemStack.getAmount();
            ItemStack cloneStack = itemStack.clone();

            player.updateInventory();

            while (amountToGive > 0) {
                if (player.getInventory().firstEmpty() != -1) {
                    int stackSize = Math.min(amountToGive, cloneStack.getMaxStackSize());
                    cloneStack.setAmount(stackSize);

                    HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(cloneStack);
                    if (remaining.isEmpty()) {
                        amountToGive -= stackSize;
                    } else {
                        amountToGive -= (stackSize - remaining.get(0).getAmount());
                        break;
                    }
                } else {
                    break;
                }
            }

            if (amountToGive > 0) {
                cloneStack.setAmount(amountToGive);
                player.getWorld().dropItemNaturally(player.getLocation(), cloneStack);
            }
        }
    }

    public static void addExp(LivingEntity entity, Pet pet, double exp) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();
            PetChangeExpEvent petEvent = new PetChangeExpEvent(uuid, pet, ExecuteType.REWARDS, ReceiveType.ADD, exp);
            Bukkit.getPluginManager().callEvent(petEvent);

            if (petEvent.isCancelled()) {
                return;
            }
            double eventExp = petEvent.getExp();

            pet.addExp(eventExp);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void setExp(LivingEntity entity, Pet pet, double exp) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();
            PetChangeExpEvent petEvent = new PetChangeExpEvent(uuid, pet, ExecuteType.REWARDS, ReceiveType.SET, exp);
            Bukkit.getPluginManager().callEvent(petEvent);

            if (petEvent.isCancelled()) {
                return;
            }

            double eventExp = petEvent.getExp();

            pet.setExp(eventExp);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void removeExp(LivingEntity entity, Pet pet, double exp) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            double itemExp = pet.getExp();
            double reducedExp = itemExp - exp;
            double newExp = 0;
            if (reducedExp > 0) {
                newExp = reducedExp;
            }
            pet.setExp(newExp);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void addLevel(LivingEntity entity, Pet pet, int level) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();
            PetChangeLevelEvent petEvent = new PetChangeLevelEvent(uuid, pet, ExecuteType.REWARDS, ReceiveType.ADD, level);
            Bukkit.getPluginManager().callEvent(petEvent);
            int maxLevel = pet.getConfigPet().getUpgrades().getMaxLevel();

            if (petEvent.isCancelled()) {
                return;
            }
            int eventLevel = Math.min(petEvent.getLevel(), maxLevel);
            if (eventLevel < 0) {
                eventLevel = 0;
            }

            pet.addLevel(eventLevel);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void setLevel(LivingEntity entity, Pet pet, int level) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();
            PetChangeLevelEvent petEvent = new PetChangeLevelEvent(uuid, pet, ExecuteType.REWARDS, ReceiveType.SET, level);
            Bukkit.getPluginManager().callEvent(petEvent);
            int maxLevel = pet.getConfigPet().getUpgrades().getMaxLevel();

            if (petEvent.isCancelled()) {
                return;
            }
            int eventLevel = Math.min(petEvent.getLevel(), maxLevel);
            if (eventLevel < 1) {
                eventLevel = 1;
            }

            pet.setLevel(eventLevel);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void removeLevel(LivingEntity entity, Pet pet, int level) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            int itemLevel = pet.getLevel();
            int reducedLevel = itemLevel - level;
            int newLevel = Math.max(reducedLevel, 1);
            pet.setLevel(newLevel);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void setVariable(LivingEntity entity, Pet pet, VariableArg variable) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            pet.setVariable(variable);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void removeVariable(LivingEntity entity, Pet pet, String variable) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            pet.removeVariable(variable);
            PetUpdater.verifyPet(player, pet, false);
            PetUpdater.updateItemInfo(player, pet, false);
        }
    }

    public static void setDamage(Entity entity, double amount) {
        if (entity == null) return;
        if (entity instanceof Damageable) {
            Damageable target = (Damageable) entity;
            target.damage(amount);
        }
    }


}
