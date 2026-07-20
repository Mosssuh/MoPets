package mv.mossuh.mopets.API.Events;

import mv.mossuh.mopets.ENUMS.ChangePetType;
import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerChangePetEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player = null;
    private Pet pet = new Pet(null, null, null, null, null, null, null);
    private ChangePetType changePetType = ChangePetType.NONE;

    public PlayerChangePetEvent(Player player, Pet pet, ChangePetType changePetType) {
        this.player = player;
        if (pet != null) { this.pet = pet; }
        if (changePetType != null) { this.changePetType = changePetType; }
    }

    public Player getPlayer() { return player; }
    public Pet getPet() { return pet; }
    public ChangePetType getChangePetType() { return changePetType; }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
