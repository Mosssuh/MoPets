package mv.mossuh.mopets.MANAGERS;

import mv.mossuh.mopets.PETS.Pet.PetsPlayer;

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
        for (PetsPlayer p : players) {
            if (p.getUUID() == uuid) {
                players.remove(p);
                break;
            }
        }
    }
}
