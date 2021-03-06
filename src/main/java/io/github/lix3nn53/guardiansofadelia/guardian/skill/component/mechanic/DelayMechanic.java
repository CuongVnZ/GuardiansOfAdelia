package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.MechanicComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DelayMechanic extends MechanicComponent {

    private final long ticks;

    public DelayMechanic(long ticks) {
        this.ticks = ticks;
    }

    public DelayMechanic(ConfigurationSection configurationSection) {
        if (!configurationSection.contains("ticks")) {
            configLoadError("ticks");
        }

        this.ticks = configurationSection.getLong("ticks");
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter) {
        if (targets.isEmpty()) return false;

        new BukkitRunnable() {

            @Override
            public void run() {
                if (!caster.isDead()) {
                    executeChildren(caster, skillLevel, targets, castCounter);
                }
            }
        }.runTaskLater(GuardiansOfAdelia.getInstance(), ticks);

        return true;
    }

    @Override
    public List<String> getSkillLoreAdditions(List<String> additions, int skillLevel) {
        return getSkillLoreAdditionsOfChildren(additions, skillLevel);
    }
}
