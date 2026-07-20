package mv.mossuh.mopets.EVENTS.Detector.Pickup;

import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.MoPets;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DetectorWithEntityPickup implements Listener {
    private final MoPets instance = MoPets.getInstance();

    @EventHandler(priority = EventPriority.LOW)
    public void byItemPickup(EntityPickupItemEvent event) {
        if (event.isCancelled()) { return; }

        LivingEntity entity = event.getEntity();
        if (entity.getType().equals(EntityType.PLAYER)) {
            Player player = (Player) entity;
            UUID uuid = player.getUniqueId();

            ItemStack itemStack = event.getItem().getItemStack();

            Bukkit.getScheduler().runTask(instance, () -> {
                Pet pet = Pet.getPet(itemStack);
                if (pet.isPet()) {
                    PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
                    if (petsPlayer.isPlayer()) {
                        petsPlayer.addPet(pet);
                    }
                }
            });
        }
    }
}
