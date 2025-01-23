package io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated;
//io.github.sefiraat.networks
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;

@Data
@UtilityClass
public class Keys {
    @Nonnull
    public static NamespacedKey newKey(@Nonnull String value) {
        return new NamespacedKey(minimizeFactoryInstance, value);
    }
}

