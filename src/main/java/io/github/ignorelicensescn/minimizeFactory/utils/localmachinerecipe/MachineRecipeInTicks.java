package io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MachineRecipeInTicks extends MachineRecipe {
    public MachineRecipeInTicks(int ticks, ItemStack[] input, ItemStack[] output) {
        super(ticks,input,output);
        this.setTicks(ticks);
    }

    public static ItemStack[] material2Stack(Material[] materials,int itemCount){
        ItemStack[] stacks = new ItemStack[materials.length];
        for (int i=0;i<materials.length;i+=1){
            stacks[i] = new ItemStack(materials[i],itemCount);
        }
        return stacks;
    }
}
