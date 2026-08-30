package mv.mossuh.mopets.DATA.Config;

import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.TimeFormat;
import mv.mossuh.mopets.UTILITIES.Enums.ExpType;
import mv.mossuh.mopets.MoPets;
import mv.mossuh.mopets.UTILITIES.UtilMethods;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    private static final Map<String, String> intervalStringMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> intervalLongMap = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> intervalBooleanMap = new ConcurrentHashMap<>();
    private static List<Conflict> conflictList = new ArrayList<>();
    private static Set<ExpType> expTypes = new HashSet<>();
    private static Set<EventType> eventTypes = new HashSet<>();

    private static final List<String> intervalsList = new ArrayList<>(Arrays.asList(
            "Config.time-format.year", "Config.time-format.month",
            "Config.time-format.week", "Config.time-format.day",
            "Config.time-format.hour", "Config.time-format.minute",
            "Config.time-format.second",
            "Config.update-item-info-interval", "Config.save-in-database-interval",
            "Config.check-items-interval", "Config.actions", "Config.upgrades",
            "Config.conflicts", "Config.multiple-pets",
            "Config.exp-events", "Config.actions-events"
    ));

    public static String TIME_FORMAT_YEAR;
    public static String TIME_FORMAT_MONTH;
    public static String TIME_FORMAT_WEEK;
    public static String TIME_FORMAT_DAY;
    public static String TIME_FORMAT_HOUR;
    public static String TIME_FORMAT_MINUTE;
    public static String TIME_FORMAT_SECOND;
    public static TimeFormat TIME_FORMAT;
    public static long UPDATE_ITEM_INFO_INTERVAL;
    public static long SAVE_IN_DATABASE_INTERVAL;
    public static long CHECK_ITEMS_INTERVAL;
    public static boolean ACTIONS;
    public static boolean UPGRADES;
    public static List<Conflict> CONFLICT_ITEMS;
    public static boolean HAS_CONFLICTED_ITEMS;
    public static boolean MULTIPLE_PETS;
    public static String PREFIX = "&bMoPets";
    public static String PLUGIN_NAME = "MoPets";
    public static String BOOSTER_IDENTIFIER = PLUGIN_NAME;
    public static Set<ExpType> EXP_EVENTS = new HashSet<>();
    public static Set<EventType> ACTIONS_EVENTS = new HashSet<>();

    private static void loadConfig() {
        TIME_FORMAT_YEAR = new Config("Config.time-format.year").getOrDefault("");
        TIME_FORMAT_MONTH = new Config("Config.time-format.month").getOrDefault("");
        TIME_FORMAT_WEEK = new Config("Config.time-format.week").getOrDefault("");
        TIME_FORMAT_DAY = new Config("Config.time-format.day").getOrDefault("");
        TIME_FORMAT_HOUR = new Config("Config.time-format.hour").getOrDefault("");
        TIME_FORMAT_MINUTE = new Config("Config.time-format.minute").getOrDefault("");
        TIME_FORMAT_SECOND = new Config("Config.time-format.second").getOrDefault("");
        TIME_FORMAT = new TimeFormat(TIME_FORMAT_YEAR, TIME_FORMAT_MONTH, TIME_FORMAT_WEEK, TIME_FORMAT_DAY, TIME_FORMAT_HOUR, TIME_FORMAT_MINUTE, TIME_FORMAT_SECOND);
        UPDATE_ITEM_INFO_INTERVAL = new Config("Config.update-item-info-interval").getOrDefault(5L);
        SAVE_IN_DATABASE_INTERVAL = new Config("Config.save-in-database-interval").getOrDefault(300L);
        CHECK_ITEMS_INTERVAL = new Config("Config.check-items-interval").getOrDefault(5L);
        ACTIONS = new Config("Config.actions").getOrDefault(true);
        UPGRADES = new Config("Config.upgrades").getOrDefault(true);
        CONFLICT_ITEMS = conflictList;
        HAS_CONFLICTED_ITEMS = !CONFLICT_ITEMS.isEmpty();
        MULTIPLE_PETS = new Config("Config.multiple-pets").getOrDefault(false);
        EXP_EVENTS = expTypes;
        ACTIONS_EVENTS = eventTypes;
    }

    private final String intervalPosition;

    public Config(String intervalPosition) {
        this.intervalPosition = intervalPosition;
    }

    public String getOrDefault(String defaultValue) {
        return intervalStringMap.getOrDefault(intervalPosition, defaultValue);
    }

    public long getOrDefault(long defaultValue) {
        if (intervalLongMap.containsKey(intervalPosition)) {
            return intervalLongMap.get(intervalPosition);
        }
        return defaultValue;
    }

    public boolean getOrDefault(boolean defaultValue) {
        return intervalBooleanMap.getOrDefault(intervalPosition, defaultValue);
    }

    public static void load() {
        FileConfiguration mainConfig = MoPets.getConfigs().getMainConfig().getConfig();

        for (String intervalConfig : intervalsList) {
            if (mainConfig.isString(intervalConfig)) {
                intervalStringMap.put(intervalConfig, mainConfig.getString(intervalConfig));
            } else if (mainConfig.isList(intervalConfig)) {
                switch (intervalConfig) {
                    case "Config.conflicts":
                        conflictList = Conflict.loadConflicts(mainConfig.getStringList(intervalConfig));
                        break;
                    case "Config.exp-events":
                        expTypes = UtilMethods.getExpTypeList(mainConfig.getStringList(intervalConfig));
                        break;
                    case "Config.actions-events":
                        eventTypes = UtilMethods.getEventTypeList(mainConfig.getStringList(intervalConfig));
                        break;
                }
            } else if (mainConfig.isBoolean(intervalConfig)) {
                intervalBooleanMap.put(intervalConfig, mainConfig.getBoolean(intervalConfig));
            } else if (mainConfig.isLong(intervalConfig) || mainConfig.isInt(intervalConfig)) {
                intervalLongMap.put(intervalConfig, mainConfig.getLong(intervalConfig));
            }
        }

        loadConfig();
    }
}
