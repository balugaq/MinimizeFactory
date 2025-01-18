package io.github.ignorelicensescn.minimizeFactory.utils;

import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.Validate;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public class TweakedMachineFuel{
    public final int ticks;
    public final ItemStack[] fuel;
    public final ItemStack[] output;
    public TweakedMachineFuel(int seconds, ItemStack[] fuel) {
        this(seconds, fuel, null);
    }

    public TweakedMachineFuel(int seconds, ItemStack[] fuel, ItemStack[] output) {
        Validate.notNull(fuel, "Fuel must never be null!");
        Validate.isTrue(seconds > 0, "Fuel must last at least one second!");

        this.ticks = seconds * 2;
        this.fuel = fuel;
        this.output = output;
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

}
