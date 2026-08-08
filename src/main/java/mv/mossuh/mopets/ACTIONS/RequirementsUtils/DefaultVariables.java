package mv.mossuh.mopets.ACTIONS.RequirementsUtils;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.VERSION.ServerVersion;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DefaultVariables {
    
    public static List<VariableArg> player(Player player) {
        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%player%", player.getName()));
        return variables;
    }

    public static List<VariableArg> itemStack(ItemStack itemStack) {
        List<VariableArg> variables = new ArrayList<>();
        String name = "";
        String material = "";
        byte data = 0;
        String loreString = "";
        int amount = 0;
        if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
            material = itemStack.getType().name();
            data = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? itemStack.getData().getData() : -1;
            amount = itemStack.getAmount();
            if (itemStack.getItemMeta() != null) {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta.hasDisplayName()) {
                    name = UtilString.get(meta.getDisplayName()).removeColors().apply();
                }
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        String line = lore.get(i);

                        if (i == lore.size() - 1) {
                            loreString = loreString + UtilString.get(line).removeColors().apply();
                        } else {
                            loreString = loreString + UtilString.get(line).removeColors().apply() + " ";
                        }
                    }

                }
            }
        }

        Pet pet = Pet.getPet(itemStack);
        String isPet = "false";
        if (pet.isPet()) { isPet = "true"; }
        ConfigPet configPet = pet.getConfigPet();
        PetIdentifier petIdentifier = configPet.getPetIdentifier();
        String code = petIdentifier.getCode();
        String tags = petIdentifier.getTagsAsString();
        List<VariableArg> itemVariables = pet.getVariables();

        String level = String.valueOf(pet.getLevel());
        String exp = pet.getExp()+"";
        String cost = pet.getCost()+"";
        int maxLevel = configPet.getUpgrades().getMaxLevel();

        variables.add(new VariableArg("%itemstack_is_mopet%", isPet));
        variables.add(new VariableArg("%itemstack_mopet_level%", level));
        variables.add(new VariableArg("%itemstack_mopet_exp%", exp));
        variables.add(new VariableArg("%itemstack_mopet_cost%", cost));
        variables.add(new VariableArg("%itemstack_mopet_max_level%", maxLevel+""));
        variables.add(new VariableArg("%itemstack_mopet_tags%", tags));
        variables.add(new VariableArg("%itemstack_mopet_code%", code));
        // %itemstack_mopet_variable_{<variable}%
        for (VariableArg variable : itemVariables) {
            variables.add(new VariableArg("%itemstack_mopet_variable_{" + variable.getVariable() + "}%", variable.getValue()));
        }

        variables.add(new VariableArg("%event_entity%", material));
        variables.add(new VariableArg("%event_data%", data+""));
        variables.add(new VariableArg("%itemstack_material%", material));
        variables.add(new VariableArg("%itemstack_data%", data + ""));
        variables.add(new VariableArg("%itemstack_name%", name));
        variables.add(new VariableArg("%itemstack_lore%", loreString));
        variables.add(new VariableArg("%itemstack_amount%", amount + ""));
        return variables;
    }


    public static List<VariableArg> block(Block block) {
        List<VariableArg> variables = new ArrayList<>();
        if (block != null && !block.getType().equals(Material.AIR)) {
            Location location = block.getLocation();
            String type = block.getType().name();
            String data = !ServerVersion.isAtLeast(ServerVersion.MC1_13) ? block.getData()+"" : "-1";
            variables.add(new VariableArg("%event_entity%", type));
            variables.add(new VariableArg("%event_data%", data));
            variables.add(new VariableArg("%block%", type));
            variables.add(new VariableArg("%block_data%", block.getData() + ""));
            variables.add(new VariableArg("%block_x%", location.getBlockX() + ""));
            variables.add(new VariableArg("%block_y%", location.getBlockY() + ""));
            variables.add(new VariableArg("%block_z%", location.getBlockZ() + ""));
            variables.add(new VariableArg("%block_world%", location.getWorld().getName()));
        }
        return variables;
    }
    
    public static List<VariableArg> pet(Pet pet) {
        List<VariableArg> variables = new ArrayList<>();
        if (pet.isPet()) {
            ConfigPet configItem = pet.getConfigPet();
            PetIdentifier petIdentifier = configItem.getPetIdentifier();
            String code = petIdentifier.getCode();
            String tags = petIdentifier.getTagsAsString();
            List<VariableArg> itemVariables = pet.getVariables();

            String level = String.valueOf(pet.getLevel());
            String exp = pet.getExp()+"";
            String cost = pet.getCost()+"";
            int maxLevel = configItem.getUpgrades().getMaxLevel();
            variables.add(new VariableArg("%level%", level));
            variables.add(new VariableArg("%exp%", exp));
            variables.add(new VariableArg("%cost%", cost));
            variables.add(new VariableArg("%max_level%", maxLevel+""));
            variables.add(new VariableArg("%tags%", tags));
            variables.add(new VariableArg("%code%", code));
            // %variable_{<variable}%
            for (VariableArg variable : itemVariables) {
                variables.add(new VariableArg("%variable_{" + variable.getVariable() + "}%", variable.getValue()));
            }
        }
        return variables;
    }


    public static List<VariableArg> entity(Entity entity) {
        List<VariableArg> variables = new ArrayList<>();
        if (entity != null) {
            String victimType = entity.getType().name();
            String victimData = "-1";
            String victimName = entity.getName();
            Location location = entity.getLocation();

            variables.add(new VariableArg("%event_entity%", victimType));
            variables.add(new VariableArg("%event_data%", victimData));
            variables.add(new VariableArg("%entity_type%", victimType));
            variables.add(new VariableArg("%entity_name%", victimName));
            variables.add(new VariableArg("%entity_x%", location.getBlockX() + ""));
            variables.add(new VariableArg("%entity_y%", location.getBlockY() + ""));
            variables.add(new VariableArg("%entity_z%", location.getBlockZ() + ""));
            variables.add(new VariableArg("%entity_world%", location.getWorld().getName()));
        }
        return variables;
    }

    public static List<VariableArg> livingEntity(LivingEntity entity) {
        List<VariableArg> variables = new ArrayList<>();
        if (entity != null) {
            String victimType = entity.getType().name();
            String victimData = "-1";
            String victimName = entity.getName();
            Location location = entity.getLocation();

            variables.add(new VariableArg("%event_entity%", victimType));
            variables.add(new VariableArg("%event_data%", victimData));
            variables.add(new VariableArg("%entity_type%", victimType));
            variables.add(new VariableArg("%entity_name%", victimName));
            variables.add(new VariableArg("%entity_x%", location.getBlockX() + ""));
            variables.add(new VariableArg("%entity_y%", location.getBlockY() + ""));
            variables.add(new VariableArg("%entity_z%", location.getBlockZ() + ""));
            variables.add(new VariableArg("%entity_world%", location.getWorld().getName()));
        }
        return variables;
    }
}
