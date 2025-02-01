package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress;

import io.github.acdeasdff.infinityCompress.items.Multiblock_Autocrafter;
import io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStacksToStackRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.EnhancedAutoCrafter;
import io.ncbpfluffybear.fluffymachines.objects.AutoCrafter;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.*;

import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;

public class InfinityCompressConsts {

    /**
     * Multiblock_Autocrafter_ID List< Pair< Input,Output > >
     */
    public static final Map<String, ItemStacksToStackRecipe[]> multiblockAutocrafterRecipes = new HashMap<>();

    public static ItemStacksToStackRecipe[] getMultiblockAutocrafterRecipes(Multiblock_Autocrafter sfItem) {
        try {
            if (multiblockAutocrafterRecipes.containsKey(sfItem.getId())){
                return multiblockAutocrafterRecipes.get(sfItem.getId());
            }
            Field f = sfItem.getClass().getSuperclass().getDeclaredField("mblock");
            f.setAccessible(true);
            MultiBlockMachine machine = (MultiBlockMachine) getInUnsafe(sfItem,f);
            List<ItemStack[]> itemIn = RecipeType.getRecipeInputList(machine);
            List<ItemStacksToStackRecipe> recipes = new ArrayList<>(itemIn.size());
            for (ItemStack[] itemStacks:itemIn){
                ItemStack output = RecipeType.getRecipeOutputList(machine,itemStacks);
                if (output != null)
                    recipes.add(new ItemStacksToStackRecipe(ItemStackUtil.collapseItems(itemStacks),output));
            }
            ItemStacksToStackRecipe[] result = recipes.toArray(EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE);
            multiblockAutocrafterRecipes.put(sfItem.getId(),recipes.toArray(EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE));
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE;
        }
    }
    public static ItemStacksToStackRecipe[] getMultiblockAutocrafterRecipes(EnhancedAutoCrafter sfItem) {
        if (multiblockAutocrafterRecipes.containsKey(sfItem.getId())){
            return multiblockAutocrafterRecipes.get(sfItem.getId());
        }
        MultiBlockMachine machine = (MultiBlockMachine) RecipeType.ENHANCED_CRAFTING_TABLE.getMachine();
        List<ItemStack[]> itemIn = RecipeType.getRecipeInputList(machine);
        List<ItemStacksToStackRecipe> recipes = new ArrayList<>(itemIn.size());
        for (ItemStack[] itemStacks:itemIn){
            ItemStack output = RecipeType.getRecipeOutputList(machine,itemStacks);
            if (output != null)
                recipes.add(new ItemStacksToStackRecipe(ItemStackUtil.collapseItems(itemStacks),output));
        }

        ItemStacksToStackRecipe[] result = recipes.toArray(EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE);
        multiblockAutocrafterRecipes.put(sfItem.getId(),recipes.toArray(EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE));
        return result;
    }

    //in fact,this is for FluffyMachine
    //however,I already copied FluffyMachine's AutoCrafter code to InfinityCompress Multiblock Autocrafter.
    public static ItemStacksToStackRecipe[] getMultiblockAutocrafterRecipes(AutoCrafter sfItem){
        ItemStacksToStackRecipe[] result = EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE;
        try {
            if (multiblockAutocrafterRecipes.containsKey(sfItem.getId())){
                return multiblockAutocrafterRecipes.get(sfItem.getId());
            }
            Field f = AutoCrafter.class.getDeclaredField("mblock");
            f.setAccessible(true);
            MultiBlockMachine machine = (MultiBlockMachine) getInUnsafe(sfItem,f);
            List<ItemStack[]> itemIn = RecipeType.getRecipeInputList(machine);
            List<ItemStacksToStackRecipe> recipes = new ArrayList<>(itemIn.size());
            for (ItemStack[] itemStacks:itemIn){
                ItemStack output = RecipeType.getRecipeOutputList(machine,itemStacks);
                if (output != null) {
                    recipes.add(new ItemStacksToStackRecipe(ItemStackUtil.collapseItems(itemStacks), output));
                }
            }
            result = recipes.toArray(EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE);
            multiblockAutocrafterRecipes.put(sfItem.getId(),result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
