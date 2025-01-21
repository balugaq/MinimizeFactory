package io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.implementations;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.Initializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.LocationBasedInfoProvider;
import io.github.ignorelicensescn.minimizeFactory.datastorage.bytebasedserialization.Serializer;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations.BlockDataOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations.NodeTypeOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.NodeInfo;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.StorageInfo;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.math.BigInteger;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.databaseInstance;

public class StorageInfoSerializer implements Serializer<StorageInfo>, LocationBasedInfoProvider<StorageInfo>, Initializer<StorageInfo> {
    public static final StorageInfoSerializer INSTANCE = new StorageInfoSerializer();
    private StorageInfoSerializer(){}
    private static final Kryo kryoInstance = new Kryo();
    private static final NodeType TYPE = NodeType.STORAGE;
    static  {
        kryoInstance.register(SerializeFriendlyBlockLocation.class);
        kryoInstance.register(NodeType.class);
        kryoInstance.register(NodeInfo.class);
        kryoInstance.register(ItemStack.class);
        kryoInstance.register(StorageInfo.class);
        kryoInstance.register(BigInteger.class);
    }

    @Override
    public void serialize(StorageInfo nodeInfo, OutputStream outTo) {
        Output output = new Output(outTo);
        kryoInstance.writeObject(output,nodeInfo);
        output.close();
    }

    @Override
    public StorageInfo deserialize(InputStream from) {
        Input input = new Input(from);
        StorageInfo info = kryoInstance.readObject(input, StorageInfo.class);
        input.close();
        return info;
    }

    @Override
    public StorageInfo getFromLocation(SerializeFriendlyBlockLocation location){
        try {
            return deserialize(databaseInstance.getLocationData(location).getBinaryStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveToLocation(StorageInfo info,SerializeFriendlyBlockLocation toLocation) throws IOException {
        PipedOutputStream outToStream = new PipedOutputStream();
        serialize(info,outToStream);
        BlockDataOperator.INSTANCE.set(toLocation,new PipedInputStream(outToStream));
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
        NodeTypeOperator.INSTANCE.set(location,TYPE);
        saveToLocationNoThrow(new StorageInfo(),location);
    }
}
