package mv.mossuh.mopets.EVENTS.Rewards;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.EVENTS.ItemSelectEvent.ItemSelectEvent;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.RewardArgs.RewardArgsType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mopets.UTILITIES.MoArgs;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RewardItem implements Listener {
    @EventHandler
    public void playerItemConsumeReward(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemConsume = event.getItem();

        if (itemConsume.getType() != Material.AIR) {
            EventType eventType = EventType.ITEM_CONSUME;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemConsume);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }

    @EventHandler
    public void playerItemBreakReward(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemBroken = event.getBrokenItem();

        if (itemBroken.getType() != Material.AIR) {
            EventType eventType = EventType.ITEM_BREAK;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemBroken);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
        }
    }

    @EventHandler
    public void playerItemPickupReward(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemPicked = event.getItem().getItemStack();

        if (itemPicked.getType() != Material.AIR) {
            EventType eventType = EventType.ITEM_PICKUP;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemPicked);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }

    @EventHandler
    public void playerItemDropReward(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemDropped = event.getItemDrop().getItemStack();

        if (itemDropped.getType() != Material.AIR) {
            EventType eventType = EventType.ITEM_DROP;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemDropped);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }

    @EventHandler
    public void playerItemHeldReward(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack itemHeld = event.getPlayer().getInventory().getItem(event.getNewSlot());

        if (itemHeld != null && itemHeld.getType() != Material.AIR) {
            EventType eventType = EventType.ITEM_HELD;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemHeld);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }

    @EventHandler
    public void playerItemUnHeldReward(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack itemUnHeld = event.getPlayer().getInventory().getItem(event.getPreviousSlot());

        if (itemUnHeld != null && itemUnHeld.getType() != Material.AIR) {
            EventType eventType = EventType.ITEM_UNHELD;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemUnHeld);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }

    @EventHandler
    public void playerItemSelectUnSelectReward(ItemSelectEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack selectedItem = event.getSelectedItem();
        ItemStack unSelectedItem = event.getUnSelectedItem();

        if (selectedItem != null && !selectedItem.getType().equals(Material.AIR)) {
            EventType eventType = EventType.ITEM_SELECT;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, selectedItem);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }

        if (unSelectedItem != null && !unSelectedItem.getType().equals(Material.AIR)) {
            EventType eventType = EventType.ITEM_UNSELECT;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, unSelectedItem);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }


    @EventHandler
    public void playerItemEnchantReward(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        UUID uuid = player.getUniqueId();
        ItemStack itemStack = event.getItem();
        int expCost = event.getExpLevelCost();

        if (!itemStack.getType().equals(Material.AIR)) {
            List<VariableArg> variables = new ArrayList<>();
            variables.add(new VariableArg("%exp_cost%", expCost+""));

            EventType eventType = EventType.ITEM_ENCHANT;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, itemStack);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, variables, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }


    @EventHandler
    public void playerItemCraftReward(CraftItemEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (humanEntity.getType().equals(EntityType.PLAYER)) {
            Player player = (Player) humanEntity;
            UUID uuid = player.getUniqueId();
            ItemStack craftItem = event.getCurrentItem();

            EventType eventType = EventType.ITEM_CRAFT;
            int times = 1;
            MoArgs args = new MoArgs();
            RewardArgs rewardArgs = new RewardArgs(RewardArgsType.ITEMSTACK, craftItem);
            args.setRewardArgs(rewardArgs);

            RewardExecutor executor = new RewardExecutor(player, event, eventType, args, null, times);
            executor.execute();
            if (executor.isCancelledEvent()) { event.setCancelled(true); }
        }
    }
}
