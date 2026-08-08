package mv.mossuh.mopets.COMMANDS;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.VERSION.ServerVersion;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Messages;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.UUID;

public class ActivePetsCommands {

    public static void onCommand(CommandSender sender, String[] strings) {


        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("activepets")) {
                // /mopets activeitems
                if (UtilString.get("mopets.admin").hasPermission(sender)) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        UUID uuid = player.getUniqueId();

                        List<String> codes = PetsAPI.getManager().getPlayer(uuid).getCodes();
                        int amount = codes.size();

                        UtilString.get("&r").hex().sendMessage(player);
                        UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                        UtilString.get("&r").hex().sendMessage(player);
                        UtilString.get("&bPets: ").hex().sendMessage(player);
                        UtilString.get(String.join("&f, &7", codes)).hex().sendMessage(player);
                        UtilString.get("&r").hex().sendMessage(player);
                        UtilString.get("&bTotal: &7" + amount).hex().sendMessage(player);
                        UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                        UtilString.get("&r").hex().sendMessage(player);

                    } else {
                        UtilString.get("&8[" + Config.PREFIX + "&8] &cYou can't execute this command in console.").hex().sendMessage(sender);
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            } else if (strings[0].equalsIgnoreCase("check")) {
                if (UtilString.get("mopets.admin").hasPermission(sender)) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        ItemStack itemStack = player.getItemInHand();

                        Pet pet = Pet.getPet(itemStack);
                        if (!itemStack.getType().equals(Material.AIR)) {
                            ConfigPet configPet = pet.getConfigPet();
                            PetIdentifier petIdentifier = configPet.getPetIdentifier();

                            String petUUID = pet.getPetUUID().toString();
                            String code = petIdentifier.getCode();
                            int level = pet.getLevel();
                            double exp = pet.getExp();
                            double cost = pet.getCost();
                            String tags = petIdentifier.getTagsAsString();

                            UtilString.get("&r").hex().sendMessage(player);
                            UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                            UtilString.get("&r").hex().sendMessage(player);
                            if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                                UtilString.get("&bMaterial: &7" + itemStack.getType().name()).hex().sendMessage(player);
                            } else {
                                UtilString.get("&bMaterial: &7" + itemStack.getType().name() + ":" + itemStack.getData().getData()).hex().sendMessage(player);
                            }
                            if (pet.isPet()) {
                                UtilString.get("&bUUID: &7" + petUUID).hex().sendMessage(player);
                                UtilString.get("&bCode: &7" + code).hex().sendMessage(player);
                                UtilString.get("&bLevel: &7" + level).hex().sendMessage(player);
                                UtilString.get("&bExp: &7" + exp).hex().sendMessage(player);
                                UtilString.get("&bCost: &7" + cost).hex().sendMessage(player);
                                UtilString.get("&bTags: &7" + tags).hex().sendMessage(player);
                                if (pet.hasVariables()) {
                                    UtilString.get("&bVariables: " + VariableArg.toString(pet.getVariables())).hex().sendMessage(player);
                                }
                            } else {
                                UtilString.get(Messages.INVALID_PET).hex().setPlaceholders(sender).sendMessage(sender);
                            }
                            UtilString.get("&r").hex().sendMessage(player);
                            UtilString.get("&8--------------------------------------").hex().sendMessage(player);
                            UtilString.get("&r").hex().sendMessage(player);
                        }
                    }
                } else {
                    UtilString.get(Messages.NO_PERMISSION).hex().setPlaceholders(sender).sendMessage(sender);
                }
            }
        }
    }
}
