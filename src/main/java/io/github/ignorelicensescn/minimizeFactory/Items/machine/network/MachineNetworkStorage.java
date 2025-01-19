package io.github.ignorelicensescn.minimizeFactory.Items.machine.network;

import io.github.ignorelicensescn.minimizeFactory.utils.ItemStackUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.StorageUtils;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.WordUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeKeys.MINIMIZEFACTORY_CORE_LOCATION;
import static io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeKeys.MINIMIZEFACTORY_NODE_TYPE;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.BukkitSerializer.LOCATION_SERIALIZER;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.Serializer.BIG_INTEGER_SERIALIZER;

/**
 * a chest from FluffyMachines
 * <p>Tweaked to fit in network</p>
 */
public class MachineNetworkStorage extends NetworkNode{
    public static final ItemStack EMPTY_ITEM = new CustomItemStack(Material.BARRIER, properties.getReplacedProperty("MachineNetworkStorage_Empty"));
    public static final String KEY_STORED = "stored";
    public static final BigInteger BIGINTEGER_FIVE = BigInteger.valueOf(5);
    public static final int[] inputBorder = {9, 10, 11, 12, 18, 21, 27, 28, 29, 30};
    public static final int[] outputBorder = {14, 15, 16, 17, 23, 26, 32, 33, 34, 35, 36};
    public static final int[] plainBorder = {0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 36, 37, 38, 39, 40, 41, 42, 43, 44};

    public static final int[] INPUT_SLOTS = {19, 20};
    public static final int[] OUTPUT_SLOTS = {24, 25};
    public static final int STATUS_SLOT = 22;
    public static final int DISPLAY_SLOT = 31;
    public static final int HINT_SLOT = 0;
    public static final int CHECK_SLIMEFUN_ITEM_SLOT = 4;

    public static final int OVERFLOW_AMOUNT_INTEGER = 3240;
    public static final BigInteger OVERFLOW_AMOUNT = BigInteger.valueOf(OVERFLOW_AMOUNT_INTEGER);
    public static final DecimalFormat STORAGE_INDICATOR_FORMAT = new DecimalFormat("###,###.####",
            DecimalFormatSymbols.getInstance(Locale.ROOT));

    public MachineNetworkStorage(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, NodeType.STORAGE);

        new BlockMenuPreset(getId(), getItemName()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                buildMenu(menu, b);
                showCoreLocation(b.getLocation(),HINT_SLOT,menu);;
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(),
                        Interaction.INTERACT_BLOCK);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                return new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.INSERT) {
                    return INPUT_SLOTS;
                }
                else if (flow == ItemTransportFlow.WITHDRAW) {
                    return OUTPUT_SLOTS;
                } else {
                    return new int[0];
                }
            }
        };

        addItemHandler(onBreak(),
                new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                initNode(e.getBlock().getLocation());
            }
        });
    }

    private ItemHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                Block b = e.getBlock();
                Player p = e.getPlayer();
                BlockMenu inv = BlockStorage.getInventory(b);
                BigInteger stored = getStored(b);

                if (BlockStorage.getBlockInfoAsJson(b).contains(MINIMIZEFACTORY_CORE_LOCATION)){
                    p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_Core_Bounded"));
                    e.setCancelled(true);
                    return;
                }

                if (inv != null) {

                    BigInteger itemCount = BigInteger.ZERO;
                    int storedCompareToZero = stored.compareTo(BigInteger.ZERO);

                    for (Entity en : p.getNearbyEntities(5, 5, 5)) {
                        if (en instanceof Item) {
                            itemCount = itemCount.add(BigInteger.ONE);
                        }
                    }

                    if (itemCount.compareTo(BIGINTEGER_FIVE) > 0) {
                        p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_FluffyMachine_Item_Nearby"));
                        e.setCancelled(true);
                        return;
                    }

                    inv.dropItems(b.getLocation(), INPUT_SLOTS);
                    inv.dropItems(b.getLocation(), OUTPUT_SLOTS);

                    if (storedCompareToZero > 0) {
                        int stackSize = inv.getItemInSlot(DISPLAY_SLOT).getMaxStackSize();
                        ItemStack unKeyed = getStoredItem(b);

                        if (unKeyed.getType() == Material.BARRIER) {
                            setStored(b, BigInteger.ZERO);
                            updateMenu(b, inv, true);
                            return;
                        }

                        if (stored.compareTo(OVERFLOW_AMOUNT) > 0) {

                            p.sendMessage(properties.getReplacedProperty("MachineNetworkStorage_FluffyMachine_Dropping_Item_Part1")
                            +OVERFLOW_AMOUNT+properties.getReplacedProperty("MachineNetworkStorage_FluffyMachine_Dropping_Item_Part2"));
                            int toRemove = 3240;
                            while (toRemove >= stackSize) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(stackSize);
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());
                                toRemove = toRemove - stackSize;
                            }
                            if (toRemove > 0) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(toRemove);
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());
                            }
                            setStored(b, stored.subtract(OVERFLOW_AMOUNT));
                            updateMenu(b, inv, false);
                            e.setCancelled(true);
                        }
                        else {
                            BigInteger stackSizeBigInteger = BigInteger.valueOf(stackSize);
                            // Everything greater than 1 stack
                            while (stored.compareTo(stackSizeBigInteger) > 0) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(stackSize);
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());

                                stored = stored.subtract(stackSizeBigInteger);
                            }

                            // Drop remaining, if there is any
                            if (stored.compareTo(BigInteger.ZERO) > 0) {
                                unKeyed = unKeyed.clone();
                                unKeyed.setAmount(stored.intValue());
                                b.getWorld().dropItemNaturally(b.getLocation(), unKeyed.clone());
                            }

                            // In case they use an explosive pick
                            setStored(b, BigInteger.ZERO);
                            updateMenu(b, inv, true);
                        }
                    }

                }
            }
        };
    }

    private void constructMenu(BlockMenuPreset preset) {
        StorageUtils.createBorder(preset, ChestMenuUtils.getOutputSlotTexture(), outputBorder);
        StorageUtils.createBorder(preset, ChestMenuUtils.getInputSlotTexture(), inputBorder);
        ChestMenuUtils.drawBackground(preset, plainBorder);
    }

    protected void buildMenu(BlockMenu menu, Block b) {

        // Initialize an empty barrel
        if (BlockStorage.getLocationInfo(b.getLocation(), KEY_STORED) == null) {

            menu.replaceExistingItem(STATUS_SLOT, new CustomItemStack(
                    Material.LIME_STAINED_GLASS_PANE, properties.getReplacedProperty("MachineNetworkStorage_Items_Stored_0")));
            menu.replaceExistingItem(DISPLAY_SLOT, EMPTY_ITEM);

            setStored(b, BigInteger.ZERO);

        }

        // Every time setup
        menu.addMenuClickHandler(STATUS_SLOT, ChestMenuUtils.getEmptyClickHandler());
        menu.addMenuClickHandler(DISPLAY_SLOT, ChestMenuUtils.getEmptyClickHandler());

        // Insert all
        int INSERT_ALL_SLOT = 43;
        menu.replaceExistingItem(INSERT_ALL_SLOT,
                new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, properties.getReplacedProperty("MachineNetworkStorage_Insert_All"),
                        properties.getReplacedProperties("MachineNetworkStorage_Insert_All_Lore_1",ChatColor.GRAY)));
        menu.addMenuClickHandler(INSERT_ALL_SLOT, (pl, slot, item, action) -> {
            if(!isLocked(b.getLocation())){insertAll(pl, menu, b);}
            return false;
        });

        // Extract all
        int EXTRACT_SLOT = 44;
        menu.replaceExistingItem(EXTRACT_SLOT,
                new CustomItemStack(Material.RED_STAINED_GLASS_PANE, properties.getReplacedProperty("MachineNetworkStorage_Extract_All"),
                        properties.getReplacedProperties("MachineNetworkStorage_Extract_All_Lore_1",ChatColor.GRAY)
                ));
        menu.addMenuClickHandler(EXTRACT_SLOT, (pl, slot, item, action) -> {
            if(!isLocked(b.getLocation())){extract(pl, menu, b, action);}
            return false;
        });
        showCoreLocation(b.getLocation(),HINT_SLOT,menu);
        menu.replaceExistingItem(CHECK_SLIMEFUN_ITEM_SLOT,new CustomItemStack(
                Material.GREEN_STAINED_GLASS_PANE,
                properties.getReplacedProperty("MachineNetworkStorage_Convert_To_Slimefun_Item")
        ));
        menu.addMenuClickHandler(CHECK_SLIMEFUN_ITEM_SLOT, (p, slot, item, action) -> {
            ItemStack unkey = StorageUtils.unKeyItem(menu.getItemInSlot(DISPLAY_SLOT));
            SlimefunItem sfItem = SlimefunItem.getByItem(unkey);
            if (sfItem == null){return false;}
            if (ItemStackUtil.isItemStackSimilar(sfItem.getItem(),unkey)){
                menu.replaceExistingItem(DISPLAY_SLOT,StorageUtils.keyItem(sfItem.getItem().clone()));
                menu.addMenuClickHandler(DISPLAY_SLOT,ChestMenuUtils.getEmptyClickHandler());
            }
            return false;
        });
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                MachineNetworkStorage.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    protected void tick(Block b) {
        if(isLocked(b.getLocation())){return;}
        BlockMenu inv = BlockStorage.getInventory(b);

        for (int slot : INPUT_SLOTS) {
            acceptInput(inv, b, slot);
        }

        for (int ignored : OUTPUT_SLOTS) {
            pushOutput(inv, b);
        }
    }

    void acceptInput(BlockMenu inv, Block b, int slot) {
        if (inv.getItemInSlot(slot) == null) {
            return;
        }

        BigInteger stored = getStored(b);
        ItemStack item = inv.getItemInSlot(slot);

        int storedCompareToZero = stored.compareTo(BigInteger.ZERO);

        if (storedCompareToZero == 0) {
            registerItem(b, inv, slot, item, stored);
        }
        else if (storedCompareToZero > 0 && inv.getItemInSlot(DISPLAY_SLOT) != null
                && ItemStackUtil.isItemStackSimilar(StorageUtils.unKeyItem(inv.getItemInSlot(DISPLAY_SLOT)), item))
        {
                storeItem(b, inv, slot, item, stored);
            }
    }

    void pushOutput(BlockMenu inv, Block b) {
        ItemStack displayItem = inv.getItemInSlot(DISPLAY_SLOT);
        if (displayItem != null && displayItem.getType() != Material.BARRIER) {
            BigInteger stored = getStored(b);
            // Output stack
            if (stored.compareTo(BigInteger.valueOf(displayItem.getMaxStackSize())) > 0) {
                ItemStack clone = StorageUtils.unKeyItem(displayItem);
                clone.setAmount(clone.getMaxStackSize());
                if (inv.fits(clone, OUTPUT_SLOTS)) {
                    int amount = clone.getMaxStackSize();

                    setStored(b, stored.subtract(BigInteger.valueOf(amount)));
                    inv.pushItem(clone, OUTPUT_SLOTS);
                    updateMenu(b, inv, false);
                }
            }
            else if (stored.compareTo(BigInteger.ZERO) == 0) {   // Output remaining

                ItemStack clone = StorageUtils.unKeyItem(displayItem);
                clone.setAmount(stored.intValue());

                if (inv.fits(clone, OUTPUT_SLOTS)) {
                    setStored(b, BigInteger.ZERO);
                    inv.pushItem(clone, OUTPUT_SLOTS);
                    updateMenu(b, inv, false);
                }
            }
        }
    }

    private static void registerItem(Block b, BlockMenu inv, int slot, ItemStack item, BigInteger stored) {
        inv.replaceExistingItem(DISPLAY_SLOT, StorageUtils.keyItem(item));
        storeItem(b, inv, slot, item, stored);
    }
    public static void registerItem(Location l,ItemStack itemStack){
        BlockStorage.getInventory(l).replaceExistingItem(DISPLAY_SLOT, StorageUtils.keyItem(itemStack));
    }

    public static void setStoredStackNoThrow(Location l, ItemStack item){
        BlockStorage.getInventory(l).replaceExistingItem(DISPLAY_SLOT, StorageUtils.keyItem(item));
    }

    public static void storeItem(Block b, BlockMenu inv, int slot, ItemStack item, BigInteger stored) {
        int amount = item.getAmount();
        inv.consumeItem(slot, amount);

        setStored(b, stored.add(BigInteger.valueOf(amount)));
        updateMenu(b, inv, false);
    }

    public static void networkStoreItem(Block b, BlockMenu inv, ItemStack item, BigInteger store) {
        BigInteger stored = getStored(b);
        if (stored.compareTo(BigInteger.ZERO) == 0) {
            inv.replaceExistingItem(DISPLAY_SLOT, StorageUtils.keyItem(item));
            setStored(b, store);
        }
        else {
            setStored(b, stored.add(store));
        }
        updateMenu(b, inv, false);
    }

    /**
     * This method updates the barrel's menu and hologram displays
     *
     * @param b   is the barrel block
     * @param inv is the barrel's inventory
     */
    public static void updateMenu(Block b, BlockMenu inv, boolean force) {
        BigInteger stored = getStored(b);
        BigDecimal storedBigDecimal = new BigDecimal(stored);
        String itemName;

        BigDecimal maxStackSizeBigDecimal = BigDecimal.valueOf(inv.getItemInSlot(DISPLAY_SLOT).getMaxStackSize());
        String storedStacks = doubleRoundAndFade(storedBigDecimal.divide(maxStackSizeBigDecimal,4, RoundingMode.DOWN));

        if (inv.getItemInSlot(DISPLAY_SLOT) != null && inv.getItemInSlot(DISPLAY_SLOT).getItemMeta().hasDisplayName()) {
            itemName = inv.getItemInSlot(DISPLAY_SLOT).getItemMeta().getDisplayName();
        }
        else {
            itemName = WordUtils.capitalizeFully(inv.getItemInSlot(DISPLAY_SLOT).getType().name().replace("_", " "));
        }

        // This helps a bit with lag, but may have visual impacts
        if (inv.hasViewer() || force) {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItemStack(
                    Material.LIME_STAINED_GLASS_PANE,itemName ,
                    properties.getReplacedProperty("MachineNetworkStorage_Items_Stored") + stored,
                    ChatColor.GRAY + storedStacks + properties.getReplacedProperty("MachineNetworkStorage_Stacks")));
        }


        if (stored.compareTo(BigInteger.ZERO) == 0) {
            inv.replaceExistingItem(DISPLAY_SLOT, EMPTY_ITEM);
        }
    }

    /**
     * Sets a key in BlockStorage and replaces an item
     */
    private void putBlockData(Block b, int slot, String key, ItemStack displayItem, boolean data) {
        BlockStorage.addBlockInfo(b.getLocation(), key, String.valueOf(data));
        BlockStorage.getInventory(b).replaceExistingItem(slot, displayItem);
    }

    public void insertAll(Player p, BlockMenu menu, Block b) {
        ItemStack storedItem = StorageUtils.unKeyItem(menu.getItemInSlot(DISPLAY_SLOT));
        PlayerInventory inv = p.getInventory();

        BigInteger stored = getStored(b);

        for (int i = 0; i < inv.getContents().length; i+=1) {
            ItemStack item = inv.getItem(i);
            if (item == null) {
                continue;
            }
            int amount = item.getAmount();
            if (ItemStackUtil.isItemStackSimilar(item, storedItem)) {
                inv.setItem(i, null);
                stored = stored.add(BigInteger.valueOf(amount));
            }
        }

        BlockStorage.addBlockInfo(b.getLocation(), KEY_STORED, BIG_INTEGER_SERIALIZER.SerializableToString(stored));
        updateMenu(b, menu, false);
    }

    public void extract(Player p, BlockMenu menu, Block b, ClickAction action) {
        ItemStack storedItem = getStoredItem(b);
        if (storedItem == null){return;}
        ItemStack storedClone = storedItem.clone();

        PlayerInventory inv = p.getInventory();
        BigInteger stored = getStored(b);

        // Extract single
        if (action.isRightClicked()) {
            if (stored.compareTo(BigInteger.ZERO) > 0) { // Extract from stored
                StorageUtils.giveOrDropItem(p, storedClone);
                stored = stored.subtract(BigInteger.ONE);
                setStored(b, stored);
                updateMenu(b, menu, false);
                return;
            } else {
                for (int slot : OUTPUT_SLOTS) { // Extract from slot
                    ItemStack inSlot = menu.getItemInSlot(slot);
                    if (inSlot != null) {
                        StorageUtils.giveOrDropItem(p, inSlot);
                        menu.consumeItem(slot);
                        return;
                    }
                }
            }
            return;
        }

        if (storedItem.getType() == Material.BARRIER) {
            return;
        }

        // Extract all
        ItemStack[] contents = inv.getStorageContents().clone();
        int maxStackSize = storedItem.getMaxStackSize();
        BigInteger maxStackSizeBigInteger = BigInteger.valueOf(maxStackSize);
        int outI = 0;

        for (int i = 0; i < contents.length; i+=1) {

            if (contents[i] == null) {
                if (stored.compareTo(maxStackSizeBigInteger) > 0)
                {
                    storedItem = storedItem.clone();
                    storedItem.setAmount(maxStackSize);
                    inv.setItem(i, storedItem.clone());
                    stored = stored.subtract(maxStackSizeBigInteger);
                }
                else if (stored.compareTo(BigInteger.ZERO) > 0) {
                    storedItem = storedItem.clone();
                    storedItem.setAmount(stored.intValue());
                    inv.setItem(i, storedItem.clone());
                    stored = BigInteger.ZERO;
                }
                else {
                    if (outI > 1) {
                        break;
                    }

                    ItemStack item = menu.getItemInSlot(OUTPUT_SLOTS[outI]);

                    if (item == null) {
                        continue;
                    }

                    inv.setItem(i, item.clone());
                    menu.replaceExistingItem(OUTPUT_SLOTS[outI], null);

                    outI+=1;
                }
            }
        }

        setStored(b, stored);
        updateMenu(b, menu, false);
    }

    public static String doubleRoundAndFade(BigDecimal num) {
        // Using same format that is used on lore power
        String formattedString = STORAGE_INDICATOR_FORMAT.format(num);
        if (formattedString.indexOf('.') != -1) {
            return formattedString.substring(0, formattedString.indexOf('.')) + ChatColor.DARK_GRAY
                    + formattedString.substring(formattedString.indexOf('.')) + ChatColor.GRAY;
        } else {
            return formattedString;
        }
    }

    public static BigInteger getStored(Block b) {
        return BIG_INTEGER_SERIALIZER.StringToSerializable(BlockStorage.getLocationInfo(b.getLocation(), KEY_STORED));
    }
    public static BigInteger getStored(Location b) {
        return BIG_INTEGER_SERIALIZER.StringToSerializable(BlockStorage.getLocationInfo(b, KEY_STORED));
    }

    public static void setStored(Block b, BigInteger amount) {
        BlockStorage.addBlockInfo(b.getLocation(), KEY_STORED, BIG_INTEGER_SERIALIZER.SerializableToString(amount));
        updateMenu(b,BlockStorage.getInventory(b),true);
    }
    public static void setStored(Location b, BigInteger amount) {
        BlockStorage.addBlockInfo(b, KEY_STORED, BIG_INTEGER_SERIALIZER.SerializableToString(amount));
        updateMenu(b.getBlock(),BlockStorage.getInventory(b),true);
    }
    public static void addStored(Location b,BigInteger amount){
        BlockStorage.addBlockInfo(b, KEY_STORED, BIG_INTEGER_SERIALIZER.SerializableToString(amount.add(getStored(b))));
        updateMenu(b.getBlock(),BlockStorage.getInventory(b),true);
    }
    public static void addStored(Location b, BigRational amount){
        BigRational rational = amount.add(getStored(b));
        if (rational.denominator().compareTo(BigInteger.ZERO) == 0){
            new ArithmeticException("divide by zero:"+rational).printStackTrace();
            return;
        }
        BigInteger result = rational.numerator().divide(rational.denominator());
        BlockStorage.addBlockInfo(b, KEY_STORED, BIG_INTEGER_SERIALIZER.SerializableToString(result));
        updateMenu(b.getBlock(),BlockStorage.getInventory(b),true);
    }

    public static ItemStack getStoredItem(Block b) {
        return getStoredItem(b.getLocation());
    }
    public static ItemStack getStoredItem(Location b) {
        ItemStack result = StorageUtils.unKeyItem(BlockStorage.getInventory(b).getItemInSlot(DISPLAY_SLOT));
        if (ItemStackUtil.isItemStackSimilar(result,EMPTY_ITEM)){return null;}
        return result;
    }

    public static void showCoreLocation(Location nodeLocation,int hintSlot){
        showCoreLocation(nodeLocation,hintSlot,BlockStorage.getInventory(nodeLocation));
    }
    static void showCoreLocation(Location nodeLocation, int hintSlot, BlockMenu nodeMenu){
        JSONObject jsonObject = new JSONObject(BlockStorage.getBlockInfoAsJson(nodeLocation));
        if (jsonObject.has(MINIMIZEFACTORY_CORE_LOCATION)
                && BlockStorage.hasInventory(LOCATION_SERIALIZER.StringToSerializable(jsonObject.get(MINIMIZEFACTORY_CORE_LOCATION).toString()).getBlock())
                && isNodeRegisteredToCore(nodeLocation, LOCATION_SERIALIZER.StringToSerializable(jsonObject.get(MINIMIZEFACTORY_CORE_LOCATION).toString()))
        )
        {
            String locationStr = jsonObject.get(MINIMIZEFACTORY_CORE_LOCATION).toString();
            Location coreLocation = LOCATION_SERIALIZER.StringToSerializable(locationStr);
            nodeMenu.replaceExistingItem(hintSlot, new CustomItemStack(Material.YELLOW_STAINED_GLASS_PANE,
                    properties.getReplacedProperty("MachineNetwork_Core_Location") + coreLocation,
                    properties.getReplacedProperties("MachineNetwork_Core_Location_Lore_1", ChatColor.GRAY)
            ));
            nodeMenu.addMenuClickHandler(hintSlot, (p, slot, item, action) -> {
                if (!BlockStorage.hasInventory(coreLocation.getBlock())
                        || !BlockStorage.hasBlockInfo(coreLocation)
                        || !new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation)).has(MINIMIZEFACTORY_NODE_TYPE)
                        || !new JSONObject(BlockStorage.getBlockInfoAsJson(coreLocation)).get(MINIMIZEFACTORY_NODE_TYPE).equals(NodeType.CONTROLLER.name())
                ){
                    nodeMenu.replaceExistingItem(hintSlot, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE,""));
                    nodeMenu.addMenuClickHandler(hintSlot, (p1, slot1, item1, action1) -> false);
                    return false;
                }
                BlockMenu menu1 = BlockStorage.getInventory(coreLocation);
                if (menu1 != null)
                {menu1.open(p);}
                return false;
            });
        }
        else {
            nodeMenu.replaceExistingItem(hintSlot, new CustomItemStack(Material.GRAY_STAINED_GLASS_PANE,""));
            nodeMenu.addMenuClickHandler(hintSlot, (p, slot, item, action) -> false);
        }
    }

    public static boolean isValidStorage(Block b){
        return BlockStorage.check(b) instanceof MachineNetworkStorage;
    }
    public static boolean isValidStorage(Location l){
        return BlockStorage.check(l) instanceof MachineNetworkStorage;
    }
}
