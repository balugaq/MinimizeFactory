package io.github.ignorelicensescn.minimizeFactory.utils.network.calculation;

import io.github.ignorelicensescn.minimizeFactory.utils.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Collection;

import static io.github.ignorelicensescn.minimizeFactory.utils.network.calculation.ItemStackMap.EMPTY_ITEM_STACK_MAP;

public record ContainerCalculationResult(@Nonnull ItemStackMap inputs,
                                         @Nonnull ItemStackMap outputs,
                                         @Nonnull ItemStackMap stableOutputs,
                                         @Nonnull BigInteger energyConsumption,
                                         @Nonnull BigInteger energyConsumptionStable) {
    public ContainerCalculationResult combineWith(ContainerCalculationResult another){
        ItemStackMap inputMap = new ItemStackMap();
        ItemStackMap outputMap = new ItemStackMap();
        ItemStackMap stableOutputMap = new ItemStackMap();
        BigInteger energyConsumption = this.energyConsumption().add(another.energyConsumption());
        BigInteger energyConsumptionStable = this.energyConsumptionStable().add(another.energyConsumptionStable());
        inputMap.addAllItems(this.inputs);
        inputMap.addAllItems(another.inputs);
        outputMap.addAllItems(this.outputs());
        outputMap.addAllItems(another.outputs());
        stableOutputMap.addAllItems(this.stableOutputs());
        stableOutputMap.addAllItems(another.stableOutputs());

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
        ItemStackMap inputMap = new ItemStackMap();
        ItemStackMap outputMap = new ItemStackMap();
        ItemStackMap stableOutputMap = new ItemStackMap();
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
            if (recipe.inputs != null){
                if (recipe.Singularity_Material_amount != -1 && recipe.inputs.length == 1){
                    inputMap.addItem(recipe.inputs[0],repeats.multiply(recipe.Singularity_Material_amount).divide(recipe.ticks));
                }
                else {
                    inputMap.addAllItems(recipe.inputs, (BigRational[]) null,new BigRational(repeatsInt,recipe.ticks));
                }

            }
            if (recipe.outputs != null){
                outputMap.addAllItems(recipe.outputs,recipe.outputExpectations,new BigRational(repeatsInt,recipe.ticks));
            }
            long energy = (recipe.energyPerTick + recipe.energyPerTickAtNight);
            energyConsumption = energyConsumption.add(BigInteger.valueOf((energy/2) + (energy%2)));
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
        ItemStackMap inputMap = toGiveATry.inputs;
        ItemStackMap outputMap = toGiveATry.outputs;
        ItemStackMap stableOutputMap = toGiveATry.stableOutputs;
        BigInteger energyConsumption = toGiveATry.energyConsumption;
        BigInteger energyConsumptionStable = toGiveATry.energyConsumptionStable;
        BigInteger tryConsumeEnergy = energyConsumption.add(energyConsumptionStable);
        if (tryConsumeEnergy.signum() <= 0){
            ItemStackMap tryConsume = outputMap.tryConsume(inputMap);
            if (tryConsume != null) {
                tryConsume.addAllItems(stableOutputMap);
                return new ContainerCalculationResult(
                        EMPTY_ITEM_STACK_MAP,
                        EMPTY_ITEM_STACK_MAP,
                        stableOutputMap,
                        BigInteger.ZERO,
                        energyConsumptionStable
                );
            }
        }
        return new ContainerCalculationResult(inputMap,outputMap,stableOutputMap,energyConsumption,energyConsumptionStable);
    }
}
