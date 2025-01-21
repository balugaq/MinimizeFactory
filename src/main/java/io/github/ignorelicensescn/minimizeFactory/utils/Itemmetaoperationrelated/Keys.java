package io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated;
//io.github.sefiraat.networks
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.minimizeFactoryInstance;

@Data
@UtilityClass
public class Keys {
    @Nonnull
    public static NamespacedKey newKey(@Nonnull String value) {
        return new NamespacedKey(minimizeFactoryInstance, value);
    }
}

