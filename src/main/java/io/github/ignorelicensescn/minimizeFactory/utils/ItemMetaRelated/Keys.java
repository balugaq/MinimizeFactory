package io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated;
//io.github.sefiraat.networks
import io.github.ignorelicensescn.minimizeFactory.MinimizeFactory;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.instance;

@Data
@UtilityClass
public class Keys {
    @Nonnull
    public static NamespacedKey newKey(@Nonnull String value) {
        return new NamespacedKey(instance, value);
    }
}

