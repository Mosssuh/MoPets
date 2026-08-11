package mv.mossuh.mopets.API.Events;

import mv.mossuh.mopets.ENUMS.ChangePetType;
import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerChangePetEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private UUID uuid = null;
    private Pet pet = new Pet(null, null, null, null, null, null, null);
    private ChangePetType changePetType = ChangePetType.NONE;

    public PlayerChangePetEvent(UUID uuid, Pet pet, ChangePetType changePetType) {
        this.uuid = uuid;
        if (pet != null) { this.pet = pet; }
        if (changePetType != null) { this.changePetType = changePetType; }
    }

    public UUID getUUID() { return uuid; }
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
