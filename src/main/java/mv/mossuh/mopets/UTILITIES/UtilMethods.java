package mv.mossuh.mopets.UTILITIES;

import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.EventType;
import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.mocore.VERSION.ServerVersion;
import mv.mossuh.mopets.ENUMS.ChangePetType;
import mv.mossuh.mopets.API.Events.PlayerChangePetEvent;
import mv.mossuh.mopets.API.PetsAPI;
import mv.mossuh.mopets.CONFIGS.Config.Config;
import mv.mossuh.mopets.CONFIGS.Config.Conflict;
import mv.mossuh.mopets.ENUMS.ConflictType;
import mv.mossuh.mopets.ENUMS.MultiplierType;
import mv.mossuh.mopets.ENUMS.ExpType;
import mv.mossuh.mopets.CONFIGS.Pets.PetIdentifier;
import mv.mossuh.mopets.PETS.Pet.Pet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UtilMethods {

    public static List<String> separateString(String utilString) {
        if (utilString != null) {
            String[] utilStrings = utilString.replaceAll(" ", "").split(",");
            return new ArrayList<>(Arrays.asList(utilStrings));
        }
        return new ArrayList<>();
    }

    public static double transformChance(ChanceType chanceType, double chance, int level) {
        double chanceFormat = chance * 100;
        if (chanceType.equals(ChanceType.CHANCE_PER_LEVEL)) {
            chanceFormat = (chance * level) * 100;
        }
        return 10000 - chanceFormat;
    }

    public static EventType getEventType(String eventTypeString) {
        EventType eventType = EventType.NONE;

        if (eventTypeString != null) {
            for (EventType et : EventType.values()) {
                if (et.name().equalsIgnoreCase(eventTypeString)) {
                    return et;
                }
            }
            return EventType.INVALID;
        }
        return eventType;
    }

    public static Set<EventType> getEventTypeList(List<String> eventTypesString) {
        Set<EventType> eventTypes = new HashSet<>();

        if (eventTypesString != null) {
            for (String eventTypeString : eventTypesString) {
                EventType eventType = getEventType(eventTypeString);
                if (!eventType.equals(EventType.NONE) && !eventType.equals(EventType.INVALID)) {
                    eventTypes.add(eventType);
                }
            }
        }
        return eventTypes;
    }

    public static ExpType getExpType(String expTypeString) {
        ExpType expType = ExpType.NONE;

        if (expTypeString != null) {
            for (ExpType et : ExpType.values()) {
                if (et.name().equalsIgnoreCase(expTypeString)) {
                    return et;
                }
            }
            return ExpType.NONE;
        }
        return expType;
    }

    public static Set<ExpType> getExpTypeList(List<String> expTypesString) {
        Set<ExpType> expTypes = new HashSet<>();

        if (expTypesString != null) {
            for (String expTypeString : expTypesString) {
                ExpType expType = getExpType(expTypeString);
                if (!expType.equals(ExpType.NONE)) {
                    expTypes.add(expType);
                }
            }
        }
        return expTypes;
    }

    public static MultiplierType getMultiplierType(String multiplierTypeString) {
        MultiplierType multiplierType = MultiplierType.BASE;
        if (multiplierTypeString != null) {
            switch (multiplierTypeString.toLowerCase()) {
                case "base":
                case "default":
                    return MultiplierType.BASE;
                case "boost_per_level":
                case "multiplier_per_level":
                    return MultiplierType.BOOST_PER_LEVEL;
            }
        }
        return multiplierType;
    }

    public static StringAmount separateStringAmount(String string) {
        String[] itemStringSplit = string.split("\\[", 2);
        String code = itemStringSplit[0];
        double amountString = 1;
        if (itemStringSplit.length == 2) {
            if (UsefulMethods.isNumeric(itemStringSplit[1])) {
                amountString = Double.parseDouble(itemStringSplit[1]);
            }
        }

        return new StringAmount(code, amountString);
    }


    public static List<Pet> cleanConflicts(UUID uuid, List<Pet> pets) {
        List<Conflict> conflictList = Config.CONFLICT_ITEMS;
        Map<Conflict, Integer> conflictCounter = new ConcurrentHashMap<>();
        List<Pet> filteredPets = new ArrayList<>();
        List<Pet> active = PetsAPI.getManager().getPlayer(uuid).getPets();

        Set<Pet> uniquePets = new HashSet<>();
        for (Pet pet : pets) {
            boolean duplicate = false;
            for (Pet existingPet : uniquePets) {
                if (existingPet.getConfigPet().getPetIdentifier().getCode().equalsIgnoreCase(pet.getConfigPet().getPetIdentifier().getCode())) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                uniquePets.add(pet);
            }
        }

        if (Config.HAS_CONFLICTED_ITEMS) {
            for (Pet pet : uniquePets) {
                String isRegisteredItem = "no";

                secondFor:
                for (Conflict conflict : conflictList) {
                    PetIdentifier itemIdentifier = pet.getConfigPet().getPetIdentifier();
                    ConflictType conflictType = conflict.getConflictType();
                    List<String> conflictedStrings = conflict.getConflicts();

                    if (conflictType.equals(ConflictType.CODE)) {
                        String code = itemIdentifier.getCode();
                        if (conflict.hasConflict(code)) {
                            int limit = conflict.getLimit();
                            int conflictSaved = conflictCounter.computeIfAbsent(conflict, k -> 0);
                            if (conflictSaved < limit) {
                                filteredPets.add(pet);
                                conflictCounter.put(conflict, conflictSaved + 1);
                            }
                            isRegisteredItem = "yes";
                            break;
                        }

                    } else if (conflictType.equals(ConflictType.TAGS)) {
                        for (String conflicted : conflictedStrings) {
                            if (itemIdentifier.hasTag(conflicted)) {
                                int limit = conflict.getLimit();
                                int conflictSaved = conflictCounter.computeIfAbsent(conflict, k -> 0);
                                if (conflictSaved < limit) {
                                    filteredPets.add(pet);
                                    conflictCounter.put(conflict, conflictSaved + 1);
                                }
                                isRegisteredItem = "yes";
                                break secondFor;
                            }
                        }
                    }
                }

                if (isRegisteredItem.equals("no")) {
                    filteredPets.add(pet);
                }
            }
        }

        List<UUID> activePetsUUIDs = active.stream().map(Pet::getPetUUID).collect(Collectors.toList());
        List<UUID> filteredPetUUIDs = filteredPets.stream().map(Pet::getPetUUID).collect(Collectors.toList());

        // This is when a new pet is activated
        for (Pet pet : filteredPets) {
            if (!activePetsUUIDs.contains(pet.getPetUUID())) {
                PlayerChangePetEvent event = new PlayerChangePetEvent(uuid, pet, ChangePetType.ACTIVATED);
                Bukkit.getPluginManager().callEvent(event);
            }
        }

        // This is when a pet was active, execute the event that now is inactive
        for (Pet pet : active) {
            if (!filteredPetUUIDs.contains(pet.getPetUUID())) {
                PlayerChangePetEvent event = new PlayerChangePetEvent(uuid, pet, ChangePetType.DEACTIVATED);
                Bukkit.getPluginManager().callEvent(event);
            }
        }

        return filteredPets;
    }


    public static ItemStack getItemInHand(Player player) {
        try {
            if (ServerVersion.isAtLeast(ServerVersion.MC1_9)) {
                return player.getInventory().getItemInMainHand();
            } else {
                return player.getInventory().getItemInHand();
            }
        } catch (Exception e) {
            return new ItemStack(Material.AIR);
        }
    }

    public static String progressPercentage(double exp, double cost) {
        Locale locale = new Locale("en", "US");

        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

        double percentage = (exp / cost) * 100;
        return numberFormat.format(percentage).replace("$", "");
    }
}
