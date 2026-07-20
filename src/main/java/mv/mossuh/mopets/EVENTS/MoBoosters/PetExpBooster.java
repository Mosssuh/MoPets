package mv.mossuh.mopets.EVENTS.MoBoosters;

import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.mopets.API.Events.PetChangeExpEvent;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.GlobalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.PersonalBooster;
import mv.mossuh.moboosters.BOOSTERS.BoosterTypes.SuperiorSkyblock2Booster;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;

import java.util.UUID;

public class PetExpBooster implements Listener {
    @EventHandler
    public void expBoost(PetChangeExpEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Pet pet = event.getPet();
        ConfigPet configPet = pet.getConfigPet();
        PetIdentifier identifier = configPet.getPetIdentifier();
        String boosterIdentifier = identifier.getBoosterIdentifier();

        double personal = 0;
        double global = 0;
        double superiorSkyblock2 = 0;

        if (boosterIdentifier != null) {
            if (identifier.isBoosterIdentifier()) {
                personal = BoostersAPI.getManager().getBoost(new PersonalBooster(uuid, new BoosterIdentifier(boosterIdentifier, BoosterType.PERSONAL, ApplicatorType.MOPETS, "exp")), false);
                global = BoostersAPI.getManager().getBoost(new GlobalBooster(new BoosterIdentifier(boosterIdentifier, BoosterType.GLOBAL, ApplicatorType.MOPETS, "exp")), false);
                superiorSkyblock2 = BoostersAPI.getManager().getBoost(new SuperiorSkyblock2Booster(UtilMethods.getIslandUUID(player), new BoosterIdentifier(boosterIdentifier, BoosterType.SUPERIORSKYBLOCK2, ApplicatorType.MOPETS, "exp")), false);
            }
        } else {
            personal = BoostersAPI.getManager().getBoost(new PersonalBooster(uuid, new BoosterIdentifier(null, BoosterType.PERSONAL, ApplicatorType.MOPETS, "exp")), true);
            global = BoostersAPI.getManager().getBoost(new GlobalBooster(new BoosterIdentifier(null, BoosterType.GLOBAL, ApplicatorType.MOPETS, "exp")), true);
            superiorSkyblock2 = BoostersAPI.getManager().getBoost(new SuperiorSkyblock2Booster(UtilMethods.getIslandUUID(player), new BoosterIdentifier(null, BoosterType.SUPERIORSKYBLOCK2, ApplicatorType.MOPETS, "exp")), true);
        }

        double total = personal + global + superiorSkyblock2;
        event.addBoost(total);
    }
}
