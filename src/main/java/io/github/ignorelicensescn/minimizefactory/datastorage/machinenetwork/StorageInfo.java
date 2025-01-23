package io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork;

import io.github.ignorelicensescn.minimizefactory.items.machine.network.MachineNetworkStorage;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigInteger;

@ParametersAreNonnullByDefault
public class StorageInfo extends NodeInfo{
    public ItemStack storeItem = MachineNetworkStorage.EMPTY_ITEM;
    public BigInteger storeAmount = BigInteger.ZERO;

    public StorageInfo() {
        super(null, NodeType.STORAGE);
    }
}
