package io.github.ignorelicensescn.minimizeFactory.utils;

import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeWithExpectations;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SerializeMachineRecipeUtils {

    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipe(
            Collection<MachineRecipe> machineRecipes,
            SlimefunItem sfItem,
            long consumption,
            int speed){
        return fromMachineRecipe(machineRecipes,sfItem.getItem(),null,consumption);
    }
    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipe(
            Collection<MachineRecipe> machineRecipes,
            ItemStack slimefunItemStack,
            long consumption,
            int speed){
        return fromMachineRecipe(machineRecipes,slimefunItemStack,null,consumption);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipe(
            Collection<MachineRecipe> machineRecipes,
            ItemStack slimefunItemStack,
            ItemStack catalyzer,
            long consumption){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
        for (MachineRecipe recipe : machineRecipes) {
            SerializedMachine_MachineRecipe serialized =
                    new SerializedMachine_MachineRecipe(
                            slimefunItemStack,
                            recipe,
                            consumption
                    );
            result.add(new SimplePair<>(serialized, catalyzer));
        }
        return result;
    }
    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineFuel(
            Collection<MachineFuel> machineRecipes,
            SlimefunItem sfItem,
            long consumption){
        return fromMachineFuel(machineRecipes,sfItem.getItem(),null,consumption);
    }
    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineFuel(
            Collection<MachineFuel> machineRecipes,
            ItemStack slimefunItemStack,
            long consumption){
        return fromMachineFuel(machineRecipes,slimefunItemStack,null,consumption);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineFuel(
            Collection<MachineFuel> machineRecipes,
            ItemStack slimefunItemStack,
            ItemStack catalyzer,
            long consumption){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
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
        return fromMachineRecipeWithExpectations(machineRecipes,sfItem.getItem(),null,consumption);
    }
    @Nonnull
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipeWithExpectations(
            Collection<MachineRecipeWithExpectations> machineRecipes,
            ItemStack slimefunItemStack,
            long consumption,
            int speed){
        return fromMachineRecipeWithExpectations(machineRecipes,slimefunItemStack,null,consumption);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> fromMachineRecipeWithExpectations(
            Collection<MachineRecipeWithExpectations> machineRecipes,
            ItemStack slimefunItemStack,
            ItemStack catalyzer,
            long consumption){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
        for (MachineRecipeWithExpectations recipe : machineRecipes) {
            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                    slimefunItemStack,
                    recipe,
                    consumption
            );
            result.add(new SimplePair<>(serialized, catalyzer));
        }
        return result;
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>>
    fromInputsAndSingleOutput(SimplePair<ItemStack[],ItemStack>[] recipes,
                              SlimefunItem sfItem,
                              long consumption,
                              int speed){
        return fromInputsAndSingleOutput(List.of(recipes),sfItem.getItem(),consumption,speed);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>>
    fromInputsAndSingleOutput(Collection<SimplePair<ItemStack[],ItemStack>> recipes,
                                            SlimefunItem sfItem,
                                            long consumption,
                                            int speed){
        return fromInputsAndSingleOutput(recipes,sfItem.getItem(),consumption,speed);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>>
    fromInputsAndSingleOutput(Collection<SimplePair<ItemStack[],ItemStack>> recipes,
                              ItemStack machineItem,
                              long consumption,
                              int speed){
        return fromInputsAndSingleOutput(recipes,machineItem,null,consumption);
    }
    public static List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>>
    fromInputsAndSingleOutput(Collection<SimplePair<ItemStack[],ItemStack>> recipes,
                              ItemStack machineItem,
                              ItemStack catalyzer,
                              long consumption){
        List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
        for (SimplePair<ItemStack[],ItemStack> recipe:recipes){
            MachineRecipeInTicks machineRecipeInTicks = new MachineRecipeInTicks(1,recipe.first,new ItemStack[]{recipe.second});
            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                    machineItem,
                    machineRecipeInTicks,
                    consumption
            );
            result.add(new SimplePair<>(serialized,catalyzer));
        }
        return result;
    }
}
