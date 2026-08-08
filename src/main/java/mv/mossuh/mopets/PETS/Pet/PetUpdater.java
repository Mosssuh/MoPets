package mv.mossuh.mopets.PETS.Pet;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import mv.mossuh.mopets.ENUMS.ExecuteType;
import mv.mossuh.mopets.ENUMS.ReceiveType;
import mv.mossuh.mopets.API.Events.PetChangeLevelEvent;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Messages;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfo;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.Upgrades;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetUpdater {

    private static final int updateItemInfoInterval = (int) Config.UPDATE_ITEM_INFO_INTERVAL;

    public static void updateItemInfo(Player player, Pet pet, boolean force) {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);
        String code = pet.getConfigPet().getPetIdentifier().getCode();
        if (force) {
            updaterItemInfo(player, pets);
        } else {
            if (!Cooldown.startAndIsOnCooldown( Config.PLUGIN_NAME + "-PetUpdater-updateItemInfo-" + code + "-" + player.getUniqueId(), updateItemInfoInterval)) {
                updaterItemInfo(player, pets);
            }
        }
    }

    public static void verifyPet(Player player, Pet pet, boolean force) {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);
        String code = pet.getConfigPet().getPetIdentifier().getCode();
        if (force) {
            verifierPet(player, pets);
        } else {
            if (!Cooldown.startAndIsOnCooldown( Config.PLUGIN_NAME+ "-PetUpdater-verifyPet-" + code + "-" + player.getUniqueId(), updateItemInfoInterval)) {
                verifierPet(player, pets);
            }
        }
    }


    private static void updaterItemInfo(Player player, List<Pet> pets) {
        UUID uuid = player.getUniqueId();

        for (Pet pet : pets) {
            if (pet.isItemStack()) {
                ConfigPet configPet = pet.getConfigPet();
                if (configPet != null) {
                    int level = pet.getLevel();
                    int maxlevel = pet.getConfigPet().getUpgrades().getMaxLevel();
                    double exp = pet.getExp();
                    double cost = pet.getCost();
                    String progressPercentage = UtilMethods.progressPercentage(exp, cost);

                    List<VariableArg> variables = new ArrayList<>();
                    variables.add(new VariableArg("%progress_percentage%", progressPercentage));

                    if (level < maxlevel) {
                        ItemStack itemStack = pet.getItemStack();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        ItemInfo itemInfo = configPet.getItemInfo();


                        if (itemInfo.hasName()) {
                            String name = UtilString.get(itemInfo.getName()).hex().setDefaultNumberRandomVariable().setDefaultVariables(pet)
                                    .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter()
                                    .setVariables(variables).apply();
                            itemMeta.setDisplayName(name);
                        }

                        if (itemInfo.hasLore()) {
                            List<String> lore = new ArrayList<>();
                            for (String line : itemInfo.getLore()) {
                                if (line.contains("%progress%")) {
                                    List<String> progressMessage = configPet.getUpgrades().getProgressMessage();
                                    if (!progressMessage.isEmpty()) {
                                        for (String pLine : progressMessage) {
                                            lore.add(UtilString.get(pLine).hex().setDefaultNumberRandomVariable().setDefaultVariables(pet)
                                                    .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter()
                                                    .setVariables(variables).apply());
                                        }
                                    }
                                } else {
                                    lore.add(UtilString.get(line).hex().setDefaultNumberRandomVariable().setDefaultVariables(pet).setPlaceholders(uuid)
                                            .setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().setVariables(variables).apply());
                                }
                            }
                            itemMeta.setLore(lore);
                        }

                        itemStack.setItemMeta(itemMeta);
                    } else {
                        ItemStack itemStack = pet.getItemStack();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        ItemInfo itemInfo = configPet.getItemInfo();

                        if (itemInfo.hasName()) {
                            String name = UtilString.get(itemInfo.getName()).hex().setDefaultNumberRandomVariable().setDefaultVariables(pet).setPlaceholders(uuid).setChangeOutputPlaceholder()
                                    .setMathPlaceholder().setTimeFormatter().setVariables(variables).apply();
                            itemMeta.setDisplayName(name);
                        }

                        if (itemInfo.hasLore()) {
                            List<String> lore = new ArrayList<>();
                            for (String line : itemInfo.getLore()) {
                                if (line.contains("%progress%")) {
                                    List<String> progressMaxedMessage = configPet.getUpgrades().getMaxedProgressMessage();
                                    if (!progressMaxedMessage.isEmpty()) {
                                        for (String pLine : progressMaxedMessage) {
                                            lore.add(UtilString.get(pLine).hex().setDefaultNumberRandomVariable().setDefaultVariables(pet)
                                                    .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter()
                                                    .setVariables(variables).apply());
                                        }
                                    }
                                } else {
                                    lore.add(UtilString.get(line).hex().setDefaultNumberRandomVariable().setDefaultVariables(pet)
                                            .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter()
                                            .setVariables(variables).apply());
                                }
                            }
                            itemMeta.setLore(lore);
                        }

                        itemStack.setItemMeta(itemMeta);
                    }
                }
            }
        }
    }
    private static void verifierPet(Player player, List<Pet> pets) {
        UUID uuid = player.getUniqueId();

        firstFor:
        for (Pet pet : pets) {
            if (pet.isPet()) {
                ConfigPet configPet = pet.getConfigPet();
                Upgrades upgrades = configPet.getUpgrades();
                ItemInfo itemInfo = configPet.getItemInfo();
                String name = UtilString.get(itemInfo.getName()).hex().apply();

                int maxLevel = upgrades.getMaxLevel();
                double levelCost = upgrades.getCostPerLevel();
                double obtainedExp = pet.getExp();
                double obtainedCost = pet.getCost();
                int obtainedLevel = pet.getLevel();

                while (obtainedExp >= obtainedCost && obtainedLevel < maxLevel) {
                    PetChangeLevelEvent petEvent = new PetChangeLevelEvent(player, pet, ExecuteType.NATURAL, ReceiveType.ADD, 1);
                    Bukkit.getPluginManager().callEvent(petEvent);

                    if (petEvent.isCancelled()) { continue firstFor; }
                    int eventLevel = petEvent.getLevel();

                    int newLevel = obtainedLevel + eventLevel; // Here new level with boost
                    double newCost = newLevel * levelCost;

                    double newExp = obtainedExp - obtainedCost;

                    pet.setLevel(newLevel);
                    pet.setExp(newExp);
                    pet.setCost(newCost);

                    obtainedLevel = pet.getLevel();
                    obtainedExp = pet.getExp();
                    obtainedCost = pet.getCost();

                    UtilString.get(Messages.PET_LEVEL_UP).hex().replaceString("%item_name%", name)
                            .setDefaultNumberRandomVariable().setDefaultVariables(pet).setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().sendMessage(player);
                }
            }
        }
    }
}
