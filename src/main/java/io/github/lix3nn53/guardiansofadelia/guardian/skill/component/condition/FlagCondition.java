package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.condition;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.SkillDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.ConditionComponent;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class FlagCondition extends ConditionComponent {

    private final String key;
    private final boolean isSet;
    private final boolean isUnique;

    /**
     * @param key
     * @param isSet check to true or false
     */
    public FlagCondition(String key, boolean isSet, boolean isUnique) {
        this.key = key;
        this.isSet = isSet;
        this.isUnique = isUnique;
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter) {
        if (targets.isEmpty()) return false;

        boolean success = false;
        for (LivingEntity target : targets) {
            boolean hasFlag;

            if (isUnique) {
                hasFlag = SkillDataManager.hasFlag(target, key + castCounter);
            } else {
                hasFlag = SkillDataManager.hasFlag(target, key);
            }

            if (hasFlag == isSet) {
                success = executeChildren(caster, skillLevel, targets, castCounter) || success;
            }
        }

        return success;
    }

    @Override
    public List<String> getSkillLoreAdditions(List<String> additions, int skillLevel) {
        return getSkillLoreAdditionsOfChildren(additions, skillLevel);
    }
}
