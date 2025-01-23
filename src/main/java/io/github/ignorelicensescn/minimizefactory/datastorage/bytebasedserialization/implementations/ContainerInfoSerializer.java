package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.HashMapReferenceResolver;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.ContainerInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.NodeInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;

import java.io.*;
import java.sql.Blob;

public class ContainerInfoSerializer implements Serializer<ContainerInfo>, LocationBasedInfoProvider<ContainerInfo>, Initializer<ContainerInfo> {
    public static final ContainerInfoSerializer INSTANCE = new ContainerInfoSerializer();
    private ContainerInfoSerializer(){}
    private static final NodeType TYPE = NodeType.MACHINE_CONTAINER;
    private static final Kryo kryoInstance = new Kryo();
    static  {
        kryoInstance.register(SerializeFriendlyBlockLocation.class);
        kryoInstance.register(NodeType.class);
        kryoInstance.register(NodeInfo.class);
        kryoInstance.register(ContainerInfo.class);
    }

    @Override
    public void serialize(ContainerInfo nodeInfo, OutputStream outTo) {
        Output output = new Output(outTo);
        kryoInstance.writeObject(output,nodeInfo);
        output.close();
    }

    @Override
    public ContainerInfo deserialize(InputStream from) {
        Input input = new Input(from);
        ContainerInfo info = kryoInstance.readObject(input, ContainerInfo.class);
        input.close();
        return info;
    }

    @Override
    public ContainerInfo getFromLocation(SerializeFriendlyBlockLocation location){
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
    public ContainerInfo getOrDefault(SerializeFriendlyBlockLocation location){
        ContainerInfo info = getFromLocation(location);
        if (info == null){
            info = new ContainerInfo();
        }
        return info;
    }

    @Override
    public void saveToLocation(ContainerInfo info, SerializeFriendlyBlockLocation toLocation) throws IOException {
        ByteArrayOutputStream outToStream = new ByteArrayOutputStream();
        serialize(info,outToStream);
        BlockDataOperator.INSTANCE.set(toLocation,outToStream.toByteArray());
    }

    @Override
    public void saveToLocationNoThrow(ContainerInfo info, SerializeFriendlyBlockLocation toLocation) {
        try {
            saveToLocation(info,toLocation);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initializeAtLocation(SerializeFriendlyBlockLocation location) {
        NodeTypeOperator.INSTANCE.set(location,TYPE);
        saveToLocationNoThrow(new ContainerInfo(),location);
    }
}
