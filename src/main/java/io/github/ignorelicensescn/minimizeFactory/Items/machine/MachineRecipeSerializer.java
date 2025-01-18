package io.github.ignorelicensescn.minimizeFactory.Items.machine;

import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.PersistentSerializedMachineRecipeType;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemStackUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.MachineRecipeSerializerInitCrafting;
import io.github.ignorelicensescn.minimizeFactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeOutEntity;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.EnhancedAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.autocrafters.VanillaAutoCrafter;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.FluidPump;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities.AbstractEntityAssembler;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
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

import static io.github.ignorelicensescn.minimizeFactory.Items.Registers.MACHINE_STABILIZER;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.InfoScan.findEnergyInfo_AbstractEntityAssembler;
import static io.github.ignorelicensescn.minimizeFactory.utils.InfoScan.findRecipes_AbstractEntityAssembler;
import static io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.PersistentSerializedMachineRecipeType.SERIALIZED_MACHINE_RECIPE;
import static io.github.ignorelicensescn.minimizeFactory.utils.SerializedMachineRecipeFinder.getSerializedRecipeProviderForMachine;
import static io.github.ignorelicensescn.minimizeFactory.utils.SlimefunConsts.FLUID_PUMP_ENERGY_CONSUMPTION;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityCompress.InfinityCompressConsts.getMultiblockAutocrafterRecipes;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.GEOTHERMAL;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.REINFORCED_GEOTHERMAL;

public class MachineRecipeSerializer extends SlimefunItem {
    public static final String PAGE_KEY = "page";

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

    //default:0
    public static int getPage(Location l){
        JSONObject json =new JSONObject(BlockStorage.getBlockInfoAsJson(l));
        if (json.has(PAGE_KEY)){
            return json.getInt(PAGE_KEY);
        }
        return 0;
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
                        Bukkit.getScheduler().runTask(instance, p::updateInventory);
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
                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_SerializedRecipes(menu, 0, recipes, sfItem,rp);
                            menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
                            menu.addMenuClickHandler(prevAndNext[0], (pClickedPrev, slotPrev, item, actionPrev) -> {
                                int page = getPage(menu.getLocation());
                                if (page > 0) {
                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_SerializedRecipes(menu, page, recipes, sfItem,rp);
                                }
                                return false;
                            });
                            menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
                            menu.addMenuClickHandler(prevAndNext[1], (pClickedNext, slotNext, item, actionNext) -> {

                                int page = getPage(menu.getLocation());

                                if (page < Integer.MAX_VALUE) {
                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_SerializedRecipes(menu, page, recipes, sfItem,rp);
                                }
                                return false;
                            });
                            return true;
                        }
                        return false;
//                        if (InfinityExpansionFlag){
//                            if (sfItem instanceof AbstractMachineBlock){
//                                if (sfItem instanceof MaterialGenerator) {
//                                    long[] energyInfo = findEnergyInfo_InfinityCompress_MaterialGenerator((MaterialGenerator) sfItem);
//                                    if (energyInfo[0] != 0) {
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    } else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    List<MachineRecipe> machineRecipes = findRecipes_MaterialGenerator((MaterialGenerator) sfItem);
//                                    if (machineRecipes.size() != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    } else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof ResourceSynthesizer) {
//                                    long[] energyInfo = findEnergyInfo_InfinityExpansion_ResourceSynthesizer((ResourceSynthesizer) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    List<MachineRecipe> machineRecipes = findRecipes_ResourceSynthesizer((ResourceSynthesizer) sfItem);
//                                    if (machineRecipes.size() != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    } else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof SingularityConstructor) {
//                                    long[] energyInfo = findEnergyInfo_InfinityExpansion_SingularityConstructor((SingularityConstructor) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    List<SimplePair<SingularityRecipe, Double>> machineRecipes = findRecipes_SingularityConstructor((SingularityConstructor) sfItem);
//                                    if (machineRecipes.size() != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_Singularity(menu, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_Singularity(menu, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_Singularity(menu, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    } else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof MachineBlock) {
//                                    long[] energyInfo = findEnergyInfo_InfinityExpansion_MachineBlock((MachineBlock) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//
//                                    List<MachineRecipeInTicks> machineRecipes = findRecipes_MachineBlock((MachineBlock) sfItem);
//                                    if (machineRecipes.size() != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_MachineBlock(menu, 1, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_MachineBlock(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_MachineBlock(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                    } else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof GeoQuarry) {
//                                    long[] energyInfo = findEnergyInfo_InfinityExpansion_GeoQuarry((GeoQuarry) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//
//                                    if (geoMinerResourcesInfo_ResourcesList.size() != 0) {
//                                        int speed = (int)energyInfo[1];
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_GeoMiner(menu, speed, 0, geoMinerResourcesInfo_ResourcesList, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_GeoMiner(menu, speed, page, geoMinerResourcesInfo_ResourcesList, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_GeoMiner(menu, speed, page, geoMinerResourcesInfo_ResourcesList, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    } else {
//                                        borderReset(menu);
//                                    }
//
//
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof Quarry) {
//                                    try {
//                                        SimpleTri<Integer, Integer, SimpleTri<SimpleTri<Material,Integer,Integer>[], Oscillator, World.Environment>[]> info = quarryInfo((Quarry) sfItem);
//                                        long[] energyInfo = findEnergyInfo_InfinityExpansion_Quarry((Quarry) sfItem);
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                                + info.first
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                        if (quarryOutputListMap.size() != 0) {
//                                            int speed = info.first;
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_Quarry(menu, speed, 0, quarryOutputListMap.get(sfItem.getId()), sfItem,energyInfo);
//                                            menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                            menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                                int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                                if (page > 0) {
//                                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_Quarry(menu, speed, page, quarryOutputListMap.get(sfItem.getId()), sfItem,energyInfo);
//                                                }
//                                                return false;
//                                            });
//                                            menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                            menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                                int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                                if (page < Integer.MAX_VALUE) {
//                                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_Quarry(menu, speed, page, quarryOutputListMap.get(sfItem.getId()), sfItem,energyInfo);
//                                                }
//                                                return false;
//                                            });
//
//                                        } else {
//                                            borderReset(menu);
//                                        }
//                                    } catch (Exception ex) {
//                                        ex.printStackTrace();
//                                    }
//
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof StoneworksFactory) {
//                                    long[] energyInfo = findEnergyInfo_InfinityExpansion_StoneWorksFactory((StoneworksFactory) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    List<SimplePair<MachineRecipeInTicks, String[]>> machineRecipes = findRecipes_StoneworksFactory((StoneworksFactory) sfItem);
//                                    if (machineRecipes.size() != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_StoneworksFactory(menu, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_StoneworksFactory(menu, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_StoneworksFactory(menu, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    } else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof VoidHarvester){
//                                    try {
//                                        long[] energyInfo = getVoidHarvesterEnergyInfo((VoidHarvester) sfItem);
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                                + (VOID_HARVESTER_TIME / energyInfo[1])
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe(
//                                                cursor.clone()
//                                                ,new MachineRecipeInTicks((int) (VOID_HARVESTER_TIME / energyInfo[1]),new ItemStack[0], new ItemStack[]{VOID_BIT}
//                                        ),energyInfo[0],1);
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }catch (Exception ex){
//                                        ex.printStackTrace();
//                                    }
//                                }
//                                else
//                                    if (sfItem instanceof GrowingMachine){
//                                    try{
//                                        long[] energyInfo = findEnergyInfo_InfinityExpansion_GrowingMachine((GrowingMachine)sfItem);
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                                + (VOID_HARVESTER_TIME / energyInfo[1])
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                        SimplePair<ItemStack,ItemStack[]>[] recipes = getGrowingMachineOutput((GrowingMachine)sfItem);
//                                        if (recipes.length != 0) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_GrowingMachine(menu, 0, recipes, sfItem, energyInfo);
//                                            menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                            menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                                int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                                if (page > 0) {
//                                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_GrowingMachine(menu, page, recipes, sfItem,energyInfo);
//                                                }
//                                                return false;
//                                            });
//                                            menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                            menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                                int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                                if (page < Integer.MAX_VALUE) {
//                                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_GrowingMachine(menu, page, recipes, sfItem,energyInfo);
//                                                }
//                                                return false;
//                                            });
//
//                                        }
//                                        return true;
//                                    }catch (Exception ex){
//                                        ex.printStackTrace();
//                                    }
//                                }
//                            }
//                            else
//                                if (sfItem instanceof MenuBlock){
//                                if (sfItem instanceof EnergyGenerator){
//                                    if (sfItem.getId().equals(GEOTHERMAL.getItemId())){
//                                        long[] energyInfo = new long[]{GEO_ENERGY,GEO_ENERGY*2L,GEO_ENERGY*100L};
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Nether")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }
//                                    else if (sfItem.getId().equals(REINFORCED_GEOTHERMAL.getItemId())){
//                                        long[] energyInfo = new long[]{ADVANCED_GEO_ENERGY,ADVANCED_GEO_ENERGY*2L,ADVANCED_GEO_ENERGY*100L};
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Nether")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }
//                                    else if (sfItem.getId().equals(BASIC_PANEL.getItemId())){
//                                        long[] energyInfo = new long[]{
//                                                InfinityExpansionConsts.BASIC_SOLAR_ENERGY
//                                                ,0
//                                                ,InfinityExpansionConsts.BASIC_SOLAR_ENERGY*100L};
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }
//                                    else if (sfItem.getId().equals(ADVANCED_PANEL.getItemId())){
//                                        long[] energyInfo = new long[]{
//                                                InfinityExpansionConsts.ADVANCED_SOLAR_ENERGY
//                                                ,0
//                                                ,InfinityExpansionConsts.ADVANCED_SOLAR_ENERGY*100L};
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }
//                                    else if (sfItem.getId().equals(CELESTIAL_PANEL.getItemId())){
//                                        long[] energyInfo = new long[]{
//                                                InfinityExpansionConsts.CELESTIAL_ENERGY
//                                                ,0
//                                                ,InfinityExpansionConsts.CELESTIAL_ENERGY * 100L
//                                        };
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }
//                                    else if (sfItem.getId().equals(VOID_PANEL.getItemId())){
//                                        long[] energyInfo = new long[]{
//                                                0
//                                                ,InfinityExpansionConsts.VOID_ENERGY
//                                                ,InfinityExpansionConsts.VOID_ENERGY * 100L
//                                        };
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Usable_In_Alien_When_Place")
//                                                )
//                                        );
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }
//                                    else if (sfItem.getId().equals(INFINITE_PANEL.getItemId())){
//                                        long[] energyInfo = new long[]{
//                                                InfinityExpansionConsts.INFINITY_ENERGY
//                                                ,InfinityExpansionConsts.INFINITY_ENERGY
//                                                ,InfinityExpansionConsts.INFINITY_ENERGY * 100L
//                                        };
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Usable_In_Alien_When_Place")
//                                                )
//                                        );
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }
//                                    else {
//                                        if (InfinityCompressFlag){
//                                            if (sfItem.getId().equals(INFINITY_INFINITE_PANEL.getItemId())){
//                                                long[] energyInfo = new long[]{
//                                                        INFINITY_ENERGY * 36L
//                                                        ,INFINITY_ENERGY * 36L
//                                                        ,INFINITY_ENERGY * 36L * 100L
//                                                };
//                                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
//                                                                        + energyInfo[0]
//                                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
//                                                                        + energyInfo[1]
//                                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                        + energyInfo[2]
//                                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"),
//                                                                properties.getReplacedProperty("Test_InfoProvider_Info_Usable_In_Alien_When_Place")
//                                                        )
//                                                );
//                                                SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                                recipe1.sfItem = sfItem;
//                                                recipe1.sfItemStack = sfItem.getItem();
//                                                recipe1.energyPerTick = energyInfo[0];
//                                                recipe1.energyPerTickAtNight = energyInfo[1];
//                                                menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                                return true;
//                                            }
//                                        }
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof InfinityReactor){
//                                    long[] energyInfo = new long[]{
//                                            InfinityExpansionConsts.INFINITY_REACTOR_ENERGY
//                                            ,1
//                                            ,InfinityExpansionConsts.INFINITY_REACTOR_ENERGY * 100L
//                                    };
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    Set<TweakedMachineFuel> machineFuels = findFuels_InfinityExpansion_InfinityReactor((InfinityReactor) sfItem);
//                                    if (machineFuels.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initGeneratorFuels_Tweaked(menu, 0, machineFuels, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels_Tweaked(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels_Tweaked(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }
//                                    else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof MobSimulationChamber){
//                                    long[] energyInfo = new long[]{1145141919810L,1,((MobSimulationChamber)sfItem).getCapacity()};
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Depends_On_Recipe"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                            + (CHAMBER_INTERVAL)
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    if (CARDS_INFO_LIST.size() != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_MobSimulationChamber(menu, 0, CARDS_INFO_LIST, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_MobSimulationChamber(menu, page, CARDS_INFO_LIST, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_MobSimulationChamber(menu, page, CARDS_INFO_LIST, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }
//                                    else {
//                                        borderReset(menu);
//                                    }
//                                }
//                                else if (sfItem instanceof InfinityWorkbench){
//                                    long[] energyInfo = new long[]{((InfinityWorkbench)sfItem).getCapacity(),1,((InfinityWorkbench)sfItem).getCapacity()};
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    if (INFINITY_WORKBENCH_RECIPES.length != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, 0, INFINITY_WORKBENCH_RECIPES, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, INFINITY_WORKBENCH_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, INFINITY_WORKBENCH_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof GearTransformer){
//                                    long[] energyInfo = new long[]{((GearTransformer)sfItem).getCapacity(),1,((GearTransformer)sfItem).getCapacity()};
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    if (GEAR_TRANSFORMER_RECIPES.length != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, 0, GEAR_TRANSFORMER_RECIPES, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, GEAR_TRANSFORMER_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, GEAR_TRANSFORMER_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }
//                                    return true;
//                                }
//                            }
//                            else
//                                if (InfinityCompressFlag){
//                                if (sfItem instanceof TweakedGenerator){
//                                    long[] energyInfo = findEnergyInfo_InfinityCompress_TweakedGenerator((TweakedGenerator) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                    recipe1.sfItem = sfItem;
//                                    recipe1.sfItemStack = sfItem.getItem();
//                                    recipe1.energyPerTick = energyInfo[0];
//                                    recipe1.energyPerTickAtNight = energyInfo[0];
//                                    menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    return true;
//                                }
//                                else if (sfItem instanceof TweakedMaterialGenerator){
//                                    long[] energyInfo = findEnergyInfo_InfinityCompress_TweakedMaterialGenerator((TweakedMaterialGenerator) sfItem);
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }
//                                    else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//
//                                    }
//                                    List<MachineRecipe> machineRecipes =findRecipes_TweakedMaterialGenerator((TweakedMaterialGenerator) sfItem);
//                                    if (machineRecipes.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1,0,machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 2,page,machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 2,page,machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof TweakedGEOQuarry){
//                                    long[] energyInfo = findEnergyInfo_InfinityExpansion_TweakedGeoQuarry((TweakedGEOQuarry) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//
//                                    if (geoResourcesInfo_ResourcesList.size() != 0){
//                                        int speed = (int) energyInfo[1];
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_TweakedGeoMiner(menu,speed,0,geoResourcesInfo_ResourcesList, sfItem,energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_TweakedGeoMiner(menu,speed,page,geoResourcesInfo_ResourcesList, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_TweakedGeoMiner(menu,speed,page,geoMinerResourcesInfo_ResourcesList, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof TweakedGEOQuarry_Filter){
//                                    long[] energyInfo = findEnergyInfo_InfinityExpansion_TweakedGeoQuarry((TweakedGEOQuarry_Filter) sfItem);
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//
//                                    if (geoResourcesInfo_ResourcesList.size() != 0){
//                                        int speed = (int) energyInfo[1];
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_TweakedGeoMiner(menu,speed,0,geoResourcesInfo_ResourcesList, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_TweakedGeoMiner(menu,speed,page,geoResourcesInfo_ResourcesList, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_TweakedGeoMiner(menu,speed,page,geoMinerResourcesInfo_ResourcesList, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof Multiblock_Autocrafter){
//                                    long[] energyInfo = new long[]{Multiblock_Autocrafter.ENERGY_CONSUMPTION,1,Multiblock_Autocrafter.CAPACITY};
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//
//                                    try{
//                                        List<SimplePair<ItemStack[],ItemStack>> machineRecipes = getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) sfItem);
//                                        if (machineRecipes.size() != 0){
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, 0, machineRecipes, sfItem, energyInfo);
//                                            menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                            menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                                int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                                if (page > 0) {
//                                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                                }
//                                                return false;
//                                            });
//                                            menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                            menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                                int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                                if (page < Integer.MAX_VALUE) {
//                                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                                }
//                                                return false;
//                                            });
//
//                                        }else {
//                                            borderReset(menu);
//                                        }
//                                    }catch (Exception ex){
//                                        ex.printStackTrace();
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof AutoInfinityWorkbench){
//                                    long[] energyInfo = new long[]{
//                                            io.github.acdeasdff.infinityCompress.AutoCrafter.ENERGY_CONSUMPTION,
//                                            1,
//                                            io.github.acdeasdff.infinityCompress.AutoCrafter.CAPACITY
//                                    };
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Depends_On_Recipe"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    if (INFINITY_WORKBENCH_RECIPES.length != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, 0, INFINITY_WORKBENCH_RECIPES, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, INFINITY_WORKBENCH_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, INFINITY_WORKBENCH_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof TweakedGearTransformer){
//                                    long[] energyInfo = new long[]{((TweakedGearTransformer)sfItem).getCapacity(),1,((TweakedGearTransformer)sfItem).getCapacity()};
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    if (GEAR_TRANSFORMER_RECIPES.length != 0) {
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, 0, GEAR_TRANSFORMER_RECIPES, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page - 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, GEAR_TRANSFORMER_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page + 1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_InfinityWorkbench(menu, page, GEAR_TRANSFORMER_RECIPES, sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }
//                                }
//                            }
//                        }
//                        if (DynaTechFlag){
//                            if (sfItem instanceof AbstractGenerator){
//                                if (sfItem instanceof StardustReactor){
//                                    long[] energyInfo = findEnergyInfo_StardustReactor((StardustReactor) sfItem);
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    List<MachineFuel> machineFuels = stardustReactorFuels;
//                                    if (machineFuels.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, 0, machineFuels, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof CulinaryGenerator){
//                                    long[] energyInfo = findEnergyInfo_CulinaryGenerator((CulinaryGenerator) sfItem);
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    List<MachineFuel> machineFuels = culinaryGeneratorFuels;
//                                    if (machineFuels.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, 0, machineFuels, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                            }
//                            else if (sfItem.getClass().getPackageName().equals("me.profelements.dynatech.items.electric.growthchambers")){
//                                long[] energyInfo = findEnergyInfo_AbstractElectricMachine((AbstractElectricMachine) sfItem);
//                                if (energyInfo[0] != 0){
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                }else {
//                                    statusBorderReset(menu);
//                                    borderReset(menu);
//                                }
//                                List<MachineRecipe> machineRecipes = growthChamberRecipes.get(sfItem.getClass());
//                                if (machineRecipes.size() != 0){
//                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, energyInfo[1], 0, machineRecipes, sfItem, energyInfo);
//                                    menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                    menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//                                        if (page > 0) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, energyInfo[1],page, machineRecipes, sfItem, energyInfo);
//                                        }
//                                        return false;
//                                    });
//                                    menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                    menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                        if (page < Integer.MAX_VALUE) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, energyInfo[1],page, machineRecipes, sfItem, energyInfo);
//                                        }
//                                        return false;
//                                    });
//                                }else {
//                                    borderReset(menu);
//                                }
//                                return true;
//                            }
//                        }
//                        if (LiteXpansionFlag){
//                            if (sfItem instanceof AdvancedSolarPanel){
//                                long[] energyInfo = findEnergyInfo_LiteX_AdvancedSolarPanel((AdvancedSolarPanel) sfItem);
//                                List<String> lore = new ArrayList<>(Arrays.asList(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNightOrEnd").split("\n")));
//                                lore.set(lore.size() - 1,lore.get(lore.size()-1) + energyInfo[1]
//                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
//                                lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                        + energyInfo[2]
//                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"));
//                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDayOrNether")
//                                                + energyInfo[0]
//                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                        lore)
//                                );
//
//                                SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                recipe1.sfItem = sfItem;
//                                recipe1.sfItemStack = sfItem.getItem();
//                                recipe1.energyPerTick = energyInfo[0];
//                                recipe1.energyPerTickAtNight = energyInfo[1];
//                                menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                return true;
//                            }
//                        }
//                        if (FNAmplificationsFlag){
//                            if (sfItem instanceof CustomMaterialGenerator){
//                                try {
//                                    long[] energyInfo = new long[]{
//                                            0,
//                                            (FNAmplifications
//                                                    .getInstance()
//                                                    .getConfigManager()
//                                                    .getCustomConfig("material-gen-tickrate")
//                                                    .getInt(sfItem.getId() + "." + "tickrate", 1)),
//                                            0
//                                    };
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe(
//                                            cursor.clone()
//                                            ,new MachineRecipeInTicks((int) energyInfo[1],new ItemStack[0], new ItemStack[]{getMaterialGeneratorsOutput((CustomMaterialGenerator) sfItem)}
//                                    ),0,1);
//                                    menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                }catch (Exception ex){
//                                    ex.printStackTrace();
//                                }
//                                return true;
//                            }
//                            else if (sfItem instanceof CustomSolarGen){
//                                long[] energyInfo = findEnergyInfo_FNAmp_CustomSolarGen((CustomSolarGen) sfItem);
//                                List<String> lore = new ArrayList<>(Arrays.asList(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNightOrEnd").split("\n")));
//                                lore.set(lore.size() - 1,lore.get(lore.size()-1) + energyInfo[1]
//                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
//                                lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                        + energyInfo[2]
//                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"));
//                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDayOrNether")
//                                                + energyInfo[0]
//                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                        lore)
//                                );
//
//                                SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                recipe1.sfItem = sfItem;
//                                recipe1.sfItemStack = sfItem.getItem();
//                                recipe1.energyPerTick = energyInfo[0];
//                                recipe1.energyPerTickAtNight = energyInfo[1];
//                                menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                return true;
//                            }
//                            else if (sfItem instanceof CustomPowerGen){
//                                long[] energyInfo = findEnergyInfo_FNAmp_CustomPowerGen((CustomPowerGen) sfItem);
//                                List<String> lore = new ArrayList<>(Arrays.asList(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNightOrEnd").split("\n")));
//                                lore.set(lore.size() - 1,lore.get(lore.size()-1) + energyInfo[1]
//                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
//                                lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                        + energyInfo[2]
//                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"));
//                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDayOrNether")
//                                                + energyInfo[0]
//                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                        lore)
//                                );
//
//                                SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                recipe1.sfItem = sfItem;
//                                recipe1.sfItemStack = sfItem.getItem();
//                                recipe1.energyPerTick = energyInfo[0];
//                                recipe1.energyPerTickAtNight = energyInfo[1];
//                                menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                return true;
//                            }
//                        }
//                        if (SlimeFrameFlag){
//                        }
//                        if (FluffyMachinesFlag){
//                            if (sfItem instanceof AutoTableSaw){
//                                try{
//                                    long[] energyInfo = new long[]{AutoTableSaw.ENERGY_CONSUMPTION,1,AutoTableSaw.CAPACITY};
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                    List<MachineRecipe> machineRecipes = findRecipes_AutoTableSaw((AutoTableSaw) sfItem);
//                                    if (machineRecipes.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                }catch (Exception ex){
//                                    ex.printStackTrace();
//                                }
//                                return true;
//                            }
//                            else if(sfItem instanceof io.ncbpfluffybear.fluffymachines.objects.AutoCrafter){
//                                long[] energyInfo = new long[]{AutoCrafter.ENERGY_CONSUMPTION,1, AutoCrafter.CAPACITY};
//                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                        + energyInfo[0]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                        + energyInfo[2]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                        )
//                                );
//                                try{
//                                    List<SimplePair<ItemStack[],ItemStack>> machineRecipes = getMultiblockAutocrafterRecipes((AutoCrafter) sfItem);
//                                    if (machineRecipes.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                }catch (Exception ex){
//                                    ex.printStackTrace();
//                                    borderReset(menu);
//                                }
//                                return true;
//                            }
//                            else if(sfItem instanceof AutoCraftingTable){
//                                long[] energyInfo = new long[]{AutoCraftingTable.ENERGY_CONSUMPTION,1,AutoCraftingTable.CAPACITY};
//                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                        + energyInfo[0]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                        + energyInfo[2]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                        )
//                                );
//                                if (vanillaRecipeArray.length != 0){
//                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_VanillaAutoCraftingTable(menu, 0, vanillaRecipeArray, sfItem, energyInfo);
//                                    menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                    menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                        if (page > 0) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_VanillaAutoCraftingTable(menu, page, vanillaRecipeArray, sfItem, energyInfo);
//                                        }
//                                        return false;
//                                    });
//                                    menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                    menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                        if (page < Integer.MAX_VALUE) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_VanillaAutoCraftingTable(menu, page, vanillaRecipeArray, sfItem, energyInfo);
//                                        }
//                                        return false;
//                                    });
//
//                                }
//                                else {
//                                    borderReset(menu);
//                                }
//                            }
//                        }
//                        if (sfItem instanceof EnergyNetComponent){
//                            if (sfItem instanceof AContainer){
//                                if (sfItem instanceof ElectricDustWasher){
//                                    long[] energyInfo = findEnergyInfo_ElectricDustWasher((ElectricDustWasher) sfItem);
//                                    long speed = energyInfo[1];
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    List<MachineRecipeWithExpectations> machineRecipes = findRecipes_ElectricDustWasher((ElectricDustWasher) sfItem);
//                                    if (machineRecipes.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipesWithExpectations(menu,speed,0,machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipesWithExpectations(menu,speed,page,machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipesWithExpectations(menu,speed,page,machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                    }
//                                    else {
//                                        borderReset(menu);
//                                    }
//                                }
//                                else if (sfItem instanceof ElectricGoldPan){
//                                    long[] energyInfo = findEnergyInfo_ElectricGoldPan((ElectricGoldPan) sfItem);
//                                    long speed = energyInfo[1];
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    List<MachineRecipeWithExpectations> machineRecipes = findRecipes_ElectricGoldPan((ElectricGoldPan) sfItem);
//                                    if (machineRecipes.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipesWithExpectations(menu,speed,0,machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipesWithExpectations(menu,speed,page,machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipesWithExpectations(menu,speed,page,machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                }
//                                else {
//                                    long[] energyInfo = findEnergyInfo_AContainer((AContainer) sfItem);
//                                    long speed = energyInfo[1];
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    List<MachineRecipe> machineRecipes = findRecipes((AContainer) sfItem);
//                                    if (machineRecipes.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, speed, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, speed, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes(menu, speed, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                }
//                            }
//                            else
//                                if (sfItem instanceof EnergyNetProvider){
//                                if (sfItem instanceof AGenerator){
//                                    long[] energyInfo = findEnergyInfo_AGenerator((AGenerator) sfItem);
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    Set<MachineFuel> machineFuels = findFuels((AGenerator) sfItem);
//                                    if (machineFuels.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, 0, machineFuels, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels(menu, page, machineFuels, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else if (sfItem instanceof Reactor){
//                                    long[] energyInfo = findEnergyInfo_Reactor((Reactor) sfItem);
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_Coolant_Time")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_Coolant_Time_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//                                    }
//                                    else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    Set<MachineFuel> machineFuels = findFuels_Reactor((Reactor) sfItem);
//                                    if (machineFuels.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initGeneratorFuels_Reactor(menu, 0, machineFuels, (Reactor) sfItem,energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels_Reactor(menu, page, machineFuels, (Reactor) sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initGeneratorFuels_Reactor(menu, page, machineFuels, (Reactor) sfItem,energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }else {
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else
//                                    if (sfItem instanceof SolarGenerator){
//                                    long[] energyInfo = findEnergyInfo_SolarGenerator((SolarGenerator) sfItem);//{day energy,night energy,capacity}
//                                    if (energyInfo[0] != 0){
//                                        menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
//                                                                + energyInfo[0]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
//                                                                + energyInfo[1]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                                + energyInfo[2]
//                                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                                )
//                                        );
//
//                                        SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe();
//                                        recipe1.sfItem = sfItem;
//                                        recipe1.sfItemStack = sfItem.getItem();
//                                        recipe1.energyPerTick = energyInfo[0];
//                                        recipe1.energyPerTickAtNight = energyInfo[1];
//                                        menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                    }else {
//                                        statusBorderReset(menu);
//                                        borderReset(menu);
//                                    }
//                                    return true;
//                                }
//                                else {
//                                    statusBorderReset(menu);
//                                    borderReset(menu);
//                                }
//                                return true;
//                            }
//                            else
//                                if (sfItem instanceof Capacitor){
//                                long[] energyInfo = findEnergyInfo_Capacitor((Capacitor) sfItem);
//                                if (energyInfo[2] != 0){
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                }
//                                else {
//                                    statusBorderReset(menu);
//                                    borderReset(menu);
//                                }
//                                return true;
//                            }
//                            else
//                                if (sfItem instanceof AbstractEntityAssembler){
//                                long[] energyInfo = findEnergyInfo_AbstractEntityAssembler((AbstractEntityAssembler) sfItem);
//                                if (energyInfo[0] != 0){
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_Cooldown")
//                                                            + energyInfo[1]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Cooldown_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                }else {
//                                    statusBorderReset(menu);
//                                    borderReset(menu);
//                                }
//                                List<MachineRecipeOutEntity> machineRecipesOutEntity = findRecipes_AbstractEntityAssembler((AbstractEntityAssembler) sfItem);
//                                if (machineRecipesOutEntity.size() != 0){
//                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                    MachineRecipeSerializerInitCrafting.initCraftingRecipesOutEntity(menu, energyInfo[1], 0, machineRecipesOutEntity, sfItem,energyInfo);
//                                    menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                    menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                        if (page > 0) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipesOutEntity(menu, energyInfo[1], page, machineRecipesOutEntity, sfItem,energyInfo);
//                                        }
//                                        return false;
//                                    });
//                                    menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                    menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                        if (page < Integer.MAX_VALUE) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipesOutEntity(menu, energyInfo[1], page, machineRecipesOutEntity, sfItem,energyInfo);
//                                        }
//                                        return false;
//                                    });
//
//                                }else {
//                                    borderReset(menu);
//                                }
//                                return true;
//                            }
//                            else
//                                if (sfItem instanceof FluidPump){
//                                long[] energyInfo = new long[]{FLUID_PUMP_ENERGY_CONSUMPTION,1,((FluidPump)sfItem).getCapacity()};
//                                if (FLUID_PUMP_ENERGY_CONSUMPTION >= 0){
//                                    menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                            + energyInfo[0]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                            + energyInfo[2]
//                                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                            )
//                                    );
//                                }
//                                else {
//                                    statusBorderReset(menu);
//                                    borderReset(menu);
//                                }
//                                SerializedMachine_MachineRecipe recipe1 = new SerializedMachine_MachineRecipe(
//                                        sfItem.getItem(),
//                                        new MachineRecipeInTicks(
//                                                1,new ItemStack[]{new ItemStack(Material.BUCKET)},new ItemStack[]{new ItemStack(Material.WATER_BUCKET)}
//                                        ),
//                                        FLUID_PUMP_ENERGY_CONSUMPTION,
//                                        1
//                                );
//                                menu.addMenuClickHandler(outputButton,initClickHandler(menu,recipe1,null,sfItem));
//                                return true;
//                            }
//                            else
//                                if (sfItem instanceof VanillaAutoCrafter){
//                                long[] energyInfo = new long[]{((VanillaAutoCrafter)sfItem).getEnergyConsumption(),1,((VanillaAutoCrafter)sfItem).getCapacity()};
//                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                        + energyInfo[0]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                        + energyInfo[2]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                        )
//                                );
//                                if (vanillaRecipeArray.length != 0){
//                                    BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                    MachineRecipeSerializerInitCrafting.initCraftingRecipes_VanillaAutoCraftingTable(menu, 0, vanillaRecipeArray, sfItem, energyInfo);
//                                    menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                    menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                        if (page > 0) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_VanillaAutoCraftingTable(menu, page, vanillaRecipeArray, sfItem, energyInfo);
//                                        }
//                                        return false;
//                                    });
//                                    menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                    menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                        int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                        if (page < Integer.MAX_VALUE) {
//                                            BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                            MachineRecipeSerializerInitCrafting.initCraftingRecipes_VanillaAutoCraftingTable(menu, page, vanillaRecipeArray, sfItem, energyInfo);
//                                        }
//                                        return false;
//                                    });
//
//                                }
//                                else {
//                                    borderReset(menu);
//                                }
//                            }
//                            else if (sfItem instanceof EnhancedAutoCrafter){
//                                long[] energyInfo = new long[]{((EnhancedAutoCrafter)sfItem).getEnergyConsumption(),1,((EnhancedAutoCrafter)sfItem).getCapacity()};
//                                menu.replaceExistingItem(statusBorder[0], new CustomItemStack(Material.CYAN_STAINED_GLASS_PANE,
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
//                                                        + energyInfo[0]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
//                                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
//                                                        + energyInfo[2]
//                                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
//                                        )
//                                );
//                                try{
//                                    List<SimplePair<ItemStack[],ItemStack>> machineRecipes = getMultiblockAutocrafterRecipes((EnhancedAutoCrafter) sfItem);
//                                    if (machineRecipes.size() != 0){
//                                        BlockStorage.addBlockInfo(menu.getLocation(), "page", "0");
//                                        MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, 0, machineRecipes, sfItem, energyInfo);
//                                        menu.replaceExistingItem(prevAndNext[0], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_PrevPage")));
//                                        menu.addMenuClickHandler(prevAndNext[0], (p1, slot1, item, action1) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page > 0) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page-1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//                                        menu.replaceExistingItem(prevAndNext[1], new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE, properties.getReplacedProperty("Test_InfoProvider_NextPage")));
//                                        menu.addMenuClickHandler(prevAndNext[1], (p12, slot12, item, action12) -> {
//
//                                            int page = new JSONObject(BlockStorage.getBlockInfoAsJson(menu.getLocation())).getInt("page");
//
//                                            if (page < Integer.MAX_VALUE) {
//                                                BlockStorage.addBlockInfo(menu.getLocation(), "page", String.valueOf(page+1));
//                                                MachineRecipeSerializerInitCrafting.initCraftingRecipes_RecipePairs(menu, 1, page, machineRecipes, sfItem, energyInfo);
//                                            }
//                                            return false;
//                                        });
//
//                                    }
//                                    else {
//                                        borderReset(menu);
//                                    }
//                                }catch (Exception ex){
//                                    ex.printStackTrace();
//                                    borderReset(menu);
//                                }
//                            }
//                        }
//                        else {
//                            statusBorderReset(menu);
//                            borderReset(menu);
//                        }
//                        return true;
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
                return new int[0];
            }
        };
    }
    public static void statusBorderReset(BlockMenu preset){
        for (int i:statusBorder){
            preset.replaceExistingItem(i, new CustomItemStack(Material.RED_STAINED_GLASS_PANE, ""));
            preset.addMenuClickHandler(i, (p, slot, item, action) -> false);
        }
    }
    public static void borderReset(BlockMenu preset){
        for (int i:border){
            preset.replaceExistingItem(i, new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ""));
            preset.addMenuClickHandler(i, (p, slot, item, action) -> {
                if (preset.getItemInSlot(slot).hasItemMeta() && preset.getItemInSlot(slot).getItemMeta().hasLore()){
                    instance.msgSend(p,preset.getItemInSlot(slot).getItemMeta().getLore());
                }
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
        preset.addMenuClickHandler(outputDescriptionSlot,(p, slot, item, action) -> false);
        preset.replaceExistingItem(inputDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_inputDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_inputDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(inputDescriptionSlot,(p, slot, item, action) -> false);
        preset.replaceExistingItem(catalyzerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_catalyzerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_catalyzerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(catalyzerDescriptionSlot,(p, slot, item, action) -> false);
        preset.replaceExistingItem(stabilizerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_stabilizerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_stabilizerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(stabilizerDescriptionSlot,(p, slot, item, action) -> false);
    }
    public static void statusBorderInit(BlockMenuPreset preset){
        for (int i:statusBorder){
            preset.addItem(i, new CustomItemStack(Material.RED_STAINED_GLASS_PANE, ""));
            preset.addMenuClickHandler(i, (p, slot, item, action) -> false);
        }
    }
    public static void borderInit(BlockMenuPreset preset){
        for (int i:border){
            preset.addItem(i, new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, ""));
            preset.addMenuClickHandler(i, (p, slot, item, action) -> {
                if (preset.getItemInSlot(slot).hasItemMeta() && preset.getItemInSlot(slot).getItemMeta().hasLore()){
                    instance.msgSend(p,preset.getItemInSlot(slot).getItemMeta().getLore());
                }
                return false;
            });
        }
        preset.addItem(outputButton,new CustomItemStack(Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_OutputButton_Name"),
                properties.getReplacedProperties("Serializer_OutputButton_Lore_1",
                        ChatColor.GRAY)));
        preset.addMenuClickHandler(outputButton,(p, slot, item, action) -> false);
        preset.addItem(outputDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_outputDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_outputDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(outputDescriptionSlot,(p, slot, item, action) -> false);
        preset.addItem(inputDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_inputDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_inputDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(inputDescriptionSlot,(p, slot, item, action) -> false);
        preset.addItem(catalyzerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_catalyzerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_catalyzerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(catalyzerDescriptionSlot,(p, slot, item, action) -> false);
        preset.addItem(stabilizerDescriptionSlot,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("Serializer_stabilizerDescriptionSlot_Name"),
                properties.getReplacedProperties("Serializer_stabilizerDescriptionSlot_Lore_1",ChatColor.GRAY)));
        preset.addMenuClickHandler(stabilizerDescriptionSlot,(p, slot, item, action) -> false);
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
    public static ChestMenu.MenuClickHandler initClickHandler(BlockMenu menu,SerializedMachine_MachineRecipe machineRecipe, ItemStack catalyzer,SlimefunItem sfItem){
        return (p, slot, item, action) -> {
            Bukkit.getScheduler().runTask(instance, p::updateInventory);
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
            String inputName;
            if (inSlotItem.hasItemMeta() && inSlotItem.getItemMeta().hasDisplayName()){
                inputName = inSlotItem.getItemMeta().getDisplayName();
            }else {
                inputName = NameUtil.nameForMaterial(inSlotItem.getType());
            }

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
                        lore.add(ChatColor.WHITE + name + properties.getReplacedProperty("Stabilizer_Output_Unit") + output1.getAmount());
                    }
                }else {
                    for (int i=0;i<machineRecipe.outputs.length;i++){
                        ItemStack output1 = machineRecipe.outputs[i];
                        String name = NameUtil.findName(output1);
                        lore.add(ChatColor.WHITE + name
                                + properties.getReplacedProperty("Stabilizer_Output_Unit") + output1.getAmount()
                                + properties.getReplacedProperty("Stabilizer_Output_Expectation")
                                + machineRecipe.outputExpectations[i]);
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
            if (!worldStr.equals("")){
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
            if (lore.size() != 0){
                lore = new CustomItemStack(Material.BEDROCK,"1",lore).getItemMeta().getLore();
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
