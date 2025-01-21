package io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.ContainerInfo;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.NodeInfo;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;

import java.io.*;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.databaseInstance;

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
            return deserialize(BlockDataOperator.INSTANCE.get(location).getBinaryStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveToLocation(ContainerInfo info, SerializeFriendlyBlockLocation toLocation) throws IOException {
        PipedOutputStream outToStream = new PipedOutputStream();
        serialize(info,outToStream);
        BlockDataOperator.INSTANCE.set(toLocation,new PipedInputStream(outToStream));
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
