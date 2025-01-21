package io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork;

import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigInteger;

import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeKeys.NETWORK_CONTROLLER_OFFLINE;

@ParametersAreNonnullByDefault
public class CoreInfo extends NodeInfo{
    public CoreInfo(){
        super(null,false, NodeType.CONTROLLER);
    }
    public ItemStack[] inputs = new ItemStack[0];
    public ItemStack[] outputs = new ItemStack[0];
    public ItemStack[] stableOutputs = new ItemStack[0];
    public BigRational[] inputAmount = new BigRational[0];
    public BigRational[] outputAmount = new BigRational[0];
    public BigRational[] stableOutputAmount = new BigRational[0];
    public long lockTime = Long.MAX_VALUE;
    public SerializeFriendlyBlockLocation[] BridgeLocations = new SerializeFriendlyBlockLocation[0];
    public SerializeFriendlyBlockLocation[] ContainerLocations = new SerializeFriendlyBlockLocation[0];
    public SerializeFriendlyBlockLocation[] StorageLocations = new SerializeFriendlyBlockLocation[0];
    String networkStatus = NETWORK_CONTROLLER_OFFLINE;
    public BigInteger energyProduction = BigInteger.ZERO;
    public BigInteger energyConsumption = BigInteger.ZERO;
    public BigInteger stableEnergyProduction = BigInteger.ZERO;
    public BigInteger stableEnergyConsumption = BigInteger.ZERO;

}
