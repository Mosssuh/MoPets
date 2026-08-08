package mv.mossuh.mopets.EVENTS.Leveling;

import mv.mossuh.mopets.ENUMS.ExecuteType;
import mv.mossuh.mopets.ENUMS.ReceiveType;
import mv.mossuh.mopets.API.Events.PetChangeExpEvent;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.ENUMS.ExpType;
import mv.mossuh.mopets.CONFIGS.Pets.Pet.ConfigPet;
import mv.mossuh.mopets.CONFIGS.Pets.ItemInfoUtil.EntityExp;
import mv.mossuh.mopets.MoPets;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetsPlayer;
import mv.mossuh.mopets.PETS.Pet.PetUpdater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LevelingExecutor {
    private static final MoPets main = MoPets.getInstance();
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private Player player;
    private ExpType expType = ExpType.NONE;
    private String type = "";
    private short data = -1;
    private double multiplier = 1;

    public LevelingExecutor(Player player, ExpType expType, String type, Short data, Double multiplier) {
        this.player = player;
        if (expType != null) { this.expType = expType; }
        if (type != null) { this.type = type; }
        if (data != null) { this.data = data; }
        if (multiplier != null) {
            if (multiplier > 1) {
                this.multiplier = multiplier;
            }
        }
    }

    public void execute() {
        UUID uuid = player.getUniqueId();

        PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
        if (petsPlayer == null) return;
        if (petsPlayer.isPlayer() && petsPlayer.hasPets()) {
            List<Pet> pets = petsPlayer.getPets();

            executor.submit(() -> {
                List<Map.Entry<Pet, Double>> cached = new ArrayList<>();

                for (Pet pet : pets) {
                    ConfigPet configPet = pet.getConfigPet();
                    List<EntityExp> exps = configPet.getExp(expType);
                    if (exps.isEmpty()) continue;

                    if (EntityExp.containsEntity(exps, type, data)) {
                        double exp = EntityExp.getExpFromList(exps, type, data) * multiplier;
                        cached.add(new AbstractMap.SimpleEntry<>(pet, exp));
                    }
                }

                if (cached.isEmpty()) return;

                Bukkit.getScheduler().runTask(main, () -> {
                    if (!player.isOnline()) return;

                    for (Map.Entry<Pet, Double> entry : cached) {
                        Pet pet = entry.getKey();
                        double exp = entry.getValue();

                        PetChangeExpEvent petEvent = new PetChangeExpEvent(player, pet, ExecuteType.NATURAL, ReceiveType.ADD, exp);
                        Bukkit.getPluginManager().callEvent(petEvent);

                        if (petEvent.isCancelled()) continue;

                        double boost = petEvent.getBoost();
                        double newExp = petEvent.getExp() * boost;

                        pet.addExp(newExp, true);
                        PetUpdater.verifyPet(player, pet, false);
                        PetUpdater.updateItemInfo(player, pet, false);
                    }
                });
            });
        }
    }
}
