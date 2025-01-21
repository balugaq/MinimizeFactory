package io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.NodeInfo;

import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.StorageInfo;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import org.bukkit.inventory.ItemStack;

import java.io.*;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.databaseInstance;

public class CoreInfoSerializer implements Serializer<CoreInfo>, LocationBasedInfoProvider<CoreInfo>, Initializer<CoreInfo> {
    public static final CoreInfoSerializer INSTANCE = new CoreInfoSerializer();
    private CoreInfoSerializer(){}
    private static final NodeType TYPE = NodeType.CONTROLLER;
    private static final Kryo kryoInstance = new Kryo();
    static  {
        kryoInstance.register(SerializeFriendlyBlockLocation.class);
        kryoInstance.register(NodeType.class);
        kryoInstance.register(NodeInfo.class);
        kryoInstance.register(CoreInfo.class);
        kryoInstance.register(SerializeFriendlyBlockLocation[].class);
        kryoInstance.register(ItemStack.class);
        kryoInstance.register(ItemStack[].class);
        kryoInstance.register(BigRational.class);
        kryoInstance.register(BigRational[].class);
    }

    @Override
    public void serialize(CoreInfo nodeInfo, OutputStream outTo) {
        Output output = new Output(outTo);
        kryoInstance.writeObject(output,nodeInfo);
        output.close();
    }

    @Override
    public CoreInfo deserialize(InputStream from) {
        Input input = new Input(from);
        CoreInfo info = kryoInstance.readObject(input, CoreInfo.class);
        input.close();
        return info;
    }

    @Override
    public CoreInfo getFromLocation(SerializeFriendlyBlockLocation location){
        try {
            return deserialize(BlockDataOperator.INSTANCE.get(location).getBinaryStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveToLocation(CoreInfo info, SerializeFriendlyBlockLocation toLocation) throws IOException {
        PipedOutputStream outToStream = new PipedOutputStream();
        serialize(info,outToStream);
        BlockDataOperator.INSTANCE.set(toLocation,new PipedInputStream(outToStream));
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
