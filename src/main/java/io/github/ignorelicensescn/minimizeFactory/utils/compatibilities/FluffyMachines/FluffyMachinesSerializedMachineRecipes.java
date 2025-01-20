package io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.FluffyMachines;

import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializeMachineRecipeUtils;
import io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.ncbpfluffybear.fluffymachines.machines.AutoAncientAltar;
import io.ncbpfluffybear.fluffymachines.machines.AutoCraftingTable;
import io.ncbpfluffybear.fluffymachines.machines.AutoTableSaw;
import io.ncbpfluffybear.fluffymachines.objects.AutoCrafter;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.InfoScan.findRecipes_AutoTableSaw;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;
import static io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion.InfinityCompress.InfinityCompressConsts.getMultiblockAutocrafterRecipes;

public class FluffyMachinesSerializedMachineRecipes {

    private static boolean initFlag = false;
    public static void init(){
        if (initFlag){return;}
        if (FluffyMachinesFlag){
            initFlag = true;

            registerSerializedRecipeProvider_byClassName(AutoTableSaw.class.getName(),
                    new SerializedRecipeProvider<AutoTableSaw>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoTableSaw m) {
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
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoCrafter m) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{AutoCrafter.ENERGY_CONSUMPTION,1, AutoCrafter.CAPACITY};
                            List<SimplePair<ItemStack[],ItemStack>> machineRecipes = getMultiblockAutocrafterRecipes(m);
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
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoCraftingTable m) {
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
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoAncientAltar m) {
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
        }
    }
}
