package io.github.ignorelicensescn.minimizeFactory.utils;

import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;

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

    public static final BigRational[] emptyDividingsArray_BigInteger = new BigRational[0];
    //does not check amount
    public static boolean isItemStackSimilar(ItemStack i1,ItemStack i2){
        ItemMeta m1 = i1.hasItemMeta()?i1.getItemMeta():null;
        ItemMeta m2 = i2.hasItemMeta()?i2.getItemMeta():null;
        SlimefunItem sfItem1 = SlimefunItem.getByItem(i1);
        SlimefunItem sfItem2 = SlimefunItem.getByItem(i2);

        if (m1 != null && m2 != null){
            if (sfItem1 instanceof DistinctiveItem d) {
                if (!d.canStack(m1,m2)) {
                    return false;
                }
            }else if (sfItem2 instanceof DistinctiveItem d) {
                if (!d.canStack(m1,m2)) {
                    return false;
                }
            }
        }
        if (i1 instanceof SlimefunItemStack s1){
            if (i2 instanceof SlimefunItemStack s2){
                if (m1 != null && m2 != null){
                    if (s1 instanceof DistinctiveItem d) {
                        if (!d.canStack(m1, m2)) {
                            return false;
                        }
                    } else if (s2 instanceof DistinctiveItem d) {
                        if (!d.canStack(m1, m2)) {
                            return false;
                        }
                    }
                }
            }
        }
        return SlimefunUtils.isItemSimilar(i1, i2, false, false, true);
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
