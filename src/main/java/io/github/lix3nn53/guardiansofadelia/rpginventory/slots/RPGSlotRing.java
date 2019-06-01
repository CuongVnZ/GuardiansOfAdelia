package io.github.lix3nn53.guardiansofadelia.rpginventory.slots;

import io.github.lix3nn53.guardiansofadelia.utilities.PersistentDataContainerUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class RPGSlotRing extends RPGSlotPassive implements RPGSlot {

    private final int passiveTypeNum = 1;

    public boolean doesFit(ItemStack itemStack) {
        if (PersistentDataContainerUtil.hasInteger(itemStack, "passive")) {
            Integer typeNum = PersistentDataContainerUtil.getInteger(itemStack, "passive");
            return typeNum == this.passiveTypeNum;
        }
        return false;
    }

    public ItemStack getFillItem() {
        ItemStack itemStack = new ItemStack(Material.IRON_AXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(ChatColor.YELLOW + "Ring Slot");
        itemMeta.setLore(new ArrayList() {{
            add("");
        }});
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        if (itemMeta instanceof Damageable) {
            Damageable damageable = (Damageable) itemMeta;
            damageable.setDamage(12);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
