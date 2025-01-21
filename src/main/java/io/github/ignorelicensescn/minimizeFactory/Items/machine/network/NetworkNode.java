package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.implementations.BridgeInfoSerializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.implementations.ContainerInfoSerializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.implementations.CoreInfoSerializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.implementations.StorageInfoSerializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.StorageInfo;
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
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeKeys.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.BukkitSerializer.LOCATION_SERIALIZER;

public abstract class NetworkNode extends SlimefunItem {
    public static final JSONArray emptyJSONArray = new JSONArray();
    public final NodeType nodeType;

    public NetworkNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,NodeType nodeType) {
        super(itemGroup, item, recipeType, recipe);
        this.nodeType = nodeType == null? NodeType.INVALID : nodeType;
    }
    public NetworkNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,NodeType nodeType,ItemStack out) {
        super(itemGroup, item, recipeType, recipe,out);
        this.nodeType = nodeType == null? NodeType.INVALID : nodeType;
    }

    public void initNode(Location location){
        SerializeFriendlyBlockLocation sfLocation = SerializeFriendlyBlockLocation.fromLocation(location);
        NodeTypeOperator.INSTANCE.set(sfLocation,nodeType);
        switch (nodeType){
            case STORAGE -> StorageInfoSerializer.INSTANCE.initializeAtLocation(sfLocation);
            case CONTROLLER -> CoreInfoSerializer.INSTANCE.initializeAtLocation(sfLocation);
            case BRIDGE -> BridgeInfoSerializer.INSTANCE.initializeAtLocation(sfLocation);
            case MACHINE_CONTAINER -> ContainerInfoSerializer.INSTANCE.initializeAtLocation(sfLocation);
            default -> new Exception("trying to init unexpected node " + nodeType + " at " + location).printStackTrace();
        }
//        BlockStorage.addBlockInfo(location,MINIMIZEFACTORY_NODE_TYPE,nodeType.name());
    }

    public static void lockNode(Location location)
    {
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
                && !BlockGeometry.blockLocationEquals(LOCATION_SERIALIZER.StringToSerializable(locationInfo.get(MINIMIZEFACTORY_CORE_LOCATION).toString()),coreLocation)
        )){
            return false;//node registered to another core
        }

        String nodeTypeStr = locationInfo.get(MINIMIZEFACTORY_NODE_TYPE).toString();
        switch (NodeType.valueOf(nodeTypeStr)){
            case BRIDGE, STORAGE, MACHINE_CONTAINER -> valid.add(nodeLocation);
        }
        return true;
    }
    public static void registerNodes(
            Location sourceLocation,
            Location coreLocation){
        Set<BlockLocation> toRegister = new HashSet<>(63);
        toRegister.add(new BlockLocation(0,0,0));
        registerNodes(sourceLocation,coreLocation,toRegister,new HashSet<>(),0);
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
        assert sourceLocation.getWorld() != null;
        Set<BlockLocation> toRegisterForNextRun = new HashSet<>(63);
        for (BlockLocation probablyNodeRelatedLocation:toRegister){
            boolean successFlag = tryRegisterNode(sourceLocation,coreLocation,probablyNodeRelatedLocation,valid);
            registeredOrFailed.add(probablyNodeRelatedLocation);
            if (successFlag){
                for (BlockLocation locationForNextRunRelated:probablyNodeRelatedLocation.sign().getMoveDirections()){
                    BlockLocation next = probablyNodeRelatedLocation.add(locationForNextRunRelated);
                    if (!registeredOrFailed.contains(next)){
                        toRegisterForNextRun.add(next);
                    }
                }
            }
        }
        if (distance < NETWORK_MAX_DISTANCE && !toRegisterForNextRun.isEmpty()){
            registerNodes(sourceLocation,coreLocation,toRegisterForNextRun,registeredOrFailed,valid,distance + 1);
        }else{
            JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
            JSONArray bridgesArray = new JSONArray(coreInfo.get(MINIMIZEFACTORY_BRIDGES).toString());
            JSONArray containersArray = new JSONArray(coreInfo.get(MINIMIZEFACTORY_CONTAINERS).toString());
            JSONArray storagesArray = new JSONArray(coreInfo.get(MINIMIZEFACTORY_STORAGES).toString());
            for (Location nodeLocation:valid){
                JSONObject nodeInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(nodeLocation));
                String nodeTypeStr = nodeInfo.get(MINIMIZEFACTORY_NODE_TYPE).toString();
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
                World w = nodeLocation.getWorld();
                if (w != null){
                    w.playEffect(nodeLocation, Effect.ENDER_SIGNAL,/*Effect.ENDER_SIGNAL.getId()*/0);
                }
            }
            coreInfo.put(NETWORK_COUNTER,valid.size());
            coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_ONLINE);
            coreInfo.put(MINIMIZEFACTORY_BRIDGES,bridgesArray.toString());
            coreInfo.put(MINIMIZEFACTORY_CONTAINERS,containersArray.toString());
            coreInfo.put(MINIMIZEFACTORY_STORAGES,storagesArray.toString());
            BlockStorage.setBlockInfo(coreLocation,coreInfo.toString(),true);
            MachineNetworkCore.refresh(BlockStorage.getInventory(coreLocation), coreLocation.getBlock(), NETWORK_CONTROLLER_ONLINE);
        }
    }

    static final String emeptyJSONArrayString = new JSONArray().toString();

    public static void unregisterNodes(Location coreLocation){
        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
        JSONArray bridgesArray = new JSONArray(coreInfo.get(MINIMIZEFACTORY_BRIDGES).toString());
        JSONArray containersArray = new JSONArray(coreInfo.get(MINIMIZEFACTORY_CONTAINERS).toString());
        JSONArray storagesArray = new JSONArray(coreInfo.get(MINIMIZEFACTORY_STORAGES).toString());
        for (JSONArray array:new JSONArray[]{bridgesArray,containersArray,storagesArray}){
            if (array == null){continue;}
            for (int i=0;i<array.length();i++){
                JSONObject nodeLocationJSON = new JSONObject(array.get(i).toString());
                Location nodeLocation = LOCATION_SERIALIZER.StringToSerializable(nodeLocationJSON.get(MINIMIZEFACTORY_NODE_LOCATION).toString());
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

    public static boolean isNodeRegisteredToCore(Location nodeLocation,Location coreLocation){
        if (!BlockStorage.hasBlockInfo(nodeLocation)){return false;}
        JSONObject info = new JSONObject(BlockStorage.getBlockInfoAsJson(nodeLocation));
        if (!info.has(MINIMIZEFACTORY_NODE_TYPE)){return false;}//not a node
        if (!info.has(MINIMIZEFACTORY_CORE_LOCATION)){return false;}
        if (!BlockStorage.hasBlockInfo(coreLocation)){return false;}//there's even no core
        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation));
        if (!coreInfo.has(MINIMIZEFACTORY_NODE_TYPE)){return false;}
        if (!coreInfo.get(MINIMIZEFACTORY_NODE_TYPE).toString().equals(NodeType.CONTROLLER.name())){return false;}
        String nodeType = info.get(MINIMIZEFACTORY_NODE_TYPE).toString();
        if (nodeType.equals(NodeType.BRIDGE.name())){
            String locationString = LOCATION_SERIALIZER.SerializableToString(nodeLocation);
            JSONArray bridges = new JSONArray(coreInfo.get(MINIMIZEFACTORY_BRIDGES).toString());
            boolean releaseLockFlag = true;
            for (int i = 0; i < bridges.length(); i+=1) {
                if (new JSONObject(bridges.get(i).toString()).get(MINIMIZEFACTORY_NODE_LOCATION).toString().equals(locationString)){
                    releaseLockFlag = false;
                    break;
                }
            }
            if (releaseLockFlag){
                info.remove(MINIMIZEFACTORY_CORE_LOCATION);
                info.remove(MINIMIZEFACTORY_NODE_LOCKED);
                BlockStorage.setBlockInfo(nodeLocation,info.toString(),false);
            }
            return releaseLockFlag;
        }
        else if (nodeType.equals(NodeType.MACHINE_CONTAINER.name())){
            String locationString = LOCATION_SERIALIZER.SerializableToString(nodeLocation);
            JSONArray containers = new JSONArray(coreInfo.get(MINIMIZEFACTORY_CONTAINERS).toString());
            boolean releaseLockFlag = true;
            for (int i = 0; i < containers.length(); i+=1) {
                if (new JSONObject(containers.get(i).toString()).get(MINIMIZEFACTORY_NODE_LOCATION).toString().equals(locationString)){
                    releaseLockFlag = false;
                    break;
                }
            }
            if (releaseLockFlag){
                info.remove(MINIMIZEFACTORY_CORE_LOCATION);
                info.remove(MINIMIZEFACTORY_NODE_LOCKED);
                BlockStorage.setBlockInfo(nodeLocation,info.toString(),false);
            }
            return releaseLockFlag;
        }
        else if (nodeType.equals(NodeType.STORAGE.name())){
            String locationString = LOCATION_SERIALIZER.SerializableToString(nodeLocation);
            JSONArray storages = new JSONArray(coreInfo.get(MINIMIZEFACTORY_STORAGES).toString());
            int index = -1;
            for (int i = 0; i < storages.length(); i+=1) {
                if (new JSONObject(storages.get(i).toString()).get(MINIMIZEFACTORY_NODE_LOCATION).toString().equals(locationString)){
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
