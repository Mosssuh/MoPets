package mv.mossuh.mopets.UTILITIES.Pet;

import de.tr7zw.nbtapi.NBTItem;
import mv.mossuh.mocore.NBT.NBTMethods;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.VERSION.ServerVersion;
import mv.mossuh.mopets.MODEL.Pets.ItemInfo;
import mv.mossuh.mopets.MODEL.Pets.Config.ConfigPet;
import mv.mossuh.mopets.DATA.Pets.ConfigPets;
import mv.mossuh.mopets.MODEL.Pets.PetIdentifier;
import mv.mossuh.mopets.MODEL.Pets.ItemInfoUtil.Enchant;
import mv.mossuh.mopets.MODEL.Pets.ItemInfoUtil.Enchantments;
import mv.mossuh.mopets.UTILITIES.Nbt.NBTPet;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetCreator {

    public static ItemStack fromCode(String code, CommandArgs args, UUID uuid, int amount) {
        ConfigPet configPet = ConfigPets.getConfigPet(code);
        if (configPet.isConfigPet()) {
            String material = configPet.getItemInfo().getMaterial();
            if (material.startsWith("basehead-")) {
                return fromHead(configPet, args, uuid, amount);
            } else {
                return fromMaterial(configPet, args, uuid, amount);
            }
        }
        return new ItemStack(Material.AIR, amount);
    }

    public static ItemStack fromConfigPet(ConfigPet configPet, CommandArgs args, UUID uuid, int amount) {
        if (configPet != null && configPet.isConfigPet()) {
            String material = configPet.getItemInfo().getMaterial();
            if (material.startsWith("basehead-")) {
                return fromHead(configPet, args, uuid, amount);
            } else {
                return fromMaterial(configPet, args, uuid, amount);
            }
        }
        return new ItemStack(Material.AIR, amount);
    }

    private static ItemStack fromMaterial(ConfigPet configPet, CommandArgs args, UUID uuid, int amount) {
        ItemInfo itemInfo = configPet.getItemInfo();
        PetIdentifier petIdentifier = configPet.getPetIdentifier();
        List<VariableArg> defaultVariables = petIdentifier.getDefaultVariables();
        String material = itemInfo.getMaterial();
        byte data = itemInfo.getMaterialData();
        double cost = configPet.getUpgrades().getCostPerLevel();

        ItemStack itemStack = new ItemStack(Material.AIR, 1);
        if (material.contains("LEATHER")) {
            String[] itemTypeSeparate = material.replace(" ", "").split("->", 2);
            Material itemMaterial = Material.valueOf(itemTypeSeparate[0]);

            if (itemTypeSeparate.length > 1 && !itemTypeSeparate[1].isEmpty()) {
                String armorColor = itemTypeSeparate[1];

                if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                    itemStack = new ItemStack(itemMaterial, 1);
                } else {
                    if (data != -1) {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, data);
                    } else {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, (byte) 0);
                    }
                }
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                Color color = Color.fromRGB(Integer.parseInt(armorColor, 16));
                leatherArmorMeta.setColor(color);
                itemStack.setItemMeta(leatherArmorMeta);
            } else {
                if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                    itemStack = new ItemStack(itemMaterial, 1);
                } else {
                    if (data != -1) {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, data);
                    } else {
                        itemStack = new ItemStack(itemMaterial, 1, (short) 0, (byte) 0);
                    }
                }
            }
        } else {
            if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
                itemStack = new ItemStack(Material.valueOf(material), 1);
            } else {
                if (data != -1) {
                    itemStack = new ItemStack(Material.valueOf(material), 1, (short) 0, data);
                } else {
                    itemStack = new ItemStack(Material.valueOf(material), 1, (short) 0, (byte) 0);
                }
            }
        }
        ItemMeta itemMeta = itemStack.getItemMeta();

        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();

        String progressPercentage = UtilMethods.progressPercentage(0, cost);

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%progress_percentage%", progressPercentage));

        if (itemInfo.hasName()) {
            String name = UtilString.get(itemInfo.getName()).setArgs(args).setVariables(variables).setVariables(configPet).setPlaceholders(uuid).setTimeFormatter().hex().apply();
            itemMeta.setDisplayName(name);
        }
        if (itemInfo.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : itemInfo.getLore()) {
                if (line.contains("%progress%")) {
                    List<String> progressMessage = configPet.getUpgrades().getProgressMessage();
                    if (!progressMessage.isEmpty()) {
                        for (String pLine : progressMessage) {
                            lore.add(UtilString.get(pLine).setArgs(args).setVariables(variables).setVariables(configPet)
                                    .setPlaceholders(uuid).setTimeFormatter().hex().apply());
                        }
                    }
                } else {
                    lore.add(UtilString.get(line).setArgs(args).setVariables(variables)
                            .setVariables(configPet).setPlaceholders(uuid).setTimeFormatter().hex().apply());
                }
            }
            itemMeta.setLore(lore);
        }


        if (enchantments.hasEnchant()) {
            for (Enchant enchant : enchants) {
                String enchantName = enchant.getEnchantName();
                int level = enchant.getLevel();

                Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        if (itemInfo.hasFlag()) {
            for (String flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        itemStack.setItemMeta(itemMeta);

        NBTMethods.modify(itemStack, tags -> {
            if (itemInfo.isUnbreakable()) {
                tags.setBoolean("Unbreakable", true);
            }
        });

        if (petIdentifier.hasCode()) {
            String code = petIdentifier.getCode();
            NBTPet.setUUID(itemStack);
            NBTPet.setCode(itemStack, code);
            NBTPet.setLevel(itemStack, 1);
            NBTPet.setExp(itemStack, 0.0);
            NBTPet.setCost(itemStack, cost);

            if (petIdentifier.hasDefaultVariables()) {
                NBTPet.setVariables(itemStack, defaultVariables);
            }
            if (petIdentifier.hasTags()) {
                String tagsAsString  = petIdentifier.getTagsAsString();
                NBTPet.setTags(itemStack, tagsAsString);
            }
        }
        itemStack.setAmount(Math.max(amount, 1));
        return itemStack;
    }

    private static ItemStack fromHead(ConfigPet configPet, CommandArgs args, UUID uuid, int amount) {
        ItemInfo itemInfo = configPet.getItemInfo();
        PetIdentifier petIdentifier = configPet.getPetIdentifier();
        List<VariableArg> defaultVariables = petIdentifier.getDefaultVariables();
        String material = itemInfo.getMaterial();
        double cost = configPet.getUpgrades().getCostPerLevel();

        Material skullType;
        if (!ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            skullType = Material.valueOf("SKULL_ITEM");
        } else {
            skullType = Material.PLAYER_HEAD;
        }

        String value = material.replace("basehead-", "");
        ItemStack itemStack;
        if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            itemStack = new ItemStack(skullType, 1);
        } else {
            itemStack =  new ItemStack(skullType, 1, (short) 0, (byte) 3);
        }
        NBTPet.setHeadTexture(itemStack, value);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();


        Enchantments enchantments = itemInfo.getEnchantments();
        List<Enchant> enchants = itemInfo.getEnchantments().getEnchants();

        List<String> flags = itemInfo.getFlags();

        String progressPercentage = UtilMethods.progressPercentage(0, cost);

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%progress_percentage%", progressPercentage));

        if (itemInfo.hasName()) {
            String name = UtilString.get(itemInfo.getName()).setArgs(args).setVariables(variables).setVariables(configPet).setPlaceholders(uuid).setTimeFormatter().hex().apply();
            itemMeta.setDisplayName(name);
        }
        if (itemInfo.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String line : itemInfo.getLore()) {
                if (line.contains("%progress%")) {
                    List<String> progressMessage = configPet.getUpgrades().getProgressMessage();
                    if (!progressMessage.isEmpty()) {
                        for (String pLine : progressMessage) {
                            lore.add(UtilString.get(pLine).setArgs(args).setVariables(variables).setVariables(configPet)
                                    .setPlaceholders(uuid).setTimeFormatter().hex().apply());
                        }
                    }
                } else {
                    lore.add(UtilString.get(line).setArgs(args).setVariables(variables)
                            .setVariables(configPet).setPlaceholders(uuid).setTimeFormatter().hex().apply());
                }
            }
            itemMeta.setLore(lore);
        }


        if (enchantments.hasEnchant()) {
            for (Enchant enchant : enchants) {
                String enchantName = enchant.getEnchantName();
                int level = enchant.getLevel();

                Enchantment enchantment = Enchantment.getByName(enchantName.toUpperCase());
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        if (itemInfo.hasFlag()) {
            for (String flag : flags) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        if (itemInfo.isUnbreakable()) {
            NBTItem NBTItem = new NBTItem(itemStack);
            NBTItem.setBoolean("Unbreakable", true);
            itemStack = NBTItem.getItem();
        }

        itemStack.setItemMeta(itemMeta);

        if (itemInfo.isUnique()) {
            NBTPet.setUnique(itemStack);
        }

        if (petIdentifier.hasCode()) {
            String code = petIdentifier.getCode();
            NBTPet.setUUID(itemStack);
            NBTPet.setCode(itemStack, code);
            NBTPet.setLevel(itemStack, 1);
            NBTPet.setExp(itemStack, 0.0);
            NBTPet.setCost(itemStack, cost);

            if (petIdentifier.hasDefaultVariables()) {
                NBTPet.setVariables(itemStack, defaultVariables);
            }
            if (petIdentifier.hasTags()) {
                String tagsAsString  = petIdentifier.getTagsAsString();
                NBTPet.setTags(itemStack, tagsAsString);
            }
        }
        itemStack.setAmount(Math.max(amount, 1));
        return itemStack;
    }

    public static ItemStack fromReward(String itemString, UUID uuid) {
        // (code)[amount]
        CommandArgs args = new CommandArgs(null);
        String[] itemStringSplit = itemString.split("\\[", 2);
        String code = itemStringSplit[0];
        String amountString = "1";
        if (itemStringSplit.length == 2) {
            amountString = itemStringSplit[1].replaceAll(" ", "").replace("]", "");
        }

        int amount = (int) Math.round(Double.parseDouble(amountString));

        ConfigPet configPet = ConfigPets.getConfigPet(code);
        return fromConfigPet(configPet, args, uuid, amount);

    }
}
