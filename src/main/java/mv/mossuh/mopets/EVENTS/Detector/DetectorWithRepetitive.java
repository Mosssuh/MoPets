package mv.mossuh.mopets.EVENTS.Detector;

import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.MoPets;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetsGetter;
import mv.mossuh.mopets.PETS.Pet.PetsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class DetectorWithRepetitive {
    private static BukkitTask task;

    public static void start(MoPets main) {
        task = Bukkit.getScheduler().runTaskTimer(main, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                List<Pet> pets = PetsGetter.inventory(player);
                PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
                petsPlayer.setPets(pets);
            }
        }, 600L, Config.CHECK_ITEMS_INTERVAL * 20L);
    }

    public static void cancel() {
        if (task != null) {
            task.cancel();
        }
    }
}
