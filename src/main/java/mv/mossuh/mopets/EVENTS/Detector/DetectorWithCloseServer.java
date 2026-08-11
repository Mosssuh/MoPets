package mv.mossuh.mopets.EVENTS.Detector;

import mv.mossuh.mopets.MANAGERS.PetsManager;
import mv.mossuh.mopets.MoPets;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class DetectorWithCloseServer implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void byRespawning(PluginDisableEvent event) {
        if (!event.getPlugin().equals(MoPets.getInstance())) return;

        PetsManager.removePlayers();
    }
}
