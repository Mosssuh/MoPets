package mv.mossuh.mopets;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.VERSION.ServerVersion;
import mv.mossuh.mopets.COMMANDS.Commands;
import mv.mossuh.mopets.COMMANDS.TabCompleter;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.MANAGERS.ConfigsManager;
import mv.mossuh.mopets.CONFIGS.Messages;
import mv.mossuh.mopets.ENUMS.ExpType;
import mv.mossuh.mopets.EVENTS.Leveling.LevelingBreaking;
import mv.mossuh.mopets.EVENTS.Leveling.LevelingFishing;
import mv.mossuh.mopets.EVENTS.Leveling.LevelingKilling;
import mv.mossuh.mopets.EVENTS.Leveling.LevelingPlacing;
import mv.mossuh.mopets.EVENTS.Rewards.*;
import mv.mossuh.mopets.EVENTS.Security.PlaceItem;
import mv.mossuh.mopets.EVENTS.Detector.*;
import mv.mossuh.mopets.EVENTS.Detector.Pickup.DetectorWithEntityPickup;
import mv.mossuh.mopets.EVENTS.Detector.Pickup.DetectorWithPlayerPickup;
import mv.mossuh.mopets.UTILITIES.PluginChecker;
import mv.mossuh.mopets.UTILITIES.UtilString;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class MoPets extends JavaPlugin {

    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();
    private static ConfigsManager configsManager;
    private static MoPets instance;

    @Override
    public void onEnable() {
        instance = this;
        configsManager = new ConfigsManager(this);
        configsManager.configure();
        Messages.load();

        if (Config.UPGRADES) { registerLevelings(); }
        if (Config.ACTIONS) { registerRewards(); }
        registerCommands();
        registerDetectors();
        registerOthers();

        UtilString.get("&8[" + Config.PREFIX + "&8] &aHas been enabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UtilString.get("&8[" + Config.PREFIX + "&8] &aVersion: " + version).hex().sendMessageInConsole();
    }

    @Override
    public void onDisable() {
        DetectorWithRepetitive.cancel();

        UtilString.get("&8[" + Config.PREFIX + "&8] &aHas been disabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UtilString.get("&8[" + Config.PREFIX + "&8] &aVersion: " + version).hex().sendMessageInConsole();
    }

    public static MoPets getInstance() {
        return instance;
    }
    public static ConfigsManager getConfigs() {
        return configsManager;
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("mopets")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("mopets")).setTabCompleter(new TabCompleter());
    }

    private void registerDetectors() {
        DetectorWithRepetitive.start(this);
        getServer().getPluginManager().registerEvents(new DetectorWithJoinQuit(), this);
        getServer().getPluginManager().registerEvents(new DetectorWithInventory(), this);
        getServer().getPluginManager().registerEvents(new DetectorWithItemEvents(), this);
        getServer().getPluginManager().registerEvents(new DetectorWithRespawn(), this);

        if (ServerVersion.isAtLeast(ServerVersion.MC1_13)) {
            getServer().getPluginManager().registerEvents(new DetectorWithEntityPickup(), this);
        } else {
            getServer().getPluginManager().registerEvents(new DetectorWithPlayerPickup(), this);
        }
    }

    private void registerLevelings() {
        Map<ExpType, Listener> listeners = new HashMap<>();
        listeners.put(ExpType.BLOCK_BREAK, new LevelingBreaking());
        listeners.put(ExpType.BLOCK_PLACE, new LevelingPlacing());
        listeners.put(ExpType.PLAYER_KILLS, new LevelingKilling());
        listeners.put(ExpType.PLAYER_FISH, new LevelingFishing());

        Set<ExpType> expTypes = Config.EXP_EVENTS;
        for (ExpType expType : expTypes) {
            Listener listener = listeners.get(expType);
            if (listener != null) {
                getServer().getPluginManager().registerEvents(listener, this);
            }
        }

        if (expTypes.isEmpty()) {
            listeners.values().forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
        }
    }

    private void registerRewards() {
        Map<Set<EventType>, Listener> rewardListeners = new HashMap<>();

        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.PLAYER_BED_ENTER, EventType.PLAYER_BED_LEAVE)), new RewardBed());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.BLOCK_BREAK, EventType.BLOCK_PLACE)), new RewardBlocks());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.PLAYER_COMMAND, EventType.PLAYER_CHAT)), new RewardChat());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.PLAYER_KILLS, EventType.PLAYER_DIE, EventType.PLAYER_ATTACK, EventType.PLAYER_ATTACKED)), new RewardCombat());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.PLAYER_EQUIP_PIECE, EventType.PLAYER_UNEQUIP_PIECE)), new RewardEquipUnEquipArmor());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.PLAYER_CAUGHT_FISH, EventType.PLAYER_CAUGHT_ENTITY)), new RewardFishing());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.BLOCK_INTERACT, EventType.ITEM_INTERACT, EventType.ENTITY_INTERACT)), new RewardInteract());
        rewardListeners.put(new HashSet<>(Arrays.asList(
                EventType.ITEM_CONSUME, EventType.ITEM_BREAK, EventType.ITEM_PICKUP, EventType.ITEM_DROP,
                EventType.ITEM_HELD, EventType.ITEM_UNHELD, EventType.ITEM_SELECT, EventType.ITEM_UNSELECT,
                EventType.ITEM_ENCHANT, EventType.ITEM_CRAFT
        )), new RewardItem());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.PLAYER_JOIN, EventType.PLAYER_LEAVE)), new RewardJoinLeave());
        rewardListeners.put(new HashSet<>(Collections.singletonList(EventType.PLAYER_LEVELUP)), new RewardLevelUp());
        rewardListeners.put(new HashSet<>(Collections.singletonList(EventType.PLAYER_RESPAWN)), new RewardRespawn());
        rewardListeners.put(new HashSet<>(Arrays.asList(EventType.PLAYER_FLY, EventType.PLAYER_UNFLY, EventType.PLAYER_SNEAK, EventType.PLAYER_UNSNEAK,
                EventType.PLAYER_SPRINT, EventType.PLAYER_UNSPRINT)), new RewardToggle());
        rewardListeners.put(new HashSet<>(Collections.singletonList(EventType.PLAYER_CHANGE_WORLD)), new RewardWorld());

        Set<EventType> eventTypes = Config.ACTIONS_EVENTS;
        Set<Listener> registeredListeners = new HashSet<>();

        for (Map.Entry<Set<EventType>, Listener> entry : rewardListeners.entrySet()) {
            if (entry.getKey().stream().anyMatch(eventTypes::contains)) {
                registeredListeners.add(entry.getValue());
            }
        }

        registeredListeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        if (eventTypes.isEmpty()) {
            rewardListeners.values().forEach(listener -> {
                if (!registeredListeners.contains(listener)) {
                    getServer().getPluginManager().registerEvents(listener, this);
                }
            });
        }
    }

    private void registerOthers() {
        getServer().getPluginManager().registerEvents(new PlaceItem(), this);
        getServer().getPluginManager().registerEvents(new PluginChecker(), this);
    }
}
