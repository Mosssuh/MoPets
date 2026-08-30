package mv.mossuh.mopets.COMMANDS;

import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mopets.UTILITIES.Enums.ExecuteType;
import mv.mossuh.mopets.UTILITIES.Enums.ReceiveType;
import mv.mossuh.mopets.API.Events.PetChangeLevelEvent;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.DATA.Messages;
import mv.mossuh.mopets.MODEL.Pets.Upgrades;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import mv.mossuh.mopets.UTILITIES.Pet.PetUpdater;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LevelCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("addlevel")) {
                // /mopets addlevel <amount> <player>
                if (UtilString.get("mopets.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String itemAmount = strings[1];
                        String itemPlayer = strings[2];
                        Player player = Bukkit.getPlayer(itemPlayer);
                        if (!UtilString.get(itemAmount).isNumeric()) {
                            UtilString.get(Messages.INVALID_AMOUNT).hex().sendMessage(sender);
                            return;
                        }
                        if (!UsefulMethods.isOnline(player)) {
                            UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }

                        UUID uuid = player.getUniqueId();
                        int amount = Integer.parseInt(itemAmount);

                        ItemStack itemStack = UtilMethods.getItemInHand(player);
                        Pet pet = Pet.getPet(itemStack);
                        if (pet.isPet()) {
                            PetChangeLevelEvent event = new PetChangeLevelEvent(uuid, pet, ExecuteType.COMMAND, ReceiveType.ADD, amount);
                            Bukkit.getPluginManager().callEvent(event);

                            if (event.isCancelled()) { return; }

                            Upgrades upgrades = pet.getConfigPet().getUpgrades();
                            int maxLevel = upgrades.getMaxLevel();
                            double cost = upgrades.getCostPerLevel();

                            int level = Math.min(event.getLevel(), maxLevel);
                            if (level < 0) { level = 0; }


                            String name = UtilString.get(pet.getConfigPet().getItemInfo().getName()).hex().apply();
                            UtilString.get(Messages.COMMAND_PET_ADD_LEVEL_RECEIVER).replaceString("%pet_name%", name).replaceString("%new_level%", level+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).hex().sendMessage(player);
                            UtilString.get(Messages.COMMAND_PET_ADD_LEVEL_SENDER).replaceString("%pet_name%", name).replaceString("%new_level%", level+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).hex().sendMessage(sender);

                            pet.addLevel(level);
                            pet.setCost(cost*level);
                            PetUpdater.verifyPet(player, pet, true);
                            PetUpdater.updateItemInfo(player, pet, true);
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /mopets addlevel <amount> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("setlevel")) {
                // /mopets setlevel <amount> <player>
                if (UtilString.get("mopets.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String itemAmount = strings[1];
                        String itemPlayer = strings[2];
                        Player player = Bukkit.getPlayer(itemPlayer);
                        if (!UtilString.get(itemAmount).isNumeric()) {
                            UtilString.get(Messages.INVALID_AMOUNT).hex().sendMessage(sender);
                            return;
                        }
                        if (!UsefulMethods.isOnline(player)) {
                            UtilString.get(Messages.INVALID_PLAYER).hex().sendMessage(sender);
                            return;
                        }

                        UUID uuid = player.getUniqueId();
                        int amount = Integer.parseInt(itemAmount);

                        ItemStack itemStack = UtilMethods.getItemInHand(player);
                        Pet pet = Pet.getPet(itemStack);
                        if (pet.isPet()) {
                            PetChangeLevelEvent event = new PetChangeLevelEvent(uuid, pet, ExecuteType.COMMAND, ReceiveType.SET, amount);
                            Bukkit.getPluginManager().callEvent(event);

                            if (event.isCancelled()) { return; }

                            Upgrades upgrades = pet.getConfigPet().getUpgrades();
                            int maxLevel = upgrades.getMaxLevel();
                            double cost = upgrades.getCostPerLevel();

                            int level = Math.min(event.getLevel(), maxLevel);
                            if (level < 1) { level = 1; }


                            String name = UtilString.get(pet.getConfigPet().getItemInfo().getName()).hex().apply();
                            UtilString.get(Messages.COMMAND_PET_SET_LEVEL_RECEIVER).replaceString("%pet_name%", name).replaceString("%new_level%", level+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).hex().sendMessage(player);
                            UtilString.get(Messages.COMMAND_PET_SET_LEVEL_SENDER).replaceString("%pet_name%", name).replaceString("%new_level%", level+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).sendMessage(sender);

                            pet.setLevel(level);
                            pet.setCost(cost*level);
                            PetUpdater.verifyPet(player, pet, true);
                            PetUpdater.updateItemInfo(player, pet, true);
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /mopets setlevel <amount> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
