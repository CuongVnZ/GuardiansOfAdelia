package io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.MechanicComponent;
import io.github.lix3nn53.guardiansofadelia.utilities.particle.Direction;
import io.github.lix3nn53.guardiansofadelia.utilities.particle.ParticleArrangement;
import io.github.lix3nn53.guardiansofadelia.utilities.particle.ParticleUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class ParticleAnimationMechanic extends MechanicComponent {

    private final Particle particle;
    private final ParticleArrangement arrangement;
    private final double radiusParticle;
    private final int amountParticle;

    private final double dx;
    private final double dy;
    private final double dz;

    private final double forward;
    private final double upward;
    private final double right;

    private final double speed;

    //animation
    private final long frequency;
    private final List<Integer> repeatAmount;

    Particle.DustOptions dustOptions;

    public ParticleAnimationMechanic(Particle particle, ParticleArrangement arrangement, double radiusParticle,
                                     int amountParticle, double dx, double dy, double dz, double forward, double upward,
                                     double right, double speed, long frequency, List<Integer> repeatAmount, Particle.DustOptions dustOptions) {
        this.particle = particle;
        this.arrangement = arrangement;
        this.radiusParticle = radiusParticle;
        this.amountParticle = amountParticle;
        this.forward = forward;
        this.upward = upward;
        this.right = right;
        this.speed = speed;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.frequency = frequency;
        this.repeatAmount = repeatAmount;
        this.dustOptions = dustOptions;
    }

    @Override
    public boolean execute(LivingEntity caster, int skillLevel, List<LivingEntity> targets, int castCounter) {
        if (targets.isEmpty()) return false;

        for (LivingEntity ent : targets) {
            new BukkitRunnable() {

                int counter;

                @Override
                public void run() {
                    Location location = ent.getLocation();

                    Vector dir = location.getDirection().setY(0).normalize();
                    Vector side = dir.clone().crossProduct(UP);
                    location.add(dir.multiply(forward)).add(0, upward, 0).add(side.multiply(right));

                    ParticleUtil.play(location, particle, arrangement, radiusParticle, amountParticle, Direction.XZ, dx, dy, dz, speed, dustOptions);

                    counter++;
                    if (counter >= repeatAmount.get(skillLevel - 1)) {
                        cancel();
                    }
                }
            }.runTaskTimer(GuardiansOfAdelia.getInstance(), 0L, frequency);
        }

        return true;
    }

    @Override
    public List<String> getSkillLoreAdditions(List<String> additions, int skillLevel) {
        return getSkillLoreAdditionsOfChildren(additions, skillLevel);
    }
}
