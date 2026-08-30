package mv.mossuh.mopets.EVENTS.Detector;

import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.MODEL.Pets.Pet;
import mv.mossuh.mopets.MODEL.Player.PetsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DetectorWithItemEvents implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void byItemDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()) { return; }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ItemStack itemStack = event.getItemDrop().getItemStack();
        Pet pet = Pet.getPet(itemStack);
        if (pet.isPet()) {
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            petsPlayer.removePet(pet);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void byItemConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()) { return; }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ItemStack itemStack = event.getItem();
        Pet pet = Pet.getPet(itemStack);
        if (pet.isPet()) {
            if (itemStack.getAmount() == 1) {
                PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
                petsPlayer.removePet(pet);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void byItemBreak(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        ItemStack itemStack = event.getBrokenItem();
        Pet pet = Pet.getPet(itemStack);
        if (pet.isPet()) {
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            petsPlayer.removePet(pet);
        }
    }
}
