package mv.mossuh.mopets.CONFIGS;

import mv.mossuh.mopets.MoPets;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Messages {

    private static List<String> messagesConfigList = new ArrayList<>(Arrays.asList("INVALID_CODE", "INVALID_AMOUNT", "INVALID_PLAYER",
            "INVALID_PET", "NO_PERMISSION", "COMMAND_GIVE_PET_SENDER", "COMMAND_GIVE_ALL_PET_SENDER", "COMMAND_GIVE_PET_RECEIVER",
            "COMMAND_PET_ADD_EXP_RECEIVER", "COMMAND_PET_ADD_EXP_SENDER", "COMMAND_PET_SET_EXP_RECEIVER", "COMMAND_PET_SET_EXP_SENDER",
            "COMMAND_PET_ADD_LEVEL_RECEIVER", "COMMAND_PET_ADD_LEVEL_SENDER", "COMMAND_PET_SET_LEVEL_RECEIVER", "COMMAND_PET_SET_LEVEL_SENDER",
            "PET_LEVEL_UP"));
    private static Map<String, String> messagesMap = new ConcurrentHashMap<>();

    private String messagePosition;
    public Messages(String messagePosition) {
        this.messagePosition = messagePosition;
    }
    public String get() {
        return messagesMap.get(messagePosition);
    }

    public static String INVALID_CODE;
    public static String INVALID_AMOUNT;
    public static String INVALID_PLAYER;
    public static String INVALID_PET;
    public static String NO_PERMISSION;
    public static String COMMAND_GIVE_PET_SENDER;
    public static String COMMAND_GIVE_ALL_PET_SENDER;
    public static String COMMAND_GIVE_PET_RECEIVER;
    public static String COMMAND_PET_ADD_EXP_RECEIVER;
    public static String COMMAND_PET_ADD_EXP_SENDER;
    public static String COMMAND_PET_SET_EXP_RECEIVER;
    public static String COMMAND_PET_SET_EXP_SENDER;
    public static String COMMAND_PET_ADD_LEVEL_RECEIVER;
    public static String COMMAND_PET_ADD_LEVEL_SENDER;
    public static String COMMAND_PET_SET_LEVEL_RECEIVER;
    public static String COMMAND_PET_SET_LEVEL_SENDER;
    public static String PET_LEVEL_UP;


    public static void load() {
        FileConfiguration messagesConfig = MoPets.getConfigs().getMessagesConfig().getConfig();

        for (String messageConfig : messagesConfigList) {
            String message = messagesConfig.getString(messageConfig);
            if (message != null) {
                messagesMap.put(messageConfig, message);
                continue;
            }
            messagesMap.put(messageConfig, "");
        }

        loadMessages();
    }

    private static void loadMessages() {
        INVALID_CODE = new Messages("INVALID_CODE").get();
        INVALID_AMOUNT = new Messages("INVALID_AMOUNT").get();
        INVALID_PLAYER = new Messages("INVALID_PLAYER").get();
        INVALID_PET = new Messages("INVALID_PET").get();
        NO_PERMISSION = new Messages("NO_PERMISSION").get();
        COMMAND_GIVE_PET_SENDER = new Messages("COMMAND_GIVE_PET_SENDER").get();
        COMMAND_GIVE_ALL_PET_SENDER = new Messages("COMMAND_GIVE_ALL_PET_SENDER").get();
        COMMAND_GIVE_PET_RECEIVER = new Messages("COMMAND_GIVE_PET_RECEIVER").get();
        COMMAND_PET_ADD_EXP_RECEIVER = new Messages("COMMAND_PET_ADD_EXP_RECEIVER").get();
        COMMAND_PET_ADD_EXP_SENDER = new Messages("COMMAND_PET_ADD_EXP_SENDER").get();
        COMMAND_PET_SET_EXP_RECEIVER = new Messages("COMMAND_PET_SET_EXP_RECEIVER").get();
        COMMAND_PET_SET_EXP_SENDER = new Messages("COMMAND_PET_SET_EXP_SENDER").get();
        COMMAND_PET_ADD_LEVEL_RECEIVER = new Messages("COMMAND_PET_ADD_LEVEL_RECEIVER").get();
        COMMAND_PET_ADD_LEVEL_SENDER = new Messages("COMMAND_PET_ADD_LEVEL_SENDER").get();
        COMMAND_PET_SET_LEVEL_RECEIVER = new Messages("COMMAND_PET_SET_LEVEL_RECEIVER").get();
        COMMAND_PET_SET_LEVEL_SENDER = new Messages("COMMAND_PET_SET_LEVEL_SENDER").get();
        PET_LEVEL_UP = new Messages("PET_LEVEL_UP").get();
    }
}
