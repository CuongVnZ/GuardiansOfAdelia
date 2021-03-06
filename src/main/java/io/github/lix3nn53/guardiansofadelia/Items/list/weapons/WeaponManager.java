package io.github.lix3nn53.guardiansofadelia.Items.list.weapons;

import io.github.lix3nn53.guardiansofadelia.Items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeaponManager {

    private final static HashMap<Integer, List<WeaponSet>> gearLevelToWeapons = new HashMap<>();

    public static ItemStack get(WeaponGearType gearType, int gearLevel, int itemIndex, ItemTier tier, String itemTag, int minStatValue,
                                int maxStatValue, int minNumberofStats) {
        List<WeaponSet> weaponSetList = gearLevelToWeapons.get(gearLevel);

        AttackSpeed attackSpeed = gearType.getAttackSpeed();

        WeaponSet template = weaponSetList.get(itemIndex);

        Material material = gearType.getMaterial();
        String name = template.getName(gearType);
        int customModelData = template.getCustomModelData();
        int level = template.getRequiredLevel();
        int mainDamage = template.getDamage(gearType);

        WeaponDamageType weaponDamageType = gearType.getWeaponType();

        if (weaponDamageType.equals(WeaponDamageType.MELEE)) {
            return new WeaponMelee(name, tier, itemTag, material, customModelData, level, gearType, mainDamage,
                    attackSpeed, minStatValue, maxStatValue, minNumberofStats).getItemStack();
        } else if (weaponDamageType.equals(WeaponDamageType.RANGED)) {
            return new WeaponRanged(name, tier, itemTag, material, customModelData, level, gearType, mainDamage,
                    attackSpeed, minStatValue, maxStatValue, minNumberofStats).getItemStack();
        } else if (weaponDamageType.equals(WeaponDamageType.MAGICAL)) {
            int meleeDamage = mainDamage / 4;

            return new WeaponMagical(name, tier, itemTag, material, customModelData, level, gearType, meleeDamage, mainDamage,
                    attackSpeed, minStatValue, maxStatValue, minNumberofStats).getItemStack();
        }

        return null;
    }

    public static void add(WeaponSet weaponSet) {
        int gearLevel = GearLevel.getGearLevel(weaponSet.getRequiredLevel());

        List<WeaponSet> list = new ArrayList<>();
        if (gearLevelToWeapons.containsKey(gearLevel)) {
            list = gearLevelToWeapons.get(gearLevel);
        }
        list.add(weaponSet);

        gearLevelToWeapons.put(gearLevel, list);
    }
}
