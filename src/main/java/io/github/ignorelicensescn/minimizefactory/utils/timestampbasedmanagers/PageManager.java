package io.github.ignorelicensescn.minimizefactory.utils.timestampbasedmanagers;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PageManager {
    private static final Map<UUID, AtomicInteger> playerPageMap = new ConcurrentHashMap<>();
    private static final Map<UUID, AtomicLong> playerAccessMap = new ConcurrentHashMap<>();
    public static final long CLEAR_INTERVAL = 1000*60*20;
    private static void onPlayerOperatedMap(@Nullable Player p){
        if (p == null){return;}
        UUID uuid = p.getUniqueId();
        AtomicLong atomicLong = playerAccessMap.get(uuid);
        long currentTimeMillis = System.currentTimeMillis();
        if (atomicLong == null){
            playerAccessMap.put(uuid,new AtomicLong(currentTimeMillis));
            return;
        }
        atomicLong.set(currentTimeMillis);
    }

    private static void setIfNotExist(@Nonnull Player p){
        if (!playerPageMap.containsKey(p.getUniqueId())){
            UUID uuid = p.getUniqueId();
            playerPageMap.put(p.getUniqueId(),new AtomicInteger(0));
            playerAccessMap.put(p.getUniqueId(),new AtomicLong(System.currentTimeMillis()));
        }
    }

    public static void removePlayer(@Nullable Player p){
        if (p == null){return;}
        UUID uuid = p.getUniqueId();
        playerPageMap.remove(uuid);
        playerAccessMap.remove(uuid);
    }

    public static int addPage(@Nonnull Player p){
        setIfNotExist(p);
        onPlayerOperatedMap(p);
        AtomicInteger integer = playerPageMap.get(p.getUniqueId());
        int result = integer.addAndGet(1);
        if (result == Integer.MIN_VALUE){
            integer.set(0);
        }
        return result;
    }

    public static int getPage(@Nonnull Player p){
        setIfNotExist(p);
        onPlayerOperatedMap(p);
        AtomicInteger integer = playerPageMap.get(p.getUniqueId());
        return integer.get();
    }

    public static int decreasePage(@Nonnull Player p){
        setIfNotExist(p);
        onPlayerOperatedMap(p);
        AtomicInteger integer = playerPageMap.get(p.getUniqueId());
        int result = integer.addAndGet(-1);
        if (result < 0){
            integer.set(0);
        }
        return result;
    }

    public static void tryClear(){
        Set<UUID> uuids = new HashSet<>();
        long currentTimeMillis = System.currentTimeMillis();
        for (Map.Entry<UUID,AtomicLong> entry:playerAccessMap.entrySet()){
            if (currentTimeMillis - entry.getValue().get() > CLEAR_INTERVAL){
                uuids.add(entry.getKey());
            }
        }
        for (UUID uuid:uuids){
            playerAccessMap.remove(uuid);
            playerPageMap.remove(uuid);
        }
    }

}
