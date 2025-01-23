package io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe;

import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.inventory.ItemStack;

//"Expectations" is rational
public class MachineRecipeWithExpectations extends MachineRecipe {
    public final IntegerRational[] expectations;
    public MachineRecipeWithExpectations(int seconds, ItemStack[] input, ItemStack[] output, IntegerRational[] expectations) {
        super(seconds, input, output);
        this.expectations = expectations;
        assert output.length == expectations.length;
    }
}
