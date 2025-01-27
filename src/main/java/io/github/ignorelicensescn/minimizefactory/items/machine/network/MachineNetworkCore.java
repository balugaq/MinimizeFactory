package io.github.ignorelicensescn.minimizefactory.items.machine.network;

import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.CoreInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.implementations.StorageInfoSerializer;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedColumnAdder;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations.DataRemover;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.CoreInfo;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.StorageInfo;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.calculation.ItemStackMapForOutputCalculation;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.namemateriallore.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BlockGeometry;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.calculation.ContainerCalculationResult;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.calculation.ItemStackMapForContainerCalculation;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStackAsKey;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
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

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_BIG_RATIONAL_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_ITEM_STACK_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeKeys.*;
import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_INT_ARRAY;

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

    private static final BlockBreakHandler CORE_BREAK_HANDLER = new BlockBreakHandler(false,false) {
        @Override
        @ParametersAreNonnullByDefault
        public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
            try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                SerializeFriendlyBlockLocation coreLocationKey = SerializeFriendlyBlockLocation.fromLocation(e.getBlock().getLocation());
                CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                if (!Objects.equals(coreInfo.networkStatus, NETWORK_CONTROLLER_OFFLINE)){
                    e.getPlayer().sendMessage(properties.getReplacedProperty("MachineNetworkCore_Cannot_Break"));
                    e.setCancelled(true);
                }else {
                    DataRemover.INSTANCE.remove(coreLocationKey);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    };

    public MachineNetworkCore(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.CONTROLLER);
        new BlockMenuPreset(getId(),getItemName()){

            @Override
            public void init() {
                for (int i:FULL_BORDER){
                    addItem(i,BORDER_ITEM.clone(),(ChestMenuUtils.getEmptyClickHandler()));
                }
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                super.newInstance(menu, b);
                SerializeFriendlyBlockLocation coreLocationKey = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());

                if (!LocationBasedColumnAdder.INSTANCE.checkExistence(coreLocationKey)){
                    initNode(coreLocationKey,NodeType.CONTROLLER);
                }
                try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                    CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                    refresh(menu,b, coreInfo.networkStatus);
                }catch (Exception e){
                    e.printStackTrace();
                }
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

    @Override
    public void preRegister() {
        addItemHandler(CORE_BREAK_HANDLER);
    }
    public static void refresh(BlockMenu menu,Block b,String status){
        SerializeFriendlyBlockLocation coreLocationKey = SerializeFriendlyBlockLocation.fromLocation(b.getLocation());

        if (!Objects.equals(status,NETWORK_CONTROLLER_LOCKING) && !Objects.equals(status,NETWORK_CONTROLLER_UNLOCKING)) {//prevent deadlock
            try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()) {
                CoreInfo coreInfoForSettingStatus = coreInfoSerializer.getOrDefault(coreLocationKey);
                coreInfoForSettingStatus.networkStatus = status;
                coreInfoSerializer.saveToLocationNoThrow(coreInfoForSettingStatus, coreLocationKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        switch (status){
            case NETWORK_CONTROLLER_OFFLINE -> {
                menu.replaceExistingItem(BUTTON_LOCK_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_LOCK_MACHINE_NETWORK,ChestMenuUtils.getEmptyClickHandler());
                menu.replaceExistingItem(BUTTON_TERMINATE_MACHINE_NETWORK,BORDER_ITEM.clone());
                menu.addMenuClickHandler(BUTTON_TERMINATE_MACHINE_NETWORK,ChestMenuUtils.getEmptyClickHandler());
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
                    try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                        CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                        coreInfo.networkStatus = NETWORK_CONTROLLER_STARTING;
                        coreInfoSerializer.saveToLocationNoThrow(coreInfo, coreLocationKey);
                        registerNodes(b.getLocation(), b.getLocation());
                        refresh(menu, b, NETWORK_CONTROLLER_STARTING);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
                    try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                        CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                        coreInfo.networkStatus = NETWORK_CONTROLLER_LOCKING;
                        coreInfo.lockTime = System.currentTimeMillis();
                        coreInfoSerializer.saveToLocationNoThrow(coreInfo, coreLocationKey);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
                    menu.addMenuClickHandler(BUTTON_TERMINATE_MACHINE_NETWORK, ChestMenuUtils.getEmptyClickHandler());


                    try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                        CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                        coreInfo.networkStatus = NETWORK_CONTROLLER_DISABLING;
                        coreInfoSerializer.saveToLocationNoThrow(coreInfo, coreLocationKey);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

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
                    try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                        CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                        {
                            for (SerializeFriendlyBlockLocation nodeLocationKey:coreInfo.containerLocations){
                                Location nodeLocation = nodeLocationKey.toLocation();
                                lockNode(nodeLocation);
                                setCoreStatusForNode(NodeType.MACHINE_CONTAINER, nodeLocation);
                            }
                        }
                        {
                            for (SerializeFriendlyBlockLocation nodeLocationKey:coreInfo.storageLocations){
                                Location nodeLocation = nodeLocationKey.toLocation();
                                lockNode(nodeLocation);
                                setCoreStatusForNode(NodeType.STORAGE, nodeLocation);
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    //calculate inputs and outputs
                    new Thread(() ->
                    {
                        try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance())
                        {
                            CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);

                            coreInfo = clearCoreInputsAndOutputsInfo(coreInfo);

                            ContainerCalculationResult result = ContainerCalculationResult.EMPTY;

                            SerializeFriendlyBlockLocation[] containerLocations = coreInfo.containerLocations;

                            Arrays.sort(containerLocations, BlockGeometry.getManhattanDistanceBasedComparatorWithLocationBase(coreLocationKey));
                            for (SerializeFriendlyBlockLocation containerLocationKey : containerLocations) {
                                if (containerLocationKey == null) {
                                    new Exception("location null").printStackTrace();
                                    continue;
                                }
                                Location containerLocation = containerLocationKey.toLocation();
                                BiomeAndEnvironment bioAndEnv = BiomeAndEnvironment.fromLocation(containerLocation);

                                BlockMenu locationMenu = BlockStorage.getInventory(containerLocation);
                                if (locationMenu == null) {
                                    new Exception(containerLocation + " : " + properties.getReplacedProperty("MachineNetworkCore_Exception_Null_Container")).printStackTrace();
                                    continue;
                                }
                                //stabilizer inputs
                                List<SimplePair<SerializedMachine_MachineRecipe, Integer>> serializedInContainer = new ArrayList<>();
                                for (int slotIndex : MachineNetworkContainer.STABILIZER_INPUT_SLOTS) {
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
                                result = result.combineWith(ContainerCalculationResult.fromSerializedRecipes(bioAndEnv, serializedInContainer));
                            }

                            result = result.simplify();
                            coreInfo.networkStatus = NETWORK_CONTROLLER_LOCKED;

                            {

                                List<String> inputLore = new ArrayList<>();
                                List<String> outputLore = new ArrayList<>();
                                List<String> stableOutputLore = new ArrayList<>();

                                ItemStackMapForContainerCalculation map = result.inputs();
                                ItemStack[] inputArr = new ItemStack[map.size()];
                                BigRational[] inputAmount = new BigRational[map.size()];
                                int counter = 0;
                                for (Map.Entry<ItemStackAsKey, BigRational> inputEntry : map.entrySet()) {
                                    inputArr[counter] = inputEntry.getKey().getTemplate();
                                    inputAmount[counter] = inputEntry.getValue();
                                    inputLore.add(NameUtil.findName(inputArr[counter]) + inputAmount[counter]);
                                    counter += 1;
                                }

                                map = result.outputs();
                                ItemStack[] outputArr = new ItemStack[map.size()];
                                BigRational[] outputAmount = new BigRational[map.size()];
                                counter = 0;
                                for (Map.Entry<ItemStackAsKey, BigRational> outputEntry : map.entrySet()) {
                                    outputArr[counter] = outputEntry.getKey().getTemplate();
                                    outputAmount[counter] = outputEntry.getValue();
                                    outputLore.add(NameUtil.findName(outputArr[counter]) + outputAmount[counter]);
                                    counter += 1;
                                }

                                map = result.stableOutputs();
                                ItemStack[] stableOutputArr = new ItemStack[map.size()];
                                BigRational[] stableOutputAmount = new BigRational[map.size()];
                                counter = 0;
                                for (Map.Entry<ItemStackAsKey, BigRational> outputEntry : map.entrySet()) {
                                    stableOutputArr[counter] = outputEntry.getKey().getTemplate();
                                    stableOutputAmount[counter] = outputEntry.getValue();
                                    stableOutputLore.add(NameUtil.findName(stableOutputArr[counter]) + " | " + stableOutputAmount[counter]);
                                    counter += 1;
                                }
                                //serialize
                                coreInfo.inputs = inputArr;
                                coreInfo.inputAmount = inputAmount;
                                coreInfo.outputs = outputArr;
                                coreInfo.outputAmount = outputAmount;
                                coreInfo.stableOutputs = stableOutputArr;
                                coreInfo.stableOutputAmount = stableOutputAmount;
                                if (result.energyConsumption().compareTo(BigInteger.ZERO) <= 0) {
                                    coreInfo.energyProduction = result.energyConsumption().multiply(BigInteger.valueOf(-1));
                                    coreInfo.energyConsumption = BigInteger.ZERO;
                                } else {
                                    coreInfo.energyProduction = BigInteger.ZERO;
                                    coreInfo.energyConsumption = result.energyConsumption();
                                }
                                if (result.energyConsumptionStable().compareTo(BigInteger.ZERO) <= 0) {
                                    coreInfo.energyProductionStable = result.energyConsumptionStable().multiply(BigInteger.valueOf(-1));
                                    coreInfo.energyConsumptionStable = BigInteger.ZERO;
                                } else {
                                    coreInfo.energyProductionStable = BigInteger.ZERO;
                                    coreInfo.energyConsumptionStable = result.energyConsumptionStable();
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
                                        properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Produce") + coreInfo.energyProduction,
                                        properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Consume") + coreInfo.energyConsumption,
                                        properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Produce_Stable") + coreInfo.energyProductionStable,
                                        properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick_Consume_Stable") + coreInfo.energyConsumptionStable,
                                };
                                menu.replaceExistingItem(SHOW_POWER_SLOT,
                                        new CustomItemStack(Material.ORANGE_STAINED_GLASS_PANE,
                                                properties.getReplacedProperty("MachineNetworkCore_Power_Per_Tick"),
                                                energyLore
                                        ));
                                menu.addMenuClickHandler(SHOW_STABLE_OUTPUT, ChestMenuUtils.getEmptyClickHandler());
                            }
                            coreInfo.networkStatus = NETWORK_CONTROLLER_LOCKED;
                            coreInfoSerializer.saveToLocationNoThrow(coreInfo, coreLocationKey);
                            refresh(BlockStorage.getInventory(b), b, NETWORK_CONTROLLER_LOCKED);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
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
                    refresh(menu,b,NETWORK_CONTROLLER_UNLOCKING);
                    return false;
                });
            }
            case NETWORK_CONTROLLER_UNLOCKING -> new Thread(()->{
                try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                    CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                    {
                        for (SerializeFriendlyBlockLocation nodeLocationKey : coreInfo.containerLocations) {
                            Location nodeLocation = nodeLocationKey.toLocation();
                            unlockNode(nodeLocation);
                            setCoreStatusForNode(NodeType.MACHINE_CONTAINER, nodeLocation);
                        }
                    }
                    {
                        for (SerializeFriendlyBlockLocation nodeLocationKey : coreInfo.storageLocations) {
                            Location nodeLocation = nodeLocationKey.toLocation();
                            unlockNode(nodeLocation);
                            setCoreStatusForNode(NodeType.STORAGE, nodeLocation);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                new Thread(() -> {
                    try (
                            CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance();
                            StorageInfoSerializer storageInfoSerializer = StorageInfoSerializer.getInstance()
                    ){
                        CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);

                        List<SimplePair<SerializeFriendlyBlockLocation,StorageInfo>> emptyStorageLocations = new LinkedList<>();
                        ItemStackMapForOutputCalculation outputMap = new ItemStackMapForOutputCalculation();
                        for (SerializeFriendlyBlockLocation storageLocationKey : coreInfo.storageLocations) {
                            try {
                                StorageInfo storageInfo = storageInfoSerializer.getFromLocation(storageLocationKey);
                                Location storageLocation = storageLocationKey.toLocation();
                                if (!MachineNetworkStorage.isValidStorage(storageLocation)) {
                                    continue;
                                }
                                ItemStack storeItem = MachineNetworkStorage.getStoredItem(storageInfo);
                                if (storeItem == null) {
                                    emptyStorageLocations.add(new SimplePair<>(storageLocationKey, storageInfo));
                                } else {
                                    outputMap.put(new ItemStackAsKey(storeItem), new SimplePair<>(storageLocationKey, storageInfo));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                continue;
                            }
                        }

                        BigRational[] stablizedOutputAmountsPerTick = coreInfo.stableOutputAmount;
                        ItemStack[] stabilizedOutputs = coreInfo.stableOutputs;
                        long lockTime = coreInfo.lockTime;
                        long timePast = Math.max(0, System.currentTimeMillis() - lockTime);
                        long tickPast = timePast / 500;//slimefun tick
                        for (int i = 0; i < stablizedOutputAmountsPerTick.length; i += 1) {
                            stablizedOutputAmountsPerTick[i] = stablizedOutputAmountsPerTick[i].multiply(tickPast);
                            BigRational outputAmount = stablizedOutputAmountsPerTick[i];
                            ItemStackAsKey itemToOutput = new ItemStackAsKey(stabilizedOutputs[i]);
                            if (!outputMap.containsKey(itemToOutput) && !emptyStorageLocations.isEmpty()) {
                                SimplePair<SerializeFriendlyBlockLocation,StorageInfo> newEmptyContainer = emptyStorageLocations.remove(0);
                                outputMap.put(itemToOutput, newEmptyContainer);
                            } else if (emptyStorageLocations.isEmpty()) {
                                continue;
                            }

                            if (outputAmount.denominator().compareTo(BigInteger.ZERO) == 0){
                                new ArithmeticException("divide by zero:"+ outputAmount).printStackTrace();
                                continue;
                            }
                            BigInteger outputAmountInteger = outputAmount.numerator().divide(outputAmount.denominator());
                            if (outputAmountInteger.compareTo(BigInteger.ZERO) <= 0){
                                continue;
                            }
//                                logger.log(Level.WARNING,"output:"+itemToOutput);
                            SimplePair<SerializeFriendlyBlockLocation, StorageInfo> containerToOutput = outputMap.get(itemToOutput);
                            containerToOutput.second.storeAmount = containerToOutput.second.storeAmount.add(outputAmountInteger);
                            containerToOutput.second.storeItem = itemToOutput.getTemplate();
                            storageInfoSerializer.saveToLocationNoThrow(containerToOutput.second,containerToOutput.first);
                            MachineNetworkStorage.updateStoredStacks(BlockStorage.getInventory(containerToOutput.first.toLocation()),containerToOutput.second.storeItem, containerToOutput.second.storeAmount);

                        }
                        clearCoreInputsAndOutputsInfo(coreInfo);
                        coreInfo.networkStatus = NETWORK_CONTROLLER_ONLINE;
                        coreInfoSerializer.saveToLocationNoThrow(coreInfo, coreLocationKey);
                        unlockNode(b.getLocation());
                        refresh(menu, b, NETWORK_CONTROLLER_ONLINE);
                    }catch (Exception e ){e.printStackTrace();}
                }).start();
            }).start();
            case NETWORK_CONTROLLER_DISABLING -> new Thread(){
                @Override
                public void run() {
                    super.run();
                    try (CoreInfoSerializer coreInfoSerializer = CoreInfoSerializer.getInstance()){
                        lockNode(b.getLocation());
                        unregisterNodes(b.getLocation());
                        unlockNode(b.getLocation());

                        CoreInfo coreInfo = coreInfoSerializer.getOrDefault(coreLocationKey);
                        coreInfo.networkStatus = NETWORK_CONTROLLER_OFFLINE;
                        coreInfoSerializer.saveToLocationNoThrow(coreInfo, coreLocationKey);

                        refresh(menu, b, NETWORK_CONTROLLER_OFFLINE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * clear inputs and outputs info,or every lock operation increases production.
     * @param coreInfo MachineNetworkCore info(json) to clear in and outs.
     */
    private static CoreInfo clearCoreInputsAndOutputsInfo(CoreInfo coreInfo) {
        coreInfo.inputAmount = EMPTY_BIG_RATIONAL_ARRAY;
        coreInfo.outputAmount = EMPTY_BIG_RATIONAL_ARRAY;
        coreInfo.stableOutputAmount = EMPTY_BIG_RATIONAL_ARRAY;

        coreInfo.inputs = EMPTY_ITEM_STACK_ARRAY;
        coreInfo.outputs = EMPTY_ITEM_STACK_ARRAY;
        coreInfo.stableOutputs = EMPTY_ITEM_STACK_ARRAY;

        coreInfo.energyConsumption = BigInteger.ZERO;
        coreInfo.energyProduction = BigInteger.ZERO;
        coreInfo.energyConsumptionStable = BigInteger.ZERO;
        coreInfo.energyProductionStable = BigInteger.ZERO;
        return coreInfo;
    }


}
