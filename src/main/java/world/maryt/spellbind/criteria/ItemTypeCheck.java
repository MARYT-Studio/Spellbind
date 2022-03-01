package world.maryt.spellbind.criteria;

import net.minecraft.item.ItemStack;

public class ItemTypeCheck {
    public static boolean itemTypeCheck(ItemStack item, String itemID) {
        return item.getItem().getRegistryName().toString().equals(itemID) ? true :false;
    }
}
