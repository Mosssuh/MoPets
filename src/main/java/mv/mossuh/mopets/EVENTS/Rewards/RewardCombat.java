package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;


public class RewardCombat implements Listener {

    @EventHandler
    public void playerKillsReward(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        LivingEntity dead = event.getEntity();
        if (player != null) {

            int times = 1;
            EventType eventType = EventType.PLAYER_KILLS;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, dead);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledDrops()) { event.getDrops().clear(); }
        }
    }


    @EventHandler
    public void playerDieReward(EntityDeathEvent event) {
        LivingEntity killer = event.getEntity().getKiller();
        Entity deadEntity = event.getEntity();
        if (deadEntity instanceof Player) {
            Player player = (Player) deadEntity;

            int times = 1;
            EventType eventType = EventType.PLAYER_DIE;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, killer);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledDrops()) { event.getDrops().clear(); }
        }
    }

    @EventHandler
    public void playerAttackReward(EntityDamageByEntityEvent event) {
        Entity attackerEntity =  event.getDamager();
        Entity attackedEntity = event.getEntity();
        if (attackerEntity instanceof Player) {
            if (attackedEntity instanceof LivingEntity) {
                LivingEntity attacked = (LivingEntity) attackedEntity;
                Player player = (Player) attackerEntity;

                int times = 1;
                EventType eventType = EventType.PLAYER_ATTACK;
                MoArgs args = new MoArgs();
                RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, attacked);
                args.setRewardArgs(rewardArgs);

                RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
                executor.execute();
                if (executor.isCancelledEvent()) { event.setCancelled(true); }

            }
        }
    }

    @EventHandler
    public void playerAttackedReward(EntityDamageByEntityEvent event) {
        Entity attackerEntity =  event.getDamager();
        Entity attackedEntity = event.getEntity();
        if (attackedEntity instanceof Player) {
            if (attackerEntity instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) attackerEntity;
                Player player = (Player) attackedEntity;

                int times = 1;
                EventType eventType = EventType.PLAYER_ATTACKED;
                MoArgs args = new MoArgs();
                RewardArgs rewardArgs = new RewardArgs(RewardArgsType.LIVING_ENTITY, attacker);
                args.setRewardArgs(rewardArgs);

                RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
                executor.execute();
                if (executor.isCancelledEvent()) { event.setCancelled(true); }
            }
        }
    }
}
