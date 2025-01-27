package io.github.ignorelicensescn.minimizefactory.items.machine.network;

import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.CoreInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.ConnectorSettings;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.github.ignorelicensescn.minimizefactory.items.machine.network.NetworkNode.isLocked;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.PersistentConnectorSettingsType.CONNECTOR_SETTINGS;
import static io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.PersistentConnectorSettingsType.TYPE;

public class MachineNetworkConnector extends SlimefunItem {
    public MachineNetworkConnector(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        assert item.hasItemMeta();
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        DataTypeMethods.setCustom(meta,CONNECTOR_SETTINGS,TYPE,new ConnectorSettings(""));
        item.setItemMeta(meta);
        addItemHandler(
                (ItemUseHandler) e -> {
                    ItemStack connector = e.getItem();
                    ItemMeta connectorMeta = connector.getItemMeta();
                    if (connectorMeta == null){return;}
                    Optional<ConnectorSettings> optional = DataTypeMethods.getOptionalCustom(connectorMeta,CONNECTOR_SETTINGS, TYPE);
                    optional.ifPresent(connectorSettings -> {
                        Player p = e.getPlayer();
                        if (p.isSneaking()){
                            e.getClickedBlock().ifPresentOrElse(
                                    block -> {
                                        try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                                            SerializeFriendlyBlockLocation key = SerializeFriendlyBlockLocation.fromLocation(block.getLocation());
                                            NodeType nodeType = NodeTypeOperator.INSTANCE.get(key);
                                            if (nodeType != NodeType.CONTROLLER) {
                                                return;
                                            }
                                            CoreInfo coreInfo = coreInfoSerializer.getFromLocation(key);
                                            if (coreInfo != null) {
                                                connectorSettings.coreLocation = SerializeFriendlyBlockLocation.fromLocation(block.getLocation()).toString();
                                                DataTypeMethods.setCustom(connectorMeta, CONNECTOR_SETTINGS, TYPE, connectorSettings);
                                                connector.setItemMeta(connectorMeta);
                                                p.sendMessage(
                                                        properties.getReplacedProperty("Connector_Core_Bound"));
                                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0F);
                                                return;
                                            }
                                            TweakConnectorMode(connectorSettings, connectorMeta, connector, p);
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                    },
                                    () -> TweakConnectorMode(connectorSettings,connectorMeta,connector,p)
                            );

                        }
                        else {
                            if (Objects.equals(connectorSettings.coreLocation, "")){
                                p.sendMessage(properties.getReplacedProperty("Connector_No_Core_Found"));
                                return;
                            }
                            SerializeFriendlyBlockLocation coreLocationKey = SerializeFriendlyBlockLocation.fromString(connectorSettings.coreLocation);
                            if (coreLocationKey == null){
                                p.sendMessage(properties.getReplacedProperty("Connector_No_Core_Found"));
                                return;
                            };
                            e.getClickedBlock().ifPresent(
                                    block -> {
                                        if (!isLocked(coreLocationKey)){
                                            SerializeFriendlyBlockLocation key = SerializeFriendlyBlockLocation.fromLocation(block.getLocation());
                                            NodeType type = NodeTypeOperator.INSTANCE.get(key);
                                            if (type != null && type != NodeType.INVALID){

                                                long current = System.currentTimeMillis();
                                                UUID playerUUID = p.getUniqueId();
                                                if (!PlayerLastConnectorUsedTime.containsKey(playerUUID)){
                                                    PlayerLastConnectorUsedTime.put(playerUUID, 0L);
                                                }
                                                if (
                                                        (NETWORK_CONNECTOR_DELAY_FOR_ALL > (current - lastConnectorUsedTime))
                                                                || (
                                                                (current - PlayerLastConnectorUsedTime.get(playerUUID)) < NETWORK_CONNECTOR_DELAY_FOR_ONE
                                                        )
                                                ){
                                                    p.sendMessage(String.format(properties.getReplacedProperty("Connector_Cooldown"),
                                                            Math.max(NETWORK_CONNECTOR_DELAY_FOR_ALL - (current - lastConnectorUsedTime),NETWORK_CONNECTOR_DELAY_FOR_ONE - (current - PlayerLastConnectorUsedTime.get(playerUUID)))
                                                    ));
                                                    return;
                                                }
                                                lastConnectorUsedTime = current;
                                                PlayerLastConnectorUsedTime.put(playerUUID,current);

                                                if (connectorSettings.connectMode){
                                                    NetworkNode.registerNodes(block.getLocation(),coreLocationKey.toLocation());
                                                }else {
                                                    NetworkNode.unregisterNodes(coreLocationKey.toLocation());
                                                }
                                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0F);
                                            }
                                        }else {
                                            p.sendMessage(properties.getReplacedProperty("Connector_Core_Locked"));
                                        }
                                    }
                            );
                        }
                    });

                }
        );
    }
    public static void TweakConnectorMode(ConnectorSettings connectorSettings,ItemMeta connectorMeta,ItemStack connector,Player p){
        connectorSettings.connectMode = !connectorSettings.connectMode;
        DataTypeMethods.setCustom(connectorMeta, CONNECTOR_SETTINGS, TYPE, connectorSettings);
        connector.setItemMeta(connectorMeta);
        p.sendMessage(
                properties.getReplacedProperty("Connector_Mode_Changed") +
                        (
                                connectorSettings.connectMode
                                        ? properties.getReplacedProperty("Connector_Mode_Connect")
                                        : properties.getReplacedProperty("Connector_Mode_Disconnect")
                        ));
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0F);
    }
}
