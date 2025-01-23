package io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.UUID;

@Immutable
public record SerializeFriendlyBlockLocation(int x, int y, int z, String worldUUID) {
    public static SerializeFriendlyBlockLocation fromLocation(@Nonnull Location l){
        World w = l.getWorld();
        if (w == null){return new SerializeFriendlyBlockLocation(l.getBlockX(),l.getBlockY(),l.getBlockZ(),null);}
        return new SerializeFriendlyBlockLocation(l.getBlockX(),l.getBlockY(),l.getBlockZ(),w.getUID().toString());
    }

    public Location toLocation(){
        if (worldUUID == null
                || Objects.equals(worldUUID,"null")
        ){return new Location(null,x,y,z);}
        World w = null;
        try {
            w = Bukkit.getWorld(UUID.fromString(worldUUID));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Location(w,x,y,z);
    }

    @Override
    public String toString() {
        return x+"|"+y+"|"+z+"|"+worldUUID;
    }

    @Nullable
    public static SerializeFriendlyBlockLocation fromString(@Nonnull String from){
        try {
            String[] strings = from.split("\\|");
            if (strings.length != 4){return null;}
            int x = Integer.parseInt(strings[0]);
            int y = Integer.parseInt(strings[1]);
            int z = Integer.parseInt(strings[2]);
            String uuidString = strings[3];
            return new SerializeFriendlyBlockLocation(x,y,z,uuidString);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SerializeFriendlyBlockLocation that)) return false;

        return x == that.x && y == that.y && z == that.z && Objects.equals(worldUUID, that.worldUUID);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(worldUUID);
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + x;
        return result;
    }
}
