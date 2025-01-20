package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.calculation.ItemStackMapForOutputCalculation;
import io.github.ignorelicensescn.minimizeFactory.utils.records.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BlockGeometry;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.calculation.ContainerCalculationResult;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.calculation.ItemStackMapForContainerCalculation;
import io.github.ignorelicensescn.minimizeFactory.utils.records.ItemStackAsKey;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONArray;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigInteger;
import java.util.*;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.BukkitSerializer.LOCATION_SERIALIZER;
import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeKeys.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.Serializations.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.Serializer.BIG_RATIONAL_ARRAY_SERIALIZER;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.UnifiedSerializer.ITEM_STACK_ARRAY_SERIALIZER;

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
                initializeCoreInfo(coreInfo);

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
                refresh(menu,b, coreInfo.get(NETWORK_CONTROLLER_STATUS).toString());
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

    private static void initializeCoreInfo(JSONObject coreInfo) {
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
            coreInfo.put(MINIMIZEFACTORY_INPUT_ARRAY, EMPTY_ITEM_STACK_ARRAY_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_OUTPUT_ARRAY)){
            coreInfo.put(MINIMIZEFACTORY_OUTPUT_ARRAY, EMPTY_ITEM_STACK_ARRAY_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY)){
            coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY, EMPTY_ITEM_STACK_ARRAY_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY)){
            coreInfo.put(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY, EMPTY_BIG_RATIONAL_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY)){
            coreInfo.put(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY, EMPTY_BIG_RATIONAL_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY)){
            coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY, EMPTY_BIG_RATIONAL_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_PRODUCTION)){
            coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, BIGINTEGER_ZERO_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_CONSUMPTION)){
            coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, BIGINTEGER_ZERO_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE)){
            coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, BIGINTEGER_ZERO_BASE64);
        }
        if (!coreInfo.has(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE)){
            coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, BIGINTEGER_ZERO_BASE64);
        }
    }

    @Override
    public void preRegister() {
        addItemHandler(
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
                    registerNodes(b.getLocation(),b.getLocation());
                    refresh(menu,b,NETWORK_CONTROLLER_STARTING);
                    return false;
                });
            }
            case NETWORK_CONTROLLER_STARTING -> {
                menu.replaceExistingItem(BUTTON_ESTABLISH_MACHINE_NETWORK, BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_ESTABLISH_MACHINE_NETWORK, ChestMenuUtils.getEmptyClickHandler());
            }
            case NETWORK_CONTROLLER_ONLINE -> {
                menu.replaceExistingItem(BUTTON_ESTABLISH_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_ESTABLISH_MACHINE_NETWORK,ChestMenuUtils.getEmptyClickHandler());
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
                menu.addMenuClickHandler(BUTTON_LOCK_MACHINE_NETWORK, ChestMenuUtils.getEmptyClickHandler());
                menu.replaceExistingItem(BUTTON_TERMINATE_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_TERMINATE_MACHINE_NETWORK, ChestMenuUtils.getEmptyClickHandler());
                lockNode(b.getLocation());
                new Thread(() -> {
                    //lockNodes
                    {
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));
                        {
                            JSONArray containers = new JSONArray(coreInfo.get(MINIMIZEFACTORY_CONTAINERS).toString());
                            for (int i = 0; i < containers.length(); i+=1) {
                                Location location = LOCATION_SERIALIZER.StringToSerializable(new JSONObject(containers.get(i).toString()).get(MINIMIZEFACTORY_NODE_LOCATION).toString());
                                lockNode(location);
                                setCoreStatusForNode(NodeType.MACHINE_CONTAINER, location);
                            }
                        }
                        {
                            JSONArray storages = new JSONArray(coreInfo.get(MINIMIZEFACTORY_STORAGES).toString());
                            for (int i = 0; i < storages.length(); i+=1) {
                                Location location = LOCATION_SERIALIZER.StringToSerializable(new JSONObject(storages.get(i).toString()).get(MINIMIZEFACTORY_NODE_LOCATION).toString());
                                lockNode(location);
                                setCoreStatusForNode(NodeType.STORAGE, location);
                            }
                        }
                    }
                    //calculate inputs and outputs
                    new Thread(() -> {
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));

                        clearCoreInputsAndOutputsInfo(coreInfo);

                        ContainerCalculationResult result = ContainerCalculationResult.EMPTY;

                        JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_CONTAINERS));
                        List<Location> containerLocations = new ArrayList<>(containers.length());

                        for (int i = 0; i < containers.length(); i+=1) {
                            JSONObject jsonObject = new JSONObject(containers.get(i).toString());
                            Location containerLocation = LOCATION_SERIALIZER.StringToSerializable(jsonObject.get(MINIMIZEFACTORY_NODE_LOCATION).toString());
                            containerLocations.add(containerLocation);
                        }
                        containerLocations.sort(BlockGeometry.getManhattanDistanceBasedComparatorWithLocationBase(b.getLocation()));
                        for (Location containerLocation:containerLocations){
                            if (containerLocation == null){
                                new Exception("location null").printStackTrace();
                                continue;
                            }
                            BiomeAndEnvironment bioAndEnv = BiomeAndEnvironment.fromLocation(containerLocation);

                            BlockMenu locationMenu = BlockStorage.getInventory(containerLocation);
                            if (locationMenu == null) {
                                new Exception(containerLocation + " : " + properties.getReplacedProperty("MachineNetworkCore_Exception_Null_Container")).printStackTrace();
                                continue;
                            }
                            //stabilizer inputs
                            List<SimplePair<SerializedMachine_MachineRecipe,Integer>> serializedInContainer = new ArrayList<>();
                            for (int slotIndex:MachineNetworkContainer.STABILIZER_INPUT_SLOTS){
                                ItemStack itemStack = locationMenu.getItemInSlot(slotIndex);
                                if (itemStack == null) {
                                    continue;
                                }
                                int machineAmount = itemStack.getAmount();
                                Optional<SerializedMachine_MachineRecipe> optional = SerializedMachine_MachineRecipe.retrieveFromItemStack(itemStack);
                                optional.ifPresent(
                                        serializedMachineMachineRecipe ->
                                                serializedInContainer.add(new SimplePair<>(serializedMachineMachineRecipe, machineAmount))
                                );
                            }
                            result = result.combineWith(ContainerCalculationResult.fromSerializedRecipes(bioAndEnv,serializedInContainer));
                        }
                        coreInfo.put(
                                NETWORK_CONTROLLER_STATUS,
                                NETWORK_CONTROLLER_LOCKED
                        );
                        {

                            List<String> inputLore = new ArrayList<>();
                            List<String> outputLore = new ArrayList<>();
                            List<String> stableOutputLore = new ArrayList<>();

                            ItemStackMapForContainerCalculation map = result.inputs();
                            ItemStack[] inputArr = new ItemStack[map.size()];
                            BigRational[] inputAmount = new BigRational[map.size()];
                            int counter = 0;
                            for (Map.Entry<ItemStackAsKey,BigRational> inputEntry:map.entrySet()){
                                inputArr[counter] = inputEntry.getKey().getTemplate();
                                inputAmount[counter]=inputEntry.getValue();
                                inputLore.add(NameUtil.findName(inputArr[counter]) + inputAmount[counter]);
                                counter += 1;
                            }

                            map = result.outputs();
                            ItemStack[] outputArr = new ItemStack[map.size()];
                            BigRational[] outputAmount = new BigRational[map.size()];
                            counter = 0;
                            for (Map.Entry<ItemStackAsKey,BigRational> outputEntry:map.entrySet()){
                                outputArr[counter] = outputEntry.getKey().getTemplate();
                                outputAmount[counter]=outputEntry.getValue();
                                outputLore.add(NameUtil.findName(outputArr[counter]) + outputAmount[counter]);
                                counter += 1;
                            }

                            map = result.stableOutputs();
                            ItemStack[] stableOutputArr = new ItemStack[map.size()];
                            BigRational[] stableOutputAmount = new BigRational[map.size()];
                            counter = 0;
                            for (Map.Entry<ItemStackAsKey,BigRational> outputEntry:map.entrySet()){
                                stableOutputArr[counter] = outputEntry.getKey().getTemplate();
                                stableOutputAmount[counter]=outputEntry.getValue();
                                stableOutputLore.add(NameUtil.findName(stableOutputArr[counter]) + stableOutputAmount[counter]);
                                counter += 1;
                            }
                            //serialize
                            coreInfo.put(MINIMIZEFACTORY_INPUT_ARRAY, ITEM_STACK_ARRAY_SERIALIZER.SerializableToString(inputArr));
                            coreInfo.put(MINIMIZEFACTORY_OUTPUT_ARRAY, ITEM_STACK_ARRAY_SERIALIZER.SerializableToString(outputArr));
                            coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY, ITEM_STACK_ARRAY_SERIALIZER.SerializableToString(stableOutputArr));
                            coreInfo.put(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY, BIG_RATIONAL_ARRAY_SERIALIZER.SerializableToString(inputAmount));
                            coreInfo.put(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY, BIG_RATIONAL_ARRAY_SERIALIZER.SerializableToString(outputAmount));
                            coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY, BIG_RATIONAL_ARRAY_SERIALIZER.SerializableToString(stableOutputAmount));
                            if (result.energyConsumption().compareTo(BigInteger.ZERO) <= 0){
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, result.energyConsumption().multiply(BigInteger.valueOf(-1)));
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, BigInteger.ZERO);
                            }else {
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, BigInteger.ZERO);
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, result.energyConsumption());
                            }
                            if (result.energyConsumptionStable().compareTo(BigInteger.ZERO) <= 0){
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, result.energyConsumptionStable().multiply(BigInteger.valueOf(-1)));
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, BigInteger.ZERO);
                            }else {
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, BigInteger.ZERO);
                                coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, result.energyConsumptionStable());
                            }

                            menu.replaceExistingItem(SHOW_INPUT, new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                    properties.getReplacedProperty("MachineNetworkCore_Input_Per_Slimefun_Tick"),
                                    inputLore
                            ));
                            menu.addMenuClickHandler(SHOW_INPUT, ChestMenuUtils.getEmptyClickHandler());
                            //outputs
                            menu.replaceExistingItem(SHOW_OUTPUT, new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                    properties.getReplacedProperty("MachineNetworkCore_Output_Per_Slimefun_Tick"),
                                    outputLore
                            ));
                            menu.addMenuClickHandler(SHOW_OUTPUT, ChestMenuUtils.getEmptyClickHandler());
                            menu.replaceExistingItem(SHOW_STABLE_OUTPUT,
                                    new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                            properties.getReplacedProperty("MachineNetworkCore_Stable_Output_Per_Slimefun_Tick"),
                                            stableOutputLore
                                    ));
                            menu.addMenuClickHandler(SHOW_STABLE_OUTPUT, ChestMenuUtils.getEmptyClickHandler());

                            String[] energyLore = new String[]{
                                    properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Produce") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION),
                                    properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Consume") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION),
                                    properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Produce_Stable") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE),
                                    properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Consume_Stable") + coreInfo.getBigInteger(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE),
                            };
                            menu.replaceExistingItem(SHOW_POWER_SLOT,
                                    new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                            properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick"),
                                            energyLore
                                    ));
                            menu.addMenuClickHandler(SHOW_STABLE_OUTPUT, ChestMenuUtils.getEmptyClickHandler());
                        }
                        BlockStorage.setBlockInfo(b, coreInfo.toString(), true);
                        refresh(BlockStorage.getInventory(b), b, NETWORK_CONTROLLER_LOCKED);
                    }).start();
                    //what the f**k
                }).start();
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
                        JSONArray containers = new JSONArray(coreInfo.get(MINIMIZEFACTORY_CONTAINERS).toString());
                        for (int i = 0; i < containers.length(); i+=1) {
                            Location location = LOCATION_SERIALIZER.StringToSerializable(new JSONObject(containers.get(i).toString()).getString(MINIMIZEFACTORY_NODE_LOCATION));
                            unlockNode(location);
                            setCoreStatusForNode(NodeType.MACHINE_CONTAINER, location);
                        }
                    }
                    {
                        JSONArray containers = new JSONArray(coreInfo.getString(MINIMIZEFACTORY_STORAGES));
                        for (int i = 0; i < containers.length(); i+=1) {
                            Location location = LOCATION_SERIALIZER.StringToSerializable(new JSONObject(containers.get(i).toString()).getString(MINIMIZEFACTORY_NODE_LOCATION));
                            unlockNode(location);
                            setCoreStatusForNode(NodeType.STORAGE, location);
                        }
                    }
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        JSONObject coreInfo = new JSONObject(BlockStorage.getBlockInfoAsJson(b));

                        List<Location> emptyStorageLocations = new LinkedList<>();
                        ItemStackMapForOutputCalculation outputMap = new ItemStackMapForOutputCalculation();
                        //TODO:Rewrite output calculation
                        JSONArray storageJSONArray = new JSONArray(coreInfo.get(MINIMIZEFACTORY_STORAGES).toString());
                        for(int i=0;i<storageJSONArray.length();i+=1){
                            JSONObject storageLocationJSON = new JSONObject(storageJSONArray.get(i).toString());
                            Location storageLocation = LOCATION_SERIALIZER.StringToSerializable(storageLocationJSON.get(MINIMIZEFACTORY_NODE_LOCATION).toString());
                            if (!MachineNetworkStorage.isValidStorage(storageLocation)){
                                continue;
                            }
                            ItemStack storeItem = MachineNetworkStorage.getStoredItem(storageLocation);
                            if (storeItem == null){
                                emptyStorageLocations.add(storageLocation);
                            }else {
                                outputMap.put(new ItemStackAsKey(storeItem),storageLocation);
                            }
                        }

                        BigRational[] stablizedOutputAmountsPerTick = BIG_RATIONAL_ARRAY_SERIALIZER.StringToSerializable(coreInfo.get(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY).toString());
                        ItemStack[] stabilizedOutputs = ITEM_STACK_ARRAY_SERIALIZER.StringToSerializable(coreInfo.get(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY).toString());
                        long lockTime = coreInfo.getLong(NETWORK_CONTROLLER_LOCK_TIME);
                        long timePast = Math.max(0,System.currentTimeMillis()-lockTime);
                        long tickPast = timePast/500;//slimefun tick
                        for (int i=0;i<stablizedOutputAmountsPerTick.length;i+=1){
                            stablizedOutputAmountsPerTick[i] = stablizedOutputAmountsPerTick[i].multiply(tickPast);
                            BigRational outputAmount = stablizedOutputAmountsPerTick[i];
                            ItemStackAsKey itemToOutput = new ItemStackAsKey(stabilizedOutputs[i]);
                            if (!outputMap.containsKey(itemToOutput) && !emptyStorageLocations.isEmpty()){
                                Location newEmptyContainer = emptyStorageLocations.remove(0);
                                outputMap.put(itemToOutput,newEmptyContainer);
                            }else if (emptyStorageLocations.isEmpty()){
                                continue;
                            }
                            Location containerToOutput = outputMap.get(itemToOutput);
                            MachineNetworkStorage.setStoredStackNoThrow(containerToOutput,itemToOutput.getTemplate());
                            MachineNetworkStorage.addStored(containerToOutput,outputAmount);
                        }
                        clearCoreInputsAndOutputsInfo(coreInfo);
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
                    unregisterNodes(b.getLocation());
                    unlockNode(b.getLocation());
                    refresh(menu, b, NETWORK_CONTROLLER_OFFLINE);
                }
            }.start();
        }
    }

    /**
     * clear inputs and outputs info,or every lock operation increases production.
     * @param coreInfo MachineNetworkCore info(json) to clear in and outs.
     */
    private static void clearCoreInputsAndOutputsInfo(JSONObject coreInfo) {
        coreInfo.put(MINIMIZEFACTORY_INPUT_ARRAY, EMPTY_ITEM_STACK_ARRAY_BASE64);
        coreInfo.put(MINIMIZEFACTORY_OUTPUT_ARRAY, EMPTY_ITEM_STACK_ARRAY_BASE64);
        coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_ARRAY, EMPTY_ITEM_STACK_ARRAY_BASE64);
        coreInfo.put(MINIMIZEFACTORY_INPUT_AMOUNT_ARRAY, EMPTY_BIG_RATIONAL_BASE64);
        coreInfo.put(MINIMIZEFACTORY_OUTPUT_AMOUNT_ARRAY, EMPTY_BIG_RATIONAL_BASE64);
        coreInfo.put(MINIMIZEFACTORY_STABLE_OUTPUT_AMOUNT_ARRAY, EMPTY_BIG_RATIONAL_BASE64);
        coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION, BIGINTEGER_ZERO_BASE64);
        coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION, BIGINTEGER_ZERO_BASE64);
        coreInfo.put(MINIMIZEFACTORY_ENERGY_PRODUCTION_STABLE, BIGINTEGER_ZERO_BASE64);
        coreInfo.put(MINIMIZEFACTORY_ENERGY_CONSUMPTION_STABLE, BIGINTEGER_ZERO_BASE64);
    }


}
