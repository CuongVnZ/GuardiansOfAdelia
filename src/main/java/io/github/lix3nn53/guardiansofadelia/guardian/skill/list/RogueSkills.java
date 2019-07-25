package io.github.lix3nn53.guardiansofadelia.guardian.skill.list;

import io.github.lix3nn53.guardiansofadelia.guardian.skill.Skill;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.condition.DirectionCondition;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.condition.FlagCondition;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.*;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.buff.BuffMechanic;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.buff.BuffType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.immunity.ImmunityMechanic;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.immunity.ImmunityRemoveMechanic;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.projectile.ProjectileMechanic;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.mechanic.projectile.SpreadType;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.target.SelfTarget;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.target.SingleTarget;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.InitializeTrigger;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.LandTrigger;
import io.github.lix3nn53.guardiansofadelia.guardian.skill.component.trigger.MeleeAttackTrigger;
import io.github.lix3nn53.guardiansofadelia.sounds.GoaSound;
import io.github.lix3nn53.guardiansofadelia.utilities.particle.ArrangementParticle;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Egg;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class RogueSkills {

    public static List<Skill> getSet() {
        List<Skill> skills = new ArrayList<>();

        skills.add(getOne());
        skills.add(getTwo());
        skills.add(getThree());
        skills.add(getPassive());
        skills.add(getUltimate());

        return skills;
    }

    private static Skill getOne() {
        List<String> description = new ArrayList<>();
        description.add(ChatColor.GRAY + "Jump towards your target and deal damage.");
        description.add(ChatColor.GRAY + "If you are behind your target also apply slow.");

        List<Integer> reqLevels = new ArrayList<>();
        reqLevels.add(1);
        reqLevels.add(8);
        reqLevels.add(16);
        reqLevels.add(24);
        reqLevels.add(32);
        reqLevels.add(48);

        List<Integer> reqPoints = new ArrayList<>();
        reqPoints.add(1);
        reqPoints.add(1);
        reqPoints.add(1);
        reqPoints.add(2);
        reqPoints.add(2);
        reqPoints.add(2);

        List<Integer> manaCosts = new ArrayList<>();
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);

        List<Integer> cooldowns = new ArrayList<>();
        cooldowns.add(8);
        cooldowns.add(8);
        cooldowns.add(8);
        cooldowns.add(8);
        cooldowns.add(8);
        cooldowns.add(8);

        Skill skill = new Skill("Claw Swipe", 6, Material.IRON_HOE, 32, description, reqLevels, reqPoints, manaCosts, cooldowns);

        SelfTarget selfTarget = new SelfTarget();

        FlagCondition phantomCondition = new FlagCondition("phantom", false);

        FlagCondition phantomCondition2 = new FlagCondition("phantom", true);

        List<Double> ranges = new ArrayList<>();
        ranges.add(2D);
        ranges.add(2.5D);
        ranges.add(3D);
        ranges.add(3.5D);
        ranges.add(4D);
        ranges.add(5D);
        SingleTarget singleTarget = new SingleTarget(false, true, false, 1, ranges, 4);

        List<Double> ranges2 = new ArrayList<>();
        ranges2.add(4D);
        ranges2.add(4.5D);
        ranges2.add(5D);
        ranges2.add(5.5D);
        ranges2.add(6D);
        ranges2.add(8D);
        SingleTarget singleTarget2 = new SingleTarget(false, true, false, 1, ranges2, 4);

        DirectionCondition directionCondition = new DirectionCondition(false);

        List<Integer> ticks = new ArrayList<>();
        ticks.add(40);
        ticks.add(50);
        ticks.add(60);
        ticks.add(70);
        ticks.add(80);
        ticks.add(100);
        List<Integer> amplifiers = new ArrayList<>();
        amplifiers.add(2);
        amplifiers.add(3);
        amplifiers.add(4);
        amplifiers.add(5);
        amplifiers.add(6);
        amplifiers.add(7);
        PotionEffectMechanic slow = new PotionEffectMechanic(PotionEffectType.SLOW, ticks, amplifiers);

        WarpTargetMechanic warpTargetMechanic = new WarpTargetMechanic(false);

        List<Double> damages = new ArrayList<>();
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        DamageMechanic damageMechanic = new DamageMechanic(damages, DamageMechanic.DamageType.MELEE);

        skill.addTrigger(selfTarget);

        selfTarget.addChildren(phantomCondition);
        phantomCondition.addChildren(singleTarget);

        selfTarget.addChildren(phantomCondition2);
        phantomCondition2.addChildren(singleTarget2);

        singleTarget.addChildren(directionCondition);
        directionCondition.addChildren(slow);

        singleTarget.addChildren(warpTargetMechanic);
        singleTarget.addChildren(damageMechanic);
        singleTarget.addChildren(new SoundMechanic(GoaSound.SKILL_POISON_SLASH));
        ParticleMechanic particleMechanic = new ParticleMechanic(Particle.SWEEP_ATTACK, ArrangementParticle.CIRCLE, 1.2, 7, 0, 0, 0, 0, 0.8, 0, 0, null);
        singleTarget.addChildren(particleMechanic);

        return skill;
    }

    private static Skill getTwo() {
        List<String> description = new ArrayList<>();
        description.add(ChatColor.GRAY + "Dash forward and gain");
        description.add(ChatColor.GRAY + "resistance to fall damage");

        List<Integer> reqLevels = new ArrayList<>();
        reqLevels.add(5);
        reqLevels.add(12);
        reqLevels.add(22);
        reqLevels.add(35);
        reqLevels.add(48);
        reqLevels.add(55);

        List<Integer> reqPoints = new ArrayList<>();
        reqPoints.add(1);
        reqPoints.add(2);
        reqPoints.add(2);
        reqPoints.add(3);
        reqPoints.add(3);
        reqPoints.add(3);

        List<Integer> manaCosts = new ArrayList<>();
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);

        List<Integer> cooldowns = new ArrayList<>();
        cooldowns.add(16);
        cooldowns.add(16);
        cooldowns.add(16);
        cooldowns.add(16);
        cooldowns.add(16);
        cooldowns.add(16);

        Skill skill = new Skill("Void Dash", 6, Material.IRON_HOE, 10, description, reqLevels, reqPoints, manaCosts, cooldowns);

        SelfTarget selfTarget = new SelfTarget();

        FlagCondition phantomCondition = new FlagCondition("phantom", false);

        List<Double> forwards = new ArrayList<>();
        forwards.add(1.4);
        forwards.add(1.6);
        forwards.add(1.8);
        forwards.add(2D);
        forwards.add(2.2);
        forwards.add(2.6);
        List<Double> upwards = new ArrayList<>();
        upwards.add(1.7);
        upwards.add(1.75);
        upwards.add(1.8);
        upwards.add(1.85);
        upwards.add(1.9);
        upwards.add(2D);
        List<Double> right = new ArrayList<>();
        right.add(0D);
        right.add(0D);
        right.add(0D);
        right.add(0D);
        right.add(0D);
        right.add(0D);
        WarpMechanic warpMechanic = new WarpMechanic(false, forwards, upwards, right);

        FlagCondition phantomCondition2 = new FlagCondition("phantom", true);

        List<Double> forwards2 = new ArrayList<>();
        forwards2.add(1.6);
        forwards2.add(1.8);
        forwards2.add(2D);
        forwards2.add(2.2);
        forwards2.add(2.6);
        forwards2.add(3D);
        WarpMechanic warpMechanic2 = new WarpMechanic(false, forwards2, upwards, right);

        LandTrigger landTrigger = new LandTrigger();

        ImmunityMechanic immunityMechanic = new ImmunityMechanic(EntityDamageEvent.DamageCause.FALL, new ArrayList<>());//0 for infinite
        ImmunityRemoveMechanic immunityRemoveMechanic = new ImmunityRemoveMechanic(EntityDamageEvent.DamageCause.FALL);

        skill.addTrigger(selfTarget);

        ParticleMechanic particleMechanic = new ParticleMechanic(Particle.REDSTONE, ArrangementParticle.CIRCLE, 1.2, 4, 0, 0, 0, 0, 0.5, 0, 0, new Particle.DustOptions(Color.PURPLE, 2));
        selfTarget.addChildren(particleMechanic);

        selfTarget.addChildren(phantomCondition);
        phantomCondition.addChildren(warpMechanic);

        selfTarget.addChildren(phantomCondition2);
        phantomCondition2.addChildren(warpMechanic2);

        selfTarget.addChildren(immunityMechanic);
        selfTarget.addChildren(landTrigger);
        selfTarget.addChildren(new SoundMechanic(GoaSound.SKILL_PROJECTILE_VOID));

        selfTarget.addChildren(particleMechanic);

        landTrigger.addChildren(immunityRemoveMechanic);

        return skill;
    }

    private static Skill getThree() {
        List<String> description = new ArrayList<>();
        description.add(ChatColor.GRAY + "Throw shurikens one by one");

        List<Integer> reqLevels = new ArrayList<>();
        reqLevels.add(10);
        reqLevels.add(18);
        reqLevels.add(26);
        reqLevels.add(38);
        reqLevels.add(50);
        reqLevels.add(64);

        List<Integer> reqPoints = new ArrayList<>();
        reqPoints.add(1);
        reqPoints.add(2);
        reqPoints.add(2);
        reqPoints.add(3);
        reqPoints.add(3);
        reqPoints.add(3);

        List<Integer> manaCosts = new ArrayList<>();
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);

        List<Integer> cooldowns = new ArrayList<>();
        cooldowns.add(24);
        cooldowns.add(24);
        cooldowns.add(24);
        cooldowns.add(24);
        cooldowns.add(24);
        cooldowns.add(24);

        Skill skill = new Skill("Shurikens", 6, Material.IRON_HOE, 56, description, reqLevels, reqPoints, manaCosts, cooldowns);

        List<Integer> repetitions = new ArrayList<>();
        repetitions.add(2);
        repetitions.add(3);
        repetitions.add(4);
        repetitions.add(5);
        repetitions.add(6);
        repetitions.add(7);
        RepeatMechanic repeatMechanic = new RepeatMechanic(10L, repetitions);

        List<Integer> projectileAmounts = new ArrayList<>();
        projectileAmounts.add(1);
        projectileAmounts.add(1);
        projectileAmounts.add(1);
        projectileAmounts.add(1);
        projectileAmounts.add(1);
        projectileAmounts.add(1);
        ProjectileMechanic projectileMechanic = new ProjectileMechanic(SpreadType.CONE, 2.7, projectileAmounts, 30, 0, 1, 0, 90,
                true, Egg.class, Particle.REDSTONE, ArrangementParticle.CIRCLE, 0.5, 4, new Particle.DustOptions(Color.PURPLE, 2), false);

        FlagCondition phantomCondition = new FlagCondition("phantom", true);

        List<Integer> ticks = new ArrayList<>();
        ticks.add(60);
        ticks.add(60);
        ticks.add(60);
        ticks.add(60);
        ticks.add(60);
        ticks.add(60);
        List<Integer> amplifiers = new ArrayList<>();
        amplifiers.add(2);
        amplifiers.add(3);
        amplifiers.add(4);
        amplifiers.add(5);
        amplifiers.add(6);
        amplifiers.add(7);
        PotionEffectMechanic slow = new PotionEffectMechanic(PotionEffectType.SLOW, ticks, amplifiers);

        SelfTarget selfTarget = new SelfTarget();
        skill.addTrigger(selfTarget);
        selfTarget.addChildren(repeatMechanic);
        repeatMechanic.addChildren(projectileMechanic);
        selfTarget.addChildren(new SoundMechanic(GoaSound.SKILL_THROW_SMALL_OBJECT));

        List<Double> damages = new ArrayList<>();
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        DamageMechanic damageMechanic = new DamageMechanic(damages, DamageMechanic.DamageType.RANGED);

        projectileMechanic.addChildren(damageMechanic);
        projectileMechanic.addChildren(phantomCondition);
        phantomCondition.addChildren(slow);

        return skill;
    }

    private static Skill getPassive() {
        List<String> description = new ArrayList<>();
        description.add(ChatColor.GRAY + "Deal bonus damage when you hit");
        description.add(ChatColor.GRAY + "targets from behind with melee attacks");

        List<Integer> reqLevels = new ArrayList<>();
        reqLevels.add(20);
        reqLevels.add(30);
        reqLevels.add(40);
        reqLevels.add(50);
        reqLevels.add(60);
        reqLevels.add(80);

        List<Integer> reqPoints = new ArrayList<>();
        reqPoints.add(2);
        reqPoints.add(4);
        reqPoints.add(4);
        reqPoints.add(6);
        reqPoints.add(6);
        reqPoints.add(6);

        List<Integer> manaCosts = new ArrayList<>();
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);

        List<Integer> cooldowns = new ArrayList<>();
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);

        Skill skill = new Skill("Backstab", 6, Material.IRON_HOE, 17, description, reqLevels, reqPoints, manaCosts, cooldowns);

        InitializeTrigger initializeTrigger = new InitializeTrigger();

        MeleeAttackTrigger meleeAttackTrigger = new MeleeAttackTrigger(cooldowns);

        DirectionCondition directionCondition = new DirectionCondition(false);

        skill.addTrigger(initializeTrigger);
        initializeTrigger.addChildren(meleeAttackTrigger);
        meleeAttackTrigger.addChildren(directionCondition);

        List<Double> damages = new ArrayList<>();
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        damages.add(3D);
        DamageMechanic damageMechanic = new DamageMechanic(damages, DamageMechanic.DamageType.MELEE);

        meleeAttackTrigger.addChildren(damageMechanic);
        ParticleMechanic particleMechanic = new ParticleMechanic(Particle.CRIT, ArrangementParticle.CIRCLE, 0.8, 4, 0, 0, 0, 0, 0.5, 0, 0, null);
        meleeAttackTrigger.addChildren(particleMechanic);

        return skill;
    }

    private static Skill getUltimate() {
        List<String> description = new ArrayList<>();
        description.add(ChatColor.GRAY + "Become a phantom assassin:");
        description.add(ChatColor.GRAY + "- Increase your critical chance");
        description.add(ChatColor.GRAY + "(This can exceed the critical chance cap)");
        description.add(ChatColor.GRAY + "- Increase your critical damage");
        description.add(ChatColor.GRAY + "- Increase range of Claw Swipe");
        description.add(ChatColor.GRAY + "- Increase range of Void Dash");
        description.add(ChatColor.GRAY + "- Your shurikens slows the target");

        List<Integer> reqLevels = new ArrayList<>();
        reqLevels.add(40);
        reqLevels.add(50);
        reqLevels.add(60);
        reqLevels.add(70);
        reqLevels.add(80);
        reqLevels.add(90);

        List<Integer> reqPoints = new ArrayList<>();
        reqPoints.add(4);
        reqPoints.add(5);
        reqPoints.add(6);
        reqPoints.add(7);
        reqPoints.add(8);
        reqPoints.add(9);

        List<Integer> manaCosts = new ArrayList<>();
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);
        manaCosts.add(5);

        List<Integer> cooldowns = new ArrayList<>();
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);
        cooldowns.add(64);

        Skill skill = new Skill("Phantom Assassin", 6, Material.IRON_HOE, 45, description, reqLevels, reqPoints, manaCosts, cooldowns);

        SelfTarget selfTarget = new SelfTarget();

        List<Integer> ticks = new ArrayList<>();
        ticks.add(180);
        ticks.add(200);
        ticks.add(220);
        ticks.add(240);
        ticks.add(260);
        ticks.add(300);
        List<Double> damageMultipliers = new ArrayList<>();
        damageMultipliers.add(0.2);
        damageMultipliers.add(0.3);
        damageMultipliers.add(0.4);
        damageMultipliers.add(0.5);
        damageMultipliers.add(0.6);
        damageMultipliers.add(0.8);
        BuffMechanic critDamageBuff = new BuffMechanic(BuffType.CRIT_DAMAGE, damageMultipliers, ticks);
        List<Double> chanceMultipliers = new ArrayList<>();
        chanceMultipliers.add(0.05);
        chanceMultipliers.add(0.06);
        chanceMultipliers.add(0.07);
        chanceMultipliers.add(0.08);
        chanceMultipliers.add(0.1);
        chanceMultipliers.add(0.12);
        BuffMechanic critChanceBuff = new BuffMechanic(BuffType.CRIT_CHANCE, chanceMultipliers, ticks);
        FlagSetMechanic flagSetMechanic = new FlagSetMechanic("phantom", ticks);

        List<Integer> amplifiers = new ArrayList<>();
        amplifiers.add(1);
        amplifiers.add(1);
        amplifiers.add(1);
        amplifiers.add(1);
        amplifiers.add(1);
        amplifiers.add(1);
        PotionEffectMechanic invisibility = new PotionEffectMechanic(PotionEffectType.INVISIBILITY, ticks, amplifiers);
        PotionEffectMechanic glowing = new PotionEffectMechanic(PotionEffectType.GLOWING, ticks, amplifiers);

        skill.addTrigger(selfTarget);
        selfTarget.addChildren(critDamageBuff);
        selfTarget.addChildren(critChanceBuff);
        selfTarget.addChildren(flagSetMechanic);
        selfTarget.addChildren(invisibility);
        selfTarget.addChildren(glowing);

        List<Integer> repeatAmount = new ArrayList<>();
        repeatAmount.add(36);
        repeatAmount.add(40);
        repeatAmount.add(44);
        repeatAmount.add(48);
        repeatAmount.add(52);
        repeatAmount.add(60);
        ParticleAnimationMechanic particleAnimationMechanic = new ParticleAnimationMechanic(Particle.REDSTONE, ArrangementParticle.CIRCLE, 1, 4, 0, 0, 0,
                0, 0.5, 0, 0, 5, repeatAmount, new Particle.DustOptions(Color.PURPLE, 8));
        selfTarget.addChildren(particleAnimationMechanic);

        return skill;
    }
}
