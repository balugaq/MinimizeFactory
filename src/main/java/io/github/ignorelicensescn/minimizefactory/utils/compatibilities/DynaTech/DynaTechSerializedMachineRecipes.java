package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.DynaTech;

import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.profelements.dynatech.items.abstracts.AbstractElectricMachine;
import me.profelements.dynatech.items.electric.generators.CulinaryGenerator;
import me.profelements.dynatech.items.electric.generators.StardustReactor;
import me.profelements.dynatech.items.electric.machines.MineralizedApiary;
import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags.DynaTechFlag;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.DynaTech.DynaTechConsts.*;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.InfoScan.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils.fromMachineFuels;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byPackageName;

public class DynaTechSerializedMachineRecipes {

    private static boolean initFlag = false;
    public static void init(){
        if (initFlag){return;}
        if (DynaTechFlag){
            initFlag = true;
            registerSerializedRecipeProvider_byClassName(StardustReactor.class.getName(),
                    new SerializedRecipeProvider<StardustReactor>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable StardustReactor m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_StardustReactor(m);
                            return fromMachineFuels(stardustReactorFuels,m.getItem(),null,energyInfo[0]);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull StardustReactor m) {
                            long[] energyInfo = findEnergyInfo_StardustReactor(m);
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
            registerSerializedRecipeProvider_byClassName(CulinaryGenerator.class.getName(),
                    new SerializedRecipeProvider<CulinaryGenerator>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable CulinaryGenerator m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_CulinaryGenerator(m);
                            return fromMachineFuels(culinaryGeneratorFuels,m.getItem(),null,energyInfo[0]);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull CulinaryGenerator m) {
                            long[] energyInfo = findEnergyInfo_CulinaryGenerator(m);
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

            //@ProfElements why don't you abstract these
            SerializedRecipeProvider<AbstractElectricMachine> growthChamberSerializedRecipeProvider =
                    new SerializedRecipeProvider<>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AbstractElectricMachine m ,@Nullable ItemStack stack) {
                            if (m == null) {return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_AbstractElectricMachine(m);
                            List<MachineRecipe> machineRecipes = growthChamberRecipes.get(m.getClass());
                            return SerializeMachineRecipeUtils.fromMachineRecipe(machineRecipes,m, energyInfo[0],(int)energyInfo[1]);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull AbstractElectricMachine m) {
                            long[] energyInfo = findEnergyInfo_AbstractElectricMachine(m);
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
                    };
            registerSerializedRecipeProvider_byPackageName("me.profelements.dynatech.items.electric.growthchambers",growthChamberSerializedRecipeProvider);

            registerSerializedRecipeProvider_byClassName(MineralizedApiary.class.getName(),
                    new SerializedRecipeProvider<MineralizedApiary>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable MineralizedApiary m, @Nullable ItemStack stack) {
                            if (stack == null || m == null){return Collections.emptyList();}
                            if (!stack.hasItemMeta()){return Collections.emptyList();}
                            ItemMeta meta = stack.getItemMeta();
                            if (meta == null){return Collections.emptyList();}
                            if (meta instanceof BlockStateMeta blockStateMeta){
                                if (blockStateMeta.hasBlockState()){
                                    if (blockStateMeta.getBlockState() instanceof Beehive beehive){
                                        try {
                                            if (Field_MineralizedApiary_material == null){
                                                Field_MineralizedApiary_material = MineralizedApiary.class.getDeclaredField("material");
                                            }
                                            ItemStack generateItem = (ItemStack) getInUnsafe(m,Field_MineralizedApiary_material);
                                            int beeCount = beehive.getEntityCount();
                                            int timeCost = 60;
                                            if (beeCount != 0){
                                                timeCost = 30 - (10*(beeCount-1));
                                            }
                                            int energyPerTick = m.getEnergyConsumption();
                                            int speed = m.getSpeed();
                                            return Collections.singletonList(new SimplePair<>(
                                                    new SerializedMachine_MachineRecipe(
                                                            stack.clone(),
                                                            new MachineRecipe(
                                                                    timeCost,new ItemStack[]{null},new ItemStack[]{generateItem.clone()}
                                                    ),null,
                                                            energyPerTick,
                                                            speed,
                                                            null
                                                    ),
                                                    null
                                            ));


                                        }catch (Exception e){
                                            e.printStackTrace();
                                            return Collections.emptyList();
                                        }
                                    }
                                }
                            }
                            return Collections.emptyList();
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull MineralizedApiary m) {
                            int energyPerTick = m.getEnergyConsumption();
                            int speed = m.getSpeed();
                            int capacity = m.getCapacity();
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyPerTick
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
                                            + speed
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + capacity
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
        }
    }
}
