package io.github.ignorelicensescn.minimizefactory.items.machine.network;

import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.BridgeInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.ContainerInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.CoreInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.StorageInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.CoreLocationOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.DataRemover;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.LockStatusOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BlockLocation;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONArray;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_ITEM_STACK_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeKeys.*;

public abstract class NetworkNode extends SlimefunItem {
    public final NodeType nodeType;

    public NetworkNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,NodeType nodeType) {
        super(itemGroup, item, recipeType, recipe);
        this.nodeType = nodeType == null? NodeType.INVALID : nodeType;
    }
    public NetworkNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,NodeType nodeType,ItemStack out) {
        super(itemGroup, item, recipeType, recipe,out);
        this.nodeType = nodeType == null? NodeType.INVALID : nodeType;
    }

    public static void initNode(SerializeFriendlyBlockLocation sfLocation,NodeType nodeType){
        switch (nodeType){
            case STORAGE -> {
                try (StorageInfoSerializer storageInfoSerializer= StorageInfoSerializer.THREAD_LOCAL.get()){
                    storageInfoSerializer.initializeAtLocation(sfLocation);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            case CONTROLLER -> {
                try (CoreInfoSerializer coreInfoSerializer= CoreInfoSerializer.THREAD_LOCAL.get()){
                    coreInfoSerializer.initializeAtLocation(sfLocation);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            case BRIDGE -> {
                try (BridgeInfoSerializer bridgeInfoSerializer= BridgeInfoSerializer.THREAD_LOCAL.get()){
                    bridgeInfoSerializer.initializeAtLocation(sfLocation);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            case MACHINE_CONTAINER -> {
                try (ContainerInfoSerializer containerInfoSerializer= ContainerInfoSerializer.THREAD_LOCAL.get()){
                    containerInfoSerializer.initializeAtLocation(sfLocation);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            default -> new Exception("trying to init unexpected node " + nodeType + " at " + sfLocation).printStackTrace();
        }
    }

    public static void lockNode(Location location)
    {
        LockStatusOperator.INSTANCE.set(SerializeFriendlyBlockLocation.fromLocation(location),true);
    }
    public static void unlockNode(Location location){
        LockStatusOperator.INSTANCE.set(SerializeFriendlyBlockLocation.fromLocation(location),false);
    }


    public static boolean tryRegisterNode(Location sourceLocation,Location coreLocation,BlockLocation probablyNodeRelatedLocation,Set<Location> valid){
        SerializeFriendlyBlockLocation coreLocationKey = SerializeFriendlyBlockLocation.fromLocation(coreLocation);
        Location nodeLocation =
                new Location(sourceLocation.getWorld(),sourceLocation.getBlockX(),sourceLocation.getBlockY(),sourceLocation.getBlockZ())
                        .add(probablyNodeRelatedLocation.vector());//I don't like mutable location.

        if (!BlockStorage.hasBlockInfo(nodeLocation)){return false;}
        SerializeFriendlyBlockLocation nodeLocationKey = SerializeFriendlyBlockLocation.fromLocation(nodeLocation);
        NodeType nodeType = NodeTypeOperator.INSTANCE.get(nodeLocationKey);

        if (!NodeType.isValid(nodeType)){
            return false;
        }

        SerializeFriendlyBlockLocation coreLocationKeyRetrieved = CoreLocationOperator.INSTANCE.get(nodeLocationKey);
        if (coreLocationKeyRetrieved != null && !Objects.equals(coreLocationKeyRetrieved,coreLocationKey)
        ){
            return false;//node registered to another core
        }

        switch (nodeType){
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
        SerializeFriendlyBlockLocation coreLocationKey = SerializeFriendlyBlockLocation.fromLocation(sourceLocation);
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
        }else
        {
            try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.THREAD_LOCAL.get())
            {
                CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                List<SerializeFriendlyBlockLocation> bridges = new ArrayList<>(List.of(coreInfo.bridgeLocations));
                List<SerializeFriendlyBlockLocation> containers = new ArrayList<>(List.of(coreInfo.containerLocations));
                List<SerializeFriendlyBlockLocation> storages = new ArrayList<>(List.of(coreInfo.storageLocations));
                for (Location nodeLocation : valid) {
                    SerializeFriendlyBlockLocation nodeLocationKey = SerializeFriendlyBlockLocation.fromLocation(nodeLocation);
                    NodeType nodeType = NodeTypeOperator.INSTANCE.get(nodeLocationKey);
                    if (nodeType == null) {
                        continue;
                    }
                    switch (nodeType) {
                        case BRIDGE -> {
                            CoreLocationOperator.INSTANCE.set(nodeLocationKey, coreLocationKey);
                            bridges.add(nodeLocationKey);
                            setCoreStatusForNode(NodeType.BRIDGE, nodeLocation);
                        }
                        case MACHINE_CONTAINER -> {
                            CoreLocationOperator.INSTANCE.set(nodeLocationKey, coreLocationKey);
                            containers.add(nodeLocationKey);
                            setCoreStatusForNode(NodeType.MACHINE_CONTAINER, nodeLocation);
                        }
                        case STORAGE -> {
                            CoreLocationOperator.INSTANCE.set(nodeLocationKey, coreLocationKey);
                            storages.add(nodeLocationKey);
                            setCoreStatusForNode(NodeType.STORAGE, nodeLocation);
                        }
                        default -> {
                            logger.log(Level.WARNING,
                                    "Unexpected node type " + nodeType
                                            + " at "
                                            + nodeLocation
                            );
                            continue;
                        }
                    }
                    World w = nodeLocation.getWorld();
                    if (w != null) {
                        w.playEffect(nodeLocation, Effect.ENDER_SIGNAL,/*Effect.ENDER_SIGNAL.getId()*/0);
                    }
                }
                coreInfo.networkStatus = NETWORK_CONTROLLER_ONLINE;
                coreInfo.bridgeLocations = bridges.toArray(EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY);
                coreInfo.containerLocations = containers.toArray(EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY);
                coreInfo.storageLocations = storages.toArray(EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY);
                coreInfoSerializer.saveToLocationNoThrow(coreInfo, coreLocationKey);
                MachineNetworkCore.refresh(BlockStorage.getInventory(coreLocation), coreLocation.getBlock(), NETWORK_CONTROLLER_ONLINE);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    static final String emeptyJSONArrayString = new JSONArray().toString();

    public static void unregisterNodes(Location coreLocation){
        SerializeFriendlyBlockLocation coreLocationKey = SerializeFriendlyBlockLocation.fromLocation(coreLocation);
        try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.THREAD_LOCAL.get()){
            CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
            for (SerializeFriendlyBlockLocation[] keys:new SerializeFriendlyBlockLocation[][]{coreInfo.bridgeLocations, coreInfo.containerLocations, coreInfo.storageLocations}){
                for (SerializeFriendlyBlockLocation nodeKey:keys){
                    CoreLocationOperator.INSTANCE.set(nodeKey,null);
                }
            }
            coreInfo.bridgeLocations = EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY;
            coreInfo.containerLocations = EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY;
            coreInfo.storageLocations = EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY;
            coreInfo.networkStatus = NETWORK_CONTROLLER_OFFLINE;
            coreInfoSerializer.saveToLocationNoThrow(coreInfo,coreLocationKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Deprecated
    public static boolean isNodeRegisteredToCore(Location nodeLocation,Location coreLocation){
        return isNodeRegisteredToCore(SerializeFriendlyBlockLocation.fromLocation(nodeLocation),SerializeFriendlyBlockLocation.fromLocation(coreLocation));
    }

    public static boolean isNodeRegisteredToCore(SerializeFriendlyBlockLocation nodeLocation,SerializeFriendlyBlockLocation coreLocation){
        NodeType nodeType = NodeTypeOperator.INSTANCE.get(nodeLocation);
        if (nodeType == null){
            return false;
        }
        try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.THREAD_LOCAL.get()){
            CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocation);
            SerializeFriendlyBlockLocation[] registeredLocations = null;
            switch (nodeType){
                case BRIDGE -> registeredLocations = coreInfo.bridgeLocations;
                case MACHINE_CONTAINER -> registeredLocations = coreInfo.containerLocations;
                case STORAGE -> registeredLocations = coreInfo.storageLocations;
                default -> {return false;}
            }
            if (registeredLocations == null){return false;}
            for (SerializeFriendlyBlockLocation registeredLocation:registeredLocations){
                if (Objects.equals(registeredLocation,nodeLocation)){return true;}
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public static void removeNode(SerializeFriendlyBlockLocation location){
        DataRemover.INSTANCE.remove(location);
    }
    public static boolean isLocked(@Nonnull SerializeFriendlyBlockLocation location){
        return Boolean.TRUE.equals(LockStatusOperator.INSTANCE.get(location));
    }
    public static void setCoreStatusForNode(NodeType type, Location nodeLocation){
        switch (type){
            case STORAGE -> MachineNetworkStorage.showCoreLocation(nodeLocation,MachineNetworkStorage.HINT_SLOT);
            case MACHINE_CONTAINER -> MachineNetworkContainer.showCoreLocation(nodeLocation,MachineNetworkContainer.STATUS_BORDER);
            default -> {}
        }
    }
}
