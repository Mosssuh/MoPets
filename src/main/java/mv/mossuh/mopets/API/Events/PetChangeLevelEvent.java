package mv.mossuh.mopets.API.Events;

import mv.mossuh.mopets.ENUMS.ExecuteType;
import mv.mossuh.mopets.ENUMS.ReceiveType;
import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetChangeLevelEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Pet pet = new Pet(null, null, null, null, null, null, null);
    private int level = 0;
    private ExecuteType executeType = ExecuteType.NONE;
    private ReceiveType receiveType = ReceiveType.NONE;
    private boolean isCancelled;

    public PetChangeLevelEvent(Player player, Pet pet, ExecuteType executeType, ReceiveType receiveType, Integer level) {
        this.player = player;
        if (pet != null) { this.pet = pet; }
        if (executeType != null) { this.executeType = executeType; }
        if (receiveType != null) { this.receiveType = receiveType; }
        if (level != null) { this.level = level; }
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Pet getPet() {
        return pet;
    }

    public ExecuteType getExecuteType() {
        return executeType;
    }
    public ReceiveType getReceiveType() {
        return receiveType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int level) {
        this.level = this.level + level;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}