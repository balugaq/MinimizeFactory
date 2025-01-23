package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.StorageInfo;
import io.github.ignorelicensescn.minimizefactory.items.machine.network.MachineNetworkStorage;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;

public class StorageInfoSerializationWrapper {
    @Nullable
    public SerializeFriendlyBlockLocation coreLocation;
    public NodeType nodeType;
    public ItemStackSerializationWrapper storeItem;
    public BigInteger storeAmount;
    //for kryo
    public StorageInfoSerializationWrapper(){}


    public StorageInfoSerializationWrapper(StorageInfo info){
        this.storeItem = ItemStackSerializationWrapper.fromItemStack(info.storeItem);
        this.storeAmount = info.storeAmount;
        this.nodeType = info.nodeType;
        this.coreLocation = info.coreLocation;
    }

    public StorageInfo toStorageInfo(){
        StorageInfo storageInfo = new StorageInfo();
        storageInfo.storeAmount = this.storeAmount;
        storageInfo.storeItem = this.storeItem.toItemStack();
        storageInfo.coreLocation = this.coreLocation;
        storageInfo.nodeType = this.nodeType;
        return storageInfo;
    }
}
