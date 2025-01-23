package io.github.ignorelicensescn.minimizefactory.utils.records;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import javax.annotation.Nonnull;

public record BiomeAndEnvironment(@Nonnull Biome biome,@Nonnull World.Environment environment,int hash) {
    public BiomeAndEnvironment(@Nonnull Biome biome,@Nonnull World.Environment environment){
        this(biome,environment,biome.ordinal()<<16 + environment.ordinal());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BiomeAndEnvironment another){
            return this.biome == another.biome
                    && this.environment == another.environment;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public static BiomeAndEnvironment fromLocation(@Nonnull Location l){
        World w = l.getWorld();
        if (w != null){
            Biome b = w.getBiome(l.getBlockX(),l.getBlockY(),l.getBlockZ());
            return new BiomeAndEnvironment(b,w.getEnvironment());
        }else {
            return new BiomeAndEnvironment(Biome.CUSTOM, World.Environment.CUSTOM);
        }
    }
}
