package mv.mossuh.mopets.EVENTS.Detector;

import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetsGetter;
import mv.mossuh.mopets.PETS.Pet.PetsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class DetectorWithJoinQuit implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byJoining(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        List<Pet> pets = PetsGetter.inventory(player);
        PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
        petsPlayer.setPets(pets);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void byLeaving(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PetsAPI.getManager().removePlayer(uuid);
    }
}
