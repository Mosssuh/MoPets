package mv.mossuh.mopets.EVENTS.Detector;

import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetsGetter;
import mv.mossuh.mopets.PETS.Pet.PetsPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class DetectorWithInventory implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()) { return; }
        HumanEntity human = event.getWhoClicked();
        if (!human.getType().equals(EntityType.PLAYER)) {
            return;
        }

        Player player = (Player) human;
        UUID uuid = player.getUniqueId();

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        if (clickedInventory.getType() == InventoryType.PLAYER) {
            ItemStack selectedItemStack = event.getCurrentItem();
            ItemStack unSelectedItemStack = event.getCursor();
            Pet selectedPet = Pet.getPet(selectedItemStack);
            Pet unSelectedPet = Pet.getPet(unSelectedItemStack);
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);

            if (selectedPet.isPet()) {
                petsPlayer.removePet(selectedPet);
            }

            if (unSelectedPet.isPet()) {
                petsPlayer.addPet(unSelectedPet);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void byInventoryClose(InventoryCloseEvent event) {
        HumanEntity human = event.getPlayer();
        if (!human.getType().equals(EntityType.PLAYER)) {
            return;
        }

        Player player = (Player) human;
        UUID uuid = player.getUniqueId();

        List<Pet> pets = PetsGetter.inventory(player);
        PetsAPI.getManager().getPlayer(uuid).setPets(pets);
    }
}
