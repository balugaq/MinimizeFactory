package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Cultivation;

import dev.sefiraat.cultivation.api.datatypes.instances.FloraLevelProfile;
import dev.sefiraat.cultivation.api.slimefun.items.plants.HarvestablePlant;
import io.github.ignorelicensescn.minimizefactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.RandomizedSet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;

import static dev.sefiraat.cultivation.implementation.slimefun.items.Machines.GARDEN_CLOCHE;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags.CultivationFlag;
import static io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.MachineRecipeSerializerInitCrafting.generateDefaultLore;
import static io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.MachineRecipeSerializerInitCrafting.generateDefaultName;
import static io.github.ignorelicensescn.minimizefactory.utils.mathinminecraft.RandomizedSetSolving.solveRandomizedSet;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;

public class CultivationSerializedMachineRecipes {

    private static boolean initFlag = false;
    private static Method METHOD_HarvestablePlant_getHarvestingResults = null;
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
//                                logger.log(Level.WARNING, String.valueOf(METHOD_HarvestablePlant_getHarvestingResults));
                            }
                            RandomizedSet<ItemStack> randomOutput = (RandomizedSet<ItemStack>) METHOD_HarvestablePlant_getHarvestingResults.invoke(plant);
                            SimplePair<ItemStack[],IntegerRational[]> outputsAndExpectations = solveRandomizedSet(randomOutput, ItemStack.class);
                            SerializedMachine_MachineRecipe serialized =
                            new SerializedMachine_MachineRecipe(
                                    stack.clone()
                                    ,new MachineRecipeInTicks(growthTicks,null,outputsAndExpectations.first)
                                    ,energyInfo[0]
                                    ,1
                                    ,outputsAndExpectations.second
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

                @Nullable
                @Override
                public SimplePair<String, List<String>> getNameAndLoreForRecipe(@Nullable HarvestablePlant m, SimplePair<SerializedMachine_MachineRecipe, ItemStack> serialized, int index) {
                    List<String> lore = generateDefaultLore(serialized.first);
                    lore.add(properties.getReplacedProperty("Serializer_Need_Catalyzer")+ " : " + NameUtil.findName(GARDEN_CLOCHE.getItem()));
                    return new SimplePair<>(generateDefaultName(serialized.first),lore);
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
