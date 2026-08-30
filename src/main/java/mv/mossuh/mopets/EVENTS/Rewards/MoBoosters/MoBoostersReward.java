package mv.mossuh.mopets.EVENTS.Rewards.MoBoosters;

import mv.mossuh.moboosters.API.BoostersAPI;
import mv.mossuh.moboosters.MODEL.ActiveBooster.ActiveBooster;
import mv.mossuh.moboosters.MODEL.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.MODEL.Booster.BoosterTypes.Booster;
import mv.mossuh.moboosters.UTILITIES.Enums.ApplicatorType;
import mv.mossuh.moboosters.UTILITIES.Enums.BoosterType;
import mv.mossuh.mopets.API.Events.PlayerChangePetEvent;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.DATA.Config.Config;
import mv.mossuh.mopets.MODEL.Pets.Actions.Actions;
import mv.mossuh.mopets.MODEL.Pets.Actions.MoBoosters.LocalBooster;
import mv.mossuh.mopets.MODEL.Pets.Config.ConfigPet;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import mv.mossuh.mopets.MODEL.Player.PetsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

                LocalBooster personalBooster = actions.getBooster(new BoosterIdentifier(Config.BOOSTER_IDENTIFIER, BoosterType.PERSONAL, applicatorType, boosted));
                LocalBooster globalBooster = actions.getBooster(new BoosterIdentifier(Config.BOOSTER_IDENTIFIER, BoosterType.GLOBAL, applicatorType, boosted));
                LocalBooster boosterSuperiorSkyblock2 = actions.getBooster(new BoosterIdentifier(Config.BOOSTER_IDENTIFIER, BoosterType.SUPERIORSKYBLOCK2, applicatorType, boosted));

                personal += personalBooster.getBoost(level);
                global += globalBooster.getBoost(level);
                superiorSkyblock2 += boosterSuperiorSkyblock2.getBoost(level);
            }
        }
        double total = personal+global+superiorSkyblock2;
        event.addBoost(total);
    }

    @EventHandler
    public void onChangePet(PlayerChangePetEvent event) {
        UUID uuid = event.getUUID();
        PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
        List<Pet> pets = petsPlayer.getPets();

        Set<BoosterIdentifier> ids = new HashSet<>();
        for (Pet pet : pets) {
            for (LocalBooster local : pet.getConfigPet().getActions().getBoosters()) {
                BoosterIdentifier id = local.getIdentifier();
                if (BoostersAPI.isStateApplicator(id.getApplicatorType(), id.getBoosted())) {
                    ids.add(id);
                }
            }
        }

        for (BoosterIdentifier id : ids) {
            double totalBoost = 0;
            for (Pet pet : pets) {
                totalBoost += pet.getConfigPet().getActions().getBooster(id).getBoost(pet.getLevel());
            }

            Booster booster = Booster.getBooster(id, uuid);
            if (totalBoost > 0) {
                BoostersAPI.getManager().setPermBoost(booster, totalBoost);
            } else {
                ActiveBooster active = BoostersAPI.getManager().getBooster(booster);
                if (active.isValid()) {
                    active.getBoosts().getPermanent().cancel();
                }
            }
        }
    }
}
