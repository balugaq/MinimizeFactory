package io.github.ignorelicensescn.minimizefactory.items.machine;

import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.PersistentSerializedMachineRecipeType;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.MachineRecipeSerializerInitCrafting;
import io.github.ignorelicensescn.minimizefactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.ignorelicensescn.minimizefactory.utils.timestampbasedmanagers.PageManager;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.items.Registers.MACHINE_STABILIZER;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.PersistentSerializedMachineRecipeType.SERIALIZED_MACHINE_RECIPE;
import static io.github.ignorelicensescn.minimizefactory.utils.LoreGetter.tryGetLore;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.getSerializedRecipeProviderForMachine;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.GEOTHERMAL;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.REINFORCED_GEOTHERMAL;
import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_INT_ARRAY;

public class MachineRecipeSerializer extends SlimefunItem {
    public static final int[] border = new int[]{
                  2, 3,  4, 5, 6, 7, 8,
            10,   12,         16,
            18,19,20,            25,26,
            27,28,29,30,31,32,33,34,35,
            36,37,38,39,40,41,42,43,44,
            45,46,47,48,   50,51,52,53
    };
    public static final int[] recipeSlots = new int[]{
            27,28,29,30,31,32,33,34,35,
            36,37,38,39,40,41,42,43,44
    };
    public static final int[] prevAndNext = new int[]{
            46, 52
    };
    public static final int[] statusBorder = new int[]{
            9,  11,     15,  17
    };
    public static final int outputButton = 49;
    public static final int outputSlot = 14;
    public static final int outputDescriptionSlot = 13;
    public static final int inputSlot = 22;
    public static final int inputDescriptionSlot = 21;
    public static final int catalyzerSlot = 23;
    public static final int catalyzerDescriptionSlot = 24;
    public static final int stabilizerSlot = 0;
    public static final int stabilizerDescriptionSlot = 1;
    public static final ItemStack RED_GLASS = new CustomItemStack(Material.RED_STAINED_GLASS_PANE, "");

    public MachineRecipeSerializer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
        constructMenu(properties.getReplacedProperty("Serializer_Name"));
        addItemHandler(new BlockBreakHandler(false,false) {
            @Override
            @ParametersAreNonnullByDefault
            public void onPlayerBreak(BlockBreakEvent blockBreakEvent, ItemStack itemStack, List<ItemStack> list) {
                BlockStorage.getInventory(blockBreakEvent.getBlock()).dropItems(blockBreakEvent.getBlock().getLocation(),inputSlot,outputSlot,catalyzerSlot,stabilizerSlot);
            }
        });
    }

    private void constructMenu(String displayName){
        new BlockMenuPreset(getId(),displayName) {
            @Override
            public void init() {
                borderInit(this);
                statusBorderInit(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                menu.addMenuClickHandler(inputSlot, new AdvancedMenuClickHandler() {
                    @Override
                    public boolean onClick(Player p, int slot, ItemStack item, ClickAction action) {
                        borderReset(menu);
                        statusBorderReset(menu);
                        return true;
                    }

                    @Override
                    public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                        Bukkit.getScheduler().runTask(minimizeFactoryInstance, p::updateInventory);
                        SlimefunItem sfItem = SlimefunItem.getByItem(cursor);
                        if (sfItem == null){
                            borderReset(menu);
                            statusBorderReset(menu);
                            return true;
                        }
                        SerializedRecipeProvider<SlimefunItem> rp = (SerializedRecipeProvider<SlimefunItem>) getSerializedRecipeProviderForMachine(sfItem);
                        if (rp != null){
                            menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE, List.of(rp.getEnergyInfoWithCheck(sfItem))));
                            List<SimplePair<SerializedMachine_MachineRecipe,ItemStack>> recipes = rp.getSerializedRecipes(sfItem);
                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_SerializedRecipes(menu, PageManager.getPage(p), recipes, sfItem,rp);
                            menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
                            menu.addMenuClickHandler(prevAndNext[0], (pClickedPrev, slotPrev, item, actionPrev) -> {
                                int page = PageManager.decreasePage(pClickedPrev);
                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_SerializedRecipes(menu, page, recipes, sfItem,rp);
                                return false;
                            });
                            menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
                            menu.addMenuClickHandler(prevAndNext[1], (pClickedNext, slotNext, item, actionNext) -> {

                                int page = PageManager.addPage(pClickedNext);
                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_SerializedRecipes(menu, page, recipes, sfItem,rp);
                                return false;
                            });
                            return true;
                        }
                        return false;
                    }
                });
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(),
                        Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return EMPTY_INT_ARRAY;
            }
        };
    }
    public static void statusBorderReset(BlockMenu preset){
        for (int i:statusBorder){
            preset.replaceExistingItem(i, RED_GLASS);
            preset.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
    }
    public static void borderReset(BlockMenu preset){
        for (int i:border){
            preset.replaceExistingItem(i, new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ""));
            preset.addMenuClickHandler(i, (p, slot, item, action) -> {
                minimizeFactoryInstance.msgSend(p,tryGetLore(preset.getItemInSlot(slot)));
                return false;
            });
        }
        preset.replaceExistingItem(outputButton,new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_OutputButton_Name"),
                properties.getReplacedProperties("Serializer_OutputButton_Lore_1",
                        ChatColor.GRAY)));
        preset.addMenuClickHandler(outputButton,(p, slot, item, action) -> {
            p.sendMessage(properties.getReplacedProperty("Serializer_No_Recipe"));
            return false;
        });
        preset.replaceExistingItem(outputDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_outputDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_outputDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(outputDescriptionSlot,ChestMenuUtils.getEmptyClickHandler());
        preset.replaceExistingItem(inputDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_inputDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_inputDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(inputDescriptionSlot,ChestMenuUtils.getEmptyClickHandler());
        preset.replaceExistingItem(catalyzerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_catalyzerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_catalyzerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(catalyzerDescriptionSlot,ChestMenuUtils.getEmptyClickHandler());
        preset.replaceExistingItem(stabilizerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_stabilizerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_stabilizerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(stabilizerDescriptionSlot, ChestMenuUtils.getEmptyClickHandler());
    }
    public static void statusBorderInit(BlockMenuPreset preset){
        for (int i:statusBorder){
            preset.addItem(i, new CustomItemStack(Material.RED_STAINED_GLASS_PANE, ""));
            preset.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
    }
    public static void borderInit(BlockMenuPreset preset){
        for (int i:border){
            preset.addItem(i, new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ""));
            preset.addMenuClickHandler(i, (p, slot, item, action) -> {
                minimizeFactoryInstance.msgSend(p,tryGetLore(preset.getItemInSlot(slot)));
                return false;
            });
        }
        preset.addItem(outputButton,new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_OutputButton_Name"),
                properties.getReplacedProperties("Serializer_OutputButton_Lore_1",
                        ChatColor.GRAY)));
        preset.addMenuClickHandler(outputButton,ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(outputDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_outputDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_outputDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(outputDescriptionSlot,ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(inputDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_inputDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_inputDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(inputDescriptionSlot,ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(catalyzerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_catalyzerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_catalyzerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(catalyzerDescriptionSlot,ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(stabilizerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_stabilizerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_stabilizerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(stabilizerDescriptionSlot,ChestMenuUtils.getEmptyClickHandler());
    }

    public static ItemStack checkStabilizer(BlockMenu menu,Player p){
        ItemStack stabilizer = menu.getItemInSlot(stabilizerSlot);
        if (!SlimefunUtils.isItemSimilar(stabilizer,MACHINE_STABILIZER,false,false)){
            p.sendMessage(properties.getReplacedProperty("Serializer_Not_Enough_Stabilizer"));
            return null;
        }
        return stabilizer;
    }
    public enum CatalyzerStats{
        NO_NEED_FOR_CATALYZER,
        ENOUGH_CATALYZER,
        NOT_ENOUGH_CATALYZER,
    }
    public static CatalyzerStats checkCatalyzer(BlockMenu menu,Player p,ItemStack catalyzer){
        if (catalyzer != null && !catalyzer.getType().equals(Material.AIR)){
            ItemStack catalyzerSlotItem = menu.getItemInSlot(catalyzerSlot);
            if (catalyzerSlotItem == null || catalyzerSlotItem.getType().equals(Material.AIR)){
                p.sendMessage(properties.getReplacedProperty("Serializer_Need_Catalyzer"));
                return CatalyzerStats.NOT_ENOUGH_CATALYZER;
            }
            if (catalyzer.getAmount() > catalyzerSlotItem.getAmount()){
                p.sendMessage(properties.getReplacedProperty("Serializer_Not_Enough_Catalyzer"));
                return CatalyzerStats.NOT_ENOUGH_CATALYZER;
            }
            SlimefunItem sfCatalyzerItem = SlimefunItem.getByItem(catalyzer);
            if (sfCatalyzerItem != null){
                if (!SlimefunUtils.isItemSimilar(catalyzerSlotItem, catalyzer, false, false)){
                    p.sendMessage(properties.getReplacedProperty("Serializer_Not_Enough_Catalyzer"));
                    return CatalyzerStats.NOT_ENOUGH_CATALYZER;
                }
            }else if (!menu.getItemInSlot(catalyzerSlot).isSimilar(catalyzer)){
                p.sendMessage(properties.getReplacedProperty("Serializer_Not_Enough_Catalyzer"));
                return CatalyzerStats.NOT_ENOUGH_CATALYZER;
            }
            return CatalyzerStats.ENOUGH_CATALYZER;
        }
        return CatalyzerStats.NO_NEED_FOR_CATALYZER;
    }
    public static void consumeStabilizer(BlockMenu menu,ItemStack stabilizer){
        if (stabilizer.getAmount() == 1){
            menu.replaceExistingItem(stabilizerSlot,null);
        }else {
            stabilizer.setAmount(stabilizer.getAmount() - 1);
        }
    }
    public static void consumeMachine(BlockMenu menu,ItemStack machine){
        if (machine.getAmount() == 1){
            menu.replaceExistingItem(inputSlot,null);
        }else {
            machine.setAmount(machine.getAmount() - 1);
        }
    }
    public static void consumeCatalyzer(BlockMenu menu,ItemStack catalyzer){
        ItemStack catalyzerSlotItem = menu.getItemInSlot(catalyzerSlot);
        int catalyzerAmount = catalyzer.getAmount();
        int catalyzerSlotItemAmount = catalyzerSlotItem.getAmount();
        if (catalyzerAmount == catalyzerSlotItemAmount){
            menu.replaceExistingItem(catalyzerSlot,null);
        }else {
            catalyzerSlotItem.setAmount(catalyzerSlotItemAmount - catalyzerAmount);
        }
    }
    public static ChestMenu.MenuClickHandler initClickHandlerForOutput(BlockMenu menu, SerializedMachine_MachineRecipe machineRecipe, ItemStack catalyzer, SlimefunItem sfItem){
        return (p, slot, item, action) -> {
            Bukkit.getScheduler().runTask(minimizeFactoryInstance, p::updateInventory);
            ItemStack inSlotItem = menu.getItemInSlot(inputSlot);
            SlimefunItem sfInSlot = SlimefunItem.getByItem(inSlotItem);
            if (inSlotItem == null || sfInSlot == null || !sfInSlot.equals(sfItem)){
                borderReset(menu);
                statusBorderReset(menu);
                return false;
            }

            ItemStack stabilizer = checkStabilizer(menu,p);
            if (stabilizer == null){return false;}

            CatalyzerStats catalyzerStats = checkCatalyzer(menu,p,catalyzer);
            if (catalyzerStats == CatalyzerStats.NOT_ENOUGH_CATALYZER){return false;}

            consumeStabilizer(menu,stabilizer);
            consumeMachine(menu,inSlotItem);

            ItemStack outSlotItem = menu.getItemInSlot(outputSlot);
            ItemStack output = MACHINE_STABILIZER.clone();
            output.setAmount(1);

            List<String> lore = new ArrayList<>();
            String inputName = NameUtil.findName(inSlotItem);

//            if (inSlotItem.hasItemMeta() && inSlotItem.getItemMeta().hasDisplayName()){
//                inputName = inSlotItem.getItemMeta().getDisplayName();
//            }else {
//                inputName = NameUtil.nameForMaterial(inSlotItem.getType());
//            }

            //add lore:speed and time
            lore.add(properties.getReplacedProperty("Stabilizer_ContainsMachine") + inputName);
            if (machineRecipe.ticks == 0){
                machineRecipe.ticks = 1;
            }
            lore.add(properties.getReplacedProperty("Stabilizer_Time") + machineRecipe.ticks + properties.getReplacedProperty("Stabilizer_Time_Unit"));

            //add lore:energy per tick
            if (!sfItem.getId().equals(GEOTHERMAL.getItemId()) && !sfItem.getId().equals(REINFORCED_GEOTHERMAL.getItemId())){
                if (machineRecipe.energyPerTick != 0){
                    lore.add(properties.getReplacedProperty("Stabilizer_EnergyPerTick")
                            + machineRecipe.energyPerTick
                            + properties.getReplacedProperty("Stabilizer_EnergyPerTick_Unit"));
                }
                if (machineRecipe.energyPerTickAtNight != machineRecipe.energyPerTick){
                    lore.add(properties.getReplacedProperty("Stabilizer_EnergyPerTick_Night")
                            + machineRecipe.energyPerTickAtNight
                            + properties.getReplacedProperty("Stabilizer_EnergyPerTick_Unit"));
                }
            }else {
                if (machineRecipe.energyPerTick != 0){
                    lore.add(properties.getReplacedProperty("Stabilizer_GeoEnergy")
                            + machineRecipe.energyPerTick
                            + properties.getReplacedProperty("Stabilizer_EnergyPerTick_Unit"));
                }
                if (machineRecipe.energyPerTickAtNight != machineRecipe.energyPerTick){
                    lore.add(properties.getReplacedProperty("Stabilizer_GeoEnergy_Nether")
                            + machineRecipe.energyPerTickAtNight
                            + properties.getReplacedProperty("Stabilizer_EnergyPerTick_Unit"));
                }
            }

            //add lore:input item
            if (machineRecipe.inputs != null){
                lore.add(properties.getReplacedProperty("Stabilizer_Input"));
                machineRecipe.inputs = ItemStackUtil.collapseItems(machineRecipe.inputs);
                if (machineRecipe.inputs.length == 1 && machineRecipe.Singularity_Material_amount != -1){
                    lore.add(ChatColor.WHITE + NameUtil.findName(machineRecipe.inputs[0]) + properties.getReplacedProperty("Stabilizer_Input_Unit") + machineRecipe.Singularity_Material_amount);

                }else {
                    for (ItemStack input : machineRecipe.inputs) {
                        String name = NameUtil.findName(input);
                        lore.add(name + properties.getReplacedProperty("Stabilizer_Input_Unit") + input.getAmount());
                    }
                }
            }

            //add lore:output item
            if (machineRecipe.outputs != null){
                lore.add(properties.getReplacedProperty("Stabilizer_Output"));
                if (machineRecipe.outputExpectations == null){
                    for (ItemStack output1:machineRecipe.outputs){
                        String name = NameUtil.findName(output1);
                        lore.add(ChatColor.WHITE + name
                                + properties.getReplacedProperty("Stabilizer_Output_Unit")
                                + output1.getAmount()
                        );
                    }
                }else {
                    for (int i=0;i<machineRecipe.outputs.length;i+=1){
                        ItemStack output1 = machineRecipe.outputs[i];
                        String name = NameUtil.findName(output1);
                        lore.add(ChatColor.WHITE + name
                                + properties.getReplacedProperty("Stabilizer_Output_Unit") + output1.getAmount()
                                + properties.getReplacedProperty("Stabilizer_Output_Expectation")
                                + machineRecipe.outputExpectations[i]
                        );
                    }
                }
            }


            String worldStr = "";
            if (machineRecipe.env != null){
                worldStr += properties.getReplacedProperty("Stabilizer_World");
                switch (machineRecipe.env){
                    case NORMAL-> worldStr+=ChatColor.GREEN+ properties.getReplacedProperty("Test_InfoProvider_Info_MainWorld");
                    case NETHER-> worldStr+=ChatColor.RED+ properties.getReplacedProperty("Test_InfoProvider_Info_Nether");
                    case THE_END -> worldStr+=ChatColor.LIGHT_PURPLE + properties.getReplacedProperty("Test_InfoProvider_Info_End");
                    default -> worldStr+=ChatColor.GRAY + properties.getReplacedProperty("Test_InfoProvider_Info_CUSTOM");
                }
            }
            if (machineRecipe.biome != null){
                worldStr += properties.getReplacedProperty("Stabilizer_Biome") + NameUtil.nameForBiome(machineRecipe.biome);
            }
            if (!worldStr.isEmpty()){
                lore.add(worldStr);
            }
            if (machineRecipe.entityClassName != null){
                lore.add(properties.getReplacedProperty("Stabilizer_EntityClassName") + machineRecipe.entityClassName);
            }

            if (catalyzerStats == CatalyzerStats.ENOUGH_CATALYZER){
                ItemStack extra = catalyzer.clone();
                extra.setAmount(1);
                machineRecipe.extraItems = new ItemStack[]{extra};
            }

            ItemMeta outputMeta = output.getItemMeta();
            assert outputMeta != null;
            if (!lore.isEmpty()){
                lore = tryGetLore(new CustomItemStack(Material.BEDROCK,"1",lore));
                outputMeta.setLore(lore);
            }

            DataTypeMethods.setCustom(outputMeta,SERIALIZED_MACHINE_RECIPE, PersistentSerializedMachineRecipeType.TYPE,machineRecipe);
            output.setItemMeta(outputMeta);

            if (outSlotItem != null && !outSlotItem.getType().equals(Material.AIR)){
                if (ItemStackUtil.isItemStackSimilar(outSlotItem,output)
                        && outSlotItem.getAmount() <= outSlotItem.getMaxStackSize()) {
                    outSlotItem.setAmount(outSlotItem.getAmount() + 1);
                }else{
                    World at = menu.getLocation().getWorld();
                    if(at != null){
                        at.dropItemNaturally(menu.getLocation(), output);
                    }
                }
            }
            else {
                menu.replaceExistingItem(outputSlot,output);
            }
            if (catalyzerStats == CatalyzerStats.ENOUGH_CATALYZER){
                consumeCatalyzer(menu,catalyzer);
            }
            return false;
        };
    }
}
