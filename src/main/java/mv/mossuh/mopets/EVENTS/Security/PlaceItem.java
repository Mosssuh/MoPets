package mv.mossuh.mopets.EVENTS.Security;

import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceItem implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeItem(BlockPlaceEvent event) {
        if (event.isCancelled()) { return; }

        ItemStack itemStack = event.getItemInHand();
        Pet pet = Pet.getPet(itemStack);
        if (pet.isPet()) {
            event.setCancelled(true);
        }
    }
}
