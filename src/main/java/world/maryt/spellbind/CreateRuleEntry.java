package world.maryt.spellbind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static world.maryt.spellbind.Spellbind.ALL_CUSTOM_RULES;

public class CreateRuleEntry {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void createRuleEntry(JsonObject rule, int param_count) {
        if (rule.has("item")
                && rule.has("distance")
                && rule.has("entity")) {
            String itemID = rule.get("item").getAsString();
            String distance = rule.get("distance").getAsString();
            String entityType = rule.get("entity").getAsString();

            // NBT Key criterion is optional.
            String nbtToCheck;
            if (rule.has("nbt_to_check")) {
                nbtToCheck = rule.get("nbt_to_check").getAsString();
            }
            else {nbtToCheck = "#default_value#";}

            if (rule.has("actions")) {
                try {
                    JsonArray allActions = rule.get("actions").getAsJsonArray();
                    for (JsonElement actionElement : allActions) {
                        JsonObject action = (JsonObject) actionElement;
                        if (action.has("action_type")) {
                            String actionType = action.get("action_type").getAsString();
                            // Until we add new criterion type, the 5th String is the last necessary String.
                            // From the 6th String, it can be an empty one.
                            switch (actionType) {
                                case "potion":
                                    if (action.has("potion_id") && action.has("duration") && action.has("amplifier")) {
                                        // Form an action entry
                                        String[] ruleEntry = new String[param_count];
                                        ruleEntry[0] = itemID;
                                        ruleEntry[1] = distance;
                                        ruleEntry[2] = entityType;
                                        ruleEntry[3] = nbtToCheck;
                                        ruleEntry[4] = actionType;
                                        ruleEntry[5] = action.get("potion_id").getAsString();
                                        ruleEntry[6] = action.get("duration").getAsString();
                                        ruleEntry[7] = action.get("amplifier").getAsString();
                                        ALL_CUSTOM_RULES.add(ruleEntry);
                                    }
                                    break;
                                case "exec_command":
                                    if (action.has("command")) {
                                        // Form an action entry
                                        String[] ruleEntry = new String[param_count];
                                        ruleEntry[0] = itemID;
                                        ruleEntry[1] = distance;
                                        ruleEntry[2] = entityType;
                                        ruleEntry[3] = nbtToCheck;
                                        ruleEntry[4] = actionType;
                                        ruleEntry[5] = action.get("command").getAsString();
                                        ALL_CUSTOM_RULES.add(ruleEntry);
                                    }
                                    break;
                                case "nbt":
                                    if (action.has("nbt_string")) {
                                        // Form an action entry
                                        String[] ruleEntry = new String[param_count];
                                        ruleEntry[0] = itemID;
                                        ruleEntry[1] = distance;
                                        ruleEntry[2] = entityType;
                                        ruleEntry[3] = nbtToCheck;
                                        ruleEntry[4] = actionType;
                                        ruleEntry[5] = action.get("nbt_string").getAsString();
                                        ALL_CUSTOM_RULES.add(ruleEntry);
                                    }
                                    break;
                                case "attribute":
                                    if (action.has("namespace")
                                            && action.has("attribute_id")
                                            && action.has("operation")
                                            && action.has("amount")
                                            && action.has("stackable")) {
                                        // Form an action entry
                                        String[] ruleEntry = new String[param_count];
                                        ruleEntry[0] = itemID;
                                        ruleEntry[1] = distance;
                                        ruleEntry[2] = entityType;
                                        ruleEntry[3] = nbtToCheck;
                                        ruleEntry[4] = actionType;
                                        ruleEntry[5] = action.get("namespace").getAsString();
                                        ruleEntry[6] = action.get("attribute_id").getAsString();
                                        ruleEntry[7] = action.get("operation").getAsString();
                                        ruleEntry[8] = action.get("amount").getAsString();
                                        ruleEntry[9] = action.get("stackable").getAsString();
                                        ALL_CUSTOM_RULES.add(ruleEntry);
                                    }
                                    break;
                                case "set_on_fire":
                                    if (action.has("seconds")) {
                                        // Form an action entry
                                        String[] ruleEntry = new String[param_count];
                                        ruleEntry[0] = itemID;
                                        ruleEntry[1] = distance;
                                        ruleEntry[2] = entityType;
                                        ruleEntry[3] = nbtToCheck;
                                        ruleEntry[4] = actionType;
                                        ruleEntry[5] = action.get("seconds").getAsString();
                                        ALL_CUSTOM_RULES.add(ruleEntry);
                                    }
                                    break;
                                default:
                                    LOGGER.error("None of action types match.");
                                    return;
                            }
                        }
                    }
                } catch (IllegalArgumentException | JsonParseException e) {
                    LOGGER.error("Parsing error loading actions. Message: {}", e.getMessage());
                    return;
                }
            }
        }
        LOGGER.info("Loading rules... " + ALL_CUSTOM_RULES.size() + " rules now.");
    }
}
