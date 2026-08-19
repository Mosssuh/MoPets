package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class RewardBed implements Listener {
    @EventHandler
    public void bedEnterReward(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        int times = 1;
        EventType eventType = EventType.PLAYER_BED_ENTER;

        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
        if (executor.isCancelledEvent()) { event.setCancelled(true); }
    }

    @EventHandler
    public void bedLeaveReward(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();

        int times = 1;
        EventType eventType = EventType.PLAYER_BED_LEAVE;

        RewardExecutor executor = new RewardExecutor(player, event, eventType, null, times);
        executor.execute();
    }
}
