package io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.calculation;

import io.github.ignorelicensescn.minimizefactory.utils.records.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.records.ItemStackAsKey;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.calculation.UnmodifiableItemStackMapForContainerCalculation.EMPTY_ITEM_STACK_MAP;


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

    public ContainerCalculationResult simplify(){
        ItemStackMapForContainerCalculation inputMap = new ItemStackMapForContainerCalculation();
        ItemStackMapForContainerCalculation outputMap = new ItemStackMapForContainerCalculation();
        ItemStackMapForContainerCalculation stableOutputMap = new ItemStackMapForContainerCalculation();
        BigInteger energyConsumption = this.energyConsumption;
        BigInteger energyConsumptionStable = this.energyConsumptionStable;
        for (Map.Entry<ItemStackAsKey,BigRational> item:this.stableOutputs.entrySet()){
            if (Objects.equals(BigInteger.ZERO,item.getValue().numerator())){continue;}
            stableOutputMap.put(item.getKey(),item.getValue().simplify());
        }
        for (Map.Entry<ItemStackAsKey,BigRational> item:this.outputs.entrySet()){
            if (Objects.equals(BigInteger.ZERO,item.getValue().numerator())){continue;}
            BigRational provided = item.getValue();
            if (this.inputs.containsKey(item.getKey())){
                BigRational required = this.inputs.get(item.getKey());
                int requiredTooMuch = required.compareTo(provided);// <0 enough >0 too much
                if (requiredTooMuch < 0){
                    outputMap.put(item.getKey(),provided.subtract(required));
                }else if (requiredTooMuch > 0){
                    inputMap.put(item.getKey(),required.subtract(provided));
                }
            }else {
                outputMap.put(item.getKey(),item.getValue());
            }
        }

        for (Map.Entry<ItemStackAsKey,BigRational> item:this.inputs.entrySet()){
            if (!inputMap.containsKey(item.getKey())){
                inputMap.put(item.getKey(),item.getValue());
            }
        }
        return new ContainerCalculationResult(inputMap,outputMap,stableOutputMap,energyConsumption,energyConsumptionStable);
    }
}
