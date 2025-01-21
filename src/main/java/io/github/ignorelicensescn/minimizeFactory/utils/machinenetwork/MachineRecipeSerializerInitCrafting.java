package io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork;

import io.github.ignorelicensescn.minimizeFactory.Items.machine.MachineRecipeSerializer;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.minimizeFactoryInstance;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizeFactory.utils.LoreGetter.tryGetLore;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.ENERGIZED_CAPACITOR;

public class MachineRecipeSerializerInitCrafting {
    //rewrite the code until there is only 1 method
    public static void initCraftingRecipes_SerializedRecipes(BlockMenu menu,
                                                             int page,
                                                             List<SimplePair<SerializedMachine_MachineRecipe,ItemStack>> machineRecipes,
                                                             SlimefunItem sfItem,
                                                             SerializedRecipeProvider<SlimefunItem> provider){
        for (int i: MachineRecipeSerializer.recipeSlots){
            int indexAtList = (page*18 + ( i - 26 )) % machineRecipes.size();
            SimplePair<SerializedMachine_MachineRecipe,ItemStack> recipePair = machineRecipes.get(indexAtList);
            SerializedMachine_MachineRecipe recipe = recipePair.first;
            Material materialToShow = provider.getShowingMaterial(sfItem,recipePair,indexAtList);
            SimplePair<String,List<String>> nameAndLore = provider.getNameAndLoreForRecipe(sfItem,recipePair,indexAtList);
            String name = nameAndLore == null?null:nameAndLore.first;
            List<String> lore = nameAndLore == null?null:nameAndLore.second;
            if (lore == null){
                lore = new ArrayList<>();
                lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Input"));
                if (recipe.inputs != null){
                    if (recipe.inputs.length == 1 && recipe.Singularity_Material_amount != -1){
                        lore.add(ChatColor.WHITE + NameUtil.findName(recipe.inputs[0]) + properties.getReplacedProperty("Stabilizer_Input_Unit") + recipe.Singularity_Material_amount);
                    }else {
                        for (ItemStack itemStack : recipe.inputs) {
                            lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(itemStack));
                        }
                    }
                }

                if (recipe.outputs != null && recipe.outputs.length > 0) {
                    lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
                    if (recipe.outputExpectations != null && recipe.outputExpectations.length > 0) {
                        for (int outIndex = 0; outIndex < recipe.outputs.length; outIndex+=1) {
                            ItemStack outputItem = recipe.outputs[outIndex];
                            String nameInLore = name;
                            if (nameInLore == null){
                                nameInLore = NameUtil.findName(outputItem);
                            }
                            lore.add(ChatColor.WHITE + nameInLore
                                    + properties.getReplacedProperty("Stabilizer_Output_Unit") + outputItem.getAmount()
                                    + properties.getReplacedProperty("Stabilizer_Output_Expectation")
                                    + recipe.outputExpectations[outIndex]);
                        }
                    } else {
                        for (ItemStack itemStack : recipe.outputs) {
                            lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(itemStack));
                        }
                    }
                }

                if (recipe.env != null) {
                    switch (recipe.env) {
                        case NORMAL ->
                                lore.add(ChatColor.GREEN + properties.getReplacedProperty("Test_InfoProvider_Info_Need_MainWorld"));
                        case NETHER ->
                                lore.add(ChatColor.RED + properties.getReplacedProperty("Test_InfoProvider_Info_Need_Nether"));
                        case THE_END ->
                                lore.add(ChatColor.LIGHT_PURPLE + properties.getReplacedProperty("Test_InfoProvider_Info_Need_End"));
                        default ->
                                lore.add(ChatColor.GRAY + properties.getReplacedProperty("Test_InfoProvider_Info_Need_CUSTOM"));
                    }
                }

                if (recipe.biome != null) {
                    lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Need_Biome") + NameUtil.nameForBiome(recipe.biome));
                }
            }
            if (name == null){
                name = properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                        + (((double)recipe.ticks))
                        + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit");
            }

            ItemStack showItemTemplate;
            if (recipe.outputs != null && recipe.outputs.length > 0){
                 showItemTemplate = recipe.outputs[0].clone();
            }else{
                showItemTemplate = ENERGIZED_CAPACITOR.clone();
            }

            ItemStack showItem = showItemTemplate;
            if (showItem.getItemMeta() == null){
                showItem = new CustomItemStack(showItem.getType(),name,lore);
            }else {
                ItemMeta meta = showItem.getItemMeta();
                meta.setDisplayName(name);
                meta.setLore(lore);
                showItem.setItemMeta(meta);
            }
            if (materialToShow != null){
                showItem.setType(materialToShow);
            }
//            tryAddHeadSkin(showItem, showItemTemplate, name, lore);
            showItem.setAmount(showItemTemplate.getAmount());

            menu.replaceExistingItem(i
                    , showItem
            );

            menu.addMenuClickHandler(i, (p, slot, item, action) -> {
                minimizeFactoryInstance.msgSend(p,tryGetLore(menu.getItemInSlot(slot)));

                menu.addMenuClickHandler(
                        MachineRecipeSerializer.outputButton,
                        MachineRecipeSerializer.initClickHandlerForOutput(menu,recipe,recipePair.second,sfItem));
                p.sendMessage(properties.getReplacedProperty("Serializer_Recipe_Chose"));
                return false;
            });
        }
    }

}
