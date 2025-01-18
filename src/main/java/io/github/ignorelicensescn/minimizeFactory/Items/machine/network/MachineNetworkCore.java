package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.PersistentSerializedMachineRecipeType;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemStackUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.Serializations;
import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.DividingsOperation;
import io.github.ignorelicensescn.minimizeFactory.utils.network.NodeType;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimpleFour;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimpleTri;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONArray;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.emptyItemStackArray;
import static io.github.ignorelicensescn.minimizeFactory.utils.ItemMetaRelated.PersistentSerializedMachineRecipeType.SERIALIZED_MACHINE_RECIPE;
import static io.github.ignorelicensescn.minimizeFactory.utils.ItemStackUtil.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.DividingsOperation.DIVIDING1_BIGINTEGER;
import static io.github.ignorelicensescn.minimizeFactory.utils.network.NodeKeys.*;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.GEOTHERMAL;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.REINFORCED_GEOTHERMAL;

public class MachineNetworkCore extends NetworkNode{
    public static final int[] BORDER = new int[]{
             0, 1, 2, 3, 4, 5, 6, 7, 8,
             9,10,11,12,13,14,15,16,17,
            18,19,20,21,22,23,24,25,26,
            27,28,            33,34,35,
            36,37,38,39,40,41,42,43,44,
            45,46,47,48,49,50,51,52,53
    };
    public static final int[] FULL_BORDER = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9,10,11,12,13,14,15,16,17,
            18,19,20,21,22,23,24,25,26,
            27,28,29,30,31,32,33,34,35,
            36,37,38,39,40,41,42,43,44,
            45,46,47,48,49,50,51,52,53
    };
    public static final int SHOW_INPUT = 45;
    public static final int SHOW_OUTPUT = 46;
    public static final int SHOW_STABLE_OUTPUT = 47;
    public static final int SHOW_POWER_SLOT = 48;
    public static final int BUTTON_ESTABLISH_MACHINE_NETWORK = 29;
    public static final ItemStack ITEM_ESTABLISH_MACHINE_NETWORK = new CustomItemStack(
            Material.GREEN_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Establish_Network"),
            properties.getReplacedProperties("MachineNetworkCore_Establish_Network_Lore", ChatColor.GRAY)
    );
    public static final ItemStack ITEM_STARTING_NETWORK = new CustomItemStack(
            Material.ORANGE_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Establishing_Network")
    );
    public static final int BUTTON_LOCK_MACHINE_NETWORK = 30;
    public static final ItemStack ITEM_LOCK_MACHINE_NETWORK = new CustomItemStack(
            Material.YELLOW_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Lock_Network"),
            properties.getReplacedProperties("MachineNetworkCore_Lock_Network_Lore",ChatColor.GRAY)
    );
    public static final ItemStack ITEM_LOCKING_MACHINE_NETWORK = new CustomItemStack(
            Material.YELLOW_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Locking_Network"),
            properties.getReplacedProperties("MachineNetworkCore_Locking_Network_Lore_1",ChatColor.GRAY)
    );
    public static final int BUTTON_UNLOCK_MACHINE_NETWORK = 31;
    public static final ItemStack ITEM_UNLOCK_MACHINE_NETWORK = new CustomItemStack(
            Material.ORANGE_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Unlock_Network"),
            properties.getReplacedProperties("MachineNetworkCore_Unlock_Network_Lore",ChatColor.GRAY)
    );
    public static final ItemStack ITEM_UNLOCKING_MACHINE_NETWORK = new CustomItemStack(
            Material.ORANGE_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Unlocking_Network"),
            properties.getReplacedProperties("MachineNetworkCore_Unlocking_Network_Lore_1",ChatColor.GRAY)
    );
    public static final int BUTTON_TERMINATE_MACHINE_NETWORK = 32;
    public static final ItemStack ITEM_TERMINATE_MACHINE_NETWORK = new CustomItemStack(
            Material.RED_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Terminate_Network"),
            properties.getReplacedProperties("MachineNetworkCore_Terminate_Network_Lore",ChatColor.GRAY)
    );
    public static final ItemStack ITEM_TERMINATING_MACHINE_NETWORK = new CustomItemStack(
            Material.RED_STAINED_GLASS_PANE,
            properties.getReplacedProperty("MachineNetworkCore_Terminating_Network"),
            properties.getReplacedProperties("MachineNetworkCore_Terminating_Network_Lore",ChatColor.GRAY)
    );
    public static final ItemStack BORDER_ITEM = new CustomItemStack(
            Material.GRAY_STAINED_GLASS_PANE,""
    );

    public static final int COUNTER_SLOT = 53;

    public MachineNetworkCore(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.CONTROLLER);
        new BlockMenuPreset(getId(),getItemName()){

            @Override
            public void init() {
                for (int i:FULL_BORDER){
                    addItem(i,BORDER_ITEM.clone(),((p, slot, item1, action) -> false));
                }
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                super.newInstance(menu, b);
                initNode(b.getLocation());
                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                if (!coreInfo.has(NETWORK_CONTROLLER_STATUS)){
                    coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_OFFLINE);
                }
                if (!coreInfo.has(MINIMIZEFACTORY_BRIDGES)){
                    coreInfo.put(MINIMIZEFACTORY_BRIDGES,emptyJSONArray.toString());
                }
                if (!coreInfo.has(MINIMIZEFACTORY_STORAGES)){
                    coreInfo.put(MINIMIZEFACTORY_STORAGES,emptyJSONArray.toString());
                }
                if (!coreInfo.has(MINIMIZEFACTORY_CONTAINERS)){
                    coreInfo.put(MINIMIZEFACTORY_CONTAINERS,emptyJSONArray.toString());
                }
                if (!coreInfo.has(NETWORK_COUNTER)){
                    coreInfo.put(NETWORK_COUNTER,0);
                }
                if (!coreInfo.has(MINIMIZEFACTORY_INPUT_ARRAY)){
                    coreInfo.put(MINIMIZEFACTORY_INPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                }
                if (!coreInfo.has(MINIMIZEFACTORY_OUTPUT_ARRAY)){
                    coreInfo.put(MINIMIZEFACTORY_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                }
                if (!coreInfo.has(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY)){
                    coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                }
                if (!coreInfo.has(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY)){
                    coreInfo.put(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                }
                if (!coreInfo.has(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY)){
                    coreInfo.put(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                }
                if (!coreInfo.has(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY)){
                    coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                }
                if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_PRODUCTION)){
                    coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, BigInteger.ZERO.toString());
                }
                if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_CONSUMPTION)){
                    coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, BigInteger.ZERO.toString());
                }
                if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE)){
                    coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, BigInteger.ZERO.toString());
                }
                if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE)){
                    coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, BigInteger.ZERO.toString());
                }

                int count = coreInfo.getInt(NETWORK_COUNTER);
                String name = properties.getReplacedProperty("MachineNetworkCore_Counter") + count;
                menu.replaceExistingItem(COUNTER_SLOT,
                        new CustomItemStack(
                                Material.GREEN_STAINED_GLASS_PANE,
                                name
                        )
                );
                menu.addMenuClickHandler(COUNTER_SLOT,(p, slot, item1, action) -> false);
                BlockStorage.setBlockInfo(b,coreInfo.toString(),true);
                refresh(menu,b, coreInfo.getString(NETWORK_CONTROLLER_STATUS));
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
    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

                           @Override
                           public void tick(Block b, SlimefunItem sf, Config data) {
                               MachineNetworkCore.this.tick(b);
                           }

                           @Override
                           public boolean isSynchronized() {
                               return true;
                           }
                       },
                new BlockBreakHandler(false,false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(e.getBlock()));
                        if (coreInfo.has(NETWORK_CONTROLLER_STATUS)
                        && !coreInfo.get(NETWORK_CONTROLLER_STATUS).equals(NETWORK_CONTROLLER_OFFLINE)){
                            e.getPlayer().sendMessage(properties.getReplacedProperty("MachineNetworkCore_Cannot_Break"));
                            e.setCancelled(true);
                        }
                    }
                }
        );
    }
    public void tick(Block b){
        BlockMenu menu = BlockStorage.getInventory(b);
        int count = new JSONObject(BlockStorage.getBlockInfoAsJson(b.getLocation())).getInt(NETWORK_COUNTER);
        if (!NameUtil.findName(menu.getItemInSlot(COUNTER_SLOT)).split(" ")[1].equals(String.valueOf(count))) {
            String name = properties.getReplacedProperty("MachineNetworkCore_Counter") + count;
            menu.replaceExistingItem(COUNTER_SLOT,
                    new CustomItemStack(
                            Material.GREEN_STAINED_GLASS_PANE,
                            name
                    )
            );
            menu.addMenuClickHandler(COUNTER_SLOT,(p, slot, item1, action) -> false);
        }
    }
    public static void refresh(BlockMenu menu,Block b,String status){
        switch (status){
            case NETWORK_CONTROLLER_OFFLINE -> {
                menu.replaceExistingItem(BUTTON_LOCK_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_LOCK_MACHINE_NETWORK,ChestMenuUtils.getEmptyClickHandler());
                menu.replaceExistingItem(BUTTON_TERMINATE_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_TERMINATE_MACHINE_NETWORK,(p, slot, item1, action) -> false);
                menu.replaceExistingItem(BUTTON_ESTABLISH_MACHINE_NETWORK, ITEM_ESTABLISH_MACHINE_NETWORK.clone());
                menu.addMenuClickHandler(BUTTON_ESTABLISH_MACHINE_NETWORK, (p, slot, item, action) -> {
                    long current = System.currentTimeMillis();
                    UUID playerUUID = p.getUniqueId();

                    if (!PlayerLastConnectorUsedTime.containsKey(playerUUID)){
                        PlayerLastConnectorUsedTime.put(playerUUID, 0L);
                    }
                    if (
                            (NETWORK_CONNECTOR_DELAY_FOR_ALL > (current - lastConnectorUsedTime))
                            || (
                                    (current - PlayerLastConnectorUsedTime.get(playerUUID)) < NETWORK_CONNECTOR_DELAY_FOR_ONE
                                    )
                    ){
                        p.sendMessage(String.format(
                                properties.getReplacedProperty("Connector_Cooldown"),
                                Math.max(NETWORK_CONNECTOR_DELAY_FOR_ALL - (current - lastConnectorUsedTime),
                                        NETWORK_CONNECTOR_DELAY_FOR_ONE - (current - PlayerLastConnectorUsedTime.get(playerUUID)))
                        ));
                        return false;
                    }
                    lastConnectorUsedTime = current;
                    PlayerLastConnectorUsedTime.put(playerUUID,current);
                    JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                    coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_STARTING);
                    coreInfo.put(NETWORK_COUNTER,1);
                    BlockStorage.setBlockInfo(b,coreInfo.toString(),true);
                    registerNodes(b.getLocation(),b.getLocation(),0);
                    refresh(menu,b,NETWORK_CONTROLLER_STARTING);
                    return false;
                });
            }
            case NETWORK_CONTROLLER_STARTING -> {
                menu.replaceExistingItem(BUTTON_ESTABLISH_MACHINE_NETWORK, ITEM_STARTING_NETWORK.clone());
                menu.addMenuClickHandler(BUTTON_ESTABLISH_MACHINE_NETWORK, ChestMenuUtils.getEmptyClickHandler());
            }
            case NETWORK_CONTROLLER_ONLINE -> {
                menu.replaceExistingItem(BUTTON_ESTABLISH_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_ESTABLISH_MACHINE_NETWORK,(p, slot, item1, action) -> false);
                menu.replaceExistingItem(BUTTON_LOCK_MACHINE_NETWORK, ITEM_LOCK_MACHINE_NETWORK.clone());
                menu.addMenuClickHandler(BUTTON_LOCK_MACHINE_NETWORK, (p, slot, item, action) -> {
                    JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                    coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_LOCKING);
                    coreInfo.put(NETWORK_CONTROLLER_LOCK_TIME,System.currentTimeMillis());
                    BlockStorage.setBlockInfo(b,coreInfo.toString(),true);
                    refresh(menu,b,NETWORK_CONTROLLER_LOCKING);
                    return false;
                });
                menu.replaceExistingItem(BUTTON_TERMINATE_MACHINE_NETWORK, ITEM_TERMINATE_MACHINE_NETWORK.clone());
                menu.addMenuClickHandler(BUTTON_TERMINATE_MACHINE_NETWORK, (p, slot, item, action) -> {
                    long current = System.currentTimeMillis();
                    UUID playerUUID = p.getUniqueId();
                    if (!PlayerLastConnectorUsedTime.containsKey(playerUUID)){
                        PlayerLastConnectorUsedTime.put(playerUUID, 0L);
                    }
                    if (
                            (NETWORK_CONNECTOR_DELAY_FOR_ALL > (current - lastConnectorUsedTime))
                                    || (
                                    (current - PlayerLastConnectorUsedTime.get(playerUUID)) < NETWORK_CONNECTOR_DELAY_FOR_ONE
                            )
                    ){
                        p.sendMessage(String.format(properties.getReplacedProperty("Connector_Cooldown"),
                                Math.max(NETWORK_CONNECTOR_DELAY_FOR_ALL - (current - lastConnectorUsedTime),NETWORK_CONNECTOR_DELAY_FOR_ONE - (current - PlayerLastConnectorUsedTime.get(playerUUID)))
                        ));
                        return false;
                    }
                    lastConnectorUsedTime = current;
                    PlayerLastConnectorUsedTime.put(playerUUID,current);

                    menu.replaceExistingItem(BUTTON_TERMINATE_MACHINE_NETWORK,ITEM_TERMINATING_MACHINE_NETWORK.clone());
                    menu.addMenuClickHandler(BUTTON_TERMINATE_MACHINE_NETWORK, (p1, slot1, item1, action1) -> false);
                    JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                    coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_DISABLING);
                    BlockStorage.setBlockInfo(b,coreInfo.toString(),true);
                    refresh(menu,b,NETWORK_CONTROLLER_DISABLING);
                    return false;
                });
                menu.replaceExistingItem(BUTTON_UNLOCK_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_UNLOCK_MACHINE_NETWORK,ChestMenuUtils.getEmptyClickHandler());
            }
            case NETWORK_CONTROLLER_LOCKING -> {
                menu.replaceExistingItem(BUTTON_LOCK_MACHINE_NETWORK,ITEM_LOCKING_MACHINE_NETWORK.clone());
                menu.addMenuClickHandler(BUTTON_LOCK_MACHINE_NETWORK, (p1, slot1, item1, action1) -> false);
                menu.replaceExistingItem(BUTTON_TERMINATE_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_TERMINATE_MACHINE_NETWORK, (p1, slot1, item1, action1) -> false);
                lockNode(b.getLocation());
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        {
                            JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                            {
                                {
                                    JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                                    HashSet<String> cache = new HashSet<>();
                                    for (int i = 0; i < containers.length(); i++) {
                                        cache.add(containers.getJSONObject(i).toString());
                                    }
                                    List<JSONObject> cacheResult = new ArrayList<>();
                                    for (String jsonStr : cache) {
                                        cacheResult.add(new JSONObject(jsonStr));
                                    }
                                    containers = new JSONArray(cacheResult);
                                    coreInfo.put(MINIMIZEFACTORY_CONTAINERS, containers.toString());
                                }
                                {
                                    JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                                    HashSet<String> cache = new HashSet<>();
                                    for (int i = 0; i < storages.length(); i++) {
                                        cache.add(storages.getJSONObject(i).toString());
                                    }
                                    List<JSONObject> cacheResult = new ArrayList<>();
                                    for (String jsonStr : cache) {
                                        cacheResult.add(new JSONObject(jsonStr));
                                    }
                                    storages = new JSONArray(cacheResult);
                                    coreInfo.put(MINIMIZEFACTORY_STORAGES, storages.toString());
                                }
                                {
                                    JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
                                    HashSet<String> cache = new HashSet<>();
                                    for (int i = 0; i < bridges.length(); i++) {
                                        cache.add(bridges.getJSONObject(i).toString());
                                    }
                                    List<JSONObject> cacheResult = new ArrayList<>();
                                    for (String jsonStr : cache) {
                                        cacheResult.add(new JSONObject(jsonStr));
                                    }
                                    bridges = new JSONArray(cacheResult);
                                    coreInfo.put(MINIMIZEFACTORY_BRIDGES, bridges.toString());
                                }
                                BlockStorage.setBlockInfo(b, coreInfo.toString(), false);
                            }
                            {
                                JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                                for (int i = 0; i < containers.length(); i++) {
                                    Location location = Serializations.StringToLocation(containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION));
                                    lockNode(location);
                                    setCoreStatusIndex(NodeType.MACHINE_CONTAINER, location);
                                }
                            }
                            {
                                JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                                for (int i = 0; i < storages.length(); i++) {
                                    Location location = Serializations.StringToLocation(storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION));
                                    lockNode(location);
                                    setCoreStatusIndex(NodeType.STORAGE, location);
                                }
                            }
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                //I need to explain what I want:
                                //for every container,
                                //we have machine outputs from containers before
                                //then for every machine in container
                                //we have outputs from all machines before
                                //a machine(with recipe,serialized) has input,output,energy consumption/production
                                //find if input can be provided by machine before it,if not,use storage to provide
                                //we can see part of inputs can be provided by 'stable outputs'(outputs that no need to input)
                                //then use materialFactor(<= 1) to multiply the input and output,
                                // consume materialFactor*input from 'stable outputs',
                                // and add output*materialFactor for 'stable outputs'
                                //remaining input and output needs input from storage

                                //but there's one question:
                                //for example:now we have a fridge and a pump
                                //fridge needs water bucket to produce empty bucket and ice,while pump needs empty bucket to produce a filled one
                                //when there's power,it's in fact possible to produce lots of ice
                                //but if calculate one by one, we got this:
                                //input: EMPTY_BUCKET*1,WATER_BUCKET*1
                                //output: WATER_BUCKET*1,EMPTY_BUCKET*1,ice
                                //so we need to solve this,use ItemStackUtil.reformatInputAndOutputs() to reformat it,
                                // when input.length==0,all output goes to stable_output

                                super.run();
                                JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));

                                //clear info,or every lock increases production.
                                coreInfo.put(MINIMIZEFACTORY_INPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                                coreInfo.put(MINIMIZEFACTORY_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                                coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                                coreInfo.put(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                                coreInfo.put(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                                coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, BigInteger.ZERO.toString());
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, BigInteger.ZERO.toString());
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, BigInteger.ZERO.toString());
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, BigInteger.ZERO.toString());

                                JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                                for (int i = 0; i < containers.length(); i++) {
                                    JSONObject jsonObject = containers.getJSONObject(i);
                                    Location containerLocation = Serializations.StringToLocation(jsonObject.getString(MINIMIZEFACTORY_NODE_LOCATION));
                                    Biome containerBiome = containerLocation.getBlock().getBiome();
                                    World.Environment containerEnvironment = containerLocation.getWorld().getEnvironment();
                                    BlockMenu locationMenu = BlockStorage.getInventory(containerLocation);
                                    if (locationMenu == null) {
                                        logger.log(Level.WARNING, containerLocation + " : " + properties.getReplacedProperty("MachineNetworkCore_Exception_Null_Container"));
                                        continue;
                                    }
                                    ItemStack[] calculatedStableOutputs = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY));
                                    BigInteger[][] calculatedStableOutputAmount = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY));
                                    ItemStack[] calculatedOutputs = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_OUTPUT_ARRAY));
                                    BigInteger[][] calculatedOutputAmount = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY));
                                    ItemStack[] calculatedInputs = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_INPUT_ARRAY));
                                    BigInteger[][] calculatedInputAmount = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY));
                                    SimplePair<ItemStack[], BigInteger[][]> calculatedInputPair = new SimplePair<>(calculatedInputs, calculatedInputAmount);
                                    SimplePair<ItemStack[], BigInteger[][]> calculatedOutputPair = new SimplePair<>(calculatedOutputs, calculatedOutputAmount);
                                    SimplePair<ItemStack[], BigInteger[][]> calculatedStableOutputPair = new SimplePair<>(calculatedStableOutputs, calculatedStableOutputAmount);
                                    BigInteger[] energy = new BigInteger[]{
                                            coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION),
                                            coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION)
                                    };
                                    BigInteger[] energyStable = new BigInteger[]{
                                            coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE),
                                            coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE)
                                    };
                                    for (int j : MachineNetworkContainer.INPUTS) {
                                        ItemStack itemStack = locationMenu.getItemInSlot(j);
                                        if (itemStack == null) {
                                            continue;
                                        }
                                        if (!itemStack.getType().equals(Material.TRAPPED_CHEST)) {
                                            continue;
                                        }
                                        if (!itemStack.hasItemMeta()) {
                                            continue;
                                        }
                                        ItemMeta itemMeta = itemStack.getItemMeta();
                                        assert itemMeta != null;
                                        int machineAmount = itemStack.getAmount();
                                        Optional<SerializedMachine_MachineRecipe> optional = DataTypeMethods.getOptionalCustom(itemMeta, SERIALIZED_MACHINE_RECIPE, PersistentSerializedMachineRecipeType.TYPE);
                                        optional.ifPresent(machineRecipe -> {
                                            BigInteger energyInfo;
                                            if (InfinityExpansionFlag && machineRecipe.sfItem != null && (machineRecipe.sfItem.getId().equals(GEOTHERMAL.getItemId()) || machineRecipe.sfItem.getId().equals(REINFORCED_GEOTHERMAL.getItemId()))) {
                                                if (containerEnvironment.equals(World.Environment.NETHER)) {
                                                    energyInfo = BigInteger.valueOf(machineRecipe.energyPerTickAtNight).multiply(BigInteger.valueOf(machineAmount));
                                                } else {
                                                    energyInfo = BigInteger.valueOf(machineRecipe.energyPerTick).multiply(BigInteger.valueOf(machineAmount));
                                                }
                                            } else {
                                                if (machineRecipe.env != null && !machineRecipe.env.equals(containerEnvironment)) {
                                                    return;
                                                }
                                                if (machineRecipe.biome != null && !machineRecipe.biome.equals(containerBiome)) {
                                                    return;
                                                }
                                                energyInfo = BigInteger.valueOf(machineRecipe.energyPerTick).add(BigInteger.valueOf(machineRecipe.energyPerTickAtNight)).multiply(BigInteger.valueOf(machineAmount)).divide(BigInteger.TWO);
                                            }
                                            BigInteger[] materialFactor = new BigInteger[]{BigInteger.ONE, BigInteger.ONE};
                                            SimplePair<ItemStack[], BigInteger[][]> outputPair = null;

                                            if (machineRecipe.outputs != null) {
                                                if (machineRecipe.outputExpectations != null) {
                                                    outputPair = new SimplePair<>(machineRecipe.outputs, DividingsOperation.dividingsToBigInteger(machineRecipe.outputExpectations));
                                                    for (int k = 0; k < machineRecipe.outputs.length; k++) {
                                                        outputPair.second[k][0] = outputPair.second[k][0].multiply(BigInteger.valueOf(outputPair.first[k].getAmount()));
                                                    }
                                                } else {
                                                    outputPair = reformatItemStackArrayWithAmountDividing_BigInteger(machineRecipe.outputs);
                                                }
                                                for (int i2 = 0; i2 < outputPair.second.length; i2++) {
                                                    outputPair.second[i2][0] = outputPair.second[i2][0].multiply(BigInteger.valueOf(machineAmount));
                                                    if (machineRecipe.ticks != 1) {
                                                        outputPair.second[i2][1] = outputPair.second[i2][1].multiply(BigInteger.valueOf(machineRecipe.ticks));
                                                    }
                                                }
                                            }
                                            if (machineRecipe.inputs != null) {

                                                SimplePair<ItemStack[], BigInteger[][]> inputPair = reformatItemStackArrayWithAmountDividing_BigInteger(machineRecipe.inputs);
                                                for (int i2 = 0; i2 < inputPair.second.length; i2++) {
                                                    inputPair.second[i2][0] = inputPair.second[i2][0].multiply(BigInteger.valueOf(machineAmount));
                                                    if (machineRecipe.ticks != 1) {
                                                        inputPair.second[i2][1] = inputPair.second[i2][1].multiply(BigInteger.valueOf(machineRecipe.ticks));
                                                    }
                                                }
                                                SimpleFour<ItemStack[], BigInteger[][], ItemStack[], BigInteger[][]> tryFitIn = reformatInputAndOutputs_BigInteger(inputPair.first, inputPair.second, calculatedStableOutputPair.first, calculatedStableOutputPair.second);
                                                if (tryFitIn.a.length == 0) {
                                                    int compare = energyInfo.compareTo(BigInteger.ZERO);
                                                    if (compare > 0) {
                                                        energy[1] = energy[1].add(energyInfo);
                                                        energyStable[1] = energyStable[1].add(energyInfo);
                                                    }
                                                    else if (compare < 0) {
                                                        energy[0] = energy[0].add(energyInfo);
                                                        energyStable[0] = energyStable[0].add(energyInfo);
                                                    }
                                                    if (outputPair != null) {
                                                        List<ItemStack> first = new ArrayList<>(Arrays.asList(tryFitIn.c));
                                                        List<BigInteger[]> second = new ArrayList<>(Arrays.asList(tryFitIn.d));
                                                        for (int k = 0; k < outputPair.first.length; k++) {
                                                            first.add(outputPair.first[k]);
                                                            second.add(outputPair.second[k]);
                                                        }
                                                        calculatedStableOutputPair.first = first.toArray(emptyItemStackArray);
                                                        calculatedStableOutputPair.second = second.toArray(emptyDividingsArray_BigInteger);
                                                    }
                                                } else {
                                                    //calculate materialFactor
                                                    for (int inputIndex = 0; inputIndex < inputPair.first.length; inputIndex++) {
                                                        boolean breakFlag = true;
                                                        ItemStack required = inputPair.first[inputIndex];
                                                        BigInteger[] requiredAmount = inputPair.second[inputIndex];
                                                        for (int stableOutIndex = 0; stableOutIndex < calculatedStableOutputPair.first.length; stableOutIndex++) {
                                                            if (isItemStackSimilar(required, calculatedStableOutputPair.first[stableOutIndex])) {
                                                                breakFlag = false;
                                                                BigInteger[] divided = DividingsOperation.divide(
                                                                        calculatedStableOutputPair.second[stableOutIndex], requiredAmount);
                                                                materialFactor = DividingsOperation.min(materialFactor,
                                                                        divided
                                                                );
                                                                break;
                                                            }
                                                        }
                                                        if (breakFlag) {
                                                            materialFactor[0] = BigInteger.ZERO;
                                                            break;
                                                        }
                                                    }

                                                    //END:calculate materialFactor
                                                    if (!Objects.equals(materialFactor[0], BigInteger.ZERO)) {
                                                        BigInteger[] remainFactor = new BigInteger[]{
                                                                materialFactor[1].subtract(materialFactor[0]),
                                                                materialFactor[1]
                                                        };
                                                        List<ItemStack> newStableOuts = new ArrayList<>();
                                                        List<ItemStack> newInputs = new ArrayList<>();
                                                        List<ItemStack> newOutputs = new ArrayList<>();
                                                        List<BigInteger[]> newStableOutsAmount = new ArrayList<>();
                                                        List<BigInteger[]> newInputsAmount = new ArrayList<>();
                                                        List<BigInteger[]> newOutputsAmount = new ArrayList<>();
                                                        for (int index = 0; index < inputPair.first.length; index++) {
                                                            newStableOuts.add(inputPair.first[index]);
                                                            newStableOutsAmount.add(
                                                                    DividingsOperation.multiply(
                                                                            new BigInteger[]{materialFactor[0].multiply(BigInteger.valueOf(-1)),
                                                                                    materialFactor[1]}
                                                                            , inputPair.second[index])
                                                            );
                                                            newInputs.add(
                                                                    inputPair.first[index]
                                                            );
                                                            newInputsAmount.add(
                                                                    DividingsOperation.multiply(remainFactor, inputPair.second[index])
                                                            );
                                                        }
                                                        newInputs.addAll(List.of(calculatedInputPair.first));
                                                        newInputsAmount.addAll(List.of(calculatedInputPair.second));
                                                        calculatedInputPair.first = newInputs.toArray(emptyItemStackArray);
                                                        calculatedInputPair.second = newInputsAmount.toArray(emptyDividingsArray_BigInteger);
                                                        if (outputPair != null) {
                                                            for (int outputIndex = 0; outputIndex < outputPair.first.length; outputIndex++) {
                                                                newStableOuts.add(outputPair.first[outputIndex]);
                                                                newStableOutsAmount.add(
                                                                        DividingsOperation.multiply(remainFactor, outputPair.second[outputIndex])
                                                                );
                                                                newOutputs.add(outputPair.first[outputIndex]);
                                                                newOutputsAmount.add(
                                                                        DividingsOperation.multiply(materialFactor, outputPair.second[outputIndex])
                                                                );
                                                            }
                                                            newStableOuts.addAll(List.of(calculatedStableOutputPair.first));
                                                            newStableOutsAmount.addAll(List.of(calculatedStableOutputPair.second));
                                                            calculatedStableOutputPair.first = newStableOuts.toArray(emptyItemStackArray);
                                                            calculatedStableOutputPair.second = newStableOutsAmount.toArray(emptyDividingsArray_BigInteger);
                                                            newOutputs.addAll(List.of(calculatedOutputPair.first));
                                                            newOutputsAmount.addAll(List.of(calculatedOutputPair.second));
                                                            calculatedOutputPair.first = newOutputs.toArray(emptyItemStackArray);
                                                            calculatedOutputPair.second = newOutputsAmount.toArray(emptyDividingsArray_BigInteger);
                                                        }
                                                        int compare = energyInfo.compareTo(BigInteger.ZERO);
                                                        if (compare > 0) {
                                                            energyInfo = energyInfo.multiply(materialFactor[0]).divide(materialFactor[1]);
                                                            energy[1] = energy[1].add(energyInfo);
                                                        } else if (compare < 0) {
                                                            energyInfo = energyInfo.multiply(materialFactor[0]).divide(materialFactor[1]);
                                                            energy[0] = energy[0].add(energyInfo);
                                                        }
                                                    } else {

                                                        if (outputPair != null) {
                                                            List<ItemStack> newOuts = new ArrayList<>(List.of(calculatedOutputPair.first));
                                                            List<BigInteger[]> newOutsAmount = new ArrayList<>(List.of(calculatedOutputPair.second));
                                                            newOuts.addAll(Arrays.asList(outputPair.first));
                                                            newOutsAmount.addAll(Arrays.asList(outputPair.second));
                                                            calculatedOutputPair.first = newOuts.toArray(emptyItemStackArray);
                                                            calculatedOutputPair.second = newOutsAmount.toArray(emptyDividingsArray_BigInteger);
                                                        }

                                                        SimplePair<ItemStack[], BigInteger[][]> sumOfInputs = addItemStacks_BigInteger(calculatedInputPair.first, calculatedInputPair.second, inputPair.first, inputPair.second);
                                                        calculatedInputPair.first = sumOfInputs.first;
                                                        calculatedInputPair.second = sumOfInputs.second;
                                                        int compare = energyInfo.compareTo(BigInteger.ZERO);
                                                        if (compare > 0) {
                                                            energy[1] = energy[1].add(energyInfo);
                                                        } else if (compare < 0) {
                                                            energy[0] = energy[0].add(energyInfo);
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (outputPair != null) {
                                                    List<ItemStack> newStableOuts = new ArrayList<>();
                                                    List<BigInteger[]> newStableOutsAmount = new ArrayList<>();
                                                    for (int outputIndex = 0; outputIndex < outputPair.first.length; outputIndex++) {
                                                        newStableOuts.add(outputPair.first[outputIndex]);
                                                        newStableOutsAmount.add(
                                                                outputPair.second[outputIndex]
                                                        );
                                                    }
                                                    newStableOuts.addAll(List.of(calculatedStableOutputPair.first));
                                                    newStableOutsAmount.addAll(List.of(calculatedStableOutputPair.second));
                                                    calculatedStableOutputPair.first = newStableOuts.toArray(emptyItemStackArray);
                                                    calculatedStableOutputPair.second = newStableOutsAmount.toArray(emptyDividingsArray_BigInteger);
                                                }
                                                int compare = energyInfo.compareTo(BigInteger.ZERO);
                                                if (compare > 0) {
                                                    energy[1] = energy[1].add(energyInfo);
                                                    energyStable[1] = energyStable[1].add(energyInfo);
                                                } else if (compare < 0) {
                                                    energy[0] = energy[0].add(energyInfo);
                                                    energyStable[0] = energyStable[0].add(energyInfo);
                                                }
                                            }
                                            {
                                                SimplePair<ItemStack[], BigInteger[][]> cache = reformatItemStackArrayWithAmount_BigInteger(calculatedInputPair.first, calculatedInputPair.second);
                                                calculatedInputPair.first = cache.first;
                                                calculatedInputPair.second = cache.second;
                                            }
                                            {
                                                SimplePair<ItemStack[], BigInteger[][]> cache = reformatItemStackArrayWithAmount_BigInteger(calculatedOutputPair.first, calculatedOutputPair.second);
                                                calculatedOutputPair.first = cache.first;
                                                calculatedOutputPair.second = cache.second;
                                            }
                                            {
                                                SimplePair<ItemStack[], BigInteger[][]> cache = reformatItemStackArrayWithAmount_BigInteger(calculatedStableOutputPair.first, calculatedStableOutputPair.second);
                                                calculatedStableOutputPair.first = cache.first;
                                                calculatedStableOutputPair.second = cache.second;
                                            }
                                            {
                                                SimpleFour<ItemStack[], BigInteger[][], ItemStack[], BigInteger[][]> cache = reformatInputAndOutputs_BigInteger(
                                                        calculatedInputPair.first, calculatedInputPair.second,
                                                        calculatedOutputPair.first, calculatedOutputPair.second
                                                );
                                                calculatedInputPair.first = cache.a;
                                                calculatedInputPair.second = cache.b;
                                                calculatedOutputPair.first = cache.c;
                                                calculatedOutputPair.second = cache.d;
                                            }
                                            {
                                                SimplePair<ItemStack[], BigInteger[][]> cache = reformatItemStackArrayWithAmount_BigInteger(calculatedStableOutputPair.first, calculatedStableOutputPair.second);
                                                calculatedStableOutputPair.first = cache.first;
                                                calculatedStableOutputPair.second = cache.second;
                                            }
                                            {
                                                SimpleFour<ItemStack[], BigInteger[][], ItemStack[], BigInteger[][]> tryFitIn = reformatInputAndOutputs_BigInteger(calculatedInputPair.first, calculatedInputPair.second, calculatedStableOutputPair.first, calculatedStableOutputPair.second);
                                                if (tryFitIn.a.length == 0) {
                                                    energyStable[0] = energy[0];
                                                    energyStable[1] = energy[1];
                                                    List<ItemStack> cacheStable = new ArrayList<>(List.of(tryFitIn.c));
                                                    List<BigInteger[]> cacheStableAmount = new ArrayList<>(List.of(tryFitIn.d));
                                                    for (int k = 0; k < calculatedOutputPair.first.length; k++) {
                                                        cacheStable.add(calculatedOutputPair.first[k]);
                                                        cacheStableAmount.add(calculatedOutputPair.second[k]);
                                                    }
                                                    calculatedOutputPair.first = emptyItemStackArray;
                                                    calculatedOutputPair.second = emptyDividingsArray_BigInteger;
                                                    calculatedInputPair.first = emptyItemStackArray;
                                                    calculatedInputPair.second = emptyDividingsArray_BigInteger;
                                                    calculatedStableOutputPair.first = cacheStable.toArray(emptyItemStackArray);
                                                    calculatedStableOutputPair.second = cacheStableAmount.toArray(emptyDividingsArray_BigInteger);
                                                }
                                            }
                                        });
                                        {
                                            DividingsOperation.simplify(calculatedStableOutputPair.second);
                                        }
                                        if (calculatedInputPair.first.length == 0 && calculatedOutputPair.first.length != 0) {
                                            SimplePair<ItemStack[], BigInteger[][]> cache =
                                                    addItemStacks_BigInteger(
                                                            calculatedStableOutputPair.first,
                                                            calculatedStableOutputPair.second,
                                                            calculatedOutputPair.first,
                                                            calculatedOutputPair.second);
                                            calculatedStableOutputPair.first = cache.first;
                                            calculatedStableOutputPair.second = cache.second;
                                            calculatedOutputPair.first = emptyItemStackArray;
                                            calculatedOutputPair.second = emptyDividingsArray_BigInteger;
                                        }
                                    }

                                    {
                                        SimplePair<ItemStack[], BigInteger[][]> cache = reformatItemStackArrayWithAmount_BigInteger(calculatedInputPair.first, calculatedInputPair.second);
                                        calculatedInputPair.first = cache.first;
                                        calculatedInputPair.second = cache.second;
                                    }
                                    {
                                        SimplePair<ItemStack[], BigInteger[][]> cache = reformatItemStackArrayWithAmount_BigInteger(calculatedOutputPair.first, calculatedOutputPair.second);
                                        calculatedOutputPair.first = cache.first;
                                        calculatedOutputPair.second = cache.second;
                                    }
                                    {
                                        SimplePair<ItemStack[], BigInteger[][]> cache = reformatItemStackArrayWithAmount_BigInteger(calculatedStableOutputPair.first, calculatedStableOutputPair.second);
                                        calculatedStableOutputPair.first = cache.first;
                                        calculatedStableOutputPair.second = cache.second;
                                    }
                                    coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, energy[0]);
                                    coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, energy[1]);
                                    coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, energyStable[0]);
                                    coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, energyStable[1]);
                                    coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(calculatedStableOutputPair.first));
                                    coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(calculatedStableOutputPair.second));
                                    coreInfo.put(MINIMIZEFACTORY_INPUT_ARRAY, Serializations.ItemStackArrayToString(calculatedInputPair.first));
                                    coreInfo.put(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(calculatedInputPair.second));
                                    coreInfo.put(MINIMIZEFACTORY_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(calculatedOutputPair.first));
                                    coreInfo.put(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(calculatedOutputPair.second));
                                }
                                coreInfo.put(NETWORK_CONTROLLER_STATUS, NETWORK_CONTROLLER_LOCKED);
                                {
                                    ItemStack[] inputArr = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_INPUT_ARRAY));
                                    BigInteger[][] amount = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY));
                                    assert amount != null && amount.length == inputArr.length;
                                    List<String> inputLore = new ArrayList<>();
                                    for (int i = 0; i < inputArr.length; i++) {
                                        inputLore.add(NameUtil.findName(inputArr[i]) + Arrays.toString(amount[i]));
                                    }
                                    menu.replaceExistingItem(SHOW_INPUT, new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                            properties.getReplacedProperty("MachineNetworkCore_Input_Per_Slimefun_Tick"),
                                            inputLore
                                    ));
                                    menu.addMenuClickHandler(SHOW_INPUT, ChestMenuUtils.getEmptyClickHandler());
                                }
                                {
                                    ItemStack[] outputArr = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_OUTPUT_ARRAY));
                                    BigInteger[][] amount = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY));
                                    assert amount != null && amount.length == outputArr.length;
                                    List<String> outputLore = new ArrayList<>();
                                    for (int i = 0; i < outputArr.length; i++) {
                                        outputLore.add(NameUtil.findName(outputArr[i]) + Arrays.toString(amount[i]));
                                    }
                                    menu.replaceExistingItem(SHOW_OUTPUT, new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                            properties.getReplacedProperty("MachineNetworkCore_Output_Per_Slimefun_Tick"),
                                            outputLore
                                    ));
                                    menu.addMenuClickHandler(SHOW_OUTPUT, ChestMenuUtils.getEmptyClickHandler());
                                }
                                {
                                    ItemStack[] stableOutputArr = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY));
                                    BigInteger[][] amount = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY));
                                    assert amount != null && amount.length == stableOutputArr.length;
                                    List<String> stableOutputLore = new ArrayList<>();
                                    for (int i = 0; i < stableOutputArr.length; i++) {
                                        stableOutputLore.add(NameUtil.findName(stableOutputArr[i]) + Arrays.toString(amount[i]));
                                    }
                                    menu.replaceExistingItem(SHOW_STABLE_OUTPUT,
                                            new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                                    properties.getReplacedProperty("MachineNetworkCore_Stable_Output_Per_Slimefun_Tick"),
                                                    stableOutputLore
                                            ));
                                    menu.addMenuClickHandler(SHOW_STABLE_OUTPUT, ChestMenuUtils.getEmptyClickHandler());
                                }
                                {
                                    List<String> energyLore = new ArrayList<>();
                                    energyLore.add(properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Produce") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION));
                                    energyLore.add(properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Consume") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION));
                                    energyLore.add(properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Produce_Stable") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE));
                                    energyLore.add(properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Consume_Stable") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE));
                                    menu.replaceExistingItem(SHOW_POWER_SLOT,
                                            new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                                    properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick"),
                                                    energyLore
                                            ));
                                    menu.addMenuClickHandler(SHOW_STABLE_OUTPUT, ChestMenuUtils.getEmptyClickHandler());
                                }
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION).abs());
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION).abs());
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE).abs());
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE).abs());
                                BlockStorage.setBlockInfo(b, coreInfo.toString(), true);
                                refresh(BlockStorage.getInventory(b), b, NETWORK_CONTROLLER_LOCKED);
                            }
                        }.start();
                        //what the f**k
                    }
                }.start();
            }
            case NETWORK_CONTROLLER_LOCKED -> {
                menu.replaceExistingItem(BUTTON_LOCK_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_LOCK_MACHINE_NETWORK,ChestMenuUtils.getEmptyClickHandler());
                menu.replaceExistingItem(BUTTON_UNLOCK_MACHINE_NETWORK,ITEM_UNLOCK_MACHINE_NETWORK.clone());
                menu.addMenuClickHandler(BUTTON_UNLOCK_MACHINE_NETWORK,(p, slot, item, action) -> {
                    menu.replaceExistingItem(SHOW_INPUT,BORDER_ITEM.clone());
                    menu.replaceExistingItem(SHOW_OUTPUT,BORDER_ITEM.clone());
                    menu.replaceExistingItem(SHOW_STABLE_OUTPUT,BORDER_ITEM.clone());
                    menu.replaceExistingItem(SHOW_POWER_SLOT,BORDER_ITEM.clone());
                    menu.replaceExistingItem(BUTTON_UNLOCK_MACHINE_NETWORK,ITEM_UNLOCKING_MACHINE_NETWORK.clone());
                    menu.addMenuClickHandler(BUTTON_UNLOCK_MACHINE_NETWORK,ChestMenuUtils.getEmptyClickHandler());
                    JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                    coreInfo.put(NETWORK_CONTROLLER_STATUS,NETWORK_CONTROLLER_UNLOCKING);
                    BlockStorage.setBlockInfo(b,coreInfo.toString(),true);
                    refresh(menu,b,NETWORK_CONTROLLER_UNLOCKING);
                    return false;
                });
            }
            case NETWORK_CONTROLLER_UNLOCKING -> {
                {
                    JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                    {
                        JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                        for (int i = 0; i < containers.length(); i++) {
                            Location location = Serializations.StringToLocation(containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION));
                            unlockNode(location);
                            setCoreStatusIndex(NodeType.MACHINE_CONTAINER, location);
                        }
                    }
                    {
                        JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                        for (int i = 0; i < containers.length(); i++) {
                            Location location = Serializations.StringToLocation(containers.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION));
                            unlockNode(location);
                            setCoreStatusIndex(NodeType.STORAGE, location);
                        }
                    }
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));

                        BigInteger[] energy = new BigInteger[]{coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION), coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION)};
                        BigInteger[] powerFactor = DividingsOperation.min(DIVIDING1_BIGINTEGER, energy);
                        BigInteger[] energyStable = new BigInteger[]{coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE), coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE)};
                        BigInteger[] powerFactorStable = DividingsOperation.min(DIVIDING1_BIGINTEGER, energyStable);
                        long totalTime = System.currentTimeMillis() - coreInfo.getLong(NETWORK_CONTROLLER_LOCK_TIME);
                        BigInteger totalTicks = BigInteger.valueOf(totalTime / 500);//every second is 2 (slimefun) ticks
                        BigInteger stableTicks = totalTicks;
                        BigInteger remainingTicks = BigInteger.ZERO;

                        JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                        //itemStorage,StorageLocation,StorageAmount,ConsumptionOnce
                        int inputFind = 0;
                        //no output || no power->no calculate
                        if (storages.length() >= 1 && !powerFactor[0].equals(BigInteger.ZERO)) {
                            ItemStack[] inputs = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_INPUT_ARRAY));
                            ItemStack[] stableOutputs = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY));
                            BigInteger[][] stableOutputAmounts = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY));
                            List<SimpleTri<Location, BigInteger, BigInteger[]>> consumptionStorageList = new ArrayList<>();
                            List<SimplePair<ItemStack, BigInteger[]>> finalOutput = new ArrayList<>();
                            if (inputs.length != 0) {
                                //↓name in another way:validTick
                                BigInteger[] minimumMaterialFactor = new BigInteger[]{totalTicks, BigInteger.ONE};
                                minimumMaterialFactor = DividingsOperation.multiply(minimumMaterialFactor, powerFactor);
                                BigInteger[][] inputAmounts = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY));
                                assert inputAmounts != null && inputAmounts.length == inputs.length;
                                for (int inputIndex=0;inputIndex<inputs.length;inputIndex++) {
                                    ItemStack input = inputs[inputIndex];
                                    boolean breakFlag = true;
                                    for (int i = 0; i < storages.length(); i++) {
                                        Location location = Serializations.StringToLocation(storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION));
                                        ItemStack stored = MachineNetworkStorage.getStoredItem(location);
                                        BigInteger amount = MachineNetworkStorage.getStored(location);
                                        if (amount.equals(BigInteger.ZERO)) {
                                            continue;
                                        }
                                        if (ItemStackUtil.isItemStackSimilar(input, stored)) {
                                            inputFind += 1;
                                            breakFlag = false;
                                            minimumMaterialFactor = DividingsOperation.min(
                                                    new BigInteger[]{
                                                            amount.multiply(inputAmounts[inputIndex][1]).multiply(powerFactor[1]),
                                                            inputAmounts[inputIndex][0].multiply(powerFactor[0])
                                                    },//ticks that the material can hold:materialAmount / consumeSpeed;consumeSpeed = powerFactor * basicConsumeSpeed
                                                    minimumMaterialFactor);
                                            consumptionStorageList.add(new SimpleTri<>(location, amount, inputAmounts[inputIndex]));
                                            break;
                                        }
                                    }
                                    if (breakFlag || inputFind == inputs.length) {
                                        break;
                                    }
                                }

                                if (inputFind == inputs.length) {
                                    for (SimpleTri<Location, BigInteger, BigInteger[]> storage : consumptionStorageList) {
                                        MachineNetworkStorage.setStored(
                                                storage.first,
                                                storage.second.subtract(
                                                        storage.third[0].multiply(minimumMaterialFactor[0])
                                                                .divide(minimumMaterialFactor[1]).divide(storage.third[1])
                                                )
                                        );
                                        MachineNetworkStorage.updateMenu(storage.first.getBlock(),BlockStorage.getInventory(storage.first),true);
                                    }
                                    ItemStack[] outputs = Serializations.StringToItemStackArray(coreInfo.getString(MINIMIZEFACTORY_OUTPUT_ARRAY));
                                    BigInteger[][] outputAmounts = Serializations.StringToBigIntegerDividings(coreInfo.getString(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY));
                                    assert outputAmounts != null;
                                    for (int j = 0; j < outputs.length; j++) {
                                        finalOutput.add(new SimplePair<>(outputs[j],
                                                DividingsOperation.multiply(
                                                        DividingsOperation.multiply(outputAmounts[j], powerFactor),
                                                        minimumMaterialFactor)
                                        ));
                                    }
                                    stableTicks = stableTicks.subtract(minimumMaterialFactor[0].divide(minimumMaterialFactor[1]));
                                    remainingTicks = totalTicks.subtract(stableTicks);
                                }
                            }
                            assert stableOutputAmounts != null && stableOutputAmounts.length == stableOutputs.length;
                            for (int i = 0; i < stableOutputs.length; i++) {
                                BigInteger[] dividingStable = DividingsOperation.multiply(stableOutputAmounts[i], powerFactorStable);
                                dividingStable[0] = dividingStable[0].multiply(stableTicks);
                                BigInteger[] dividingRemaining = DividingsOperation.multiply(stableOutputAmounts[i], powerFactor);
                                dividingRemaining[0] = dividingRemaining[0].multiply(remainingTicks);
                                finalOutput.add(new SimplePair<>(
                                        stableOutputs[i],
                                        DividingsOperation.sumOfDividings(dividingStable, dividingRemaining)
                                ));
                            }
                            //output item,those can't be output will be destroyed
                            for (SimplePair<ItemStack, BigInteger[]> output : finalOutput) {
                                for (int i = 0; i < storages.length(); i++) {
                                    Location location = Serializations.StringToLocation(storages.getJSONObject(i).getString(MINIMIZEFACTORY_NODE_LOCATION));
                                    ItemStack stored = MachineNetworkStorage.getStoredItem(location);
                                    BigInteger amount = MachineNetworkStorage.getStored(location);
                                    if (stored.getType().equals(Material.BARRIER)) {
                                        MachineNetworkStorage.registerItem(location, output.first);
                                        MachineNetworkStorage.setStored(location, output.second[0].divide(output.second[1]));
                                        MachineNetworkStorage.updateMenu(location.getBlock(),BlockStorage.getInventory(location),true);
                                        break;
                                    }
                                    if (ItemStackUtil.isItemStackSimilar(stored, output.first)) {
                                        MachineNetworkStorage.setStored(location, amount.add(output.second[0].divide(output.second[1])));
                                        MachineNetworkStorage.updateMenu(location.getBlock(),BlockStorage.getInventory(location),true);
                                        break;
                                    }
                                }
                            }
                        }

                        coreInfo.put(MINIMIZEFACTORY_INPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                        coreInfo.put(MINIMIZEFACTORY_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                        coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY, Serializations.ItemStackArrayToString(new ItemStack[0]));
                        coreInfo.put(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                        coreInfo.put(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                        coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY, Serializations.BigIntegerDividingsToString(emptyDividingsArray_BigInteger));
                        coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, BigInteger.ZERO.toString());
                        coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, BigInteger.ZERO.toString());
                        coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, BigInteger.ZERO.toString());
                        coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, BigInteger.ZERO.toString());
                        coreInfo.put(NETWORK_CONTROLLER_STATUS, NETWORK_CONTROLLER_ONLINE);
                        BlockStorage.setBlockInfo(b, coreInfo.toString(), false);
                        unlockNode(b.getLocation());
                        refresh(menu, b, NETWORK_CONTROLLER_ONLINE);
                    }
                }.start();
            }
            case NETWORK_CONTROLLER_DISABLING -> new Thread(){
                @Override
                public void run() {
                    super.run();
                    lockNode(b.getLocation());
                    JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                    JSONArray storages = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                    JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                    JSONArray bridges = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_BRIDGES));
                    for (int i = 0; i < storages.length(); i++) {
                        JSONObject nodeJSON = storages.getJSONObject(i);
                        Location nodeLocation = Serializations.StringToLocation(nodeJSON.getString(MINIMIZEFACTORY_NODE_LOCATION));
                        unregisterOneNode(nodeLocation);
                    }
                    coreInfo.put(MINIMIZEFACTORY_STORAGES,emptyJSONArray.toString());
                    for (int i = 0; i < containers.length(); i++) {
                        JSONObject nodeJSON = containers.getJSONObject(i);
                        Location nodeLocation = Serializations.StringToLocation(nodeJSON.getString(MINIMIZEFACTORY_NODE_LOCATION));
                        unregisterOneNode(nodeLocation);
                    }
                    coreInfo.put(MINIMIZEFACTORY_CONTAINERS,emptyJSONArray.toString());
                    for (int i = 0; i < bridges.length(); i++) {
                        JSONObject nodeJSON = bridges.getJSONObject(i);
                        Location nodeLocation = Serializations.StringToLocation(nodeJSON.getString(MINIMIZEFACTORY_NODE_LOCATION));
                        unregisterOneNode(nodeLocation);
                    }
                    coreInfo.put(MINIMIZEFACTORY_BRIDGES,emptyJSONArray.toString());
                    BlockStorage.setBlockInfo(b,coreInfo.toString(),true);
                    unlockNode(b.getLocation());
                    refresh(menu, b, NETWORK_CONTROLLER_OFFLINE);
                }
            }.start();
        }
    }
}
