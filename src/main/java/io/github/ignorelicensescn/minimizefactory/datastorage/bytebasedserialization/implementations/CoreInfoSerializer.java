package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers.CoreInfoSerializationWrapper;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers.ItemStackSerializationWrapper;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedColumnAdder;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.CoreLocationOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.NodeInfo;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;

import java.io.*;
import java.math.BigInteger;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.logger;

public class CoreInfoSerializer implements Serializer<CoreInfoSerializationWrapper>, LocationBasedInfoProvider<CoreInfo>, Initializer<CoreInfo> {
    public static final ThreadLocal<CoreInfoSerializer> THREAD_LOCAL = ThreadLocal.withInitial(CoreInfoSerializer::new);
    private CoreInfoSerializer(){}
    private static final NodeType TYPE = NodeType.CONTROLLER;
    private  final Kryo kryoInstance = new Kryo(){
        {
            register(byte.class);
            register(byte[].class);
            register(SerializeFriendlyBlockLocation.class);
            register(SerializeFriendlyBlockLocation[].class);
            register(BigRational.class);
            register(BigRational[].class);
            register(BigInteger.class);
            register(NodeType.class);
            register(NodeInfo.class);
            register(ItemStackSerializationWrapper.class);
            register(ItemStackSerializationWrapper[].class);
            register(CoreInfoSerializationWrapper.class);
        }
    };
      

    @Override
    public void serialize(CoreInfoSerializationWrapper nodeInfo, OutputStream outTo) {
        synchronized (kryoInstance){
            Output output = new Output(outTo);
            kryoInstance.writeObject(output, nodeInfo);
            output.close();
        }
    }

    @Override
    public CoreInfoSerializationWrapper deserialize(InputStream from) {
        synchronized (kryoInstance){
            Input input = new Input(from);
            CoreInfoSerializationWrapper info = kryoInstance.readObject(input, CoreInfoSerializationWrapper.class);
            input.close();
            return info;
        }
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
        CoreInfo coreInfo = CoreInfoSerializer.THREAD_LOCAL.get().getFromLocation(location);
        if (coreInfo == null){
            initializeAtLocation(location);
            coreInfo = new CoreInfo();
            CoreLocationOperator.INSTANCE.set(location,location);
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
        if (LocationBasedColumnAdder.INSTANCE.checkExistence(location)){return;}
        LocationBasedColumnAdder.INSTANCE.addIfNotExist(location);
        NodeTypeOperator.INSTANCE.set(location,TYPE);
        saveToLocationNoThrow(new CoreInfo(),location);
    }
}
