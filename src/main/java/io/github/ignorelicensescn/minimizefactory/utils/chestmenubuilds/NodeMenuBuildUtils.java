package io.github.ignorelicensescn.minimizefactory.utils.chestmenubuilds;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.CoreLocationOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.items.machine.network.NetworkNode.isLocked;
import static io.github.ignorelicensescn.minimizefactory.items.machine.network.NetworkNode.isNodeRegisteredToCore;

public class NodeMenuBuildUtils {


    public static void showCoreLocation(Location nodeLocation, int hintSlot){
        showCoreLocation(nodeLocation,hintSlot, BlockStorage.getInventory(nodeLocation));
    }
    public static void showCoreLocation(Location nodeLocation, int hintSlot, BlockMenu nodeMenu){
        SerializeFriendlyBlockLocation nodeLocationKey = SerializeFriendlyBlockLocation.fromLocation(nodeLocation);
        boolean lockFlag = isLocked(nodeLocationKey);
        SerializeFriendlyBlockLocation coreLocationKey = CoreLocationOperator.INSTANCE.get(nodeLocationKey);

        if (coreLocationKey != null
                && BlockStorage.hasInventory(coreLocationKey.toLocation().getBlock())
                && isNodeRegisteredToCore(nodeLocationKey, coreLocationKey)
        )
        {
            Location coreLocation = coreLocationKey.toLocation();
            nodeMenu.replaceExistingItem(hintSlot,
                    !lockFlag
                            ? new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,
                            properties.getReplacedProperty("MachineNetwork_Core_Location") + coreLocation,
                            properties.getReplacedProperties("MachineNetwork_Core_Location_Lore_1", ChatColor.GRAY))
                            : new CustomItemStack(Material.RED_STAINED_GLASS_PANE,
                            properties.getReplacedProperty("MachineNetwork_Core_Location") + coreLocation,
                            properties.getReplacedProperties("MachineNetwork_Core_Location_Lore_1_Locked", ChatColor.GRAY)
                    ));
            nodeMenu.addMenuClickHandler(hintSlot, (p, slot, item, action) -> {
                NodeType coreNodeType = NodeTypeOperator.INSTANCE.get(coreLocationKey);
                boolean validCoreTypeFlag = coreNodeType == NodeType.CONTROLLER;
                if (!BlockStorage.hasInventory(coreLocation.getBlock())
//                        || !BlockStorage.hasBlockInfo(coreLocation)
                        || !validCoreTypeFlag
                ){
                    nodeMenu.replaceExistingItem(hintSlot, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE,""));
                    nodeMenu.addMenuClickHandler(hintSlot, ChestMenuUtils.getEmptyClickHandler());
                    return false;
                }
                BlockMenu menu1 = BlockStorage.getInventory(coreLocation);
                if (menu1 != null) {menu1.open(p);nodeMenu.close();}
                return false;
            });
        }
        else {
            nodeMenu.replaceExistingItem(hintSlot,new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE,""));
            nodeMenu.addMenuClickHandler(hintSlot,ChestMenuUtils.getEmptyClickHandler());
        }
    }
}
