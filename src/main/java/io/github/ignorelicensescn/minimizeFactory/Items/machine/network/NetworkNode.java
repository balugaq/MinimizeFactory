package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.utils.Serializations;
import io.github.ignorelicensescn.minimizeFactory.utils.network.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONArray;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.network.NodeKeys.*;

public class NetworkNode extends SlimefunItem {
    public static HashSet<String> scheduleOperateSet = new HashSet<>();
    public static final JSONArray emptyJSONArray = new JSONArray();
    public static final BlockFace[] nearBlockFaces = new BlockFace[]{
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.WEST,
            BlockFace.EAST,
            BlockFace.UP,
            BlockFace.DOWN
    };
    public final NodeType nodeType;
    public Configuration config = new MemoryConfiguration();

    public NetworkNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,NodeType nodeType) {
        super(itemGroup, item, recipeType, recipe);
        this.nodeType = nodeType;
    }
    public NetworkNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,NodeType nodeType,ItemStack out) {
        super(itemGroup, item, recipeType, recipe,out);
        this.nodeType = nodeType;
    }

    public void initNode(Location location){
        BlockStorage.addBlockInfo(location,MINIMIZEFACTORY_NODE_TYPE,nodeType.name());
    }

    public static void lockNode(Location location){
        BlockStorage.addBlockInfo(location,MINIMIZEFACTORY_NODE_LOCKED,TRUE);
    }
    public static void unlockNode(Location location){
        BlockStorage.addBlockInfo(location,MINIMIZEFACTORY_NODE_LOCKED,FALSE);
    }
    public static void registerNodes(Location location,Location coreLocation,int distance){
        registerNodes(location,coreLocation,distance,0);
    }
    
    public static void registerNodes(Location location,Location coreLocation,int distance, int executed){
        scheduleOperateSet.remove(Serializations.LocationToString(location));
        JSONObject locationInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
//        if (locationInfo.has(MINIMIZEFACTORY_NODE_TYPE)){
        String nodeTypeStr = locationInfo.getString(MINIMIZEFACTORY_NODE_TYPE);
        BlockStorage.addBlockInfo(location,MINIMIZEFACTORY_CORE_LOCATION,Serializations.LocationToString(coreLocation));
        switch (NodeType.valueOf(nodeTypeStr)){
            case BRIDGE -> BlockStorage.addBlockInfo(coreLocation,
                    MINIMIZEFACTORY_BRIDGES,
                    (new JSONArray(
                            new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation))
                                    .getString(MINIMIZEFACTORY_BRIDGES)
                    )
                    ).put(new JSONObject()
                                    .put(MINIMIZEFACTORY_NODE_LOCATION, Serializations.LocationToString(location)))
                            .toString()
            );
            case MACHINE_CONTAINER -> {
                BlockStorage.addBlockInfo(coreLocation,
                        MINIMIZEFACTORY_CONTAINERS,
                        (new JSONArray(new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation))
                                .getString(MINIMIZEFACTORY_CONTAINERS)))
                                .put(new JSONObject()
                                        .put(MINIMIZEFACTORY_NODE_LOCATION, Serializations.LocationToString(location)))
                                .toString()
                );
                setCoreStatusIndex(NodeType.MACHINE_CONTAINER,location);
            }

            case STORAGE -> {
                BlockStorage.addBlockInfo(coreLocation,
                        MINIMIZEFACTORY_STORAGES,
                        (new JSONArray(new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation))
                                .getString(MINIMIZEFACTORY_STORAGES)))
                                .put(new JSONObject()
                                        .put(MINIMIZEFACTORY_NODE_LOCATION, Serializations.LocationToString(location)))
                                .toString()
                );
                setCoreStatusIndex(NodeType.STORAGE,location);
            }
            default -> {}
        }
        int counter = -1;
        if (distance < NETWORK_MAX_DISTANCE){
            for (BlockFace face : nearBlockFaces) {
                Location target = location.clone().add(face.getModX(), face.getModY(), face.getModZ());
                if (isNotRegisteredNode(target) && !scheduleOperateSet.contains(Serializations.LocationToString(target))) {
                    executed += 1;
                    int finalExecuted = executed;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(instance,() -> registerNodes(target,coreLocation, distance + 1, finalExecuted),distance + executed * 2L);
                    counter += 1;
                }
            }
        }
        int resultCounter = counter
                + new JSONObject(BlockStorage
                .getBlockInfoAsJson(coreLocation))
                .getInt(NETWORK_COUNTER);
        BlockStorage.addBlockInfo(coreLocation,NETWORK_COUNTER,
            String.valueOf(
                    resultCounter
            )
        );
        if (resultCounter == -1 || resultCounter == 0) {
            BlockStorage.addBlockInfo(coreLocation,NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_ONLINE);
            MachineNetworkCore.refresh(BlockStorage.getInventory(coreLocation), coreLocation.getBlock(), NETWORK_CONTROLLER_ONLINE);
        }
        location.getWorld().playEffect(location, Effect.ENDER_SIGNAL,Effect.ENDER_SIGNAL.getId());
        setCoreStatusIndex(NodeType.valueOf(nodeTypeStr),location);
//        }
    }

    public static void unregisterNodes(Location location, Location coreLocation, int distance){
        unregisterNodes(location,coreLocation,distance,0);
    }

    public static void unregisterNodes(Location location, Location coreLocation, int distance, int executed){
        scheduleOperateSet.remove(Serializations.LocationToString(location));
        location.getWorld().playEffect(location, Effect.ENDER_SIGNAL,Effect.ENDER_SIGNAL.getId());
        if (!BlockStorage.hasBlockInfo(location)){return;}
        JSONObject info = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
        info.remove(MINIMIZEFACTORY_CORE_LOCATION);
        String nodeType = info.getString(MINIMIZEFACTORY_NODE_TYPE);
        if(nodeType.equals(NodeType.CONTROLLER.name())){
            MachineNetworkCore.refresh(BlockStorage.getInventory(location),location.getBlock(),UNREGISTERING);
        }
        else {
            if (nodeType.equals(NodeType.BRIDGE.name())){
                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
                String locationString = Serializations.LocationToString(location);
                JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
                int index = -1;
                for (int i = 0; i < bridges.length(); i++) {
                    if (bridges.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                        index = i;
                        break;
                    }
                }
                if (index != -1){
                    bridges.remove(index);
                    coreInfo.put(MINIMIZEFACTORY_BRIDGES,bridges.toString());
                    BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
                }
            }
            else if (nodeType.equals(NodeType.MACHINE_CONTAINER.name())){
                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
                String locationString = Serializations.LocationToString(location);
                JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                int index = -1;
                for (int i = 0; i < containers.length(); i++) {
                    if (containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                        index = i;
                        break;
                    }
                }
                if (index != -1){
                    containers.remove(index);
                    coreInfo.put(MINIMIZEFACTORY_CONTAINERS,containers.toString());
                    BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
                }
            }
            else if (nodeType.equals(NodeType.STORAGE.name())){
                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
                String locationString = Serializations.LocationToString(location);
                JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                int index = -1;
                for (int i = 0; i < storages.length(); i++) {
                    if (storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                        index = i;
                        break;
                    }
                }
                if (index != -1){
                    storages.remove(index);
                    coreInfo.put(MINIMIZEFACTORY_STORAGES,storages.toString());
                    BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
                }
            }
            setCoreStatusIndex(NodeType.valueOf(info.getString(MINIMIZEFACTORY_NODE_TYPE)),location);
        }
        BlockStorage.setBlockInfo(location,info.toString(),false);
        int counter = -1;
        if (distance < NETWORK_MAX_DISTANCE) {
            for (BlockFace face : nearBlockFaces) {
                Location target = location.clone().add(face.getModX(), face.getModY(), face.getModZ());
                if (isNodeRegisteredToCore(target, coreLocation) && !scheduleOperateSet.contains(Serializations.LocationToString(target))) {
                    executed += 1;
                    int finalExecuted = executed;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> unregisterNodes(target, coreLocation, distance + 1, finalExecuted), distance + executed * 2L);
                    counter += 1;
                }
            }
        }
        int resultCounter = counter
                + new JSONObject(BlockStorage
                .getBlockInfoAsJson(coreLocation))
                .getInt(NETWORK_COUNTER);
        BlockStorage.addBlockInfo(coreLocation,NETWORK_COUNTER,
                String.valueOf(
                        resultCounter
                )
        );
        if (resultCounter == -1 || resultCounter == 0) {
            BlockStorage.addBlockInfo(coreLocation,NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_OFFLINE);
            MachineNetworkCore.refresh(BlockStorage.getInventory(coreLocation), coreLocation.getBlock(), NETWORK_CONTROLLER_OFFLINE);
        }
    }

    public static void unregisterOneNode(Location location){
        scheduleOperateSet.remove(Serializations.LocationToString(location));
        location.getWorld().playEffect(location, Effect.ENDER_SIGNAL,Effect.ENDER_SIGNAL.getId());
        if (!BlockStorage.hasBlockInfo(location)){return;}
        JSONObject info = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
        info.remove(MINIMIZEFACTORY_CORE_LOCATION);
        if(info.getString(MINIMIZEFACTORY_NODE_TYPE).equals(NodeType.CONTROLLER.name())){
            info.put(MINIMIZEFACTORY_CONTAINERS, emptyJSONArray.toString());
            info.put(MINIMIZEFACTORY_STORAGES, emptyJSONArray.toString());
            info.put(MINIMIZEFACTORY_BRIDGES, emptyJSONArray.toString());
        }else {
            if (info.has(MINIMIZEFACTORY_CORE_LOCATION)){
                Location coreLocation = Serializations.StringToLocation(info.getString(MINIMIZEFACTORY_CORE_LOCATION));
                if (coreLocation != null){
                    String nodeType = info.getString(MINIMIZEFACTORY_NODE_TYPE);
                    if (nodeType.equals(NodeType.BRIDGE.name())){
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
                        String locationString = Serializations.LocationToString(location);
                        JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
                        int index = -1;
                        for (int i = 0; i < bridges.length(); i++) {
                            if (bridges.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                                index = i;
                                break;
                            }
                        }
                        if (index != -1){
                            bridges.remove(index);
                            coreInfo.put(MINIMIZEFACTORY_BRIDGES,bridges.toString());
                            BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
                        }
                    }
                    else if (nodeType.equals(NodeType.MACHINE_CONTAINER.name())){
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
                        String locationString = Serializations.LocationToString(location);
                        JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                        int index = -1;
                        for (int i = 0; i < containers.length(); i++) {
                            if (containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                                index = i;
                                break;
                            }
                        }
                        if (index != -1){
                            containers.remove(index);
                            coreInfo.put(MINIMIZEFACTORY_CONTAINERS,containers.toString());
                            BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),true);
                        }
                    }
                    else if (nodeType.equals(NodeType.STORAGE.name())){
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
                        String locationString = Serializations.LocationToString(location);
                        JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                        int index = -1;
                        for (int i = 0; i < storages.length(); i++) {
                            if (storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                                index = i;
                                break;
                            }
                        }
                        if (index != -1){
                            storages.remove(index);
                            coreInfo.put(MINIMIZEFACTORY_STORAGES,storages.toString());
                            BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),true);
                        }
                    }
                }
            }
            setCoreStatusIndex(NodeType.valueOf(info.getString(MINIMIZEFACTORY_NODE_TYPE)),location);
        }
        BlockStorage.setBlockInfo(location,info.toString(),false);
    }

    public static boolean isNode(Location location){
        if (!BlockStorage.hasBlockInfo(location)){return false;}
        return new JSONObject(BlockStorage.getBlockInfoAsJson(location)).has(MINIMIZEFACTORY_NODE_TYPE);
    }
    public static boolean isRegisteredNode(Location location){
        if (!BlockStorage.hasBlockInfo(location)){return false;}
        JSONObject targetJSON = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
        return (targetJSON.has(MINIMIZEFACTORY_CORE_LOCATION) && targetJSON.has(MINIMIZEFACTORY_NODE_TYPE));
    }

    /**
     * return true if the node is not registered
     * <p>false if the node is registered or there's no node</p>
     */
    public static boolean isNotRegisteredNode(Location location){
        if (!BlockStorage.hasBlockInfo(location)){return false;}
        JSONObject targetJSON = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
        if (!targetJSON.has(MINIMIZEFACTORY_NODE_TYPE)){return false;}
        return (!targetJSON.has(MINIMIZEFACTORY_CORE_LOCATION));
    }
    public static boolean isNodeRegisteredToCore(Location nodeLocation,Location coreLocation){
        if (!BlockStorage.hasBlockInfo(nodeLocation)){return false;}
        JSONObject info = new JSONObject(BlockStorage.getBlockInfoAsJson(nodeLocation));
        if (!info.has(MINIMIZEFACTORY_NODE_TYPE)){return false;}//not a node
        if (!info.has(MINIMIZEFACTORY_CORE_LOCATION)){return false;}
        if (!BlockStorage.hasBlockInfo(coreLocation)){return false;}//there's even no core
        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
        if (!coreInfo.has(MINIMIZEFACTORY_NODE_TYPE)){return false;}
        if (!coreInfo.getString(MINIMIZEFACTORY_NODE_TYPE).equals(NodeType.CONTROLLER.name())){return false;}
        String nodeType = info.getString(MINIMIZEFACTORY_NODE_TYPE);
        if (nodeType.equals(NodeType.BRIDGE.name())){
            String locationString = Serializations.LocationToString(nodeLocation);
            JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
            int index = -1;
            for (int i = 0; i < bridges.length(); i++) {
                if (bridges.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                    index = i;
                    break;
                }
            }
            boolean result = (index != -1);
            if (!result){
                info.remove(MINIMIZEFACTORY_CORE_LOCATION);
                info.remove(MINIMIZEFACTORY_NODE_LOCKED);
                BlockStorage.setBlockInfo(nodeLocation,info.toString(),false);
            }
            return result;
        }
        else if (nodeType.equals(NodeType.MACHINE_CONTAINER.name())){
            String locationString = Serializations.LocationToString(nodeLocation);
            JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
            int index = -1;
            for (int i = 0; i < containers.length(); i++) {
                if (containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                    index = i;
                    break;
                }
            }
            boolean result = (index != -1);
            if (!result){
                info.remove(MINIMIZEFACTORY_CORE_LOCATION);
                info.remove(MINIMIZEFACTORY_NODE_LOCKED);
                BlockStorage.setBlockInfo(nodeLocation,info.toString(),false);
            }
            return result;
        }
        else if (nodeType.equals(NodeType.STORAGE.name())){
            String locationString = Serializations.LocationToString(nodeLocation);
            JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
            int index = -1;
            for (int i = 0; i < storages.length(); i++) {
                if (storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
                    index = i;
                    break;
                }
            }
            boolean result = (index != -1);
            if (!result){
                info.remove(MINIMIZEFACTORY_CORE_LOCATION);
                info.remove(MINIMIZEFACTORY_NODE_LOCKED);
                BlockStorage.setBlockInfo(nodeLocation,info.toString(),false);
            }
            return result;
        }
        return false;//(a core) or (not a node)
    }
    public void removeNode(Location location){
        BlockStorage.clearBlockInfo(location);
    }
    public static boolean isLocked(Location location){
        JSONObject blockInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
        return blockInfo.has("minimizefactory_node_locked")
                && blockInfo.getString("minimizefactory_node_locked").equals(TRUE);
    }
    public static void setCoreStatusIndex(NodeType type,Location nodeLocation){
        switch (type){
            case STORAGE -> MachineNetworkStorage.showCoreLocation(nodeLocation,MachineNetworkStorage.HINT_SLOT);
            case MACHINE_CONTAINER -> MachineNetworkContainer.showCoreLocation(nodeLocation,MachineNetworkContainer.STATUS_BORDER);
            default -> {}
        }
    }
}
