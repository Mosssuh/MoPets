package mv.mossuh.mopets.COMMANDS;

import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.DATA.Messages;
import mv.mossuh.mopets.DATA.Pets.ConfigPets;
import mv.mossuh.mopets.UTILITIES.Nbt.NBTPet;
import mv.mossuh.mopets.UTILITIES.Pet.PetCreator;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GivePetCommands {

    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("give")) {
                // /mopets give <code> <amount> <player> <args...>
                if (UtilString.get("mopets.admin").hasPermission(sender)) {
                    if (strings.length >= 4) {
                        String itemCode = strings[1];
                        String itemAmount = strings[2];
                        String itemPlayer = strings[3];
                        Player player = Bukkit.getPlayer(itemPlayer);
                        if (!ConfigPets.exist(itemCode)) {
                            UtilString.get(Messages.INVALID_CODE).hex().sendMessage(sender);
                            return;
                        }
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

                        CommandArgs args = new CommandArgs(strings, 4);
                        String argsAsString = args.getArgsAsString();
                        ItemStack itemStack = PetCreator.fromCode(itemCode, args, player.getUniqueId(), 1);
                        NBTPet.setArgs(itemStack, argsAsString);

                        for (int i = 0; i < amount; i++) {
                            ItemStack clone = itemStack.clone();
                            NBTPet.setUUID(clone);
                            player.getInventory().addItem(itemStack);
                        }

                        UtilString.get(Messages.COMMAND_GIVE_PET_SENDER).hex().replaceString("%amount%", itemAmount).replaceString("%code%", itemCode)
                                .replaceString("%player%", itemPlayer).setPlaceholders(sender).sendMessage(sender);
                        UtilString.get(Messages.COMMAND_GIVE_PET_RECEIVER).hex().replaceString("%amount%", itemAmount).replaceString("%code%", itemCode)
                                .setPlaceholders(player).sendMessage(player);

                        PetsAPI.getManager().getPlayer(uuid).updatePets();
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /mopets give <code> <amount> <player> <args...>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("giveall")) {
                // /mopets giveall <code> <amount> <args...>
                if (UtilString.get("mopets.admin").hasPermission(sender)) {
                    if (strings.length >= 3) {
                        String itemCode = strings[1];
                        String itemAmount = strings[2];
                        if (!ConfigPets.exist(itemCode)) {
                            UtilString.get(Messages.INVALID_CODE).hex().sendMessage(sender);
                            return;
                        }
                        if (!UtilString.get(itemAmount).isNumeric()) {
                            UtilString.get(Messages.INVALID_AMOUNT).hex().sendMessage(sender);
                            return;
                        }

                        int amount = Integer.parseInt(itemAmount);
                        CommandArgs args = new CommandArgs(strings, 3);
                        String argsAsString = args.getArgsAsString();

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            UUID uuid = player.getUniqueId();

                            ItemStack itemStack = PetCreator.fromCode(itemCode, args, player.getUniqueId(), 1);
                            NBTPet.setArgs(itemStack, argsAsString);

                            for (int i = 0; i < amount; i++) {
                                ItemStack clone = itemStack.clone();
                                NBTPet.setUUID(clone);
                                player.getInventory().addItem(itemStack);
                            }

                            UtilString.get(Messages.COMMAND_GIVE_PET_RECEIVER).hex().replaceString("%amount%", itemAmount).replaceString("%code%", itemCode)
                                    .setPlaceholders(player).sendMessage(player);

                            PetsAPI.getManager().getPlayer(uuid).updatePets();
                        }
                        UtilString.get(Messages.COMMAND_GIVE_ALL_PET_SENDER).hex().replaceString("%amount%", itemAmount).replaceString("%code%", itemCode)
                                .setPlaceholders(sender).sendMessage(sender);
                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cUse: /mopets giveall <code> <amount> <args...>").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
