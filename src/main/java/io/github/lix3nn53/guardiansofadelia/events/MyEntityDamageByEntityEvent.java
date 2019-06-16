package io.github.lix3nn53.guardiansofadelia.events;

import io.github.lix3nn53.guardiansofadelia.bossbar.HealthBar;
import io.github.lix3nn53.guardiansofadelia.bossbar.HealthBarManager;
import io.github.lix3nn53.guardiansofadelia.creatures.drops.DropManager;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetExperienceManager;
import io.github.lix3nn53.guardiansofadelia.creatures.pets.PetManager;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianData;
import io.github.lix3nn53.guardiansofadelia.guardian.GuardianDataManager;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacter;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGCharacterStats;
import io.github.lix3nn53.guardiansofadelia.guardian.character.RPGClass;
import io.github.lix3nn53.guardiansofadelia.jobs.GatheringType;
import io.github.lix3nn53.guardiansofadelia.minigames.MiniGameManager;
import io.github.lix3nn53.guardiansofadelia.party.PartyManager;
import io.github.lix3nn53.guardiansofadelia.quests.Quest;
import io.github.lix3nn53.guardiansofadelia.utilities.PersistentDataContainerUtil;
import io.github.lix3nn53.guardiansofadelia.utilities.hologram.FakeIndicator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;
import java.util.UUID;

public class MyEntityDamageByEntityEvent implements Listener {

    private static int getExperience(Entity entity) {
        if (PersistentDataContainerUtil.hasInteger(entity, "experience")) {
            return PersistentDataContainerUtil.getInteger(entity, "experience");
        }
        return 0;
    }

    private static int getCustomDamage(Entity entity) {
        if (PersistentDataContainerUtil.hasInteger(entity, "customDamage")) {
            return PersistentDataContainerUtil.getInteger(entity, "customDamage");
        }
        return 0;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();

        if (target instanceof LivingEntity) {
            boolean isEventCanceled = false;
            boolean isAttackerPlayer = false;
            LivingEntity livingTarget = (LivingEntity) target;

            //DAMAGER
            if (damager.getType().equals(EntityType.PLAYER)) { //player is attacker
                Player player = (Player) damager;
                isEventCanceled = onPlayerAttackEntity(event, player, livingTarget, false, false, false);
                isAttackerPlayer = true;
            } else if (damager instanceof Projectile) { //projectile is attacker
                boolean isMagic = false;
                Projectile projectile = (Projectile) damager;
                if (PersistentDataContainerUtil.hasInteger(projectile, "rangedDamage")) {
                    int rangedDamage = PersistentDataContainerUtil.getInteger(projectile, "rangedDamage");
                    event.setDamage(rangedDamage);
                } else if (PersistentDataContainerUtil.hasInteger(projectile, "magicDamage")) {
                    int magicDamage = PersistentDataContainerUtil.getInteger(projectile, "magicDamage");
                    event.setDamage(magicDamage);
                    isMagic = true;
                }
                ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Player) {
                    Player player = (Player) shooter;
                    isEventCanceled = onPlayerAttackEntity(event, player, livingTarget, false, true, isMagic);
                    isAttackerPlayer = true;
                }
            } else if (damager instanceof LivingEntity) {
                if (damager instanceof Wolf) {
                    Wolf wolf = (Wolf) damager;
                    if (PetManager.isPet(wolf)) { //pet is attacker
                        Player owner = PetManager.getOwner(wolf);
                        isEventCanceled = onPlayerAttackEntity(event, owner, livingTarget, true, false, false);
                        isAttackerPlayer = true;
                    }
                }
            }

            //TARGET
            if (!isEventCanceled) {
                if (target.getType().equals(EntityType.PLAYER)) { //player is target
                    int customDamage = getCustomDamage(damager);
                    if (customDamage > 0) {
                        event.setDamage(customDamage);
                    }

                    Player playerTarget = (Player) target;

                    if (!isAttackerPlayer) { //we are managing this on onPlayerAttackEntity method if attacker is player
                        //custom defense formula if target is another player
                        UUID uniqueId = playerTarget.getUniqueId();
                        if (GuardianDataManager.hasGuardianData(uniqueId)) {
                            GuardianData guardianData = GuardianDataManager.getGuardianData(uniqueId);
                            if (guardianData.hasActiveCharacter()) {
                                double damage = event.getDamage();

                                RPGCharacter activeCharacter = guardianData.getActiveCharacter();

                                RPGCharacterStats targetRpgCharacterStats = activeCharacter.getRpgCharacterStats();
                                int totalDefense = targetRpgCharacterStats.getTotalDefense();
                                double reduction = (1 - (totalDefense / (totalDefense + 3000.0))); //damage reduction formula, if totalDefense equals second paramater reduction is %50

                                damage = damage * reduction;

                                event.setDamage(damage);
                            }
                        }
                    }

                    //manage target player's pet's target
                    if (damager instanceof LivingEntity) {
                        LivingEntity livingDamager = (LivingEntity) damager;
                        if (PetManager.hasActivePet(playerTarget)) {
                            LivingEntity activePet = PetManager.getActivePet(playerTarget);
                            if (activePet instanceof Wolf) {
                                Wolf wolf = (Wolf) activePet;
                                if (wolf.getTarget() == null) {
                                    wolf.setTarget(livingDamager);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param event
     * @param player
     * @param livingTarget
     * @param isPet        isAttackerPet else attacker is Player
     * @return isEventCanceled
     */
    private boolean onPlayerAttackEntity(EntityDamageByEntityEvent event, Player player, LivingEntity livingTarget, boolean isPet, boolean isRangedAttack, boolean isMagicAttack) {
        UUID uniqueId = player.getUniqueId();
        if (GuardianDataManager.hasGuardianData(uniqueId)) {
            GuardianData guardianData = GuardianDataManager.getGuardianData(uniqueId);
            if (guardianData.hasActiveCharacter()) {
                RPGCharacter activeCharacter = guardianData.getActiveCharacter();
                //on player attack to pet
                if (livingTarget.getType().equals(EntityType.WOLF) || livingTarget.getType().equals(EntityType.HORSE)) {
                    boolean pvp = livingTarget.getWorld().getPVP();
                    if (pvp) {
                        if (PetManager.isPet(livingTarget)) {
                            Player owner = PetManager.getOwner(livingTarget);
                            //attack own pet
                            if (owner.equals(player)) {
                                event.setCancelled(true);
                                return true;
                            } else {
                                //attack pet of party member
                                if (PartyManager.inParty(player)) {
                                    if (PartyManager.getParty(player).getMembers().contains(owner)) {
                                        event.setCancelled(true);
                                        return true;
                                    }
                                }
                            }
                        }
                    } else {
                        event.setCancelled(true);
                        return true;
                    }
                }

                if (!isPet) { //attacker is Pet so don't edit pet's target
                    //if player has active pet manage pet's target
                    if (PetManager.hasActivePet(player)) {
                        LivingEntity activePet = PetManager.getActivePet(player);
                        if (activePet instanceof Wolf) {
                            Wolf wolf = (Wolf) activePet;
                            if (wolf.getTarget() == null) {
                                wolf.setTarget(livingTarget);
                            }
                        }
                    }
                }

                //custom damage modifiers
                double damage = event.getDamage();

                RPGCharacterStats rpgCharacterStats = activeCharacter.getRpgCharacterStats();
                RPGClass rpgClass = activeCharacter.getRpgClass();
                if (isMagicAttack) { //ranged overrides magic so check magic first
                    damage += rpgCharacterStats.getTotalMagicDamage(player, rpgClass); //add to spell damage
                } else if (isRangedAttack) {
                    damage += rpgCharacterStats.getFire().getIncrement(); //add to projectile damage
                } else { //melee
                    ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
                    Material type = itemInMainHand.getType();

                    if (type.equals(Material.DIAMOND_SWORD) || type.equals(Material.DIAMOND_HOE) || type.equals(Material.DIAMOND_SHOVEL) || type.equals(Material.DIAMOND_AXE)
                            || type.equals(Material.DIAMOND_PICKAXE) || type.equals(Material.TRIDENT) || type.equals(Material.BOW) || type.equals(Material.CROSSBOW)) {
                        if (PersistentDataContainerUtil.hasInteger(itemInMainHand, "reqLevel")) {
                            int reqLevel = PersistentDataContainerUtil.getInteger(itemInMainHand, "reqLevel");
                            if (player.getLevel() < reqLevel) {
                                event.setCancelled(true);
                                player.sendMessage("Required level for this weapon is " + reqLevel);
                                return false;
                            }
                        }

                        if (PersistentDataContainerUtil.hasString(itemInMainHand, "reqClass")) {
                            String reqClassString = PersistentDataContainerUtil.getString(itemInMainHand, "reqClass");
                            RPGClass reqClass = RPGClass.valueOf(reqClassString);
                            if (!rpgClass.equals(reqClass)) {
                                event.setCancelled(true);
                                player.sendMessage("Required class for this weapon is " + reqClass.getClassString());
                                return false;
                            }
                        }

                        damage += rpgCharacterStats.getFire().getIncrement(); //add to weapon damage
                    }
                }

                //custom defense formula if target is another player
                if (livingTarget.getType().equals(EntityType.PLAYER)) {
                    Player playerTarget = (Player) livingTarget;
                    UUID targetUniqueId = playerTarget.getUniqueId();
                    if (GuardianDataManager.hasGuardianData(targetUniqueId)) {
                        GuardianData targetGuardianData = GuardianDataManager.getGuardianData(targetUniqueId);
                        if (targetGuardianData.hasActiveCharacter()) {

                            RPGCharacter targetActiveCharacter = targetGuardianData.getActiveCharacter();

                            RPGCharacterStats targetRpgCharacterStats = targetActiveCharacter.getRpgCharacterStats();
                            int totalDefense = targetRpgCharacterStats.getTotalDefense();
                            double reduction = (1 - (totalDefense / (totalDefense + 3000.0))); //damage reduction formula, if totalDefense equals second paramater reduction is %50

                            damage = damage * reduction;

                            event.setDamage(damage);
                        }
                    }
                }

                event.setDamage(damage);

                double finalDamage = event.getFinalDamage();

                //show bossbar
                HealthBar healthBar = new HealthBar(livingTarget, (int) (finalDamage + 0.5), isPet);
                HealthBarManager.showToPlayerFor10Seconds(player, healthBar);

                //progress deal damage tasks
                List<Quest> questList = activeCharacter.getQuestList();
                for (Quest quest : questList) {
                    quest.progressDealDamageTasks(player, livingTarget, (int) (finalDamage + 0.5));
                }
                PartyManager.progressDealDamageTasksOfOtherMembers(player, livingTarget, finalDamage);

                //on Kill
                if (finalDamage >= livingTarget.getHealth()) {

                    int experience = getExperience(livingTarget);
                    if (experience > 0) {
                        if (PartyManager.inParty(player)) {
                            PartyManager.shareExpOnMobKill(player, experience);
                        } else {
                            rpgCharacterStats.giveExp(experience);
                            PetExperienceManager.giveExperienceToActivePet(player, experience);
                        }
                    }

                    //progress kill tasks
                    for (Quest quest : questList) {
                        quest.progressKillTasks(player, livingTarget);
                    }
                    PartyManager.progressMobKillTasksOfOtherMembers(player, livingTarget);

                    if (MiniGameManager.isInMinigame(player)) {
                        if (livingTarget.getType().equals(EntityType.PLAYER)) {
                            MiniGameManager.onPlayerKill(player);
                        } else {
                            MiniGameManager.onMobKillDungeon(player, livingTarget);
                        }
                    }

                    if (livingTarget.getType().equals(EntityType.COW) || livingTarget.getType().equals(EntityType.SHEEP)) {
                        ItemStack itemStack = GatheringType.HUNTING.onHunt(player);
                        if (itemStack != null) {
                            Item item = livingTarget.getWorld().dropItemNaturally(livingTarget.getLocation().add(0, 0.5, 0), itemStack);
                            DropManager.setItem(item.getItemStack(), player);
                        }
                    }
                }
                DropManager.onPlayerDealDamageToMob(player, livingTarget, (int) (finalDamage + 0.5));

                //indicator
                String text = ChatColor.RED.toString() + (int) (finalDamage + 0.5) + " ➹";
                FakeIndicator.showPlayer(player, text, livingTarget.getLocation());
            }
        }
        return false;
    }
}
