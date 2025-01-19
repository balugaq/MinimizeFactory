package io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.calculation;

import io.github.ignorelicensescn.minimizeFactory.utils.records.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Collection;

import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.calculation.UnmodifiableItemStackMapForContainerCalculation.EMPTY_ITEM_STACK_MAP;


public record ContainerCalculationResult(@Nonnull ItemStackMapForContainerCalculation inputs,
                                         @Nonnull ItemStackMapForContainerCalculation outputs,
                                         @Nonnull ItemStackMapForContainerCalculation stableOutputs,
                                         @Nonnull BigInteger energyConsumption,
                                         @Nonnull BigInteger energyConsumptionStable) {

    @Nonnull
    public ItemStackMapForContainerCalculation inputs() {
        return new UnmodifiableItemStackMapForContainerCalculation(inputs);
    }

    @Override
    @Nonnull
    public ItemStackMapForContainerCalculation outputs() {
        return new UnmodifiableItemStackMapForContainerCalculation(outputs);
    }

    @Override
    @Nonnull
    public ItemStackMapForContainerCalculation stableOutputs() {
        return new UnmodifiableItemStackMapForContainerCalculation(stableOutputs);
    }

    public static final ContainerCalculationResult EMPTY = new ContainerCalculationResult(new ItemStackMapForContainerCalculation(),new ItemStackMapForContainerCalculation(),new ItemStackMapForContainerCalculation(),BigInteger.ZERO,BigInteger.ZERO);

    public ContainerCalculationResult combineWith(ContainerCalculationResult another){
        ItemStackMapForContainerCalculation inputMap = new ItemStackMapForContainerCalculation();
        ItemStackMapForContainerCalculation outputMap = new ItemStackMapForContainerCalculation();
        ItemStackMapForContainerCalculation stableOutputMap = new ItemStackMapForContainerCalculation();
        BigInteger energyConsumption = this.energyConsumption().add(another.energyConsumption());
        BigInteger energyConsumptionStable = this.energyConsumptionStable().add(another.energyConsumptionStable());
        inputMap.addAllItems(this.inputs);
        inputMap.addAllItems(another.inputs);
        outputMap.addAllItems(this.outputs);
        outputMap.addAllItems(another.outputs);
        stableOutputMap.addAllItems(this.stableOutputs);
        stableOutputMap.addAllItems(another.stableOutputs);

        return tryConvertToStableOutput(new ContainerCalculationResult(
                inputMap,
                outputMap,
                stableOutputMap,
                energyConsumption,
                energyConsumptionStable
        ));
    }
    public static ContainerCalculationResult fromSerializedRecipes(
            BiomeAndEnvironment bioAndEnv,
            Collection<SimplePair<SerializedMachine_MachineRecipe,Integer>> serializedRecipeWithRepeats
    ){
        ItemStackMapForContainerCalculation inputMap = new ItemStackMapForContainerCalculation();
        ItemStackMapForContainerCalculation outputMap = new ItemStackMapForContainerCalculation();
        ItemStackMapForContainerCalculation stableOutputMap = new ItemStackMapForContainerCalculation();
        BigInteger energyConsumption = BigInteger.ZERO;
        BigInteger energyConsumptionStable = BigInteger.ZERO;
        for (SimplePair<SerializedMachine_MachineRecipe,Integer> recipePair:serializedRecipeWithRepeats){
            SerializedMachine_MachineRecipe recipe = recipePair.first;
            int repeatsInt = recipePair.second;
            BigRational repeats = new BigRational(recipePair.second);
            if (recipe.env != null && bioAndEnv.environment() != recipe.env){
                continue;
            }
            if (recipe.biome != null && bioAndEnv.biome() != recipe.biome){
                continue;
            }
            if (recipe.inputs != null && recipe.inputs.length != 0){
                if (recipe.Singularity_Material_amount != -1 && recipe.inputs.length == 1){
                    inputMap.addItem(recipe.inputs[0],repeats.multiply(recipe.Singularity_Material_amount).divide(recipe.ticks));
                }
                else {
                    inputMap.addAllItems(recipe.inputs, (BigRational[]) null,new BigRational(repeatsInt,recipe.ticks));
                }

            }
            if (recipe.outputs != null && recipe.outputs.length != 0){
                outputMap.addAllItems(recipe.outputs,recipe.outputExpectations,new BigRational(repeatsInt,recipe.ticks));
            }
            long energy = (recipe.energyPerTick + recipe.energyPerTickAtNight);
            energyConsumption = energyConsumption.add(BigInteger.valueOf((energy/2) + (energy%2)).multiply(BigInteger.valueOf(repeatsInt)));
        }

        return tryConvertToStableOutput(new ContainerCalculationResult(
                inputMap,
                outputMap,
                stableOutputMap,
                energyConsumption,
                energyConsumptionStable
        ));
    }
    public static ContainerCalculationResult tryConvertToStableOutput(ContainerCalculationResult toGiveATry){
        ItemStackMapForContainerCalculation inputMap = toGiveATry.inputs;
        ItemStackMapForContainerCalculation outputMap = toGiveATry.outputs;
        ItemStackMapForContainerCalculation stableOutputMap = toGiveATry.stableOutputs;
        BigInteger energyConsumption = toGiveATry.energyConsumption;
        BigInteger energyConsumptionStable = toGiveATry.energyConsumptionStable;
        BigInteger tryConsumeEnergy = energyConsumption.add(energyConsumptionStable);
        if (tryConsumeEnergy.signum() <= 0){
            ItemStackMapForContainerCalculation tryConsume = outputMap.tryConsume(inputMap);
            if (tryConsume != null) {
                tryConsume.addAllItems(stableOutputMap);
                return new ContainerCalculationResult(
                        EMPTY_ITEM_STACK_MAP,
                        EMPTY_ITEM_STACK_MAP,
                        tryConsume,
                        BigInteger.ZERO,
                        tryConsumeEnergy
                );
            }
        }
        return new ContainerCalculationResult(inputMap,outputMap,stableOutputMap,energyConsumption,energyConsumptionStable);
    }
}
