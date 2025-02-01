package io.github.ignorelicensescn.minimizefactory.utils.recipesupport;

import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStacksToStackRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicksWithExpectations;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeWithExpectations;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SerializeMachineRecipeUtils {

    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipe(
            Collection<MachineRecipe> machineRecipes,
            SlimefunItem sfItem,
            long consumption,
            int speed){
        return fromMachineRecipe(machineRecipes,sfItem.getItem(),null,consumption,speed);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipe(
            Collection<MachineRecipe> machineRecipes,
            ItemStack slimefunItemStack,
            ItemStack catalyzer,
            long consumption,
            int speed){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(machineRecipes.size());
        for (MachineRecipe recipe : machineRecipes) {
            //cookieSlime has already divided speed
            MachineRecipeInTicks inTicks = new MachineRecipeInTicks(Math.max(1,/*recipe.getTicks() / speed*/ recipe.getTicks()),recipe.getInput(),recipe.getOutput());
            SerializedMachine_MachineRecipe serialized =
                    new SerializedMachine_MachineRecipe(
                            slimefunItemStack,
                            inTicks,
                            consumption
                    );
            result.add(new SimplePair<>(serialized, catalyzer));
        }
        return result;
    }
    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineFuels(
            Collection<MachineFuel> machineRecipes,
            SlimefunItem sfItem,
            long consumption){
        return fromMachineFuels(machineRecipes,sfItem.getItem(),null,consumption);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineFuels(
            Collection<MachineFuel> machineRecipes,
            ItemStack slimefunItemStack,
            ItemStack catalyzer,
            long consumption){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(machineRecipes.size());
        for (MachineFuel fuel : machineRecipes) {
            SerializedMachine_MachineRecipe serialized =
                    new SerializedMachine_MachineRecipe(
                            slimefunItemStack,
                            fuel,
                            consumption
                    );
            result.add(new SimplePair<>(serialized, catalyzer));
        }
        return result;
    }
    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipeWithExpectations(
            Collection<MachineRecipeWithExpectations> machineRecipes,
            SlimefunItem sfItem,
            long consumption,
            int speed){
        return fromMachineRecipeWithExpectations(machineRecipes,sfItem.getItem(),null,consumption,speed);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipeWithExpectations(
            Collection<MachineRecipeWithExpectations> machineRecipes,
            ItemStack slimefunItemStack,
            ItemStack catalyzer,
            long consumption,
            int speed){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(machineRecipes.size());
        for (MachineRecipeWithExpectations recipe : machineRecipes) {
            MachineRecipeInTicksWithExpectations inTicks = new MachineRecipeInTicksWithExpectations(
                    Math.max(recipe.getTicks()/speed,1),recipe
            );
            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                    slimefunItemStack,
                    inTicks,
                    consumption
            );
            result.add(new SimplePair<>(serialized, catalyzer));
        }
        return result;
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>>
    fromInputsAndSingleOutput(ItemStacksToStackRecipe[] recipes,
                              SlimefunItem sfItem,
                              long consumption,
                              int speed){
        return fromInputsAndSingleOutput(List.of(recipes),sfItem.getItem(),consumption,speed);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>>
    fromInputsAndSingleOutput(Collection<ItemStacksToStackRecipe> recipes,
                              ItemStack machineItem,
                              long consumption,
                              int speed){
        return fromInputsAndSingleOutput(recipes,machineItem,null,consumption,speed);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>>
    fromInputsAndSingleOutput(Collection<ItemStacksToStackRecipe> recipes,
                              ItemStack machineItem,
                              ItemStack catalyzer,
                              long consumption,
                              int speed){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(recipes.size());
        for (ItemStacksToStackRecipe recipe:recipes){
            MachineRecipeInTicks machineRecipeInTicks = new MachineRecipeInTicks(1,recipe.input(),new ItemStack[]{recipe.output()});
            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                    machineItem,
                    machineRecipeInTicks,
                    consumption
            );
            result.add(new SimplePair<>(serialized,catalyzer));
        }
        return result;
    }

    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromCraftingTableLikeRecipes(
            Collection<ItemStacksToStackRecipe> recipes,
            ItemStack machineItem,
            ItemStack catalyzer,
            long consumption
            ){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(recipes.size());
        for (ItemStacksToStackRecipe workBenchRecipe:recipes){
            MachineRecipeInTicks machineRecipeInTicks = new MachineRecipeInTicks(
                    1,
                    workBenchRecipe.input(),
                    new ItemStack[]{workBenchRecipe.output()}
            );
            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                    machineItem,
                    machineRecipeInTicks,
                    consumption
            );
            result.add(new SimplePair<>(serialized,catalyzer));
        }
        return result;
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromCraftingTableLikeRecipes(
            ItemStacksToStackRecipe[] recipes,
            ItemStack machineItem,
            ItemStack catalyzer,
            long consumption
    ){
        return fromCraftingTableLikeRecipes(List.of(recipes),machineItem,catalyzer,consumption);
    }


    public static List<SimplePair<SerializedMachine_MachineRecipe,ItemStack>> fromBioAndEnvOutputs(
            Collection<SimplePair<BiomeAndEnvironment,SimplePair<ItemStack[], IntegerRational[]>>> recipes,
            ItemStack machineItem,
            ItemStack catalyzer,
            long consumption,
            int ticks
    ){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(recipes.size());
        for (SimplePair<BiomeAndEnvironment,SimplePair<ItemStack[],IntegerRational[]>> r
                :recipes){
            BiomeAndEnvironment bioAndEnv = r.first;
            SimplePair<ItemStack[],IntegerRational[]> outputPair = r.second;
            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                    machineItem,
                    new MachineRecipeInTicks(
                            ticks,
                            null,
                            outputPair.first),
                    consumption,
                    1,
                    outputPair.second
            );
            serialized.env = bioAndEnv.environment();
            serialized.biome = bioAndEnv.biome();
            result.add(new SimplePair<>(serialized,catalyzer));
        }
        return result;
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe,ItemStack>> fromCatalyzerAndOutputs(
            SimplePair<ItemStack,ItemStack[]>[] recipes,
            ItemStack machineItem,
            int ticks,
            long energyConsumption,
            int speed
    ){
        return fromCatalyzerAndOutputs(List.of(recipes),machineItem,ticks,energyConsumption,speed);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe,ItemStack>> fromCatalyzerAndOutputs(
            Collection<SimplePair<ItemStack,ItemStack[]>> recipes,
            ItemStack machineItem,
            int ticks,
            long energyConsumption,
            int speed
    ){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(recipes.size());
        for (var recipe:recipes){
            MachineRecipeInTicks machineRecipeInTicks =
                    new MachineRecipeInTicks(
                            Math.max(ticks/speed,1),
                            new ItemStack[]{},
                            recipe.second);
            SerializedMachine_MachineRecipe serialized =
                    new SerializedMachine_MachineRecipe(
                            machineItem,
                            machineRecipeInTicks,
                            energyConsumption
                    );
            result.add(new SimplePair<>(serialized,recipe.first));
        }
        return result;
    }

    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromSolarGen(
            SlimefunItem sfItem,
            long energyPerTick,
            long energyPerTickAtNight
            ){
        SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe();
        serialized.sfItem = sfItem;
        serialized.sfItemStack = sfItem.getItem();
        serialized.energyPerTick = energyPerTick;
        serialized.energyPerTickAtNight = energyPerTickAtNight;
        return Collections.singletonList(new SimplePair<>(serialized,null));
    }
}
