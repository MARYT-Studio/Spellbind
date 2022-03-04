package world.maryt.spellbind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateRuleEntry {
    private static final Logger LOGGER = LogManager.getLogger();
    public static String[] createRuleEntry(JsonObject rule, int param_count) {
        String[] ruleEntry = new String[param_count];
        if (rule.has("item")
                && rule.has("distance")
                && rule.has("entity")) {
            ruleEntry[0] = rule.get("item").getAsString();
            ruleEntry[1] = rule.get("distance").getAsString();
            ruleEntry[2] = rule.get("entity").getAsString();

            // NBT Key criterion is optional.
            if (rule.has("nbt_to_check")) {
                ruleEntry[3] = rule.get("nbt_to_check").getAsString();
            }
            else {ruleEntry[3] = "#default_value#";}

            if (rule.has("actions")) {
                try {
                    JsonArray allActions = rule.get("actions").getAsJsonArray();
                    for (JsonElement actionElement : allActions) {
                        JsonObject action = (JsonObject) actionElement;
                        if (action.has("action_type")) {
                            ruleEntry[4] = action.get("action_type").getAsString();
                            // Until we add new criterion type, the 5th String is the last necessary String.
                            // From the 6th String, it can be an empty one.
                            switch (ruleEntry[4]) {
                                case "potion":
                                    if (action.has("potion_id") && action.has("duration") && action.has("amplifier")) {
                                        // Form an action entry
                                        ruleEntry[5] = action.get("potion_id").getAsString();
                                        ruleEntry[6] = action.get("duration").getAsString();
                                        ruleEntry[7] = action.get("amplifier").getAsString();
                                        return ruleEntry;
                                    } else {
                                        break;
                                    }
                                case "exec_command":
                                    if (action.has("command")) {
                                        // Form an action entry
                                        ruleEntry[5] = action.get("command").getAsString();
                                        return ruleEntry;
                                    } else {
                                        break;
                                    }
                                case "nbt":
                                    if (action.has("nbt_string")) {
                                        // Form an action entry
                                        ruleEntry[5] = action.get("nbt_string").getAsString();
                                        return ruleEntry;
                                    } else {
                                        break;
                                    }
                                case "attribute":
                                    if (action.has("namespace")
                                            && action.has("attribute_id")
                                            && action.has("operation")
                                            && action.has("amount")
                                            && action.has("stackable")) {
                                        // Form an action entry
                                        ruleEntry[5] = action.get("namespace").getAsString();
                                        ruleEntry[6] = action.get("attribute_id").getAsString();
                                        ruleEntry[7] = action.get("operation").getAsString();
                                        ruleEntry[8] = action.get("amount").getAsString();
                                        ruleEntry[9] = action.get("stackable").getAsString();
                                        return ruleEntry;
                                    } else {
                                        break;
                                    }
                                default:
                                    LOGGER.error("None of action types match.");
                                    return null;
                            }
                        }
                    }
                } catch (IllegalArgumentException | JsonParseException e) {
                    LOGGER.error("Parsing error loading actions. Message: {}", e.getMessage());
                    return null;
                }
            }
        }
        LOGGER.error("Invalid rule");
        return null;
    }
}
