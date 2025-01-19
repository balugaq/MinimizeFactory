package io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TweakedMachineFuel{
    public final int ticks;
    public final ItemStack[] fuel;
    public final ItemStack[] output;
    public final int hash;
    public TweakedMachineFuel(int seconds, ItemStack[] fuel) {
        this(seconds, fuel, null);
    }

    public TweakedMachineFuel(int seconds, ItemStack[] fuel, ItemStack[] output) {
        Validate.notNull(fuel, "Fuel must never be null!");
        Validate.isTrue(seconds > 0, "Fuel must last at least one second!");

        this.ticks = seconds * 2;

        this.fuel = new ItemStack[fuel.length];
        for (int i=0;i<fuel.length;i++){
            this.fuel[i] = fuel[i].clone();
        }

        this.output = new ItemStack[output.length];
        for (int i=0;i<output.length;i++){
            this.output[i] = output[i].clone();
        }
        this.hash = ticks*31*31 + Arrays.hashCode(fuel) *31 + Arrays.hashCode(output);
    }

    public ItemStack[] getInput() {
        return fuel;
    }

    public ItemStack[] getOutput() {
        return output;
    }

    /**
     * This method returns how long this {@link MachineFuel} lasts.
     * The result represents Slimefun ticks.
     *
     * @return How many ticks this fuel type lasts
     */
    public int getTicks() {
        return ticks;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof TweakedMachineFuel that)) return false;

        return ticks == that.ticks
                && Arrays.equals(fuel, that.fuel)
                && Arrays.equals(output, that.output);
    }
}
