package io.github.lix3nn53.guardiansofadelia.Items.RpgGears;

import io.github.lix3nn53.guardiansofadelia.utilities.NBTTagUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PetCompanion implements RPGGear {

    private final int itemID;
    private final ItemTier tier;
    private final String itemTag;
    private final int level;
    private ItemStack itemStack;

    public PetCompanion(String name, String petCode, ItemTier tier, String itemTag, Material material, int durability, int level,
                        int damage, int health, double speed, double jump, int itemID) {
        name = tier.getTierColor() + itemTag + " " + name + ChatColor.GOLD + " LvL 1";

        List<String> lore = new ArrayList<>();

        lore.add("");
        lore.add(ChatColor.YELLOW + "Type: " + ChatColor.GRAY + "Companion");
        lore.add(ChatColor.DARK_PURPLE + "Required Level: " + ChatColor.GRAY + level);
        lore.add(ChatColor.YELLOW + "----------------");
        lore.add(ChatColor.GOLD + "Companion Level: " + ChatColor.GRAY + "1");
        lore.add(ChatColor.LIGHT_PURPLE + "Companion Experience: " + ChatColor.GRAY + "0 / 20");
        lore.add(tier.getTierString());
        lore.add(ChatColor.YELLOW + "----------------");
        lore.add(ChatColor.RED + "➹ Companion Damage: " + ChatColor.GRAY + damage);
        lore.add(ChatColor.DARK_GREEN + "❤ Companion Health: " + ChatColor.GRAY + health);
        lore.add(ChatColor.AQUA + "⇨ Companion Speed: " + ChatColor.GRAY + speed);
        lore.add(ChatColor.YELLOW + "⇪ Companion Jump: " + ChatColor.GRAY + jump);
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "#" + itemID);

        this.itemStack = new ItemStack(material);
        this.itemStack = NBTTagUtils.putInteger("reqLevel", level, this.itemStack);
        this.itemStack = NBTTagUtils.putString("petCode", petCode, this.itemStack);
        this.itemStack = NBTTagUtils.putInteger("petLevel", 1, this.itemStack);
        this.itemStack = NBTTagUtils.putInteger("petExp", 1, this.itemStack);
        this.itemStack = NBTTagUtils.putInteger("petCurrentHealth", health, this.itemStack);

        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        if (itemMeta instanceof Damageable) {
            Damageable damageable = (Damageable) itemMeta;
            damageable.setDamage(durability);
        }
        this.itemStack.setItemMeta(itemMeta);

        this.itemID = itemID;
        this.tier = tier;
        this.itemTag = itemTag;
        this.level = level;
    }

    @Override
    public int getItemID() {
        return itemID;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ItemTier getTier() {
        return tier;
    }

    @Override
    public String getItemTag() {
        return itemTag;
    }

    @Override
    public int getLevel() {
        return level;
    }

}
