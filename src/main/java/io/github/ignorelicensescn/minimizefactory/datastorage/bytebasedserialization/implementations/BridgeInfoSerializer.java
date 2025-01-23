package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultClassResolver;
import com.esotericsoftware.kryo.util.HashMapReferenceResolver;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.NodeInfo;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;

import java.io.*;
import java.sql.Blob;

public class BridgeInfoSerializer implements Serializer<NodeInfo>, LocationBasedInfoProvider<NodeInfo>, Initializer<NodeInfo>{
    public static final BridgeInfoSerializer INSTANCE = new BridgeInfoSerializer();
    private BridgeInfoSerializer(){}
    private static final NodeType TYPE = NodeType.BRIDGE;

    private static final Kryo kryoInstance = new Kryo();
    static  {
        kryoInstance.register(SerializeFriendlyBlockLocation.class);
        kryoInstance.register(NodeType.class);
        kryoInstance.register(NodeInfo.class);
    }

    @Override
    public void serialize(NodeInfo nodeInfo, OutputStream outTo) {
        Output output = new Output(outTo);
        kryoInstance.writeObject(output,nodeInfo);
        output.close();
    }

    @Override
    public NodeInfo deserialize(InputStream from) {
        Input input = new Input(from);
        NodeInfo info = kryoInstance.readObject(input, NodeInfo.class);
        input.close();
        return info;
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
        NodeTypeOperator.INSTANCE.set(location,TYPE);
        saveToLocationNoThrow(new NodeInfo(),location);
    }
}
