package mv.mossuh.mopets.COMMANDS;

import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mopets.UTILITIES.Enums.ExecuteType;
import mv.mossuh.mopets.UTILITIES.Enums.ReceiveType;
import mv.mossuh.mopets.API.Events.PetChangeExpEvent;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.DATA.Messages;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import mv.mossuh.mopets.UTILITIES.Pet.PetUpdater;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ExpCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("addexp")) {
                // /mopets addexp <exp> <player>
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
                        double amount = Double.parseDouble(itemAmount);

                        ItemStack itemStack = UtilMethods.getItemInHand(player);
                        Pet pet = Pet.getPet(itemStack);
                        if (pet.isPet()) {
                            PetChangeExpEvent event = new PetChangeExpEvent(uuid, pet, ExecuteType.COMMAND, ReceiveType.ADD, amount);
                            Bukkit.getPluginManager().callEvent(event);

                            if (event.isCancelled()) { return; }

                            double exp = event.getExp();

                            String name = UtilString.get(pet.getConfigPet().getItemInfo().getName()).hex().apply();
                            UtilString.get(Messages.COMMAND_PET_ADD_EXP_RECEIVER).hex().replaceString("%pet_name%", name).replaceString("%new_exp%", exp+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).sendMessage(player);
                            UtilString.get(Messages.COMMAND_PET_ADD_EXP_SENDER).hex().replaceString("%pet_name%", name).replaceString("%new_exp%", exp+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).sendMessage(sender);

                            pet.addExp(exp);
                            PetUpdater.verifyPet(player, pet, true);
                            PetUpdater.updateItemInfo(player, pet, true);
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /mopets addexp <exp> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("setexp")) {
                // /mopets setexp <exp> <player>
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
                        double amount = Double.parseDouble(itemAmount);

                        ItemStack itemStack = UtilMethods.getItemInHand(player);
                        Pet pet = Pet.getPet(itemStack);
                        if (pet.isPet()) {
                            PetChangeExpEvent event = new PetChangeExpEvent(uuid, pet, ExecuteType.COMMAND, ReceiveType.SET, amount);
                            Bukkit.getPluginManager().callEvent(event);

                            if (event.isCancelled()) { return; }

                            double exp = event.getExp();

                            String name = UtilString.get(pet.getConfigPet().getItemInfo().getName()).hex().apply();
                            UtilString.get(Messages.COMMAND_PET_SET_EXP_RECEIVER).hex().replaceString("%pet_name%", name).replaceString("%new_exp%", exp+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).sendMessage(player);
                            UtilString.get(Messages.COMMAND_PET_SET_EXP_SENDER).hex().replaceString("%pet_name%", name).replaceString("%new_exp%", exp+"")
                                    .setVariables(pet).setVariables(player).setPlaceholders(uuid).sendMessage(sender);

                            pet.setExp(exp);
                            PetUpdater.verifyPet(player, pet, true);
                            PetUpdater.updateItemInfo(player, pet, true);
                        }
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /mopets setexp <exp> <player>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
