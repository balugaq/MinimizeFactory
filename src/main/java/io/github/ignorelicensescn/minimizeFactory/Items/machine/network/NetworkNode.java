package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BlockGeometry;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BlockLocation;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONArray;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeKeys.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.BukkitSerializer.LOCATION_SERIALIZER;

public class NetworkNode extends SlimefunItem {
    public static final JSONArray emptyJSONArray = new JSONArray();
    public final NodeType nodeType;

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


    public static boolean tryRegisterNode(Location sourceLocation,Location coreLocation,BlockLocation probablyNodeRelatedLocation,Set<Location> valid){
        Location nodeLocation =
                new Location(sourceLocation.getWorld(),sourceLocation.getBlockX(),sourceLocation.getBlockY(),sourceLocation.getBlockZ())
                        .add(probablyNodeRelatedLocation.vector());//I don't like mutable location.

        if (!BlockStorage.hasBlockInfo(nodeLocation)){return false;}

        JSONObject locationInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(nodeLocation));

        if (!locationInfo.has(MINIMIZEFACTORY_NODE_TYPE)){
            return false;
        }

        if ((locationInfo.has(MINIMIZEFACTORY_CORE_LOCATION)
                && !BlockGeometry.blockLocationEquals(LOCATION_SERIALIZER.StringToSerializable(locationInfo.getString(MINIMIZEFACTORY_CORE_LOCATION)),coreLocation)
        )){
            return false;//node registered to another core
        }

        String nodeTypeStr = locationInfo.getString(MINIMIZEFACTORY_NODE_TYPE);
        switch (NodeType.valueOf(nodeTypeStr)){
            case BRIDGE, STORAGE, MACHINE_CONTAINER -> valid.add(nodeLocation);
            default -> {return false;}//unsupported node type(e.g. core)
        }
        return true;
    }
    public static void registerNodes(
            Location sourceLocation,
            Location coreLocation){
        registerNodes(sourceLocation,coreLocation,new HashSet<>(),new HashSet<>(),0);
    }
    public static void registerNodes(
            Location sourceLocation,
            Location coreLocation,
            Set<BlockLocation> toRegister,
            Set<BlockLocation> registeredOrFailed,
            int distance){
        registerNodes(sourceLocation,coreLocation,toRegister,registeredOrFailed,new HashSet<>(),distance);
    }


    /**
     * register nodes for core
     * @param coreLocation location of the core
     * @param toRegister RELATED Location(to the core) to register
     * @param registeredOrFailed RELATED Location(to the core) for tried
     * @param valid valid location to register nodes
     * @param distance distances already run
     */
    public static void registerNodes(
            Location sourceLocation,
            Location coreLocation,
            Set<BlockLocation> toRegister,
            Set<BlockLocation> registeredOrFailed,
            Set<Location> valid,
            int distance
    ){
        assert coreLocation.getWorld() != null;
        Set<BlockLocation> toRegisterForNextRun = new HashSet<>();
        for (BlockLocation probablyNodeLocation:toRegister){
            boolean successFlag = tryRegisterNode(sourceLocation,coreLocation,probablyNodeLocation,valid);
            registeredOrFailed.add(probablyNodeLocation);
            if (successFlag){
                for (BlockLocation locationForNextRun:probablyNodeLocation.sign().getMoveDirections()){
                    if (!registeredOrFailed.contains(locationForNextRun)){
                        toRegisterForNextRun.add(locationForNextRun);
                    }
                }
            }
        }
        if (distance < NETWORK_MAX_DISTANCE && !toRegisterForNextRun.isEmpty()){
            registerNodes(sourceLocation,coreLocation,toRegisterForNextRun,registeredOrFailed,valid,distance + 1);
        }else{
            JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
            JSONArray bridgesArray = coreInfo.getJSONArray(MINIMIZEFACTORY_BRIDGES);
            if (bridgesArray == null){
                bridgesArray = new JSONArray();
            }
            JSONArray containersArray = coreInfo.getJSONArray(MINIMIZEFACTORY_CONTAINERS);
            if (containersArray == null){
                containersArray = new JSONArray();
            }
            JSONArray storagesArray = coreInfo.getJSONArray(MINIMIZEFACTORY_STORAGES);
            if (storagesArray == null){
                storagesArray = new JSONArray();
            }
            for (Location nodeLocation:valid){
                JSONObject nodeInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(nodeLocation));
                String nodeTypeStr = nodeInfo.getString(MINIMIZEFACTORY_NODE_TYPE);
                JSONObject putIntoArray = new JSONObject();
                putIntoArray.put(MINIMIZEFACTORY_NODE_LOCATION,LOCATION_SERIALIZER.SerializableToString(nodeLocation));
                switch (NodeType.valueOf(nodeTypeStr)){
                    case BRIDGE -> {
                        bridgesArray.put(putIntoArray);
                        setCoreStatusForNode(NodeType.BRIDGE,nodeLocation);
                    }
                    case MACHINE_CONTAINER -> {
                        containersArray.put(putIntoArray);
                        setCoreStatusForNode(NodeType.MACHINE_CONTAINER,nodeLocation);
                    }
                    case STORAGE -> {
                        storagesArray.put(putIntoArray);
                        setCoreStatusForNode(NodeType.STORAGE,nodeLocation);
                    }
                    default -> {
                        logger.log(Level.WARNING,
                                "Unexpected node type " + NodeType.valueOf(nodeTypeStr)
                                        + " at "
                                        + nodeLocation
                        );
                        continue;
                    }
                }
                BlockStorage.addBlockInfo(nodeLocation,MINIMIZEFACTORY_CORE_LOCATION,LOCATION_SERIALIZER.SerializableToString(coreLocation));
                nodeLocation.getWorld().playEffect(nodeLocation, Effect.ENDER_SIGNAL,/*Effect.ENDER_SIGNAL.getId()*/0);
            }
            coreInfo.put(NETWORK_COUNTER,valid.size());
            coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_ONLINE);
            coreInfo.put(MINIMIZEFACTORY_BRIDGES,bridgesArray);
            coreInfo.put(MINIMIZEFACTORY_CONTAINERS,containersArray);
            coreInfo.put(MINIMIZEFACTORY_STORAGES,storagesArray);
            BlockStorage.setBlockInfo(coreLocation,coreInfo.toString(),true);
            MachineNetworkCore.refresh(BlockStorage.getInventory(coreLocation), coreLocation.getBlock(), NETWORK_CONTROLLER_ONLINE);
        }
    }

    static final String emeptyJSONArrayString = new JSONArray().toString();

    public static void unregisterNodes(Location coreLocation){
        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
        JSONArray bridgesArray = coreInfo.getJSONArray(MINIMIZEFACTORY_BRIDGES);
        JSONArray containersArray = coreInfo.getJSONArray(MINIMIZEFACTORY_CONTAINERS);
        JSONArray storagesArray = coreInfo.getJSONArray(MINIMIZEFACTORY_STORAGES);
        for (JSONArray array:new JSONArray[]{bridgesArray,containersArray,storagesArray}){
            if (array == null){continue;}
            for (int i=0;i<array.length();i++){
                JSONObject nodeLocationJSON = array.getJSONObject(i);
                Location nodeLocation = LOCATION_SERIALIZER.StringToSerializable(nodeLocationJSON.getString(MINIMIZEFACTORY_NODE_LOCATION));
                JSONObject nodeInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(nodeLocation));
                nodeInfo.remove(MINIMIZEFACTORY_CORE_LOCATION);
                BlockStorage.setBlockInfo(nodeLocation,nodeInfo.toString(),true);
            }
        }
        coreInfo.put(MINIMIZEFACTORY_BRIDGES,emeptyJSONArrayString);
        coreInfo.put(MINIMIZEFACTORY_CONTAINERS,emeptyJSONArrayString);
        coreInfo.put(MINIMIZEFACTORY_STORAGES,emeptyJSONArrayString);
        coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_OFFLINE);
        BlockStorage.setBlockInfo(coreLocation,coreInfo.toString(),true);
    }

//    public static void unregisterNodes(Location location, Location coreLocation, int distance){
//        unregisterNodes(location,coreLocation,distance,0);
//    }
//
//    public static void unregisterNodes(Location location, Location coreLocation, int distance, int executed){
//        location.getWorld().playEffect(location, Effect.ENDER_SIGNAL,Effect.ENDER_SIGNAL.getId());
//        if (!BlockStorage.hasBlockInfo(location)){return;}
//        JSONObject info = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
//        info.remove(MINIMIZEFACTORY_CORE_LOCATION);
//        String nodeType = info.getString(MINIMIZEFACTORY_NODE_TYPE);
//        if(nodeType.equals(NodeType.CONTROLLER.name())){
//            MachineNetworkCore.refresh(BlockStorage.getInventory(location),location.getBlock(),UNREGISTERING);
//        }
//        else {
//            if (nodeType.equals(NodeType.BRIDGE.name())){
//                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
//                String locationString = LOCATION_SERIALIZER.SerializableToString(location);
//                JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
//                int index = -1;
//                for (int i = 0; i < bridges.length(); i+=1) {
//                    if (bridges.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
//                        index = i;
//                        break;
//                    }
//                }
//                if (index != -1){
//                    bridges.remove(index);
//                    coreInfo.put(MINIMIZEFACTORY_BRIDGES,bridges.toString());
//                    BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
//                }
//            }
//            else if (nodeType.equals(NodeType.MACHINE_CONTAINER.name())){
//                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
//                String locationString = LOCATION_SERIALIZER.SerializableToString(location);
//                JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
//                int index = -1;
//                for (int i = 0; i < containers.length(); i+=1) {
//                    if (containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
//                        index = i;
//                        break;
//                    }
//                }
//                if (index != -1){
//                    containers.remove(index);
//                    coreInfo.put(MINIMIZEFACTORY_CONTAINERS,containers.toString());
//                    BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
//                }
//            }
//            else if (nodeType.equals(NodeType.STORAGE.name())){
//                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
//                String locationString = LOCATION_SERIALIZER.SerializableToString(location);
//                JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
//                int index = -1;
//                for (int i = 0; i < storages.length(); i+=1) {
//                    if (storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
//                        index = i;
//                        break;
//                    }
//                }
//                if (index != -1){
//                    storages.remove(index);
//                    coreInfo.put(MINIMIZEFACTORY_STORAGES,storages.toString());
//                    BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
//                }
//            }
//            setCoreStatusForNode(NodeType.valueOf(info.getString(MINIMIZEFACTORY_NODE_TYPE)),location);
//        }
//        BlockStorage.setBlockInfo(location,info.toString(),false);
//        int counter = -1;
//        if (distance < NETWORK_MAX_DISTANCE) {
//            for (BlockFace face : nearBlockFaces) {
//                Location target = location.clone().add(face.getModX(), face.getModY(), face.getModZ());
//                if (isNodeRegisteredToCore(target, coreLocation) && !scheduleOperateSet.contains(LOCATION_SERIALIZER.SerializableToString(target))) {
//                    executed += 1;
//                    int finalExecuted = executed;
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> unregisterNodes(target, coreLocation, distance + 1, finalExecuted), distance + executed * 2L);
//                    counter += 1;
//                }
//            }
//        }
//        int resultCounter = counter
//                + new JSONObject(BlockStorage
//                .getBlockInfoAsJson(coreLocation))
//                .getInt(NETWORK_COUNTER);
//        BlockStorage.addBlockInfo(coreLocation,NETWORK_COUNTER,
//                String.valueOf(
//                        resultCounter
//                )
//        );
//        if (resultCounter == -1 || resultCounter == 0) {
//            BlockStorage.addBlockInfo(coreLocation,NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_OFFLINE);
//            MachineNetworkCore.refresh(BlockStorage.getInventory(coreLocation), coreLocation.getBlock(), NETWORK_CONTROLLER_OFFLINE);
//        }
//    }
//
//    public static void unregisterOneNode(Location location){
//        location.getWorld().playEffect(location, Effect.ENDER_SIGNAL,Effect.ENDER_SIGNAL.getId());
//        if (!BlockStorage.hasBlockInfo(location)){return;}
//        JSONObject info = new JSONObject(BlockStorage.getBlockInfoAsJson(location));
//        info.remove(MINIMIZEFACTORY_CORE_LOCATION);
//        if(info.getString(MINIMIZEFACTORY_NODE_TYPE).equals(NodeType.CONTROLLER.name())){
//            info.put(MINIMIZEFACTORY_CONTAINERS, emptyJSONArray.toString());
//            info.put(MINIMIZEFACTORY_STORAGES, emptyJSONArray.toString());
//            info.put(MINIMIZEFACTORY_BRIDGES, emptyJSONArray.toString());
//        }else {
//            if (info.has(MINIMIZEFACTORY_CORE_LOCATION)){
//                Location coreLocation = LOCATION_SERIALIZER.StringToSerializable(info.getString(MINIMIZEFACTORY_CORE_LOCATION));
//                if (coreLocation != null){
//                    String nodeType = info.getString(MINIMIZEFACTORY_NODE_TYPE);
//                    if (nodeType.equals(NodeType.BRIDGE.name())){
//                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
//                        String locationString = LOCATION_SERIALIZER.SerializableToString(location);
//                        JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
//                        int index = -1;
//                        for (int i = 0; i < bridges.length(); i+=1) {
//                            if (bridges.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
//                                index = i;
//                                break;
//                            }
//                        }
//                        if (index != -1){
//                            bridges.remove(index);
//                            coreInfo.put(MINIMIZEFACTORY_BRIDGES,bridges.toString());
//                            BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),false);
//                        }
//                    }
//                    else if (nodeType.equals(NodeType.MACHINE_CONTAINER.name())){
//                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
//                        String locationString = LOCATION_SERIALIZER.SerializableToString(location);
//                        JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
//                        int index = -1;
//                        for (int i = 0; i < containers.length(); i+=1) {
//                            if (containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
//                                index = i;
//                                break;
//                            }
//                        }
//                        if (index != -1){
//                            containers.remove(index);
//                            coreInfo.put(MINIMIZEFACTORY_CONTAINERS,containers.toString());
//                            BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),true);
//                        }
//                    }
//                    else if (nodeType.equals(NodeType.STORAGE.name())){
//                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
//                        String locationString = LOCATION_SERIALIZER.SerializableToString(location);
//                        JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
//                        int index = -1;
//                        for (int i = 0; i < storages.length(); i+=1) {
//                            if (storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION).equals(locationString)){
//                                index = i;
//                                break;
//                            }
//                        }
//                        if (index != -1){
//                            storages.remove(index);
//                            coreInfo.put(MINIMIZEFACTORY_STORAGES,storages.toString());
//                            BlockStorage.setBlockInfo(coreLocation.getBlock(),coreInfo.toString(),true);
//                        }
//                    }
//                }
//            }
//            setCoreStatusForNode(NodeType.valueOf(info.getString(MINIMIZEFACTORY_NODE_TYPE)),location);
//        }
//        BlockStorage.setBlockInfo(location,info.toString(),false);
//    }

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
            String locationString = LOCATION_SERIALIZER.SerializableToString(nodeLocation);
            JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
            int index = -1;
            for (int i = 0; i < bridges.length(); i+=1) {
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
            String locationString = LOCATION_SERIALIZER.SerializableToString(nodeLocation);
            JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
            int index = -1;
            for (int i = 0; i < containers.length(); i+=1) {
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
            String locationString = LOCATION_SERIALIZER.SerializableToString(nodeLocation);
            JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
            int index = -1;
            for (int i = 0; i < storages.length(); i+=1) {
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
    public static void setCoreStatusForNode(NodeType type, Location nodeLocation){
        switch (type){
            case STORAGE -> MachineNetworkStorage.showCoreLocation(nodeLocation,MachineNetworkStorage.HINT_SLOT);
            case MACHINE_CONTAINER -> MachineNetworkContainer.showCoreLocation(nodeLocation,MachineNetworkContainer.STATUS_BORDER);
            default -> {}
        }
    }
}
