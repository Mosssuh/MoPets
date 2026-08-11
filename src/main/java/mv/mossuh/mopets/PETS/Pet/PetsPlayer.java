package mv.mossuh.mopets.PETS.Pet;

import mv.mossuh.mopets.ENUMS.ChangePetType;
import mv.mossuh.mopets.API.Events.PlayerChangePetEvent;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PetsPlayer {

    private UUID uuid;
    private List<Pet> pets = new ArrayList<>();

    public PetsPlayer(UUID uuid, List<Pet> pets) {
        this.uuid = uuid;
        if (pets != null) { this.pets = pets; }
    }

    public boolean isPlayer() { return uuid != null; }

    public UUID getUUID() { return uuid; }

    public List<Pet> getPets() { return pets; }

    public Pet getPet(String code) {
        for (Pet pet : pets) {
            PetIdentifier identifier = pet.getConfigPet().getPetIdentifier();
            if (identifier.getCode().equalsIgnoreCase(code)) {
                return pet;
            }
        }
        return new Pet(null, null, null, null, null, null, null);
    }

    public void setPets(List<Pet> pets) {
        if (Config.MULTIPLE_PETS) {
            this.pets = UtilMethods.cleanConflicts(uuid, pets);
            return;
        }

        // If new pets are empty but old pets has an active pet, deactivate it
        if (pets.isEmpty()) {
            if (!this.pets.isEmpty()) {
                Pet actualPet = this.pets.get(0);
                if (actualPet.isPet()) {
                    PlayerChangePetEvent event = new PlayerChangePetEvent(uuid, actualPet, ChangePetType.DEACTIVATED);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }

            this.pets = new ArrayList<>();
            return;
        }

        // If new pet isn't valid, cancel
        Pet newPet = pets.get(0);
        if (!newPet.isPet()) return;

        // If the new pet is the same as the active pet, cancel
        if (!this.pets.isEmpty()) {
            Pet actualPet = this.pets.get(0);
            if (actualPet.isPet() && newPet.isEquals(actualPet)) {
                return;
            }
        }

        // If the pet is different, activate it
        PlayerChangePetEvent event = new PlayerChangePetEvent(uuid, newPet, ChangePetType.ACTIVATED);
        Bukkit.getPluginManager().callEvent(event);
        this.pets = Collections.singletonList(newPet);
    }


    public void addPet(Pet pet) {

        String code = pet.getConfigPet().getPetIdentifier().getCode();
        UUID petUUID = pet.getPetUUID();
        List<Pet> copyPets = new ArrayList<>(pets);
        for (Pet p : copyPets) {
            if (p.getPetUUID() == petUUID || p.getConfigPet().getPetIdentifier().getCode().equalsIgnoreCase(code)) {
                return;
            }
        }

        copyPets.add(pet);

        setPets(copyPets);
    }


    public void removePet(Pet pet) {
        UUID uuid = pet.getPetUUID();


        List<Pet> copyPets = new ArrayList<>(pets);
        Iterator<Pet> iterator = copyPets.iterator();
        while (iterator.hasNext()) {
            Pet p = iterator.next();
            if (p.getPetUUID().equals(uuid)) {
                iterator.remove();
                break;
            }
        }

        setPets(copyPets);
    }


    public void removePet(UUID petUUID) {
        List<Pet> copyPets = new ArrayList<>(pets);
        Iterator<Pet> iterator = copyPets.iterator();
        while (iterator.hasNext()) {
            Pet p = iterator.next();
            if (p.getPetUUID().equals(petUUID)) {
                iterator.remove();
                break;
            }
        }

        setPets(copyPets);
    }

    public boolean hasPets() {
        return !pets.isEmpty();
    }

    public List<String> getCodes() {
        List<String> codes = new ArrayList<>();
        for (Pet pet : pets) {
            codes.add(pet.getConfigPet().getPetIdentifier().getCode());
        }
        return codes;
    }

    public void updatePets() {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            List<Pet> pets = PetsGetter.inventory(player);
            PetsPlayer petsPlayer = PetsAPI.getManager().getPlayer(uuid);
            petsPlayer.setPets(pets);
        }
    }
}
