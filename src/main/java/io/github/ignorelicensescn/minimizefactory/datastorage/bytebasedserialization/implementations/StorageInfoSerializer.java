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

import java.io.*;
import java.math.BigInteger;
import java.sql.Blob;

public class StorageInfoSerializer implements Serializer<StorageInfoSerializationWrapper>, LocationBasedInfoProvider<StorageInfo>, Initializer<StorageInfo> {

    public static final  ThreadLocal<StorageInfoSerializer> THREAD_LOCAL = new ThreadLocal<>();

    private StorageInfoSerializer(){}
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
