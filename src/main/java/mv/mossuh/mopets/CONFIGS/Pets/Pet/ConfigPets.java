package mv.mossuh.mopets.CONFIGS.Pets.Pet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigPets {
    private static Set<ConfigPet> configs = new HashSet<>();

    public static void clearConfigPets() {
        configs.clear();
    }

    public static Set<ConfigPet> getConfigPets() {
        return new HashSet<>(configs);
    }

    public static ConfigPet getConfigPet(String code) {
        for (ConfigPet c : configs) {
            if (c.getPetIdentifier().getCode().equalsIgnoreCase(code)) {
                return c;
            }
        }
        return new ConfigPet();
    }

    public static boolean exist(String code) {
        for (ConfigPet c : configs) {
            if (c.getPetIdentifier().getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    public static void addPet(ConfigPet configPet) {
        configs.add(configPet);
    }
}
