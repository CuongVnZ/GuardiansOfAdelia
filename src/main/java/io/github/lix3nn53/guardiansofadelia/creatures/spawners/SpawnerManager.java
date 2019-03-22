package io.github.lix3nn53.guardiansofadelia.creatures.spawners;

import io.github.lix3nn53.guardiansofadelia.GuardiansOfAdelia;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnerManager {
    private HashMap<Entity, Spawner> mobToSpawner = new HashMap<>();
    private HashMap<String, List<Spawner>> chunkKeyToSpawners = new HashMap<>();
    private List<Spawner> activeSpawners = new ArrayList<>();

    void onSpawnerMobSpawn(Entity entity, Spawner spawner) {
        mobToSpawner.put(entity, spawner);
    }

    public void onMobDeath(Entity entity) {
        if (mobToSpawner.containsKey(entity)) {
            Spawner spawner = mobToSpawner.get(entity);
            mobToSpawner.remove(entity);
            spawner.onSpawnedEntityDeath();
        }
    }

    public void addSpawner(Spawner spawner) {
        Location location = spawner.getLocation();
        String chunkKey = getChunkKey(location);
        if (chunkKeyToSpawners.containsKey(chunkKey)) {
            chunkKeyToSpawners.get(chunkKey).add(spawner);
        } else {
            List<Spawner> spawnerList = new ArrayList<>();
            spawnerList.add(spawner);
            chunkKeyToSpawners.put(chunkKey, spawnerList);
        }
    }

    public void activateSpawnersOnChunk(Chunk chunk) {
        String chunkKey = getChunkKey(chunk.getBlock(0, 0, 0).getLocation());
        if (chunkKeyToSpawners.containsKey(chunkKey)) {
            activeSpawners.addAll(chunkKeyToSpawners.get(chunkKey));
        }
    }

    public void deactivateSpawnersOnChunk(Chunk chunk) {
        String chunkKey = getChunkKey(chunk.getBlock(0, 0, 0).getLocation());
        if (chunkKeyToSpawners.containsKey(chunkKey)) {
            List<Spawner> spawners = chunkKeyToSpawners.get(chunkKey);
            activeSpawners.removeAll(spawners);
            for (Spawner spawner : spawners) {
                spawner.onSpawnedEntityDeath();
            }
        }
    }

    public void startSpawners() {
        new BukkitRunnable() {

            @Override
            public void run() {
                int howManyEachTime = 50;
                for (int i = 0; i < activeSpawners.size(); i += howManyEachTime) {
                    List<Spawner> sub = activeSpawners.subList(i, Math.min(activeSpawners.size(), i + howManyEachTime));

                    if (i > 0) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Spawner spawner : sub) {
                                    spawner.spawn();
                                }
                            }
                        }.runTaskLater(GuardiansOfAdelia.getInstance(), i / 2);
                    } else {
                        for (Spawner spawner : sub) {
                            spawner.spawn();
                        }
                    }
                }
            }
        }.runTaskTimer(GuardiansOfAdelia.getInstance(), 20 * 60L, 20 * 20L);
    }

    public List<Spawner> getSpawners() {
        List<Spawner> spawners = new ArrayList<>();
        for (List<Spawner> s : chunkKeyToSpawners.values()) {
            spawners.addAll(s);
        }
        return spawners;
    }

    private String getChunkKey(Location location) {
        World world = location.getWorld();
        if (world == null) return null;
        return world.getName() + "|" + (location.getBlockX() >> 4) + "," + (location.getBlockZ() >> 4);
    }
}
