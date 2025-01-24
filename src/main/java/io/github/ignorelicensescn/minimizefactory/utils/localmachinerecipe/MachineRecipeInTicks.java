package io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MachineRecipeInTicks extends MachineRecipe {
    public MachineRecipeInTicks(int slimefunTicks, ItemStack[] input, ItemStack[] output) {
        super(slimefunTicks,input,output);
        this.setTicks(slimefunTicks);
    }

    public static ItemStack[] material2Stack(Material[] materials,int itemCount){
        ItemStack[] stacks = new ItemStack[materials.length];
        for (int i=0;i<materials.length;i+=1){
            stacks[i] = new ItemStack(materials[i],itemCount);
        }
        return stacks;
    }
}
