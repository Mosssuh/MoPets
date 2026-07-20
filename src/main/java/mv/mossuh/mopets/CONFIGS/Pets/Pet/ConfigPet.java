package mv.mossuh.mopets.CONFIGS.Pets.Pet;

import mv.mossuh.mopets.CONFIGS.Pets.Actions.Actions;
import mv.mossuh.mopets.ENUMS.ExpType;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfo;
import mv.mossuh.mopets.CONFIGS.Pets.Upgrades;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfoUtil.EntityExp;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfoUtil.Exp;

import java.util.ArrayList;
import java.util.List;

public class ConfigPet {

    private PetIdentifier petIdentifier = new PetIdentifier();
    private Upgrades upgrades = new Upgrades();
    private ItemInfo itemInfo = new ItemInfo();
    private List<Exp> exp = new ArrayList<>();
    private Actions actions = new Actions(null, new ArrayList<>());
    public ConfigPet(PetIdentifier petIdentifier, Upgrades upgrades, ItemInfo itemInfo, List<Exp> exp, Actions actions) {
        if (petIdentifier != null) {this.petIdentifier = petIdentifier; }
        if (upgrades != null) { this.upgrades = upgrades; }
        if (itemInfo != null) { this.itemInfo = itemInfo; }
        if (exp != null) { this.exp = exp; }
        if (actions != null) { this.actions = actions; }
    }
    public ConfigPet() {}

    public boolean isConfigPet() {
        return !itemInfo.getMaterial().equals("AIR");
    }

    public PetIdentifier getPetIdentifier() {
        return petIdentifier;
    }
    public Upgrades getUpgrades() {
        return upgrades;
    }
    public ItemInfo getItemInfo() {
        return itemInfo;
    }
    public List<Exp> getExps() {
        return exp;
    }

    public List<EntityExp> getExp(ExpType expType) {
        if (!exp.isEmpty()) {
            for (Exp e : exp) {
                ExpType type = e.getExpType();
                if (type.equals(expType)) {
                    return e.getExpEntities();
                }
            }
        }
        return new ArrayList<>();
    }
    public Actions getActions() {
        return actions;
    }
}
