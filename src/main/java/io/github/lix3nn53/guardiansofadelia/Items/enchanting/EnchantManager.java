package io.github.lix3nn53.guardiansofadelia.Items.enchanting;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class EnchantManager {

    private static HashMap<Player, Enchant> activeEnchants = new HashMap<Player, Enchant>();

    public static int getEnchantLevel(ItemStack itemStack) {
        ItemMeta im = itemStack.getItemMeta();
        if (im.getDisplayName().contains("+1")) {
            return 1;
        } else if (im.getDisplayName().contains("+2")) {
            return 2;
        } else if (im.getDisplayName().contains("+3")) {
            return 3;
        } else if (im.getDisplayName().contains("+4")) {
            return 4;
        } else if (im.getDisplayName().contains("+5")) {
            return 5;
        } else if (im.getDisplayName().contains("+6")) {
            return 6;
        } else if (im.getDisplayName().contains("+7")) {
            return 7;
        } else if (im.getDisplayName().contains("+8")) {
            return 8;
        } else if (im.getDisplayName().contains("+9")) {
            return 9;
        } else if (im.getDisplayName().contains("+10")) {
            return 10;
        } else if (im.getDisplayName().contains("+11")) {
            return 11;
        } else if (im.getDisplayName().contains("+12")) {
            return 12;
        }
        return 0;
    }

    public static int getRequiredEnchantStoneLevel(int currentEnchantLevel) {
        if (currentEnchantLevel < 3) {
            return 1;
        } else if (currentEnchantLevel < 6) {
            return 2;
        } else if (currentEnchantLevel < 9) {
            return 3;
        }
        return 4;
    }


}
