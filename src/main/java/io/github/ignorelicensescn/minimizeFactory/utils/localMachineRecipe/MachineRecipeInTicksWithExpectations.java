package io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe;

import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.IntegerRational;
import org.bukkit.inventory.ItemStack;

public class MachineRecipeInTicksWithExpectations extends MachineRecipeInTicks{
    public IntegerRational[] expectations;
    public MachineRecipeInTicksWithExpectations(int ticks, ItemStack[] input, ItemStack[] output, IntegerRational[] expectations) {
        super(ticks, input, output);
        this.expectations = expectations;
    }
}
