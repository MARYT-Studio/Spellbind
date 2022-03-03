package world.maryt.spellbind.actions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Set;

public class ModifyAttributes {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void modifyAttributes(LivingEntity target, String namespace, String attributeID, String operation, double amount, boolean stackable) {
        if(target.world != null && !target.world.isRemote) { // Process in the client side
            AttributeModifier customModifier = createModifier(operation, amount); // get the modifier
            if(customModifier != null && idToAttribute(namespace, attributeID) != null) { // if attribute is valid
                ModifiableAttributeInstance modifiableattributeinstance = target.getAttribute(idToAttribute(namespace, attributeID));
                if(!stackable) {
                    Set<AttributeModifier> modifiers = modifiableattributeinstance.getModifierListCopy();
                    for (AttributeModifier oldAttributeModifier: modifiers) {
                        if(oldAttributeModifier.getOperation().getId() == customModifier.getOperation().getId()) {
                            modifiableattributeinstance.removeModifier(oldAttributeModifier);
                        }
                    }
                }
                modifiableattributeinstance.applyNonPersistentModifier(customModifier);
            }
        }
    }

    private static AttributeModifier createModifier(String operation, double amount) {
        if(operation.equals("multiply_base")) {
            return new AttributeModifier("Custom Spellbind Multiply-Base Modifier", amount, AttributeModifier.Operation.MULTIPLY_BASE);
        }
        else if(operation.equals("multiply_total")) {
            return new AttributeModifier("Custom Spellbind Multiply-Total Modifier", amount, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
        else if(operation.equals("add")) {
            return new AttributeModifier("Custom Spellbind Add Modifier", amount, AttributeModifier.Operation.ADDITION);
        }
        else {
            LOGGER.error("Invalid AttributeModifier operation type.");
            LOGGER.error("Valid operation types: multiply_base, multiply_total, add.");
            return null;
        }
    }

    private static Attribute idToAttribute(String namespace, String ID) {
        ResourceLocation rl = new ResourceLocation(namespace, ID);
        if (ForgeRegistries.ATTRIBUTES.containsKey(rl)) {
            return ForgeRegistries.ATTRIBUTES.getValue(rl);
        }
        LOGGER.error("Invalid Attribute ID.");
        LOGGER.error("Valid operation types: https://minecraft.fandom.com/wiki/Attribute");
        return null;
    }
}
