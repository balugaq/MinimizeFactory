package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedColumnAdder;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.NodeInfo;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import stormpot.*;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;

public class BridgeInfoSerializer implements Serializer<NodeInfo>, LocationBasedInfoProvider<NodeInfo>, Initializer<NodeInfo>, AutoCloseable, Poolable {
    @Override
    public void close() throws Exception {
        this.release();
    }

    @Override
    public void release() {
        if (slot != null){
            slot.release(this);
        }
    }
    private static final Allocator<BridgeInfoSerializer> ALLOCATOR = new Allocator<BridgeInfoSerializer>() {
        @Override
        public BridgeInfoSerializer allocate(Slot slot) throws Exception {
            return new BridgeInfoSerializer(slot);
        }

        @Override
        public void deallocate(BridgeInfoSerializer poolable) throws Exception {
            poolable.slot = null;
        }
    };
    private static final Pool<BridgeInfoSerializer> OBJECT_POOL =
            Pool.from(ALLOCATOR)
                    .setSize(minimizeFactoryInstance.getConfig().getInt("serializer_object_pool_size",30))
                    .build();
    public static final Timeout timeout = new Timeout(3, TimeUnit.SECONDS);
    public static BridgeInfoSerializer getInstance(){
        try {
            BridgeInfoSerializer instance = OBJECT_POOL.claim(timeout);
            return instance == null?new BridgeInfoSerializer(null):instance;
        }catch (Exception e){
            return new BridgeInfoSerializer(null);
        }
    }
    private BridgeInfoSerializer(Slot slot){
        this.slot = slot;
    }
    private Slot slot;
    private static final NodeType TYPE = NodeType.BRIDGE;

    private final Kryo kryoInstance = new Kryo(){
        {
            register(SerializeFriendlyBlockLocation.class);
            register(NodeType.class);
            register(NodeInfo.class);
        }
    };
    

    @Override
    public void serialize(NodeInfo nodeInfo, OutputStream outTo) {
        synchronized (kryoInstance){
            Output output = new Output(outTo);
            kryoInstance.writeObject(output, nodeInfo);
            output.close();
        }
    }

    @Override
    public NodeInfo deserialize(InputStream from) {
        synchronized (kryoInstance){
            Input input = new Input(from);
            NodeInfo info = kryoInstance.readObject(input, NodeInfo.class);
            input.close();
            return info;
        }
    }

    @Override
    public NodeInfo getFromLocation(SerializeFriendlyBlockLocation location){
        try {
            byte[] blob = BlockDataOperator.INSTANCE.get(location);
            if (blob == null){return null;}
            return deserialize(new ByteArrayInputStream(blob));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public NodeInfo getOrDefault(SerializeFriendlyBlockLocation location){
        NodeInfo nodeInfo = getFromLocation(location);
        if (nodeInfo == null){
            nodeInfo = new NodeInfo();
            initializeAtLocation(location);
        }
        return nodeInfo;
    }

    @Override
    public void saveToLocation(NodeInfo info, SerializeFriendlyBlockLocation toLocation) throws IOException {
        ByteArrayOutputStream outToStream = new ByteArrayOutputStream();
        serialize(info,outToStream);
        BlockDataOperator.INSTANCE.set(toLocation,outToStream.toByteArray());
    }

    @Override
    public void saveToLocationNoThrow(NodeInfo info, SerializeFriendlyBlockLocation toLocation) {
        try {
            saveToLocation(info,toLocation);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initializeAtLocation(SerializeFriendlyBlockLocation location) {
        if (LocationBasedColumnAdder.INSTANCE.checkExistence(location)){return;}
        LocationBasedColumnAdder.INSTANCE.addIfNotExist(location);
        NodeTypeOperator.INSTANCE.set(location,TYPE);
        saveToLocationNoThrow(new NodeInfo(),location);
    }

}
