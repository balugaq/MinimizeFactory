package io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated;

import io.github.ignorelicensescn.minimizefactory.utils.LoreGetter;
import io.github.ignorelicensescn.minimizefactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.enumsets.MaterialSet;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static io.github.ignorelicensescn.minimizefactory.utils.NameUtil.NULL_STRING;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_ITEM_STACK_ARRAY;

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
        //I implemented DistinctiveItem to SlimefunItem but it checks (SlimefunItemStack instanceof DistinctiveItem) wtf???
        //SlimefunItemStack and SlimefunItem are not the same class and has no extend relationship
        return SlimefunUtils.isItemSimilar(i1, i2, false, false, true);
    }

    public static ItemStack[] RecipeChoiceListToItemStackArray_formated(List<RecipeChoice> recipeChoices){
        List<ItemStack> itemStacks = new ArrayList<>();
        for (RecipeChoice rc:recipeChoices){
            if (rc == null){continue;}
            itemStacks.add(rc.getItemStack());
        }
        return collapseItems(itemStacks.toArray(EMPTY_ITEM_STACK_ARRAY));
    }

    public static final MaterialSet validItemSet = new MaterialSet();
    static {
        for (String s:new String[]{"CAVE_AIR","LEGACY_AIR","AIR"}){
            Material tryGetMaterial = MaterialUtil.valueOf(s);
            if (tryGetMaterial == null){
                continue;
            }
            validItemSet.add(tryGetMaterial);
        }
    }
    public static boolean isItemStackValid(@Nullable ItemStack stack){
        if (stack == null){return false;}
        return validItemSet.contains(stack.getType());
    }
    public static boolean itemStackArrayEquals(@Nullable ItemStack[] A,@Nullable ItemStack[] B){
        if (A == null && B != null){return false;}
        if (A != null && B == null){return false;}
        if (A==null /*&& B==null*/){return true;}
        if (A.length != B.length){return false;}
        for (int i=0;i<A.length;i++){
            if (!ItemStackUtil.isItemStackSimilar(A[i],B[i])){return false;}
        }
        return true;
    }
    @Nullable
    public static ItemMeta tryGetItemMeta(ItemStack stack){
        if (stack == null){return null;}
        if (!stack.hasItemMeta()){return null;}
        return stack.getItemMeta();
    }
    @Nonnull
    public static String toString(ItemStack stack){
        if (stack == null){return NULL_STRING;}
        StringBuilder sb = new StringBuilder();
        sb.append(NameUtil.findName(stack));
        for (String s:LoreGetter.tryGetLore(stack)){
            sb.append(s);
        }
        sb.append(tryGetItemMeta(stack));
        sb.append("amount:").append(stack.getAmount());
        return sb.toString();
    }
}
