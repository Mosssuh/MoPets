package mv.mossuh.mopets.MODEL.Pets;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.Cooldown;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.MODEL.Pets.Config.ConfigPet;
import mv.mossuh.mopets.DATA.Pets.ConfigPets;
import mv.mossuh.mopets.UTILITIES.Nbt.NBTPet;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pet {
    private UUID uuid = null;
    private ItemStack itemStack = new ItemStack(Material.AIR, 1);
    private boolean isItemStack = false;
    private ConfigPet configPet = new ConfigPet(null, null, null, null, null);
    private int level = 0;
    private double exp = 0;
    private double cost = 0;
    private List<VariableArg> variables = new ArrayList<>();
    private String variablesAsString = "";
    public Pet(UUID uuid, ItemStack itemStack, ConfigPet configPet, Integer level, Double exp, Double cost, List<VariableArg> variables) {
        this.uuid = uuid;
        if (itemStack != null) {
            this.itemStack = itemStack;
            this.isItemStack = !itemStack.getType().equals(Material.AIR);
        }
        if (configPet != null) { this.configPet = configPet; }
        if (level != null) { this.level = level; }
        if (exp != null) { this.exp = exp; }
        if (cost != null) { this.cost = cost; }
        if (variables != null) {
            this.variables = variables;
            this.variablesAsString = VariableArg.toString(variables);
        }
    }
    public Pet() {}

    public boolean isPet() {
        return uuid != null && isItemStack && !configPet.getPetIdentifier().getCode().equals("invalid");
    }


    public boolean isSimilar(Pet pet) {
        return pet.isPet() && configPet.getPetIdentifier().getCode().equalsIgnoreCase(pet.getConfigPet().getPetIdentifier().getCode());
    }
    public boolean isEquals(Pet pet) {
        return pet.isPet() && pet.getPetUUID() == uuid && configPet.getPetIdentifier().getCode().equalsIgnoreCase(pet.getConfigPet().getPetIdentifier().getCode());
    }


    public UUID getPetUUID() { return uuid; }
    public ItemStack getItemStack() { return itemStack; }
    public boolean isItemStack() {
        return isItemStack;
    }
    public ConfigPet getConfigPet() { return configPet; }
    public int getLevel() { return level; }
    public double getExp() { return exp; }
    public double getCost() { return cost; }

    public void setLevel(Integer level) {
        if (level != null) {
            NBTPet.setLevel(itemStack, level);
            this.level = level;
        }
    }
    public void setExp(Double exp) {
        if (exp != null) {
            NBTPet.setExp(itemStack, exp);
            this.exp = exp;
        }
    }
    public void setCost(Double cost) {
        if (cost != null) {
            NBTPet.setCost(itemStack, cost);
            this.cost = cost;
        }
    }


    public void addLevel(Integer level) {
        if (level != null) {
            NBTPet.setLevel(itemStack, this.level + level);
            this.level = this.level + level;
        }
    }
    public synchronized void addExp(Double exp) {
        if (exp != null) {
            NBTPet.setExp(itemStack, this.exp + exp);
            this.exp = this.exp + exp;
        }
    }


    private double accumulatedEXP = 0;

    public synchronized void addExp(double exp, boolean cooldown) {
        if (!cooldown) {
            addExp(exp);
            return;
        }

        String code = Config.PLUGIN_NAME+"-Cooldown-"+uuid;
        accumulatedEXP += exp;

        if (Cooldown.startAndIsOnCooldown(code, 3)) {
            return;
        }

        addExp(accumulatedEXP);
        accumulatedEXP = 0;
    }

    public boolean hasVariables() { return !variables.isEmpty(); }

    public boolean hasVariable(String variable) {
        for (VariableArg v : variables) {
            if (v.getVariable().equalsIgnoreCase(variable)) {
                return true;
            }
        }
        return false;
    }

    public void setVariable(VariableArg variable) {
        if (variable.isVariable()) {
            variables.removeIf(ve -> ve.getVariable().equalsIgnoreCase(variable.getVariable()));
            variables.add(variable);
            NBTPet.setVariables(itemStack, variables);
        }
    }
    public void removeVariable(String variable) {
        variables.removeIf(v -> v.getVariable().equalsIgnoreCase(variable));
    }
    public List<VariableArg> getVariables() { return variables; }
    public VariableArg getVariable(String variable) {
        for (VariableArg v : variables) {
            if (v.getVariable().equalsIgnoreCase(variable)) {
                return v;
            }
        }
        return new VariableArg(null, null);
    }
    public String getVariablesAsString() { return variablesAsString; }


    public static Pet getPet(ItemStack itemStack) {
        if (itemStack != null) {
            UUID uuid = NBTPet.getUUID(itemStack);
            if (uuid != null) {
                String code = NBTPet.getCode(itemStack);
                ConfigPet configPetByCode = ConfigPets.getConfigPet(code);
                Integer level = NBTPet.getLevel(itemStack);
                Double exp = NBTPet.getExp(itemStack);
                Double cost = NBTPet.getCost(itemStack);
                List<VariableArg> variables = NBTPet.getVariables(itemStack);

                return new Pet(uuid, itemStack, configPetByCode, level, exp, cost, variables);
            }
        }
        return new Pet(null, null, null, null, null, null, null);
    }

    public static Pet getInvalidPet() {
        return new Pet(null, null, null, null, null, null, null);
    }
}
