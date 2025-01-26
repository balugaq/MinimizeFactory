package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers.ItemStackSerializationWrapper;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers.StorageInfoSerializationWrapper;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedColumnAdder;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.NodeInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.StorageInfo;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import stormpot.*;

import java.io.*;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;

public class StorageInfoSerializer implements Serializer<StorageInfoSerializationWrapper>, LocationBasedInfoProvider<StorageInfo>, Initializer<StorageInfo> , AutoCloseable,
        Poolable {
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
    private static final Allocator<StorageInfoSerializer> ALLOCATOR = new Allocator<>() {
        @Override
        public StorageInfoSerializer allocate(Slot slot) throws Exception {
            return new StorageInfoSerializer(slot);
        }

        @Override
        public void deallocate(StorageInfoSerializer poolable) throws Exception {
            poolable.slot = null;
        }
    };
    private static final Pool<StorageInfoSerializer> OBJECT_POOL =
            Pool.from(ALLOCATOR)
                    .setSize(minimizeFactoryInstance.getConfig().getInt("serializer_object_pool_size",30))
                    .build();
    public static final Timeout timeout = new Timeout(3, TimeUnit.SECONDS);
    public static StorageInfoSerializer getInstance(){
        try {
            StorageInfoSerializer instance = OBJECT_POOL.claim(timeout);
            return instance == null?new StorageInfoSerializer(null):instance;
        }catch (Exception e){
            return new StorageInfoSerializer(null);
        }
    }
    private StorageInfoSerializer(Slot slot){
        this.slot = slot;
    }
    private Slot slot;
    private final Kryo kryoInstance = new Kryo(){
        {
            register(byte.class);
            register(byte[].class);
            register(SerializeFriendlyBlockLocation.class);
            register(NodeType.class);
            register(NodeInfo.class);
            register(BigInteger.class);
            register(ItemStackSerializationWrapper.class);
            register(StorageInfoSerializationWrapper.class);
        }
    };
    private static final NodeType TYPE = NodeType.STORAGE;
    

    @Override
    public void serialize(StorageInfoSerializationWrapper nodeInfo, OutputStream outTo) {
        synchronized (kryoInstance){
            Output output = new Output(outTo);
            kryoInstance.writeObject(output, nodeInfo);
            output.close();
        }
    }

    @Override
    public StorageInfoSerializationWrapper deserialize(InputStream from) {
        synchronized (kryoInstance){
            Input input = new Input(from);
            StorageInfoSerializationWrapper info = kryoInstance.readObject(input, StorageInfoSerializationWrapper.class);
            input.close();
            return info;
        }
    }

    @Override
    public StorageInfo getFromLocation(SerializeFriendlyBlockLocation location){
        try {
            byte[] blob = BlockDataOperator.INSTANCE.get(location);
            if (blob == null){return null;}
            return deserialize(new ByteArrayInputStream(blob)).toStorageInfo();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StorageInfo getOrDefault(SerializeFriendlyBlockLocation location){
        StorageInfo info = getFromLocation(location);
        if (info == null){
            info = new StorageInfo();
            initializeAtLocation(location);
        }
        return info;
    }

    @Override
    public void saveToLocation(StorageInfo info,SerializeFriendlyBlockLocation toLocation){
        ByteArrayOutputStream outToStream = new ByteArrayOutputStream();
        serialize(new StorageInfoSerializationWrapper(info),outToStream);
        BlockDataOperator.INSTANCE.set(toLocation,outToStream.toByteArray());
    }

    @Override
    public void saveToLocationNoThrow(StorageInfo info, SerializeFriendlyBlockLocation toLocation) {
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
        saveToLocationNoThrow(new StorageInfo(),location);
    }
}
