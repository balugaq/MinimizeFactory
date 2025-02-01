package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.FluffyMachines;

import io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStacksToStackRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreWasher;
import io.ncbpfluffybear.fluffymachines.machines.*;
import io.ncbpfluffybear.fluffymachines.objects.AutoCrafter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.InfoScan.findRecipes_AutoTableSaw;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress.InfinityCompressConsts.getMultiblockAutocrafterRecipes;

public class FluffyMachinesSerializedMachineRecipes {

    private static boolean initFlag = false;
    public static void init(){
        if (initFlag){return;}
        if (PluginEnabledFlags.FluffyMachinesFlag){
            initFlag = true;

            registerSerializedRecipeProvider_byClassName(AutoTableSaw.class.getName(),
                    new SerializedRecipeProvider<AutoTableSaw>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoTableSaw m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            List<MachineRecipe> machineRecipes = findRecipes_AutoTableSaw(m);
                            return SerializeMachineRecipeUtils.fromMachineRecipe(machineRecipes,m,AutoTableSaw.ENERGY_CONSUMPTION,1);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull AutoTableSaw m) {
                            long[] energyInfo = new long[]{AutoTableSaw.ENERGY_CONSUMPTION,1,AutoTableSaw.CAPACITY};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });

            registerSerializedRecipeProvider_byClassName(AutoCrafter.class.getName(),
                    new SerializedRecipeProvider<AutoCrafter>() {

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoCrafter m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{AutoCrafter.ENERGY_CONSUMPTION,1, AutoCrafter.CAPACITY};
                            ItemStacksToStackRecipe[] machineRecipes = getMultiblockAutocrafterRecipes(m);
                            return SerializeMachineRecipeUtils.fromInputsAndSingleOutput(machineRecipes,m,
                                    energyInfo[0],1);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull AutoCrafter m) {
                            long[] energyInfo = new long[]{AutoCrafter.ENERGY_CONSUMPTION,1, AutoCrafter.CAPACITY};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });

            registerSerializedRecipeProvider_byClassName(AutoCraftingTable.class.getName(),
                    new SerializedRecipeProvider<AutoCraftingTable>() {

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoCraftingTable m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{AutoCraftingTable.ENERGY_CONSUMPTION,1, AutoCraftingTable.CAPACITY};
                            return SerializeMachineRecipeUtils.fromInputsAndSingleOutput(vanillaRecipeArray,m,
                                    energyInfo[0],1);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull AutoCraftingTable m) {
                            long[] energyInfo = new long[]{AutoCraftingTable.ENERGY_CONSUMPTION,1, AutoCraftingTable.CAPACITY};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });

            registerSerializedRecipeProvider_byClassName(AutoAncientAltar.class.getName(),
                    new SerializedRecipeProvider<AutoAncientAltar>() {

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoAncientAltar m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{AutoAncientAltar.ENERGY_CONSUMPTION,1, AutoAncientAltar.CAPACITY};
                            return SerializeMachineRecipeUtils.fromInputsAndSingleOutput(altarRecipes,m,
                                    energyInfo[0],1);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull AutoAncientAltar m) {
                            long[] energyInfo = new long[]{AutoAncientAltar.ENERGY_CONSUMPTION,1, AutoAncientAltar.CAPACITY};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });

            registerSerializedRecipeProvider_byClassName(SmartFactory.class.getName(),
                    new SerializedRecipeProvider<SmartFactory>() {
                        private Field Field_SmartFactory_ItemRecipes = null;
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SmartFactory m, @Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{SmartFactory.getEnergyConsumption(),1,SmartFactory.getEnergyCapacity()};
                            try {
                                if (Field_SmartFactory_ItemRecipes == null){
                                    Field_SmartFactory_ItemRecipes = SmartFactory.class.getDeclaredField("ITEM_RECIPES");
                                }
                                Map<SlimefunItem,List<ItemStack[]>> itemRecipes
                                        = (Map<SlimefunItem, List<ItemStack[]>>) getInUnsafe(m,Field_SmartFactory_ItemRecipes);
                                List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(itemRecipes.size());
                                int ticks = 10;
                                for (Map.Entry<SlimefunItem,List<ItemStack[]>> entry:itemRecipes.entrySet()){
                                    ItemStack[] recipeOutput = new ItemStack[]{entry.getKey().getItem()};
                                    for (ItemStack[] recipeInput:entry.getValue()){
                                        result.add(new SimplePair<>(
                                                new SerializedMachine_MachineRecipe(
                                                        m.getItem(),
                                                        new MachineRecipeInTicks(
                                                                ticks,
                                                                recipeInput,
                                                                recipeOutput
                                                        ),
                                                        null,
                                                        energyInfo[0],
                                                        (int)energyInfo[1],
                                                        null
                                                )
                                                ,null)
                                        );
                                    }
                                }
                                return result;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return Collections.emptyList();
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull SmartFactory m) {
                            long[] energyInfo = new long[]{SmartFactory.getEnergyConsumption(),1,SmartFactory.getEnergyCapacity()};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });

            registerSerializedRecipeProvider_byClassName(ElectricDustFabricator.class.getName(),
                    new SerializedRecipeProvider<ElectricDustFabricator>() {
                        private Field Field_EDF_oreWasher = null;
                        private Field Field_EDF_acceptableInputs = null;
                        private Field Field_OreWasher_dusts = null;
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable ElectricDustFabricator m, @Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}

                            long[] energyInfo = new long[]{m.getEnergyConsumption(),m.getSpeed(),m.getCapacity()};
                            try {
                                if (Field_EDF_acceptableInputs == null){
                                    Field_EDF_acceptableInputs = ElectricDustFabricator.class.getDeclaredField("acceptableInputs");
                                }
                                List<ItemStack> acceptableInputs = (List<ItemStack>) getInUnsafe(m,Field_EDF_acceptableInputs);
                                if (Field_EDF_oreWasher == null){
                                    Field_EDF_oreWasher = ElectricDustFabricator.class.getDeclaredField("oreWasher");
                                }
                                OreWasher oreWasher = (OreWasher) getInUnsafe(m,Field_EDF_oreWasher);
                                if (Field_OreWasher_dusts == null){
                                    Field_OreWasher_dusts = OreWasher.class.getDeclaredField("dusts");
                                }
                                ItemStack[] dusts
                                        = (ItemStack[]) getInUnsafe(oreWasher,Field_OreWasher_dusts);
                                List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(acceptableInputs.size() * dusts.length);
                                ItemStack[][] outputs = new ItemStack[dusts.length][1];
                                for (int i=0;i<dusts.length;i++){
                                    outputs[i] = new ItemStack[]{dusts[i]};
                                }
                                for (ItemStack input:acceptableInputs){
                                    ItemStack[] inputArr = new ItemStack[]{input};
                                    for (ItemStack[] output:outputs){
                                        result.add(new SimplePair<>(
                                                new SerializedMachine_MachineRecipe(
                                                        m.getItem(),
                                                        new MachineRecipe(
                                                                4 / m.getSpeed(),
                                                                inputArr,
                                                                output
                                                        ),
                                                        null,
                                                        energyInfo[0],
                                                        (int)energyInfo[1],
                                                        null
                                                )
                                                ,null)
                                        );
                                    }
                                }
                                return result;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return Collections.emptyList();
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull ElectricDustFabricator m) {
                            long[] energyInfo = new long[]{m.getEnergyConsumption(),m.getSpeed(),m.getCapacity()};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
                                            + energyInfo[1]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
        }
    }
}
