package io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.emptyItemStackArray;

public class MachineRecipeInTicks extends MachineRecipe {
    public MachineRecipeInTicks(int ticks, ItemStack[] input, ItemStack[] output) {
        super(ticks,input,output);
        this.setTicks(ticks);
    }

    /**
     * StoneworksFactory uses.
     */
    public static MachineRecipeInTicks recipe_B_after_A(MachineRecipeInTicks recipeA,MachineRecipeInTicks recipeB){
        ItemStack[] inputA = recipeA.getInput();
        ItemStack[] outputA = recipeA.getOutput();
        List<ItemStack> tempOutAList = new ArrayList<>();
        List<ItemStack> tempInBList = new ArrayList<>();
        ItemStack[] inputB = recipeB.getInput();
        ItemStack[] outputB = recipeB.getOutput();
        for (ItemStack iB:inputB){
            for (ItemStack oA:outputA){
                if (
                        (SlimefunItem.getByItem(iB) == null && SlimefunItem.getByItem(oA) != null)
                        || SlimefunItem.getByItem(iB) != null && SlimefunItem.getByItem(oA) == null
                ){
                    continue;
                }
                if (SlimefunItem.getByItem(iB) == null){
                    if (iB.equals(oA)){
                        int amt = iB.getAmount() - oA.getAmount();
                        if (amt > 0){
                            iB.setAmount(amt);
                            oA.setAmount(0);
                        }else {
                            iB.setAmount(0);
                            oA.setAmount(amt * (-1));
                        }
                    }
                }
                else if (SlimefunUtils.isItemSimilar(iB,oA,true,false)){
                    int amt = iB.getAmount() - oA.getAmount();
                    if (amt > 0){
                        iB.setAmount(amt);
                        oA.setAmount(0);
                    }else {
                        iB.setAmount(0);
                        oA.setAmount(amt * (-1));
                    }
                }
            }
        }
        for (ItemStack iB:inputB){
            if (iB.getAmount() > 0){
                tempInBList.add(iB);
            }
        }
        for (ItemStack oA:outputA){
            if (oA.getAmount() > 0){
                tempOutAList.add(oA);
            }
        }
        return new MachineRecipeInTicks(recipeA.getTicks() + recipeB.getTicks(),
                (ItemStack[]) ArrayUtils.addAll(tempInBList.toArray(emptyItemStackArray),inputA),
                (ItemStack[]) ArrayUtils.addAll(tempOutAList.toArray(emptyItemStackArray), outputB)
        );
    }

    public static ItemStack[] material2Stack(Material[] materials,int itemCount){
        ItemStack[] stacks = new ItemStack[materials.length];
        for (int i=0;i<materials.length;i++){
            stacks[i] = new ItemStack(materials[i],itemCount);
        }
        return stacks;
    }
}
