package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress;

import io.github.acdeasdff.infinityCompress.items.Multiblock_Autocrafter;
import io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
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
    public static final Map<String, List<SimplePair<ItemStack[],ItemStack>>> multiblockAutocrafterRecipes = new HashMap<>();

    public static List<SimplePair<ItemStack[], ItemStack>> getMultiblockAutocrafterRecipes(Multiblock_Autocrafter sfItem) {
        try {
            if (multiblockAutocrafterRecipes.containsKey(sfItem.getId())){
                return multiblockAutocrafterRecipes.get(sfItem.getId());
            }
            Field f = sfItem.getClass().getSuperclass().getDeclaredField("mblock");
            f.setAccessible(true);
            MultiBlockMachine machine = (MultiBlockMachine) getInUnsafe(sfItem,f);
            List<ItemStack[]> itemIn = RecipeType.getRecipeInputList(machine);
            List<SimplePair<ItemStack[],ItemStack>> recipes = new ArrayList<>();
            for (ItemStack[] itemStacks:itemIn){
                ItemStack output = RecipeType.getRecipeOutputList(machine,itemStacks);
                if (output != null)
                    recipes.add(new SimplePair<>(ItemStackUtil.collapseItems(itemStacks),output));
            }
            multiblockAutocrafterRecipes.put(sfItem.getId(),recipes);
            return recipes;
        }catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public static List<SimplePair<ItemStack[], ItemStack>> getMultiblockAutocrafterRecipes(EnhancedAutoCrafter sfItem) {
        if (multiblockAutocrafterRecipes.containsKey(sfItem.getId())){
            return multiblockAutocrafterRecipes.get(sfItem.getId());
        }
        MultiBlockMachine machine = (MultiBlockMachine) RecipeType.ENHANCED_CRAFTING_TABLE.getMachine();
        List<ItemStack[]> itemIn = RecipeType.getRecipeInputList(machine);
        List<SimplePair<ItemStack[],ItemStack>> recipes = new ArrayList<>();
        for (ItemStack[] itemStacks:itemIn){
            ItemStack output = RecipeType.getRecipeOutputList(machine,itemStacks);
            if (output != null)
                recipes.add(new SimplePair<>(ItemStackUtil.collapseItems(itemStacks),output));
        }
        multiblockAutocrafterRecipes.put(sfItem.getId(),recipes);
        return recipes;
    }

    //in fact,this is for FluffyMachine
    //however,I already copied FluffyMachine's AutoCrafter code to InfinityCompress Multiblock Autocrafter.
    public static List<SimplePair<ItemStack[], ItemStack>> getMultiblockAutocrafterRecipes(AutoCrafter sfItem){
        List<SimplePair<ItemStack[],ItemStack>> recipes = new ArrayList<>();
        try {
            if (multiblockAutocrafterRecipes.containsKey(sfItem.getId())){
                return multiblockAutocrafterRecipes.get(sfItem.getId());
            }
            Field f = AutoCrafter.class.getDeclaredField("mblock");
            f.setAccessible(true);
            MultiBlockMachine machine = (MultiBlockMachine) getInUnsafe(sfItem,f);
            List<ItemStack[]> itemIn = RecipeType.getRecipeInputList(machine);
            for (ItemStack[] itemStacks:itemIn){
                ItemStack output = RecipeType.getRecipeOutputList(machine,itemStacks);
                if (output != null)
                    recipes.add(new SimplePair<>(ItemStackUtil.collapseItems(itemStacks),output));
            }
            multiblockAutocrafterRecipes.put(sfItem.getId(),recipes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return recipes;
    }

}
