package io.github.ignorelicensescn.minimizeFactory.utils;

import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AltarRecipe;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.FluidPump;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.altarRecipes;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.logger;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.getIntInUnsafe_static;

public class SlimefunConsts {
    public static int FLUID_PUMP_ENERGY_CONSUMPTION = 42;
    public static boolean GEOInitializedFlag = false;
    public static final List<SimplePair<ItemStack, IntegerRational>> emptyGEOResourcesList = new ArrayList<>();

    /**
     * get ALL GEO resources
     */
    public static Map<World.Environment,Map<Biome,SimplePair<ItemStack[],IntegerRational[]>>> geoResourcesInfo = new HashMap<>();
    public static Map<World.Environment,Map<Biome,SimplePair<ItemStack[], IntegerRational[]>>> geoMinerResourcesInfo = new HashMap<>();

    /**
     * more useful maps for information
     */
    public static Map<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>> geoResourcesInfo_ResourcesKey = new HashMap<>();
    public static Map<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>> geoMinerResourcesInfo_ResourcesKey = new HashMap<>();
    public static List<SimplePair<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>>> geoResourcesInfo_ResourcesList = new ArrayList<>();
    public static List<SimplePair<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>>> geoMinerResourcesInfo_ResourcesList = new ArrayList<>();

    public static Map<BiomeAndEnvironment,SimplePair<ItemStack[],IntegerRational[]>> geoResourcesInfo_BiomeAndEnvironmentKey = new HashMap<>();
    public static Map<BiomeAndEnvironment,SimplePair<ItemStack[],IntegerRational[]>> geoMinerResourcesInfo_BiomeAndEnvironmentKey = new HashMap<>();
    public static List<SimplePair<BiomeAndEnvironment,SimplePair<ItemStack[],IntegerRational[]>>> geoResourcesInfo_BiomeAndEnvironmentKey_List = new ArrayList<>();
    public static List<SimplePair<BiomeAndEnvironment,SimplePair<ItemStack[],IntegerRational[]>>> geoMinerResourcesInfo_BiomeAndEnvironmentKey_List = new ArrayList<>();
    public static void initAllGEOResources(){
        logger.log(Level.INFO,"Initializing GEO resources");
        for (World.Environment env:World.Environment.values()){
            for (Biome biome:Biome.values()){
                initGEOResources(env,biome);
            }
        }
        initGEOLists();
        GEOInitializedFlag = true;
        geoResourcesInfo_ResourcesKey.clear();
        geoMinerResourcesInfo_ResourcesKey.clear();
        logger.log(Level.INFO,"GEO resources initialized");
    }


    public static void initGEOResources(World.Environment env, Biome biome){
        BiomeAndEnvironment envPair = new BiomeAndEnvironment(biome,env);
        int geoResCounter = 0;
        int geoMinerResCounter = 0;
        int geoResourcesWeightSum = 0;
        int geoResourcesWeightSumObtainable = 0;
        List<GEOResource> shouldCheck = new ArrayList<>();
        for (GEOResource geoResource: Slimefun.getRegistry().getGEOResources().values()){
            int supply = geoResource.getDefaultSupply(env,biome);
            if (supply != 0){
                geoResCounter += 1;
                shouldCheck.add(geoResource);
                geoResourcesWeightSum += supply;
                if (geoResource.isObtainableFromGEOMiner()){
                    geoMinerResCounter += 1;
                    geoResourcesWeightSumObtainable += supply;
                }
            }
        }
        ItemStack[] geoResStack = new ItemStack[geoResCounter];
        IntegerRational[] geoExpectations = new IntegerRational[geoResCounter];
        ItemStack[] geoMinerResStack = new ItemStack[geoMinerResCounter];
        IntegerRational[] geoMinerExpectations = new IntegerRational[geoMinerResCounter];
        geoResCounter = 0;
        geoMinerResCounter = 0;
        for (GEOResource geoResource: shouldCheck){
            int supply = geoResource.getDefaultSupply(env,biome);
            geoResStack[geoResCounter] = geoResource.getItem();
            geoExpectations[geoResCounter] = new IntegerRational(supply,geoResourcesWeightSum);
            geoResCounter += 1;
            if (geoResource.isObtainableFromGEOMiner()){
                geoMinerResStack[geoMinerResCounter] = geoResource.getItem();
                geoMinerExpectations[geoMinerResCounter] = new IntegerRational(supply,geoResourcesWeightSumObtainable);
                geoMinerResCounter += 1;
            }
        }
        geoResourcesInfo_BiomeAndEnvironmentKey.put(new BiomeAndEnvironment(biome,env),new SimplePair<>(geoResStack,geoExpectations));
        geoMinerResourcesInfo_BiomeAndEnvironmentKey.put(new BiomeAndEnvironment(biome,env),new SimplePair<>(geoMinerResStack,geoMinerExpectations));
        geoResourcesInfo_BiomeAndEnvironmentKey_List.add(new SimplePair<>(new BiomeAndEnvironment(biome,env),new SimplePair<>(geoResStack,geoExpectations)));
        geoMinerResourcesInfo_BiomeAndEnvironmentKey_List.add(new SimplePair<>(new BiomeAndEnvironment(biome,env),new SimplePair<>(geoMinerResStack,geoMinerExpectations)));
//        List<SimpleTri<ItemStack, IntegerRational>> geoResourcesList = new ArrayList<>();
//        List<SimpleTri<ItemStack, IntegerRational>> geoResourcesListObtainable = new ArrayList<>();
        ItemStack[] geoResourcesArray = new ItemStack[geoResCounter];
        IntegerRational[] geoResourcesExpectationArray = new IntegerRational[geoResCounter];
        ItemStack[] geoMinerResourcesArray = new ItemStack[geoMinerResCounter];
        IntegerRational[] geoMinerResourcesExpectationArray = new IntegerRational[geoMinerResCounter];

        int counter1 = 0;
        int counter2 = 0;
        for (GEOResource geoResource: shouldCheck){
            geoResourcesArray[counter1] = geoResource.getItem().clone();
            geoResourcesExpectationArray[counter1] = new IntegerRational(geoResource.getDefaultSupply(env,biome),geoResourcesWeightSum);
            if (!geoResourcesInfo_ResourcesKey.containsKey(geoResource.getItem())){
                geoResourcesInfo_ResourcesKey.put(geoResource.getItem(),new ArrayList<>());
            }
            geoResourcesInfo_ResourcesKey.get(geoResource.getItem()).add(new SimplePair<>(envPair,new IntegerRational(geoResource.getDefaultSupply(env,biome),geoResourcesWeightSum)));
            if (geoResource.isObtainableFromGEOMiner()){
                geoMinerResourcesArray[counter2] = geoResource.getItem().clone();
                geoMinerResourcesExpectationArray[counter2] = new IntegerRational(geoResource.getDefaultSupply(env,biome),geoResourcesWeightSumObtainable);
                counter2 += 1;
                if (!geoMinerResourcesInfo_ResourcesKey.containsKey(geoResource.getItem())){
                    geoMinerResourcesInfo_ResourcesKey.put(geoResource.getItem(),new ArrayList<>());
                }
                geoMinerResourcesInfo_ResourcesKey.get(geoResource.getItem()).add(new SimplePair<>(envPair,new IntegerRational(geoResource.getDefaultSupply(env,biome),geoResourcesWeightSumObtainable)));
            }
            counter1 += 1;
        }
        SimplePair<ItemStack[],IntegerRational[]> pair1 = new SimplePair<>(geoResourcesArray,geoResourcesExpectationArray);
        SimplePair<ItemStack[],IntegerRational[]> pair2 = new SimplePair<>(geoMinerResourcesArray,geoMinerResourcesExpectationArray);
        if (!geoResourcesInfo.containsKey(env)){
            Map<Biome,SimplePair<ItemStack[],IntegerRational[]>> newMap = new HashMap<>();
            newMap.put(biome,pair1);
            geoResourcesInfo.put(env,newMap);
        }else {
            geoResourcesInfo.get(env).put(biome,pair1);
        }
        if (!geoMinerResourcesInfo.containsKey(env)){
            Map<Biome,SimplePair<ItemStack[],IntegerRational[]>> newMap = new HashMap<>();
            newMap.put(biome,pair2);
            geoMinerResourcesInfo.put(env,newMap);
        }else {
            geoMinerResourcesInfo.get(env).put(biome,pair2);
        }
    }

    public static void initGEOLists(){
        for (ItemStack i:geoMinerResourcesInfo_ResourcesKey.keySet()){
            geoMinerResourcesInfo_ResourcesList.add(new SimplePair<>(i,geoMinerResourcesInfo_ResourcesKey.get(i)));
        }
        for (ItemStack i:geoResourcesInfo_ResourcesKey.keySet()){
            geoResourcesInfo_ResourcesList.add(new SimplePair<>(i,geoResourcesInfo_ResourcesKey.get(i)));
        }
    }

    public static void initEnergyConst() {
        try {
            Field f = FluidPump.class.getDeclaredField("ENERGY_CONSUMPTION");
            FLUID_PUMP_ENERGY_CONSUMPTION = getIntInUnsafe_static(f);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void initAncientAltarRecipes(){
        AncientAltar altarInstance = (AncientAltar) SlimefunItems.ANCIENT_ALTAR.getItem();
        assert altarInstance != null;
        for (AltarRecipe recipe: altarInstance.getRecipes()){
            altarRecipes.add(new SimplePair<>(
                    ItemStackUtil.collapseItems(recipe.getInput().toArray(new ItemStack[0]),
                    recipe.getCatalyst()),
                    recipe.getOutput().clone()));
        }
    }
}
