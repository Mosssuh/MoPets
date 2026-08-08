package mv.mossuh.mopets.PETS.Pet;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PetsGetter {

    public static List<Pet> inventory(Player player) {
        List<Pet> pets = new ArrayList<>();
        try {
            for (ItemStack itemStack : player.getInventory().getContents()) {
                Pet pet = Pet.getPet(itemStack);
                if (pet.isPet()) {
                    pets.add(pet);
                }
            }
        } catch (NoSuchMethodError | Exception ignored) {

        }
        return pets;
    }
}
