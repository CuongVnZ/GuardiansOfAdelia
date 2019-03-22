package io.github.lix3nn53.guardiansofadelia.Items.RpgGears;

import org.bukkit.inventory.ItemStack;

public interface RPGGear {

    int getItemID();

    ItemStack getItemStack();

    ItemTier getTier();

    String getItemTag();

    int getLevel();
}
