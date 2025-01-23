package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.HashMapReferenceResolver;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers.CoreInfoSerializationWrapper;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers.ItemStackSerializationWrapper;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.NodeInfo;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.math.BigInteger;
import java.sql.Blob;

public class CoreInfoSerializer implements Serializer<CoreInfoSerializationWrapper>, LocationBasedInfoProvider<CoreInfo>, Initializer<CoreInfo> {
    public static final CoreInfoSerializer INSTANCE = new CoreInfoSerializer();
    private CoreInfoSerializer(){}
    private static final NodeType TYPE = NodeType.CONTROLLER;
    private static final Kryo kryoInstance = new Kryo();
    static  {
        kryoInstance.register(byte.class);
        kryoInstance.register(byte[].class);
        kryoInstance.register(SerializeFriendlyBlockLocation.class);
        kryoInstance.register(SerializeFriendlyBlockLocation[].class);
        kryoInstance.register(BigRational.class);
        kryoInstance.register(BigRational[].class);
        kryoInstance.register(BigInteger.class);
        kryoInstance.register(NodeType.class);
        kryoInstance.register(NodeInfo.class);
        kryoInstance.register(ItemStackSerializationWrapper.class);
        kryoInstance.register(ItemStackSerializationWrapper[].class);
        kryoInstance.register(CoreInfoSerializationWrapper.class);
    }

    @Override
    public void serialize(CoreInfoSerializationWrapper nodeInfo, OutputStream outTo) {
        Output output = new Output(outTo);
        kryoInstance.writeObject(output,nodeInfo);
        output.close();
    }

    @Override
    public CoreInfoSerializationWrapper deserialize(InputStream from) {
        Input input = new Input(from);
        CoreInfoSerializationWrapper info = kryoInstance.readObject(input, CoreInfoSerializationWrapper.class);
        input.close();
        return info;
    }

    @Override
    public CoreInfo getFromLocation(SerializeFriendlyBlockLocation location){
        try {
            byte[] blob = BlockDataOperator.INSTANCE.get(location);
            if (blob == null){return null;}
            return deserialize(new ByteArrayInputStream(blob)).toCoreInfo();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CoreInfo getOrDefault(SerializeFriendlyBlockLocation location){
        CoreInfo coreInfo = CoreInfoSerializer.INSTANCE.getFromLocation(location);
        if (coreInfo == null){
            coreInfo = new CoreInfo();
            coreInfo.coreLocation = location;
        }
        return coreInfo;
    }

    @Override
    public void saveToLocation(CoreInfo info, SerializeFriendlyBlockLocation toLocation) throws IOException {
        ByteArrayOutputStream outToStream = new ByteArrayOutputStream();
        serialize(new CoreInfoSerializationWrapper(info),outToStream);
        BlockDataOperator.INSTANCE.set(toLocation,outToStream.toByteArray());
    }

    @Override
    public void saveToLocationNoThrow(CoreInfo info, SerializeFriendlyBlockLocation toLocation) {
        try {
            saveToLocation(info,toLocation);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initializeAtLocation(SerializeFriendlyBlockLocation location) {
        NodeTypeOperator.INSTANCE.set(location,TYPE);
        saveToLocationNoThrow(new CoreInfo(),location);
    }
}
