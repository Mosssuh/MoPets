package mv.mossuh.mopets.MANAGERS;

import mv.mossuh.mopets.API.Events.PlayerChangePetEvent;
import mv.mossuh.mopets.ENUMS.ChangePetType;
import mv.mossuh.mopets.PETS.Pet.Pet;
import mv.mossuh.mopets.PETS.Pet.PetsPlayer;
import org.bukkit.Bukkit;

import java.util.*;

public class PetsManager {
    private static final Set<PetsPlayer> players = new HashSet<>();

    public Set<PetsPlayer> getPlayers() { return players; }

    public PetsPlayer getPlayer(UUID uuid) {
        for (PetsPlayer player : players) {
            if (player.getUUID() == uuid) {
                return player;
            }
        }

        PetsPlayer player = new PetsPlayer(uuid, null);
        players.add(player);

        return player;
    }

    public void addPlayer(PetsPlayer player) {
        UUID uuid = player.getUUID();
        for (PetsPlayer p : players) {
            if (p.getUUID() == uuid) {
                players.remove(p);
                break;
            }
        }
        players.add(player);
    }

    public void removePlayer(UUID uuid) {
        Iterator<PetsPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            PetsPlayer p = iterator.next();

            if (p.getUUID() == uuid) {
                List<Pet> pets = p.getPets();
                for (Pet pet : pets) {
                    if (pet.isPet()) {
                        PlayerChangePetEvent event = new PlayerChangePetEvent(uuid, pet, ChangePetType.DEACTIVATED);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                }
                iterator.remove();
            }
        }
    }

    public static void removePlayers() {
        Iterator<PetsPlayer> iterator = players.iterator();
        while (iterator.hasNext()) {
            PetsPlayer p = iterator.next();
            UUID uuid = p.getUUID();

            List<Pet> pets = p.getPets();
            for (Pet pet : pets) {
                if (pet.isPet()) {
                    PlayerChangePetEvent event = new PlayerChangePetEvent(uuid, pet, ChangePetType.DEACTIVATED);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
            iterator.remove();
        }
    }
}
