package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Cultivation;

import dev.sefiraat.cultivation.api.datatypes.instances.FloraLevelProfile;
import dev.sefiraat.cultivation.api.slimefun.items.plants.HarvestablePlant;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.Approximation;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.WeightedNode;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

import static dev.sefiraat.cultivation.implementation.slimefun.items.Machines.GARDEN_CLOCHE;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.logger;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags.CultivationFlag;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;

public class CultivationSerializedMachineRecipes {

    private static boolean initFlag = false;
    private static Method METHOD_HarvestablePlant_getHarvestingResults = null;
    private static Method METHOD_RandomizedSet_size = null;
    private static Method METHOD_RandomizedSet_sumWeights = null;
    private static Method METHOD_RandomizedSet_toMap = null;
    public static void init(){
        if (initFlag){return;}
        if (CultivationFlag){
            initFlag = true;
            CultivationConsts.init();
            registerSerializedRecipeProvider_byClassName(HarvestablePlant.class.getName(), new SerializedRecipeProvider<HarvestablePlant>() {
                @Nonnull
                @Override
                public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable HarvestablePlant plant, @Nullable ItemStack stack) {
                    if (plant == null || stack == null){return Collections.emptyList();}
                    long[] energyInfo = CultivationConsts.GardenClocheConsts.getEnergyInfo();
                    try {
                        if (SlimefunItem.getByItem(stack) == plant){
                            FloraLevelProfile profile = FloraLevelProfile.fromItemStack(stack);
                            int level = profile.getLevel();
                            double growthRate = plant.getGrowthRate(profile);
                            if (growthRate < 0){return Collections.emptyList();}
                            int growthTicks = growthRate==0?Integer.MAX_VALUE:Math.max(1,(int)Math.ceil(1/growthRate));
                            //generate randomDouble 0~1,if randomDouble<growthRate,cost energy and generate item
                            //EXPECTATION:
                            //if growthRate >= 1,generate 1 time per tick,
                            //if growthRate < 0,no generate
                            //else generate 'growthRate' time per tick(or Math.ceil(1/growthRate) ticks to generate)
                            if (METHOD_HarvestablePlant_getHarvestingResults == null){
                                METHOD_HarvestablePlant_getHarvestingResults = plant.getClass().getDeclaredMethod("getHarvestingResults");
                                logger.log(Level.WARNING, String.valueOf(METHOD_HarvestablePlant_getHarvestingResults));
                            }
                            RandomizedSet<ItemStack> randomOutput = (RandomizedSet<ItemStack>) METHOD_HarvestablePlant_getHarvestingResults.invoke(plant);
//                            if (METHOD_RandomizedSet_size == null){
//                                METHOD_RandomizedSet_size = randomOutput.getClass().getDeclaredMethod("size");
//                            }
                            int size = randomOutput.size();//(int) METHOD_RandomizedSet_size.invoke(randomOutput);
                            ItemStack[] outputs = new ItemStack[size];
                            IntegerRational[] outputExpectations = new IntegerRational[size];
                            Set<WeightedNode<ItemStack>> weightedSet = null;
                            try {
                                if (FIELD_RandomizedSet_internalSet == null){
                                    FIELD_RandomizedSet_internalSet = randomOutput.getClass().getDeclaredField("internalSet");
                                    weightedSet = (Set<WeightedNode<ItemStack>>) getInUnsafe(randomOutput,FIELD_RandomizedSet_internalSet);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

//                            if (METHOD_RandomizedSet_sumWeights == null){
//                                METHOD_RandomizedSet_sumWeights = randomOutput.getClass().getDeclaredMethod("sumWeights");
//                            }
                            int counter = 0;
//                            if (METHOD_RandomizedSet_sumWeights == null){
//                                METHOD_RandomizedSet_toMap = RandomizedSet.class.getDeclaredMethod("toMap");
//                            }

                            if (weightedSet == null){
                                Map<ItemStack, Float> randomMap = randomOutput.toMap();//(Map<ItemStack, Float>) METHOD_RandomizedSet_toMap.invoke(randomOutput);
                                for (Map.Entry<ItemStack, Float> entry : randomMap.entrySet()) {
                                    float currentWeight = entry.getValue();
                                    int dropAmount = plant.getDropAmount(level, entry.getKey().getAmount());
                                    outputs[counter] = entry.getKey().clone();
                                    outputs[counter].setAmount(1);
                                    IntegerRational currentChance = Approximation.find(
                                            currentWeight
                                    );
                                    outputExpectations[counter] =
                                            new IntegerRational(dropAmount, 1)
                                                    .multiply(currentChance)
                                                    .divide(Approximation.find((float) growthRate))
                                    ;
                                    counter += 1;
                                }
                            }else {
                                BigRational totalWeightBig = BigRational.ZERO;
                                IntegerRational totalWeight = null;
                                double totalWeightDouble = 0.;
                                Map<ItemStack,IntegerRational> weights = new HashMap<>(weightedSet.size(),1);
                                for (WeightedNode<ItemStack> node : weightedSet){
                                    IntegerRational approxWeight = Approximation.find(node.getWeight());
                                    weights.put(node.getObject(),approxWeight);
                                    totalWeightBig = totalWeightBig.add(approxWeight);
                                    totalWeightDouble += node.getWeight();
                                }
                                totalWeightBig = totalWeightBig.simplify();
                                if (totalWeightBig.canSaveConvertToIntegerRational()){
                                    totalWeight = totalWeightBig.toIntegerRational();
                                }else {
                                    totalWeight = Approximation.find((float)totalWeightDouble);
                                }
                                for (Map.Entry<ItemStack,IntegerRational> entry : weights.entrySet()) {
                                    int dropAmount = plant.getDropAmount(level, entry.getKey().getAmount());
                                    outputs[counter] = entry.getKey().clone();
                                    outputs[counter].setAmount(1);
                                    IntegerRational currentChance = entry.getValue();
                                    outputExpectations[counter] =
                                            new IntegerRational(dropAmount, 1)
                                                    .multiply(currentChance).divide(totalWeight)
                                                    .divide(Approximation.find((float) growthRate))
                                    ;
                                    counter += 1;
                                }
                            }
                            SerializedMachine_MachineRecipe serialized =
                            new SerializedMachine_MachineRecipe(
                                    stack.clone()
                                    ,new MachineRecipeInTicks(growthTicks,null,outputs)
                                    ,energyInfo[0]
                                    ,1
                                    ,outputExpectations
                            );
                            return Collections.singletonList(
                                    new SimplePair<>(serialized,GARDEN_CLOCHE.getItem())
                            );

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    return Collections.emptyList();
                }

                @Nonnull
                @Override
                public String[] getEnergyInfoStrings(@Nonnull HarvestablePlant m) {
                    long[] energyInfo = CultivationConsts.GardenClocheConsts.getEnergyInfo();
                    return new String[]{properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                            + energyInfo[0]
                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                            properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                    + energyInfo[2]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")};
                }
            });
        }
    }
}
