package io.github.ignorelicensescn.minimizefactory;

import io.github.acdeasdff.infinityCompress.items.Multiblock_Autocrafter;
import io.github.ignorelicensescn.minimizefactory.items.Registers;
import io.github.ignorelicensescn.minimizefactory.sfgroups.Groups;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.SQLiteBlockDataStorageManager;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Cultivation.CultivationSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.DynaTech.DynaTechSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.FNAmp.FNAmplificationSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.FluffyMachines.FluffyMachinesSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress.InfinityCompressSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.LiteX.LiteXpansionSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStacksToStackRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.timestampbasedmanagers.PageManager;
import io.github.ignorelicensescn.minimizefactory.utils.tweakedproperty2.TweakedProperty2;
import io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Slimefun.SlimefunSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizefactory.utils.searchregistries.SearchRegistries;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.ncbpfluffybear.fluffymachines.machines.AutoTableSaw;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.github.acdeasdff.infinityCompress.items.FNFALsAmplifications.BlocksFN.*;
import static io.github.acdeasdff.infinityCompress.items.LiteXpansion.BlocksLiteXpansion.*;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_STACKS_TO_STACK_RECIPE;
import static io.github.ignorelicensescn.minimizefactory.utils.EmptyArrays.EMPTY_STRING_ARRAY;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Slimefun.SlimefunConsts.*;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.DynaTech.DynaTechConsts.*;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress.InfinityCompressConsts.getMultiblockAutocrafterRecipes;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.InfoScan.initAutoTableSawRecipes;
import static io.github.ignorelicensescn.minimizefactory.utils.itemstackrelated.ItemStackUtil.RecipeChoiceListToItemStackArray_formated;
import static io.ncbpfluffybear.fluffymachines.utils.FluffyItems.AUTO_TABLE_SAW;

public class MinimizeFactory extends AbstractAddon {
    public static long LONG_MESSAGE_DELAY = 3000;
    public static int LONG_MESSAGE_COUNT = 5;
    public static int LONG_MESSAGE_BLOCK_NOTIFICATION_COUNT = 5;

    //no more use now since biome is for every recipe
    public static int GEOMINER_BIOME_EVERY_LINE = 3;

    public static MinimizeFactory minimizeFactoryInstance;
    public static Logger logger;
    public static String language;
    public static TweakedProperty2 properties;

    public static long NETWORK_MAX_DISTANCE = 5;
    public static long NETWORK_CONNECTOR_DELAY_FOR_ALL = 5000;
    public static long NETWORK_CONNECTOR_DELAY_FOR_ONE = 15000;
    public static final Map<String,Long> msgSendDelay = new HashMap<>();
    public static final Map<String,Integer> blockMessageCounter = new HashMap<>();
    public static long lastConnectorUsedTime = 0;
    public static final Map<UUID,Long> PlayerLastConnectorUsedTime = new HashMap<>();
    public static ItemStacksToStackRecipe[] vanillaRecipeArray = EMPTY_STACKS_TO_STACK_RECIPE;
    public static ItemStacksToStackRecipe[] altarRecipes = EMPTY_STACKS_TO_STACK_RECIPE;

    public MinimizeFactory(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "ignorelicensescn", "MinimizeFactory", "master", "auto-update");
    }
    public MinimizeFactory() {
        //but i didn't set auto-update
        super("ignorelicensescn", "MinimizeFactory", "master", "auto-update");
    }

    public static SQLiteBlockDataStorageManager databaseInstance = null;
    public void getDatabaseInstance(){
        if (databaseInstance == null){
            databaseInstance = new SQLiteBlockDataStorageManager(this);
            databaseInstance.load();
        }
    }
    @Override
    protected void enable() {

        minimizeFactoryInstance = this;
        logger = this.getLogger();
        logger.log(Level.INFO,"Logger loaded.");

        logger.log(Level.INFO,"Loading configs.");
        GEOMINER_BIOME_EVERY_LINE = minimizeFactoryInstance.getConfig().getInt("geominer_info_lore_biome_every_line", 1, 100);
        LONG_MESSAGE_DELAY = minimizeFactoryInstance.getConfig().getLong("long_message_delay", 3000);
        LONG_MESSAGE_COUNT = minimizeFactoryInstance.getConfig().getInt("long_message_count", 5);
        LONG_MESSAGE_BLOCK_NOTIFICATION_COUNT = minimizeFactoryInstance.getConfig().getInt("long_message_block_notification_time", 5);
        NETWORK_MAX_DISTANCE = minimizeFactoryInstance.getConfig().getLong("machine_network_max_distance", 5);
        NETWORK_CONNECTOR_DELAY_FOR_ALL = minimizeFactoryInstance.getConfig().getLong("machine_network_remote_connector_delay_millseconds_for_all",5000);
        NETWORK_CONNECTOR_DELAY_FOR_ONE = minimizeFactoryInstance.getConfig().getLong("machine_network_remote_connector_delay_millseconds_for_one",15000);
        logger.log(Level.INFO,"Configs loaded.");

        logger.log(Level.INFO, "Checking installed plugins.");
        if (Bukkit.getPluginManager().isPluginEnabled("InfinityExpansion")){
            PluginEnabledFlags.InfinityExpansionFlag = true;
            logger.log(Level.INFO,"InfinityExpansion Detected");
            COST_MULTIPLIER =
                    InfinityExpansion.config().getDouble("balance-options.singularity-cost-multiplier", 0.1, 100);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("InfinityCompress")){
            PluginEnabledFlags.InfinityCompressFlag = true;
            logger.log(Level.INFO,"InfinityCompress Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("LiteXpansion")){
            PluginEnabledFlags.LiteXpansionFlag = true;
            logger.log(Level.INFO,"LiteXpansion Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("FNAmplifications")){
            PluginEnabledFlags.FNAmplificationsFlag = true;
            logger.log(Level.INFO,"FNAmplifications Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("SlimeFrame")){
            PluginEnabledFlags.SlimeFrameFlag = true;
            logger.log(Level.INFO,"SlimeFrame Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("GlobalWarming")){
            PluginEnabledFlags.GlobalWarmingFlag = true;
            logger.log(Level.INFO,"GlobalWarming Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Galactifun")){
            PluginEnabledFlags.GalactifunFlag = true;
            logger.log(Level.INFO,"Galactifun Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("DynaTech")){
            PluginEnabledFlags.DynaTechFlag = true;
            logger.log(Level.INFO,"DynaTech Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("FluffyMachines")){
            PluginEnabledFlags.FluffyMachinesFlag = true;
            logger.log(Level.INFO,"FluffyMachines Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Cultivation")){
            PluginEnabledFlags.CultivationFlag = true;
            logger.log(Level.INFO,"Cultivation Detected");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("GuizhanLibPlugin")){
            PluginEnabledFlags.GuizhanLibPluginFlag = true;
            logger.log(Level.INFO,"GuizhanLibPlugin Detected");
        }

        logger.log(Level.INFO, "Installed plugins Checked.");

        {
            properties = new TweakedProperty2();
            try {
                language = getConfig().getString("language");
                logger.log(Level.INFO,"Loading language properties...");
                InputStream inStream = this.getClassLoader().getResourceAsStream("language/" + language + ".properties");
                if (inStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inStream, StandardCharsets.UTF_8);
                    properties.load(inputStreamReader);
                }else {
                    logger.log(Level.WARNING,"Invalid property name:" + language);
                    inStream = this.getClassLoader().getResourceAsStream("language/zh_CN.properties");
                    assert inStream != null;
                    InputStreamReader inputStreamReader = new InputStreamReader(inStream, StandardCharsets.UTF_8);
                    properties.load(inputStreamReader);
                }
                logger.log(Level.INFO,"Properties loaded.");
            } catch (Exception e) {
                logger.log(Level.SEVERE,"Properties load failed.");
                e.printStackTrace();
            }
        }

        new Thread(MinimizeFactory::shapeRecipes).start();

        logger.log(Level.INFO,"Loading Groups.");
        Groups.setup(minimizeFactoryInstance);
        Registers.setup(minimizeFactoryInstance);
        logger.log(Level.INFO,"Groups loaded.");

        //if u have lots of cores.
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, this::onServerStarted);
        long period = LONG_MESSAGE_DELAY * 300;
        if (period < 0) {
            period = Long.MAX_VALUE;
        }
        Bukkit.getServer().getScheduler().runTaskTimer(minimizeFactoryInstance, () -> {
            msgSendDelay.clear();
            blockMessageCounter.clear();
            PlayerLastConnectorUsedTime.clear();
            PageManager.tryClear();
        },period, period);
    }

    protected void onServerStarted(){
            logger.log(Level.INFO,"Loading recipe information.");
            new Thread(() -> {
                initEnergyConst();
                initAllGEOResources();
                initAncientAltarRecipes();
                SlimefunSerializedMachineRecipes.init();
            }).start();
            if (PluginEnabledFlags.InfinityExpansionFlag){
                {
                    try{getEnergyConsts();} catch (Exception e){e.printStackTrace();}
                    try{initMobDataCards();} catch (Exception e){e.printStackTrace();}
                }
                new Thread(() -> {
                    try{
                        getInfinitySingularities();
                        getRandomizedItemStackClass();
                        initInfinityWorkbenchRecipes();
                        initGearTransformerRecipes();
                        getStoneworksFactoryRecipes();
                        getOscillators();
                    } catch (Exception e){e.printStackTrace();}
                }).start();

                InfinityExpansionSerializedMachineRecipes.init();
                if (PluginEnabledFlags.InfinityCompressFlag){
                    if (PluginEnabledFlags.LiteXpansionFlag){
                        new Thread(() -> {

                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_METAL_FORGE));}
                            catch (Exception e){e.printStackTrace();}
                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_SMELTRY));}
                            catch (Exception e){e.printStackTrace();}
                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_MILL));}
                            catch (Exception e){e.printStackTrace();}
                        }).start();
                    }
                    if (PluginEnabledFlags.FNAmplificationsFlag){
                        new Thread(() -> {

                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_MAGIC_ALTAR));}
                            catch (Exception e){e.printStackTrace();}
                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_ASSEMBLING_STATION));}
                            catch (Exception e){e.printStackTrace();}
                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_SCRAP_RECYCLER));}
                            catch (Exception e){e.printStackTrace();}
                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_GEM_ALTAR));}
                            catch (Exception e){e.printStackTrace();}
                            try{getMultiblockAutocrafterRecipes((Multiblock_Autocrafter) SlimefunItem.getByItem(AUTO_STICK_ALTAR));}
                            catch (Exception e){e.printStackTrace();}
                        }).start();
                    }
                    InfinityCompressSerializedMachineRecipes.init();
                }
            }
            if (PluginEnabledFlags.DynaTechFlag){
                try{
                    getStardustReactorFuels();
                    getCulinaryGeneratorFuels();
                    getGrowthChambersRecipes();
                } catch (Exception e){e.printStackTrace();}
                SearchRegistries.scan();
                DynaTechSerializedMachineRecipes.init();
            }
            if (PluginEnabledFlags.LiteXpansionFlag){
                LiteXpansionSerializedMachineRecipes.init();
            }
            if (PluginEnabledFlags.FNAmplificationsFlag){
                new Thread(() -> {
                    FNAmplificationSerializedMachineRecipes.init();
                    SearchRegistries.scan();
                }).start();
            }
            if (PluginEnabledFlags.FluffyMachinesFlag){
                new Thread(() -> {
                    try{initAutoTableSawRecipes((AutoTableSaw) AUTO_TABLE_SAW.getItem());} catch (Exception e){e.printStackTrace();}
                    FluffyMachinesSerializedMachineRecipes.init();
                }).start();
            }
            if (PluginEnabledFlags.CultivationFlag){
                new Thread(() -> {
                    try {
                        CultivationSerializedMachineRecipes.init();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
            }
            logger.log(Level.INFO,"Recipe information load thread set.");
    }


    @Override
    protected void disable() {
        Connection connection = databaseInstance.getSQLConnection();
        try {
            if (!(connection == null)
                    && !connection.isClosed()){
                connection.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * I don't know whether a new thread will bring better performance.
     */
    public void msgSend(Player p, List<String> msgs){
        if (msgs == null){return;}
        if (msgs.isEmpty()){return;}
//        new Thread(() ->
        {

            if (msgs.size() >= LONG_MESSAGE_COUNT
            ){
                if (
                        (msgSendDelay.get(p.getPlayerListName()) != null
                                && ((System.currentTimeMillis() - msgSendDelay.get(p.getPlayerListName())) >= LONG_MESSAGE_DELAY))
                                || (msgSendDelay.get(p.getPlayerListName())==null)
                ){
                    p.sendMessage(msgs.toArray(EMPTY_STRING_ARRAY));
                    blockMessageCounter.remove(p.getPlayerListName());
                    msgSendDelay.put(p.getPlayerListName(),System.currentTimeMillis());
                }else {
                    blockMessageCounter.put(p.getPlayerListName(), blockMessageCounter.get(p.getPlayerListName()) == null ? 1 : blockMessageCounter.get(p.getPlayerListName()) + 1);
                    if (blockMessageCounter.get(p.getPlayerListName()) <= LONG_MESSAGE_BLOCK_NOTIFICATION_COUNT){
                        p.sendMessage(properties.getReplacedProperty("PLEASE_TRY_AGAIN_LATER") + LONG_MESSAGE_DELAY + properties.getReplacedProperty("PLEASE_TRY_AGAIN_LATER_UNIT"));
                    }
                }
            }
            else{
                blockMessageCounter.remove(p.getPlayerListName());
                p.sendMessage(msgs.toArray(EMPTY_STRING_ARRAY));
            }
        }
//        ).start();
    }
    
    public static void shapeRecipes(){
        logger.log(Level.INFO,"Loading vanilla recipes");

        HashMap<List<RecipeChoice>, SimplePair<ItemStack, Recipe>> shapedVanillaRecipes = new HashMap<>();
        HashMap<List<RecipeChoice>, SimplePair<ItemStack, Recipe>> shapelessVanillaRecipes = new HashMap<>();
        //from fluffymachines
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            Recipe r = recipeIterator.next();
            if (r instanceof ShapedRecipe sr) {
                List<RecipeChoice> rc = new ArrayList<>(sr.getChoiceMap().size());
                for (RecipeChoice choice : sr.getChoiceMap().values()) {
                    if (choice != null) {
                        rc.add(choice);
                    }
                }
                shapedVanillaRecipes.put(rc, new SimplePair<>(sr.getResult().clone(), r));
            }
            else if (r instanceof ShapelessRecipe slr) {
                shapelessVanillaRecipes.put(slr.getChoiceList(), new SimplePair<>(slr.getResult().clone(), slr));
            }
        }
        vanillaRecipeArray = new ItemStacksToStackRecipe[shapedVanillaRecipes.size() + shapelessVanillaRecipes.size()];
        int i=0;
        for (List<RecipeChoice> rcList:shapedVanillaRecipes.keySet()){
            vanillaRecipeArray[i] = new ItemStacksToStackRecipe(RecipeChoiceListToItemStackArray_formated(rcList),shapedVanillaRecipes.get(rcList).first);
            i+=1;
        }
        shapedVanillaRecipes.clear();
        for (List<RecipeChoice> rcList:shapelessVanillaRecipes.keySet()){
            vanillaRecipeArray[i] = new ItemStacksToStackRecipe(RecipeChoiceListToItemStackArray_formated(rcList),shapelessVanillaRecipes.get(rcList).first);
            i+=1;
        }
        shapelessVanillaRecipes.clear();
        logger.log(Level.INFO,"Vanilla recipes loaded");
    }
}
