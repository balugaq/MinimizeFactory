package io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork;

import io.github.ignorelicensescn.minimizeFactory.Items.machine.network.MachineNetworkStorage;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.math.BigInteger;

public class StorageInfo extends NodeInfo{
    public ItemStack storeItem = MachineNetworkStorage.EMPTY_ITEM;
    BigInteger storeAmount = BigInteger.ZERO;

    public StorageInfo() {
        super(null, NodeType.STORAGE);
    }
}
