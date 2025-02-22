package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Slimefun;

import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStacksToStackRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.namemateriallore.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeWithExpectations;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.EnhancedAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.VanillaAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricDustWasher;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricGoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.FluidPump;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.vanillaRecipeArray;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.InfoScan.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Slimefun.SlimefunConsts.FLUID_PUMP_ENERGY_CONSUMPTION;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress.InfinityCompressConsts.getMultiblockAutocrafterRecipes;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_ITEM_STACK_ARRAY;

public class SlimefunSerializedMachineRecipes {
    private static boolean initFlag = false;
    public static void init(){
        if (initFlag){return;}
        initFlag = true;

        registerSerializedRecipeProvider_byClassName(ElectricDustWasher.class.getName(),
                new SerializedRecipeProvider<ElectricDustWasher>() {
                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable ElectricDustWasher m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        long[] energyInfo = findEnergyInfo_ElectricDustWasher(m);
                        List<MachineRecipeWithExpectations> machineRecipes = findRecipes_ElectricDustWasher(m);
                        return fromMachineRecipeWithExpectations(machineRecipes,m,energyInfo[0],(int)energyInfo[1]);
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull ElectricDustWasher m) {
                        long[] energyInfo = findEnergyInfo_ElectricDustWasher(m);
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

        registerSerializedRecipeProvider_byClassName(ElectricGoldPan.class.getName(),
                new SerializedRecipeProvider<ElectricGoldPan>() {

                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable ElectricGoldPan m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        long[] energyInfo = findEnergyInfo_ElectricGoldPan(m);
                        List<MachineRecipeWithExpectations> machineRecipes = findRecipes_ElectricGoldPan(m);
                        return fromMachineRecipeWithExpectations(machineRecipes,m,energyInfo[0],(int)energyInfo[1]);
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull ElectricGoldPan m) {
                        long[] energyInfo = findEnergyInfo_ElectricGoldPan(m);
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

        registerSerializedRecipeProvider_byClassName(AContainer.class.getName(),
                new SerializedRecipeProvider<AContainer>() {

                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AContainer m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        long[] energyInfo = findEnergyInfo_AContainer(m);
                        List<MachineRecipe> machineRecipes = findRecipes(m);
                        return fromMachineRecipe(machineRecipes,m,energyInfo[0],(int)energyInfo[1]);
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull AContainer m) {
                        long[] energyInfo = findEnergyInfo_AContainer(m);
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

        registerSerializedRecipeProvider_byClassName(AGenerator.class.getName(),
                new SerializedRecipeProvider<AGenerator>() {

                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AGenerator m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        long[] energyInfo = findEnergyInfo_AGenerator(m);
                        Set<MachineFuel> machineRecipes = findFuels(m);
                        return fromMachineFuels(machineRecipes,m,energyInfo[0]);
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull AGenerator m) {
                        long[] energyInfo = findEnergyInfo_AGenerator(m);
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

        registerSerializedRecipeProvider_byClassName(Reactor.class.getName(),
                new SerializedRecipeProvider<Reactor>() {
                    @Nonnull
                    @Override
                    public SimplePair<String, List<String>> getNameAndLoreForRecipe(@Nullable Reactor reactor, SimplePair<SerializedMachine_MachineRecipe,ItemStack> serializedPair, int index) {
                        SerializedMachine_MachineRecipe serialized = serializedPair.first;
                        if (reactor == null){new Exception("reactor not found!").printStackTrace();return new SimplePair<>("",Collections.emptyList());}
                        ItemStack itemStackIn = serialized.inputs == null ? null:serialized.inputs[0];
                        ItemStack itemStackOut = serialized.outputs == null ? null:serialized.outputs[0];
                        List<String> lore = new ArrayList<>(7);
                        if (itemStackIn != null){
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Input"));
                            lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(itemStackIn));
                        }
                        boolean coolantFlag = (reactor.getCoolant() != null);
                        if (coolantFlag){
                            lore.add(
                                    ChatColor.WHITE + NameUtil.findName(reactor.getCoolant())
                                            + ChatColor.WHITE + properties.getReplacedProperty("Test_InfoProvider_Info_Coolant_Unit")
                                            + ((serialized.ticks / COOLANT_DURATION) + ((serialized.ticks % COOLANT_DURATION) == 0 ? 0 : 1))
                            );
                        }
                        lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
                        if (itemStackOut != null){
                            lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(itemStackOut));
                        }else if (isBucket(itemStackIn)){
                            lore.add(ChatColor.WHITE + NameUtil.nameForMaterial(Material.BUCKET)
                                    + " "
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_Output_Unit")
                                    + itemStackIn.getAmount());
                        }
                        else {
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_NoItem"));
                        }

                        String name = properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                + serialized.ticks
                                + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit");
                        return new SimplePair<>(name,lore);
                    }

                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable Reactor reactor ,@Nullable ItemStack stack) {
                        if (reactor == null){return Collections.emptyList();}
                        long[] energyInfo = findEnergyInfo_Reactor(reactor);
                        Set<MachineFuel> machineRecipes = findFuels_Reactor(reactor);
                        boolean coolantFlag = (reactor.getCoolant() != null);
                        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(machineRecipes.size());
                        for (MachineFuel fuel:machineRecipes){
                            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(reactor.getItem(),fuel, energyInfo[0]);
                            if (coolantFlag){
                                int ticks = serialized.ticks;
                                ItemStack coolant = reactor.getCoolant().clone();

                                int coolantCount = ((ticks / COOLANT_DURATION) + ((ticks % COOLANT_DURATION) == 0 ? 0 : 1));
                                coolant.setAmount(coolant.getMaxStackSize());
                                int divide = coolantCount / coolant.getMaxStackSize();
                                List<ItemStack> coolants = new ArrayList<>(divide);
                                for (int j = 0; j < (divide); j+=1) {
                                    coolants.add(coolant.clone());
                                }
                                int remain = coolantCount % coolant.getMaxStackSize();
                                if (remain != 0){
                                    coolant.setAmount(remain);
                                    coolants.add(coolant);
                                }
                                List<ItemStack> cache = serialized.inputs == null ? new ArrayList<>() : new ArrayList<>(List.of(serialized.inputs));
                                cache.addAll(coolants);
                                serialized.inputs = cache.toArray(EMPTY_ITEM_STACK_ARRAY);
                            }
                            result.add(new SimplePair<>(serialized,null));
                        }
                        return result;
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull Reactor m) {
                        long[] energyInfo = findEnergyInfo_Reactor(m);
                        return new String[]{
                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                        + energyInfo[0]
                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                properties.getReplacedProperty("Test_InfoProvider_Info_Coolant_Time")
                                        + energyInfo[1]
                                        + properties.getReplacedProperty("Test_InfoProvider_Info_Coolant_Time_Unit"),
                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                        + energyInfo[2]
                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                        };
                    }
                });

        registerSerializedRecipeProvider_byClassName(SolarGenerator.class.getName(),
                new SerializedRecipeProvider<SolarGenerator>() {
                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SolarGenerator m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        long[] energyInfo = findEnergyInfo_SolarGenerator(m);
                        return fromSolarGen(m,energyInfo[0],energyInfo[1]);
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull SolarGenerator m) {
                        long[] energyInfo = findEnergyInfo_SolarGenerator(m);
                        List<String> lore = new ArrayList<>(Arrays.asList(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNightOrEnd").split("\n")));
                        lore.set(lore.size() - 1,lore.get(lore.size()-1) + energyInfo[1]
                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
                        lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                + energyInfo[2]
                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"));
                        lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDayOrNether")
                                + energyInfo[0]
                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
                        return lore.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
                    }
                });

        registerSerializedRecipeProvider_byClassName(FluidPump.class.getName(),
                new SerializedRecipeProvider<FluidPump>() {
                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable FluidPump m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                m.getItem(),
                                new MachineRecipeInTicks(
                                        1,new ItemStack[]{new ItemStack(Material.BUCKET)},new ItemStack[]{new ItemStack(Material.WATER_BUCKET)}
                                ),
                                FLUID_PUMP_ENERGY_CONSUMPTION
                        );
                        return Collections.singletonList(new SimplePair<>(serialized,null));
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull FluidPump m) {
                        long[] energyInfo = new long[]{FLUID_PUMP_ENERGY_CONSUMPTION,1,(m).getCapacity()};
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

        registerSerializedRecipeProvider_byClassName(EnhancedAutoCrafter.class.getName(),
                new SerializedRecipeProvider<EnhancedAutoCrafter>() {

                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable EnhancedAutoCrafter m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        long[] energyInfo = new long[]{m.getEnergyConsumption(),1,m.getCapacity()};
                        ItemStacksToStackRecipe[] machineRecipes = getMultiblockAutocrafterRecipes(m);
                        return SerializeMachineRecipeUtils.fromInputsAndSingleOutput(machineRecipes,m,
                                energyInfo[0],1);
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull EnhancedAutoCrafter m) {
                        long[] energyInfo = new long[]{m.getEnergyConsumption(),1,m.getCapacity()};
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

        registerSerializedRecipeProvider_byClassName(VanillaAutoCrafter.class.getName(),
                new SerializedRecipeProvider<VanillaAutoCrafter>() {

                    @Nonnull
                    @Override
                    public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable VanillaAutoCrafter m ,@Nullable ItemStack stack) {
                        if (m == null){return Collections.emptyList();}
                        long[] energyInfo = new long[]{m.getEnergyConsumption(),1,m.getCapacity()};

                        ItemStacksToStackRecipe[] machineRecipes = vanillaRecipeArray;
                        return SerializeMachineRecipeUtils.fromInputsAndSingleOutput(machineRecipes,m,
                                energyInfo[0],1);
                    }

                    @Nonnull
                    @Override
                    public String[] getEnergyInfoStrings(@Nonnull VanillaAutoCrafter m) {
                        long[] energyInfo = new long[]{m.getEnergyConsumption(),1,m.getCapacity()};
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
