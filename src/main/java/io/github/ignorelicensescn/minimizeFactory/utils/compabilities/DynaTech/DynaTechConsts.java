package io.github.ignorelicensescn.minimizeFactory.utils.compabilities.DynaTech;

import io.github.ignorelicensescn.minimizeFactory.utils.searchregistries.OnScannedSlimefunItemInstanceListener;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.profelements.dynatech.items.abstracts.AbstractElectricMachine;
import me.profelements.dynatech.items.abstracts.AbstractGenerator;
import me.profelements.dynatech.items.electric.generators.CulinaryGenerator;
import me.profelements.dynatech.items.electric.generators.StardustReactor;
import me.profelements.dynatech.items.electric.growthchambers.*;
import me.profelements.dynatech.registries.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.logger;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.getInUnsafe;
import static io.github.ignorelicensescn.minimizeFactory.utils.searchregistries.SearchRegistries.registerOnScannedSlimefunItemInstanceListener;
import static io.github.ignorelicensescn.minimizeFactory.utils.searchregistries.SearchRegistries.unregisterOnScannedSlimefunItemInstanceListener;
import static me.profelements.dynatech.registries.Items.STARDUST_GENERATOR;

public class DynaTechConsts {
    public static List<MachineFuel> stardustReactorFuels = new ArrayList<>();
    public static List<MachineFuel> culinaryGeneratorFuels = new ArrayList<>();
    public static Map<Class<? extends AbstractElectricMachine>, List<MachineRecipe>> growthChamberRecipes = new HashMap<>();
    public static void getStardustReactorFuels() {
        logger.log(Level.INFO,"Loading StardustReactor fuels");
        OnScannedSlimefunItemInstanceListener[] listener = new OnScannedSlimefunItemInstanceListener[1];
        listener[0] = sfItem -> {
                    try {
                        StardustReactor reactor = (StardustReactor) sfItem;
                        Field fuelsField = AbstractGenerator.class.getDeclaredField("fuels");
                        stardustReactorFuels = (List<MachineFuel>) getInUnsafe(reactor,fuelsField);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    unregisterOnScannedSlimefunItemInstanceListener(StardustReactor.class,listener[0]);
                };
        registerOnScannedSlimefunItemInstanceListener(StardustReactor.class, listener[0]);
        logger.log(Level.INFO,"StardustReactor fuels loaded.");
    }
    public static void getCulinaryGeneratorFuels() {
        logger.log(Level.INFO,"Loading CulinaryGenerator fuels");
        OnScannedSlimefunItemInstanceListener[] listener = new OnScannedSlimefunItemInstanceListener[1];
        listener[0] = sfItem -> {
            try {
                CulinaryGenerator reactor = (CulinaryGenerator) sfItem;
                Field fuelsField = AbstractGenerator.class.getDeclaredField("fuels");
                culinaryGeneratorFuels = (List<MachineFuel>) getInUnsafe(reactor,fuelsField);
            }catch (Exception e){
                e.printStackTrace();
            }
            unregisterOnScannedSlimefunItemInstanceListener(CulinaryGenerator.class,listener[0]);
        };
        registerOnScannedSlimefunItemInstanceListener(CulinaryGenerator.class, listener[0]);
        logger.log(Level.INFO,"CulinaryGenerator fuels loaded.");
    }
    public static void getGrowthChambersRecipes() throws Exception{
        logger.log(Level.INFO,"Loading GrowthChambers fuels");
        Field recipesField = AbstractElectricMachine.class.getDeclaredField("recipes");
        Class<? extends AbstractElectricMachine>[] classes = new Class[]{
                GrowthChamber.class, GrowthChamberMK2.class,
                GrowthChamberEnd.class, GrowthChamberEndMK2.class,
                GrowthChamberNether.class, GrowthChamberNetherMK2.class,
                GrowthChamberOcean.class,GrowthChamberOceanMK2.class,
        };
        for (Class<? extends AbstractElectricMachine> c : classes){
            OnScannedSlimefunItemInstanceListener[] listener = new OnScannedSlimefunItemInstanceListener[1];
            listener[0] = sfItem -> {
                try {
                    growthChamberRecipes.put(c, (List<MachineRecipe>) getInUnsafe(sfItem, recipesField));
                }catch (Exception e){
                    e.printStackTrace();
                }
                unregisterOnScannedSlimefunItemInstanceListener(c,listener[0]);
            };
            registerOnScannedSlimefunItemInstanceListener(c, listener[0]);
        }
//        {
//            GrowthChamber chamber = (GrowthChamber) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamber.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
//        {
//            GrowthChamberMK2 chamber = (GrowthChamberMK2) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER_MK2.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamberMK2.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
//        {
//            GrowthChamberEnd chamber = (GrowthChamberEnd) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER_END.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamberEnd.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
//        {
//            GrowthChamberEndMK2 chamber = (GrowthChamberEndMK2) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER_MK2_END.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamberEndMK2.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
//        {
//            GrowthChamberNether chamber = (GrowthChamberNether) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER_NETHER.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamberNether.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
//        {
//            GrowthChamberNetherMK2 chamber = (GrowthChamberNetherMK2) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER_MK2_NETHER.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamberNetherMK2.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
//        {
//            GrowthChamberOcean chamber = (GrowthChamberOcean) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER_OCEAN.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamberOcean.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
//        {
//            GrowthChamberOceanMK2 chamber = (GrowthChamberOceanMK2) SlimefunItem.getById(Items.Keys.GROWTH_CHAMBER_MK2_OCEAN.asSlimefunId());
//            growthChamberRecipes.put(GrowthChamberOceanMK2.class, (List<MachineRecipe>) getInUnsafe(chamber, recipesField));
//        }
        logger.log(Level.INFO,"GrowthChambers fuels loaded.");
    }
}
