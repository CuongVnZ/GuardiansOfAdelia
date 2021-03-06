package io.github.lix3nn53.guardiansofadelia.Items.list.passiveItems;

import io.github.lix3nn53.guardiansofadelia.Items.GearLevel;
import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.GearPassive;
import io.github.lix3nn53.guardiansofadelia.Items.RpgGears.ItemTier;
import io.github.lix3nn53.guardiansofadelia.rpginventory.slots.RPGSlotType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PassiveManager {

    private final static HashMap<Integer, List<PassiveSet>> gearLevelToPassives = new HashMap<>();

    public static ItemStack get(int gearLevel, int itemIndex, RPGSlotType rpgSlotType, ItemTier tier, String itemTag, int minStatValue,
                                int maxStatValue, int minNumberOfStats) {
        List<PassiveSet> templates = gearLevelToPassives.get(gearLevel);

        PassiveSet template = templates.get(itemIndex);

        String name = template.getName(rpgSlotType);
        int level = template.getLevel(rpgSlotType);
        int customModelData = template.getCustomModelData(rpgSlotType);

        final GearPassive passive = new GearPassive(name, tier, itemTag, customModelData, rpgSlotType, level, minStatValue,
                maxStatValue, minNumberOfStats);

        return passive.getItemStack();
    }

    public static void add(PassiveSet passiveSet) {
        int gearLevel = GearLevel.getGearLevel(passiveSet.getBaseLevel());

        List<PassiveSet> list = new ArrayList<>();
        if (gearLevelToPassives.containsKey(gearLevel)) {
            list = gearLevelToPassives.get(gearLevel);
        }

        list.add(passiveSet);
        gearLevelToPassives.put(gearLevel, list);
    }
}
