package io.github.ignorelicensescn.minimizeFactory.utils;

import org.bukkit.World;
import org.bukkit.block.Biome;

import javax.annotation.Nonnull;

public record BiomeAndEnvironment(@Nonnull Biome biome,@Nonnull World.Environment environment) {

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
        return this.biome.name().hashCode() << 16 + this.environment.name().hashCode();
    }
}
