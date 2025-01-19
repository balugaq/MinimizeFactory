package io.github.ignorelicensescn.minimizeFactory.utils;

import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.IntegerRational;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.*;

import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.emptyItemStackArray;

public class ItemStackUtil {
    public static ItemStack[] collapseItems(ItemStack[] array,ItemStack... additional){
        ItemStack[] allTogether = new ItemStack[array.length + additional.length];
        System.arraycopy(array, 0, allTogether, 0, array.length);
        System.arraycopy(additional, 0, allTogether, array.length, additional.length);
        return collapseItems(allTogether);
    }
    public static ItemStack[] collapseItems(ItemStack[] array){
        Map<ItemStack,Integer> amounts = new HashMap<>();
        for (ItemStack i:array){
            if (i == null){continue;}
            i = i.clone();
            int amount = i.getAmount();
            i.setAmount(1);
            amounts.put(i,amounts.getOrDefault(i,0) + amount);
        }
        Set<Map.Entry<ItemStack,Integer>> entries = amounts.entrySet();
        int counter = 0;
        ItemStack[] inputs = new ItemStack[entries.size()];
        for (Map.Entry<ItemStack,Integer> itemAndAmount:entries){
            inputs[counter] = itemAndAmount.getKey();
            inputs[counter].setAmount(itemAndAmount.getValue());
            counter += 1;
        }
        return inputs;
    }
    public static final IntegerRational[] emptyDividingsArray = new IntegerRational[0];
    public static final BigRational[] emptyDividingsArray_BigInteger = new BigRational[0];
    //does not check amount
    public static boolean isItemStackSimilar(ItemStack i1,ItemStack i2){
        return SlimefunUtils.isItemSimilar(i1, i2, false, false, true);
//        i1 = i1.clone();
//        i1.setAmount(1);
//        i2 = i2.clone();
//        i2.setAmount(1);
//        ItemMeta meta1 = i1.getItemMeta();
//        ItemMeta meta2 = i2.getItemMeta();
//        if (( (meta1 == null) && !(meta2 == null) )
//                || ( !(meta1 == null) && (meta2 == null) )
//        ){return false;}//different item meta stats
//
//        //same item meta stats
//        if (meta1 != null){//both have
//            SlimefunItem sfi1 = SlimefunItem.getByItem(i1);
//            SlimefunItem sfi2 = SlimefunItem.getByItem(i2);
//            if ((sfi1 != null) && (sfi2 != null))
//            {
//                return SlimefunUtils.isItemSimilar(i2, i1, false, false, true);
//            }
//            return Objects.equals(i1,i2);
//        }
//
//        return Objects.equals(i1,i2);

//        return i1.getType().equals(i2.getType());
    }

    public static ItemStack[] RecipeChoiceListToItemStackArray_formated(List<RecipeChoice> recipeChoices){
        List<ItemStack> itemStacks = new ArrayList<>();
        for (RecipeChoice rc:recipeChoices){
            if (rc == null){continue;}
            itemStacks.add(rc.getItemStack());
        }
        return collapseItems(itemStacks.toArray(emptyItemStackArray));
    }

}
