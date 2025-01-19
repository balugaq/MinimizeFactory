package io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.emptyItemStackArray;

public class MachineRecipeOutEntity extends MachineRecipe {
    public int ticks;
    public final ItemStack[] input;
    public final Class<? extends Entity> output;

    public MachineRecipeOutEntity(int seconds, ItemStack[] input, Class<? extends Entity> output) {
        super(seconds, input, emptyItemStackArray);
        this.input = input;
        this.output = output;
    }

    public ItemStack[] getInput() {
        return this.input;
    }

    public Class<? extends Entity> getOutputEntityClass() {
        return this.output;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public int getTicks() {
        return ticks;
    }
}
