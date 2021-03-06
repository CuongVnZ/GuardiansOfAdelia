package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.MechanicComponent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FlagSetMechanic extends MechanicComponent {

    private final String key;
    private final List<Integer> ticks;
    private final boolean isUniqueCast;

    public FlagSetMechanic(String key, List<Integer> ticks, boolean isUniqueCast) {
        this.key = key;
        this.ticks = ticks;
        this.isUniqueCast = isUniqueCast;
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter) {
        if (targets.isEmpty()) return false;

        for (LivingEntity target : targets) {
            if (isUniqueCast) {
                SkillDataManager.addFlag(target, key + castCounter);
            } else {
                SkillDataManager.addFlag(target, key);
            }
        }

        if (!ticks.isEmpty()) {
            new BukkitRunnable() { //remove buffs from buffed players

                @Override
                public void run() {
                    for (LivingEntity target : targets) {
                        if (isUniqueCast) {
                            SkillDataManager.removeFlag(target, key + castCounter);
                        } else {
                            SkillDataManager.removeFlag(target, key);
                        }
                    }
                }
            }.runTaskLaterAsynchronously(GuardiansOfAdelia.getInstance(), ticks.get(skillLevel - 1));
        }

        return true;
    }

    @Override
    public List<String> getSkillLoreAdditions(List<String> additions, int skillLevel) {
        return getSkillLoreAdditionsOfChildren(additions, skillLevel);
    }
}
