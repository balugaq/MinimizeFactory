package io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork;

import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigInteger;

import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.*;
import static io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeKeys.NETWORK_CONTROLLER_OFFLINE;

@ParametersAreNonnullByDefault
public class CoreInfo extends NodeInfo{
    public CoreInfo(){
        super(null, NodeType.CONTROLLER);
    }
    public ItemStack[] inputs = EMPTY_ITEM_STACK_ARRAY;
    public ItemStack[] outputs = EMPTY_ITEM_STACK_ARRAY;
    public ItemStack[] stableOutputs = EMPTY_ITEM_STACK_ARRAY;
    public BigRational[] inputAmount = EMPTY_BIG_RATIONAL_ARRAY;
    public BigRational[] outputAmount = EMPTY_BIG_RATIONAL_ARRAY;
    public BigRational[] stableOutputAmount = EMPTY_BIG_RATIONAL_ARRAY;
    public long lockTime = Long.MAX_VALUE;
    public SerializeFriendlyBlockLocation[] bridgeLocations = EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY;
    public SerializeFriendlyBlockLocation[] containerLocations = EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY;
    public SerializeFriendlyBlockLocation[] storageLocations = EMPTY_SERIALIZE_FRIENDLY_LOCATION_ARRAY;
    public String networkStatus = NETWORK_CONTROLLER_OFFLINE;
    public BigInteger energyProduction = BigInteger.ZERO;
    public BigInteger energyConsumption = BigInteger.ZERO;
    public BigInteger energyProductionStable = BigInteger.ZERO;
    public BigInteger energyConsumptionStable = BigInteger.ZERO;

}
