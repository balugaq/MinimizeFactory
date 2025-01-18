package io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated;


import de.jeff_media.morepersistentdatatypes.DataType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public class PersistentConnectorSettingsType implements PersistentDataType<PersistentDataContainer, ConnectorSettings> {

    public static final PersistentDataType<PersistentDataContainer, ConnectorSettings> TYPE = new PersistentConnectorSettingsType();

    public static final NamespacedKey CONNECTOR_SETTINGS = Keys.newKey("mf_connector_settings");
    public static final NamespacedKey CONNECTOR_CORE_LOCATION = Keys.newKey("mf_connector_location");
    public static final NamespacedKey CONNECTOR_MODE = Keys.newKey("mf_connector_mode");
    @Override
    @Nonnull
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    @Nonnull
    public Class<ConnectorSettings> getComplexType() {
        return ConnectorSettings.class;
    }

    @Override
    @Nonnull
    public PersistentDataContainer toPrimitive(@Nonnull ConnectorSettings complex, @Nonnull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(CONNECTOR_CORE_LOCATION,DataType.STRING,complex.coreLocation);
        container.set(CONNECTOR_MODE,DataType.BOOLEAN,complex.connectMode);
        return container;
    }

    @Override
    @Nonnull
    public ConnectorSettings fromPrimitive(@Nonnull PersistentDataContainer primitive, @Nonnull PersistentDataAdapterContext context) {
        ConnectorSettings result = new ConnectorSettings(primitive.get(CONNECTOR_CORE_LOCATION,DataType.STRING));
        result.connectMode = Boolean.TRUE.equals(primitive.get(CONNECTOR_MODE, DataType.BOOLEAN));
        return result;
    }
}
