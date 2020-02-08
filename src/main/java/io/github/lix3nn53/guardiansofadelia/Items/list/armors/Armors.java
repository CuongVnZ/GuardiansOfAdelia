package io.github.lix3nn53.guardiansofadelia.Items.list.armors;

import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.GearArmor;
import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.ItemTier;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.List;

public class Armors {

    private final static HashMap<String, List<ArmorItemTemplate>> rpgClassToArmorTemplate = new HashMap<>();

    public static ItemStack getArmor(ArmorType armorType, RPGClass rpgClass, int placementNumber, ItemTier tier, String itemTag, int minStatValue,
                                     int maxStatValue, int minNumberOfStats) {
        ArmorItemTemplate template = rpgClassToArmorTemplate.get(rpgClass.toString() + armorType.toString()).get(placementNumber - 1);

        String name = template.getName();
        Material material = template.getMaterial();
        int level = template.getLevel();
        int health = template.getHealth();
        int defense = template.getDefense();
        int magicDefense = template.getMagicDefense();

        final GearArmor gearArmor = new GearArmor(name, tier, itemTag, material, level,
                rpgClass, health,
                defense, magicDefense, minStatValue, maxStatValue, minNumberOfStats);
        if (material.toString().contains("LEATHER_") && !rpgClass.equals(RPGClass.NO_CLASS)) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) gearArmor.getItemStack().getItemMeta();
            if (rpgClass.equals(RPGClass.ARCHER)) {
                leatherArmorMeta.setColor(Color.fromRGB(0, 188, 0));
            } else if (rpgClass.equals(RPGClass.KNIGHT)) {
                leatherArmorMeta.setColor(Color.fromRGB(0, 184, 230));
            } else if (rpgClass.equals(RPGClass.MAGE)) {
                leatherArmorMeta.setColor(Color.fromRGB(153, 0, 115));
            } else if (rpgClass.equals(RPGClass.ROGUE)) {
                leatherArmorMeta.setColor(Color.fromRGB(0, 0, 26));
            } else if (rpgClass.equals(RPGClass.PALADIN)) {
                leatherArmorMeta.setColor(Color.fromRGB(230, 230, 0));
            } else if (rpgClass.equals(RPGClass.WARRIOR)) {
                leatherArmorMeta.setColor(Color.fromRGB(180, 0, 0));
            } else if (rpgClass.equals(RPGClass.MONK)) {
                leatherArmorMeta.setColor(Color.fromRGB(230, 140, 0));
            } else if (rpgClass.equals(RPGClass.HUNTER)) {
                leatherArmorMeta.setColor(Color.fromRGB(35, 140, 35));
            }
            gearArmor.getItemStack().setItemMeta(leatherArmorMeta);
        }

        return gearArmor.getItemStack();
    }

    public static void put(String key, List<ArmorItemTemplate> armorItemTemplates) {
        rpgClassToArmorTemplate.put(key, armorItemTemplates);
    }
}
