package mv.mossuh.mopets.UTILITIES;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.UsefulString;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.MODEL.Pets.Config.ConfigPet;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UtilString extends UsefulString<UtilString> {

    protected UtilString(String string) {
        super(string);
    }

    public static UtilString get(String string) { return new UtilString(string); }

    public UtilString setVariables(Player player) {
        if (!isString()) return this;
        if (player == null || !player.isOnline()) return this;

        List<VariableArg> variables = new ArrayList<>();
        variables.add(new VariableArg("%player%", player.getName()));
        setVariables(variables);
        return this;
    }

    public UtilString setVariables(Pet pet) {
        if (!isString()) return this;
        if (pet == null || !pet.isPet()) return this;
        List<VariableArg> variables = DefaultVariables.pet(pet);
        setVariables(variables);
        return this;
    }

    public UtilString setVariables(ConfigPet config) {
        if (!isString()) return this;
        if (config == null || !config.isConfigPet()) return this;
        List<VariableArg> variables = DefaultVariables.configPet(config);
        setVariables(variables);
        return this;
    }

    public UtilString setTimeFormatter() {
        if (!isString()) return this;
        setTimeFormatter(Config.TIME_FORMAT);
        return this;
    }
}
