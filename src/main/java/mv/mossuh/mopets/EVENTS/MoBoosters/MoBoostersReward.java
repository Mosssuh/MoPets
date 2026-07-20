package mv.mossuh.mopets.EVENTS.MoBoosters;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Pets.Actions.Actions;
import mv.mossuh.mopets.CONFIGS.Pets.Actions.MoBoosters.LocalBooster;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;

import java.util.List;
import java.util.UUID;

public class MoBoostersReward implements Listener {
    @EventHandler
    public void onApplyBooster(PlayerApplyBoostEvent event) {
        UUID uuid = event.getUUID();

        ApplicatorType applicatorType = event.getApplicatorType();
        String boosted = event.getBoosted();


        double personal = 0;
        double global = 0;
        double superiorSkyblock2 = 0;

        PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
        if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
            List<Pet> pets = petsPlayer.getPets();

            for (Pet pet : pets) {
                int level = pet.getLevel();
                ConfigPet configPet = pet.getConfigPet();
                Actions actions = configPet.getActions();

                LocalBooster personalBooster = actions.getBooster(new BoosterIdentifier(Config.PLUGIN_NAME, BoosterType.PERSONAL, applicatorType, boosted));
                LocalBooster globalBooster = actions.getBooster(new BoosterIdentifier(Config.PLUGIN_NAME, BoosterType.GLOBAL, applicatorType, boosted));
                LocalBooster boosterSuperiorSkyblock2 = actions.getBooster(new BoosterIdentifier(Config.PLUGIN_NAME, BoosterType.SUPERIORSKYBLOCK2, applicatorType, boosted));

                personal += personalBooster.getBoost(level);
                global += globalBooster.getBoost(level);
                superiorSkyblock2 += boosterSuperiorSkyblock2.getBoost(level);
            }
        }
        double total = personal+global+superiorSkyblock2;
        event.addBoost(total);
    }
}
