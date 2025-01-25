package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultClassResolver;
import com.esotericsoftware.kryo.util.HashMapReferenceResolver;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedColumnAdder;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.NodeInfo;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import org.bukkit.Bukkit;

import java.io.*;
import java.sql.Blob;

public class BridgeInfoSerializer implements Serializer<NodeInfo>, LocationBasedInfoProvider<NodeInfo>, Initializer<NodeInfo>, AutoCloseable{
    @Override
    public void close() throws Exception {
        if (!Bukkit.isPrimaryThread()){
            THREAD_LOCAL.remove();
        }
    }
    public static final ThreadLocal<BridgeInfoSerializer> THREAD_LOCAL = ThreadLocal.withInitial(BridgeInfoSerializer::new);
    private BridgeInfoSerializer(){}
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
