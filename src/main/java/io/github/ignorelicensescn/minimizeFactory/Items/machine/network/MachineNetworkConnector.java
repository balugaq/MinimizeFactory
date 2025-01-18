package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.ConnectorSettings;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizeFactory.utils.Serializations;
import io.github.ignorelicensescn.minimizeFactory.utils.network.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.github.ignorelicensescn.minimizeFactory.Items.machine.network.NetworkNode.isLocked;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.PersistentConnectorSettingsType.CONNECTOR_SETTINGS;
import static io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.PersistentConnectorSettingsType.TYPE;
import static io.github.ignorelicensescn.minimizeFactory.utils.network.NodeKeys.MINIMIZEFACTORY_NODE_TYPE;

public class MachineNetworkConnector extends SlimefunItem {
    public MachineNetworkConnector(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        ItemMeta meta = item.getItemMeta();
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
                                        if (BlockStorage.hasBlockInfo(block)){
                                            JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(block));
                                            if (coreInfo.has(MINIMIZEFACTORY_NODE_TYPE)){
                                                if (coreInfo.get(MINIMIZEFACTORY_NODE_TYPE).equals(NodeType.CONTROLLER.name())){
                                                    connectorSettings.coreLocation = Serializations.LocationToString(block.getLocation());
                                                    DataTypeMethods.setCustom(connectorMeta, CONNECTOR_SETTINGS, TYPE, connectorSettings);
                                                    connector.setItemMeta(connectorMeta);
                                                    p.sendMessage(
                                                            properties.getReplacedProperty("Connector_Core_Bound"));
                                                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0F);
                                                }else {
                                                    TweakConnectorMode(connectorSettings,connectorMeta,connector,p);
                                                }
                                            }else {
                                                TweakConnectorMode(connectorSettings,connectorMeta,connector,p);
                                            }
                                        }
                                        else {
                                            TweakConnectorMode(connectorSettings,connectorMeta,connector,p);
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
                            Location coreLocation = Serializations.StringToLocation(connectorSettings.coreLocation);
                            if (coreLocation == null){
                                p.sendMessage(properties.getReplacedProperty("Connector_No_Core_Found"));
                                return;
                            }
                            e.getClickedBlock().ifPresent(
                                    block -> {
                                        if (!isLocked(coreLocation)){
                                            if (BlockStorage.hasBlockInfo(block)){
                                                JSONObject nodeInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(block));
                                                if (nodeInfo.has(MINIMIZEFACTORY_NODE_TYPE)){

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
                                                        NetworkNode.registerNodes(block.getLocation(),coreLocation,0);
                                                    }else {
                                                        NetworkNode.unregisterNodes(block.getLocation(),coreLocation,0);
                                                    }
                                                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0F);
                                                }
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
