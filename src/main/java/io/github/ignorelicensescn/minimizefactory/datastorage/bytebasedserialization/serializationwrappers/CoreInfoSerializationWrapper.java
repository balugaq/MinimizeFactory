package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers;

import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;

public class CoreInfoSerializationWrapper {
    
    @Nullable
    public final SerializeFriendlyBlockLocation coreLocation;
    @Nonnull
    public final NodeType nodeType;
    
    public final ItemStackSerializationWrapper[] inputs;
    public final ItemStackSerializationWrapper[] outputs;
    public final ItemStackSerializationWrapper[] stableOutputs;
    public final BigRational[] inputAmount;
    public final BigRational[] outputAmount;
    public final BigRational[] stableOutputAmount;
    public final long lockTime;
    public final SerializeFriendlyBlockLocation[] bridgeLocations;
    public final SerializeFriendlyBlockLocation[] containerLocations;
    public final SerializeFriendlyBlockLocation[] storageLocations;
    public final String networkStatus;
    public final BigInteger energyProduction;
    public final BigInteger energyConsumption;
    public final BigInteger energyProductionStable;
    public final BigInteger energyConsumptionStable;
    
    public CoreInfo toCoreInfo(){
        CoreInfo coreInfo = new CoreInfo();
        coreInfo.coreLocation = this.coreLocation;
        coreInfo.nodeType = this.nodeType;
        {
            ItemStack[] convertedInputs = new ItemStack[this.inputs.length];
            for (int i = 0; i < convertedInputs.length; i++) {
                convertedInputs[i] = this.inputs[i].toItemStack();
            }
            coreInfo.inputs = convertedInputs;
        }
        {
            ItemStack[] convertedOutputs = new ItemStack[this.outputs.length];
            for (int i = 0; i < convertedOutputs.length; i++) {
                convertedOutputs[i] = this.outputs[i].toItemStack();
            }
            coreInfo.outputs = convertedOutputs;
        }
        {
            ItemStack[] convertedStableOutputs = new ItemStack[this.stableOutputs.length];
            for (int i = 0; i < convertedStableOutputs.length; i++) {
                convertedStableOutputs[i] = this.stableOutputs[i].toItemStack();
            }
            coreInfo.stableOutputs = convertedStableOutputs;
        }
        coreInfo.inputAmount = this.inputAmount;
        coreInfo.outputAmount = this.outputAmount;
        coreInfo.stableOutputAmount = this.stableOutputAmount;
        coreInfo.bridgeLocations = this.bridgeLocations;
        coreInfo.containerLocations = this.containerLocations;
        coreInfo.storageLocations = this.storageLocations;
        coreInfo.energyProduction = this.energyProduction;
        coreInfo.energyConsumption = this.energyConsumption;
        coreInfo.energyProductionStable = this.energyProductionStable;
        coreInfo.energyConsumptionStable = this.energyConsumptionStable;
        coreInfo.lockTime = this.lockTime;
        return coreInfo;
    }
    public CoreInfoSerializationWrapper(CoreInfo coreInfo){
        this.coreLocation = coreInfo.coreLocation;
        this.nodeType = coreInfo.nodeType;
        {
            ItemStackSerializationWrapper[] convertedInputs = new ItemStackSerializationWrapper[coreInfo.inputs.length];
            for (int i = 0; i < convertedInputs.length; i++) {
                convertedInputs[i] = ItemStackSerializationWrapper.fromItemStack(coreInfo.inputs[i]);
            }
            this.inputs = convertedInputs;
        }
        {
            ItemStackSerializationWrapper[] convertedOutputs = new ItemStackSerializationWrapper[coreInfo.outputs.length];
            for (int i = 0; i < convertedOutputs.length; i++) {
                convertedOutputs[i] = ItemStackSerializationWrapper.fromItemStack(coreInfo.outputs[i]);
            }
            this.outputs = convertedOutputs;
        }
        {
            ItemStackSerializationWrapper[] convertedStableOutputs = new ItemStackSerializationWrapper[coreInfo.stableOutputs.length];
            for (int i = 0; i < convertedStableOutputs.length; i++) {
                convertedStableOutputs[i] = ItemStackSerializationWrapper.fromItemStack(coreInfo.stableOutputs[i]);
            }
            this.stableOutputs = convertedStableOutputs;
        }
        this.lockTime = coreInfo.lockTime;
        this.inputAmount = coreInfo.inputAmount;
        this.outputAmount = coreInfo.outputAmount;
        this.stableOutputAmount = coreInfo.stableOutputAmount;
        this.bridgeLocations = coreInfo.bridgeLocations;
        this.containerLocations = coreInfo.containerLocations;
        this.storageLocations = coreInfo.storageLocations;
        this.energyProduction = coreInfo.energyProduction;
        this.energyConsumption = coreInfo.energyConsumption;
        this.energyProductionStable = coreInfo.energyProductionStable;
        this.energyConsumptionStable = coreInfo.energyConsumptionStable;
        this.networkStatus = coreInfo.networkStatus;
    }
}
