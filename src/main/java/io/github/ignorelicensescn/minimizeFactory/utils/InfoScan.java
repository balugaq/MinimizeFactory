package io.github.ignorelicensescn.minimizeFactory.utils;

import dev.j3fftw.litexpansion.machine.generators.AdvancedSolarPanel;
import io.github.acdeasdff.infinityCompress.items.blocks.TweakedGEOQuarry;
import io.github.acdeasdff.infinityCompress.items.blocks.TweakedGEOQuarry_Filter;
import io.github.acdeasdff.infinityCompress.items.blocks.TweakedGenerator;
import io.github.acdeasdff.infinityCompress.items.blocks.TweakedMaterialGenerator;
import io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.*;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeInTicksWithExpectations;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeOutEntity;
import io.github.ignorelicensescn.minimizeFactory.utils.localMachineRecipe.MachineRecipeWithExpectations;
import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.mooy1.infinityexpansion.items.generators.EnergyGenerator;
import io.github.mooy1.infinityexpansion.items.generators.InfinityReactor;
import io.github.mooy1.infinityexpansion.items.machines.*;
import io.github.mooy1.infinityexpansion.infinitylib.machines.AbstractMachineBlock;
import io.github.mooy1.infinityexpansion.infinitylib.machines.MachineBlock;
import io.github.mooy1.infinityexpansion.items.quarries.Quarry;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.generators.SolarGenerator;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricDustWasher;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricGoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities.AbstractEntityAssembler;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.OreWasher;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.NetherGoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.settings.GoldPanDrop;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import io.ncbpfluffybear.fluffymachines.machines.AutoTableSaw;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.profelements.dynatech.items.abstracts.AbstractElectricMachine;
import me.profelements.dynatech.items.electric.generators.CulinaryGenerator;
import me.profelements.dynatech.items.electric.generators.StardustReactor;
import ne.fnfal113.fnamplifications.powergenerators.implementation.CustomPowerGen;
import ne.fnfal113.fnamplifications.powergenerators.implementation.CustomSolarGen;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import sun.misc.Unsafe;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

import static com.google.common.math.IntMath.gcd;
import static dev.j3fftw.litexpansion.Items.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.LiteX.LiteXConsts.*;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.*;
import static io.github.mooy1.infinityexpansion.items.materials.Materials.INFINITE_INGOT;
import static io.github.mooy1.infinityexpansion.items.materials.Materials.VOID_INGOT;

public final class InfoScan {
    public static final long[] emptyEnergyInfo = new long[]{0,1,0};
    public static final int COOLANT_DURATION = 50;
    public static final GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
    public static final GoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(GoldPan.class);
    public static final List<MachineRecipe> emptyMachineRecipes = new ArrayList<>();
    public static final List<MachineRecipeInTicks> emptyMachineRecipesInTicks = new ArrayList<>();
    public static final List<MachineRecipeOutEntity> emptyMachineRecipeOutEntity = new ArrayList<>();
    public static final Set<MachineFuel> emptyMachineFuels = new HashSet<>();

    public static final List<MachineRecipeWithExpectations> emptyMachineRecipesWithExpectations = new ArrayList<>();
    public static long[] findEnergyInfo_AContainer(AContainer sfItem){
        //Power once(J); delay(tick); Capacity(J)
        //if work per tick:Power per tick; delay(tick)=1
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int speed = sfItem.getSpeed();
            energyInfo[1] = speed;
            energyInfo[0] = sfItem.getEnergyConsumption();
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_AGenerator(AGenerator sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getEnergyProduction();
            energyInfo[0] = energyConsumption*(-1);
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_Reactor(Reactor sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getEnergyProduction();
            energyInfo[0] = energyConsumption*(-1);
            energyInfo[1] = sfItem.getCoolant() == null ? -1 : COOLANT_DURATION;
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }

    /**
     * Deprecated is just warn to use initialized instead of abstract
     */
    @Deprecated
    public static long[] findEnergyInfo_InfinityExpansion_AbstractMachineBlock(AbstractMachineBlock sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }

    public static long[] findEnergyInfo_InfinityExpansion_GeoQuarry(GeoQuarry sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            try{energyInfo[1] = getGeoQuarryTicksPerOutput(sfItem);}catch (Exception e){
                e.printStackTrace();
            }
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_GrowingMachine(GrowingMachine sfItem){
        long[] energyInfo = new long[]{0,90,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            try{energyInfo[1] = getGrowingMachineTicksPerOutput(sfItem);}catch (Exception e){e.printStackTrace();}
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_Quarry(Quarry sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_TweakedGeoQuarry(TweakedGEOQuarry sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            try{energyInfo[1] = getTweakedGeoQuarryItemMultiplier(sfItem);}catch (Exception e){
                e.printStackTrace();
            }
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_TweakedGeoQuarry(TweakedGEOQuarry_Filter sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            try{energyInfo[1] = getTweakedGeoQuarryItemMultiplier(sfItem);}catch (Exception e){e.printStackTrace();}
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_MachineBlock(MachineBlock sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_StoneWorksFactory(StoneworksFactory sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_ResourceSynthesizer(ResourceSynthesizer sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }

    public static long[] findEnergyInfo_InfinityExpansion_SingularityConstructor(SingularityConstructor sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int energyConsumption = sfItem.getCapacity()/2;
            energyInfo[0] = energyConsumption;
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityCompress_TweakedGenerator(TweakedGenerator sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            energyInfo[2] = sfItem.getCapacity();
            energyInfo[0] = energyInfo[2] / (-10);
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityCompress_TweakedMaterialGenerator(TweakedMaterialGenerator sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            energyInfo[2] = sfItem.getCapacity();
            energyInfo[0] = energyInfo[2] == 2_147_483_647 ? 1 :
                    (energyInfo[2] == 2100787152 ? 2100787152 : energyInfo[2] / 2);
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityCompress_MaterialGenerator(MaterialGenerator sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            energyInfo[2] = sfItem.getCapacity();
            energyInfo[0] = energyInfo[2] / 2 ;
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_AbstractEntityAssembler(AbstractEntityAssembler sfItem){
        long[] energyInfo = new long[]{0,0,0};
        if (sfItem != null){
            energyInfo[1] = 60;//yes it's not tweakable
            energyInfo[0] = sfItem.getEnergyConsumption();
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_Capacitor(Capacitor sfItem){
        long[] energyInfo = new long[]{0,0,0};
        if (sfItem != null){
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_SolarGenerator(SolarGenerator sfItem){
        //day energy | night energy
        long[] energyInfo = new long[]{0,0,0};
        if (sfItem != null){
            energyInfo[0] = sfItem.getDayEnergy();
            energyInfo[1] = sfItem.getNightEnergy();
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_EnergyGenerator(EnergyGenerator sfItem){
        long[] energyInfo = new long[]{0,0,0};
        if (sfItem.getId().equals(GEOTHERMAL.getItemId())){
            energyInfo = new long[]{GEO_ENERGY,GEO_ENERGY*2L,GEO_ENERGY*100L};
        }
        else if (sfItem.getId().equals(REINFORCED_GEOTHERMAL.getItemId())){
            energyInfo = new long[]{ADVANCED_GEO_ENERGY,ADVANCED_GEO_ENERGY*2L,ADVANCED_GEO_ENERGY*100L};
        }
        else if (sfItem.getId().equals(BASIC_PANEL.getItemId())){
            energyInfo = new long[]{
                    InfinityExpansionConsts.BASIC_SOLAR_ENERGY
                    ,0
                    ,InfinityExpansionConsts.BASIC_SOLAR_ENERGY*100L};
        }
        else if (sfItem.getId().equals(ADVANCED_PANEL.getItemId())){
            energyInfo = new long[]{
                    InfinityExpansionConsts.ADVANCED_SOLAR_ENERGY
                    ,0
                    ,InfinityExpansionConsts.ADVANCED_SOLAR_ENERGY*100L};
        }
        else if (sfItem.getId().equals(CELESTIAL_PANEL.getItemId())){
            energyInfo = new long[]{
                    InfinityExpansionConsts.CELESTIAL_ENERGY
                    ,0
                    ,InfinityExpansionConsts.CELESTIAL_ENERGY * 100L
            };
        }
        else if (sfItem.getId().equals(VOID_PANEL.getItemId())){
            energyInfo = new long[]{
                    InfinityExpansionConsts.VOID_ENERGY
                    ,InfinityExpansionConsts.VOID_ENERGY
                    ,InfinityExpansionConsts.VOID_ENERGY * 100L
            };
        }
        else if (sfItem.getId().equals(INFINITE_PANEL.getItemId())){
            energyInfo = new long[]{
                    InfinityExpansionConsts.INFINITY_ENERGY
                    ,InfinityExpansionConsts.INFINITY_ENERGY
                    ,InfinityExpansionConsts.INFINITY_ENERGY * 100L
            };
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_InfinityExpansion_InfinityReactor(InfinityReactor sfItem){
        return new long[]{
                InfinityExpansionConsts.INFINITY_REACTOR_ENERGY
                ,1
                ,InfinityExpansionConsts.INFINITY_REACTOR_ENERGY * 100L
        };
    }
    public static long[] findEnergyInfo_AbstractElectricMachine(AbstractElectricMachine sfItem){
        //Power once(J); delay(tick); Capacity(J)
        //if work per tick:Power per tick; delay(tick)=1
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            energyInfo[1] = sfItem.getSpeed();
            energyInfo[0] = sfItem.getEnergyConsumption();
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_StardustReactor(StardustReactor sfItem){
        //Power once(J); delay(tick); Capacity(J)
        //if work per tick:Power per tick; delay(tick)=1
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            energyInfo[0] = sfItem.getEnergyProduction() * (-1);
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_FNAmp_CustomSolarGen(CustomSolarGen sfItem){
        return new long[]{sfItem.getDayEnergy() * (-1),sfItem.getNightEnergy() * (-1),sfItem.getCapacity()};
    }
    public static long[] findEnergyInfo_LiteX_AdvancedSolarPanel(AdvancedSolarPanel sfItem){
        if (sfItem.getId().equals(ADVANCED_SOLAR_PANEL.getItemId())){return ADVANCED_RATE;}
        if (sfItem.getId().equals(ULTIMATE_SOLAR_PANEL.getItemId())){return ULTIMATE_RATE;}
        if (sfItem.getId().equals(HYBRID_SOLAR_PANEL.getItemId())){return HYBRID_RATE;}
        return emptyEnergyInfo;
    }
    public static long[] findEnergyInfo_FNAmp_CustomPowerGen(CustomPowerGen sfItem){
        return new long[]{sfItem.getDayRate(),sfItem.getNightRate(),sfItem.getCapacity()};
    }
    public static long[] findEnergyInfo_CulinaryGenerator(CulinaryGenerator sfItem){
        //Power once(J); delay(tick); Capacity(J)
        //if work per tick:Power per tick; delay(tick)=1
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            energyInfo[0] = sfItem.getEnergyProduction() * (-1);
            energyInfo[2] = sfItem.getCapacity();
        }
        return energyInfo;
    }
    //=================================================================================================================
    public static List<MachineRecipe> findRecipes(AContainer sfItem){
        if (sfItem != null){
            return sfItem.getMachineRecipes();
        }else {
            return emptyMachineRecipes;
        }
    }
    public static Set<MachineFuel> findFuels(AGenerator sfItem){
        if (sfItem != null){
            return sfItem.getFuelTypes();
        }else {
            return emptyMachineFuels;
        }
    }
    public static Set<MachineFuel> findFuels_Reactor(Reactor sfItem){
        if (sfItem != null){
            return sfItem.getFuelTypes();
        }else {
            return emptyMachineFuels;
        }
    }
    public static long[] findEnergyInfo_ElectricGoldPan(ElectricGoldPan sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int speed = sfItem.getSpeed();
            energyInfo[2] = sfItem.getCapacity();
            energyInfo[1] = speed;
            energyInfo[0] = sfItem.getEnergyConsumption();
        }
        return energyInfo;
    }
    public static long[] findEnergyInfo_ElectricDustWasher(ElectricDustWasher sfItem){
        long[] energyInfo = new long[]{0,1,0};
        if (sfItem != null){
            int speed = sfItem.getSpeed();
            energyInfo[2] = sfItem.getCapacity();
            energyInfo[1] = speed;
            energyInfo[0] = sfItem.getEnergyConsumption();
        }
        return energyInfo;
    }
    
    private static List<MachineRecipeWithExpectations> Recipes_ElectricDustWasher_List = new ArrayList<>();
    public static void initRecipes_ElectricDustWasher(){
        try {
            OreWasher oreWasher = SlimefunItems.ORE_WASHER.getItem(OreWasher.class);
            Field f = OreWasher.class.getDeclaredField("dusts");
            ItemStack[] dusts = (ItemStack[]) getInUnsafe(oreWasher,f);
            ItemStack[] goldPanWashDustOutput = new ItemStack[dusts.length + 1];
            goldPanWashDustOutput[dusts.length] = SlimefunItems.STONE_CHUNK;
            System.arraycopy(dusts, 0, goldPanWashDustOutput, 0, dusts.length);
            IntegerRational[] expectations = new IntegerRational[goldPanWashDustOutput.length];
            for (int i=0;i<expectations.length - 1;i+=1){
                expectations[i] = new IntegerRational(1,dusts.length);
            }
            expectations[expectations.length - 1] = IntegerRational.ONE;
            MachineRecipeWithExpectations recipe =
                    new MachineRecipeWithExpectations(
                            4,
                            new ItemStack[] { SlimefunItems.SIFTED_ORE },
                            goldPanWashDustOutput,
                            expectations);
            
            Recipes_ElectricDustWasher_List.add(recipe);
            Recipes_ElectricDustWasher_List.add(new MachineRecipeWithExpectations(4, 
                    new ItemStack[] { SlimefunItems.PULVERIZED_ORE }, 
                    new ItemStack[]{ SlimefunItems.PURE_ORE_CLUSTER }, 
                    new IntegerRational[]{IntegerRational.ONE}));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static List<MachineRecipeWithExpectations> findRecipes_ElectricDustWasher(ElectricDustWasher sfItem){
        if (Recipes_ElectricDustWasher_List.isEmpty()){
            initRecipes_ElectricDustWasher();
        }
        return Recipes_ElectricDustWasher_List;
    }

//    public static List<MachineRecipeWithExpectations> eGoldPanRecipes = new ArrayList<>();
//    public static Map<Material,SimplePair<ItemStack[],IntegerRational[]>> eGoldPanRecipeMap = new HashMap<>();
//    static{
//        int denominator = 0;
//        assert goldPan != null;
//        List<GoldPanDrop> drops = new ArrayList<>();
//        for (Object i:goldPan.getItemSettings().toArray(new io.github.thebusybiscuit.slimefun4.api.items.ItemSetting[0])){
//            if (i instanceof GoldPanDrop){
//                drops.add((GoldPanDrop) i);
//            }
//        }
//        for (GoldPanDrop drop:
//                drops
//        ){
//            denominator += drop.getDefaultValue();
//        }
////        SimplePair<ItemStack[],IntegerRational[]> recipeArray = new SimplePair<ItemStack[],IntegerRational[]>();
//        ItemStack[] outs = new ItemStack[drops.size()];
//        IntegerRational[] expectations = new IntegerRational[drops.size()];
//        for (int i=0;
//                i<drops.size();
//                i++
//        ){
//            GoldPanDrop drop = drops.get(i);
//            MachineRecipeWithExpectations recipe = new MachineRecipeWithExpectations(4,
//                    new ItemStack[] { new ItemStack(goldPan.getInputMaterial()) },
//                    new ItemStack[]{ drop.getOutput() },
//                    new IntegerRational[]{new IntegerRational(drop.getDefaultValue(), denominator)});
//            outs[i] = drop.getOutput();
//            expectations[i] = new IntegerRational(drop.getDefaultValue(), denominator);
//            eGoldPanRecipes.add(recipe);
//        }
//        eGoldPanRecipeMap.put(goldPan.getInputMaterial(), new SimplePair<>(outs,expectations));
//        denominator = 0;
//        drops = new ArrayList<>();
//        assert netherGoldPan != null;
//        for (Object i:netherGoldPan.getItemSettings().toArray(new io.github.thebusybiscuit.slimefun4.api.items.ItemSetting[0])){
//            if (i instanceof GoldPanDrop){
//                drops.add((GoldPanDrop) i);
//            }
//        }
//        for (GoldPanDrop drop:
//                drops
//        ){
//            denominator += drop.getDefaultValue();
//        }
//        outs = new ItemStack[drops.size()];
//        expectations = new IntegerRational[drops.size()];
//
//        for (int i=0;
//             i<drops.size();
//             i++
//        ){
//            GoldPanDrop drop = drops.get(i);
//            MachineRecipeWithExpectations recipe = new MachineRecipeWithExpectations(4, new ItemStack[] { new ItemStack(netherGoldPan.getInputMaterial()) },
//                    new ItemStack[]{ drop.getOutput() },
//                    new IntegerRational[]{new IntegerRational(drop.getDefaultValue(), denominator)});
//            outs[i] = drop.getOutput();
//            expectations[i] = new IntegerRational(drop.getDefaultValue(), denominator);
//            eGoldPanRecipes.add(recipe);
//        }
//        eGoldPanRecipeMap.put(netherGoldPan.getInputMaterial(), new SimplePair<>(outs,expectations));
//    }

    private static List<MachineRecipeWithExpectations> eGoldPanRecipes = new ArrayList<>();
    public static List<MachineRecipeWithExpectations> findRecipes_ElectricGoldPan(ElectricGoldPan sfItem){
        if (eGoldPanRecipes.isEmpty()){
            initRecipes_ElectricGoldPan();
        }
        return eGoldPanRecipes;
    }
    public static void initRecipes_ElectricGoldPan(){
        try {
            GoldPan goldPan = SlimefunItems.GOLD_PAN.getItem(GoldPan.class);
            NetherGoldPan netherGoldPan = SlimefunItems.NETHER_GOLD_PAN.getItem(NetherGoldPan.class);
            SimplePair<GoldPan,Integer>[] panPairs = new SimplePair[]{new SimplePair<>(goldPan,3),new SimplePair<>(netherGoldPan,4)};
            Field dropField = GoldPan.class.getDeclaredField("drops");
            for (SimplePair<GoldPan,Integer> panPair:panPairs){
                GoldPan pan = panPair.first;
                int seconds = panPair.second;
                Set<GoldPanDrop> outputs = (Set<GoldPanDrop>) getInUnsafe(pan,dropField);
                ItemStack[] outputStacks = new ItemStack[outputs.size()];
                IntegerRational[] outputExpectations = new IntegerRational[outputs.size()];
                int totalWeight = 0;
                int counter = 0;
                for (GoldPanDrop drop:outputs){
                    outputStacks[counter] = drop.getOutput().clone();
                    outputExpectations[counter] = new IntegerRational(drop.getValue(),1);
                    counter += 1;
                }
                for (int i=0;i<counter;i++){
                    outputExpectations[i] = new IntegerRational(outputExpectations[i].numerator(),totalWeight);
                }
                for (Material input:pan.getInputMaterials()){
                    eGoldPanRecipes.add(new MachineRecipeWithExpectations(
                            seconds,
                            new ItemStack[]{new ItemStack(input)},
                            outputStacks,
                            outputExpectations
                    ));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<MachineRecipeOutEntity> findRecipes_AbstractEntityAssembler(AbstractEntityAssembler sfItem){
        try {
            List<MachineRecipeOutEntity> result = new ArrayList<>();
            ItemStack[] input = new ItemStack[]{
                    sfItem.getBody().clone(),sfItem.getHead().clone()
            };
            result.add(
                    new MachineRecipeOutEntity(
                            30,
                            input,
                            (Class<? extends Entity>) sfItem.getClass().getDeclaredMethod("spawnEntity", Location.class).getReturnType()
                    )
            );
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return emptyMachineRecipeOutEntity;
        }
    }

    public static List<MachineRecipe> findRecipes_TweakedMaterialGenerator(TweakedMaterialGenerator sfItem){
        List<ItemStack> output = sfItem.getDisplayRecipes();
        output.remove(null);
        List<MachineRecipe> result = new ArrayList<>();
        result.add(new MachineRecipeInTicks(1, new ItemStack[0], new ItemStack[]{output.get(0)}));
        return result;
    }

    /**
     This solution could be messy,but once for all
     */
    public static Map<String,Map<String,SimplePair<ItemStack[],IntegerRational[]>>> machineBlockRecipeMapWithExpectation = new HashMap<>();
    public static List<MachineRecipeInTicks> findRecipes_MachineBlock(MachineBlock sfItem){
        try {
            List<MachineRecipeInTicks> result = new ArrayList<>();
            MachineBlockInfo machineBlockInfo1 = getMachineBlockInfo(sfItem);
            for (MachineBlockRecipe machineBlockRecipe:machineBlockInfo1.recipes){
                ItemStack output = machineBlockRecipe.output;
                if (!(output instanceof InfinityExpansion_RandomizedItemStack)){
                    result.add(new MachineRecipeInTicks(machineBlockInfo1.ticksPerOutput, machineBlockRecipe.input, new ItemStack[]{machineBlockRecipe.output}));
                }
                else {
                    int len = ((InfinityExpansion_RandomizedItemStack) output).items.length;
                    ItemStack[] itemStacks = new ItemStack[len];
                    IntegerRational[] expectations = new IntegerRational[len];
                    for (int i=0;i<len;i++){
                        ItemStack itemStack = ((InfinityExpansion_RandomizedItemStack) output).items[i];
                        result.add(
                                new MachineRecipeInTicksWithExpectations(machineBlockInfo1.ticksPerOutput,
                                        machineBlockRecipe.input,
                                        new ItemStack[]{itemStack},
                                        new IntegerRational[]{new IntegerRational(1, len)}));
                        itemStacks[i] = itemStack.clone();
                        expectations[i] = new IntegerRational(
                                1,
                                len);

                    }
                    StringBuilder itemName = new StringBuilder();
                    if (machineBlockRecipe.input.length == 0){
                        itemName.append("NULL");}
                    else {
                        for (ItemStack i:machineBlockRecipe.input){
                            itemName.append(NameUtil.findNameWithAmount(i));
                        }
                    }
                    SimplePair<ItemStack[],IntegerRational[]> pair = new SimplePair<>(itemStacks,expectations);
                    if (!machineBlockRecipeMapWithExpectation.containsKey(sfItem.getId())){
                        Map<String,SimplePair<ItemStack[],IntegerRational[]>> tempMap = new HashMap<>();
                        tempMap.put(itemName.toString(),pair);
                        machineBlockRecipeMapWithExpectation.put(sfItem.getId(),tempMap);
                    }
                    else {
                        machineBlockRecipeMapWithExpectation.get(sfItem.getId()).put(itemName.toString(),pair);
                    }
                }
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return emptyMachineRecipesInTicks;
        }
    }

    public static List<MachineRecipe> findRecipes_MaterialGenerator(MaterialGenerator sfItem){
        List<ItemStack> output = sfItem.getDisplayRecipes();
        output.remove(null);
        List<MachineRecipe> result = new ArrayList<>();
        result.add(new MachineRecipeInTicks(1, new ItemStack[0], new ItemStack[]{output.get(0)}));
        return result;
    }
    public static Set<TweakedMachineFuel> findFuels_InfinityExpansion_InfinityReactor(InfinityReactor sfItem){
        Set<TweakedMachineFuel> result = new HashSet<>();
        int LCM = lcm(INFINITY_INTERVAL,VOID_INTERVAL);
        int infIngotAmount = LCM/INFINITY_INTERVAL;
        int voidIngotAmount = LCM/VOID_INTERVAL;
        ItemStack infIngot = INFINITE_INGOT.clone();
        //we now want a input array like
        //[infIngot*64,infIngot*64,infIngot*64,...infIngot*14,voidIngot*64,voidIngot*64,voidIngot*64,...voidIngot*42]
        //then find it.
        ItemStack[] Ingots = new ItemStack[infIngotAmount / 64 + (infIngotAmount % 64 == 0 ? 0 : 1) + voidIngotAmount / 64 + (voidIngotAmount % 64 == 0 ? 0 : 1)];
        infIngot.setAmount(64);
        for (int i=0;i<infIngotAmount / 64;i++){
            Ingots[i] = infIngot.clone();
        }
        if ((infIngotAmount % 64 == 0 ? 0 : 1) == 1){
            infIngot.setAmount(infIngotAmount % 64);
            Ingots[infIngotAmount / 64] = infIngot.clone();
        }
        ItemStack voidIngot = VOID_INGOT.clone();
        voidIngot.setAmount(64);
        for (int i=infIngotAmount / 64 + (infIngotAmount % 64 == 0 ? 0 : 1);i<Ingots.length - (voidIngotAmount % 64 == 0 ? 0 : 1);i++){
            Ingots[i] = voidIngot.clone();
        }
        if ((voidIngotAmount % 64 == 0 ? 0 : 1) == 1){
            voidIngot.setAmount(voidIngotAmount % 64);
            Ingots[Ingots.length - 1] = voidIngot.clone();
        }
        result.add(new TweakedMachineFuel((LCM/2), Ingots, new ItemStack[0]));
        return result;
    }

    public static List<MachineRecipe> findRecipes_ResourceSynthesizer(ResourceSynthesizer sfItem){
        List<MachineRecipe> result = new ArrayList<>();
        List<ItemStack> showItem = sfItem.getDisplayRecipes();
        for (int i=0;i<showItem.size();i+=4){
            result.add(new MachineRecipeInTicks(1,new ItemStack[]{showItem.get(i),showItem.get(i+2)},new ItemStack[]{showItem.get(i+1)}));
        }
        return result;
    }

    public static List<SimplePair<SingularityRecipe,Double>> findRecipes_SingularityConstructor(SingularityConstructor sfItem){
        List<SimplePair<SingularityRecipe,Double>> result = new ArrayList<>();
        for (SingularityRecipe sr:SINGULARITY_RECIPES){
            result.add(new SimplePair<>(
                    sr,
                    (double)sr.amount / (double)SingularityConstructorSpeed(sfItem.getId())
            ));
        }
        return result;
    }
    public static List<SimplePair<MachineRecipeInTicks, String[]>> findRecipes_StoneworksFactory(StoneworksFactory sfItem){
        return stoneworksFactoryRecipes;
    }
    public static final Map<String,List<MachineRecipe>> autoTableSawRecipes = new HashMap<>();
    public static List<MachineRecipe> findRecipes_AutoTableSaw(AutoTableSaw sfItem){
        if (autoTableSawRecipes.containsKey(sfItem.getId())){return autoTableSawRecipes.get(sfItem.getId());}
        return initAutoTableSawRecipes(sfItem);
    }
    public static List<MachineRecipe> initAutoTableSawRecipes(AutoTableSaw sfItem) {
        List<MachineRecipe> result = new ArrayList<>();
        try {
            Map<ItemStack,ItemStack> recipeMap = (Map<ItemStack, ItemStack>) getInUnsafe(sfItem,sfItem.getClass().getDeclaredField("tableSawRecipes"));
            for (ItemStack itemStack:recipeMap.keySet()){
                result.add(new MachineRecipeInTicks(1,new ItemStack[]{itemStack.clone()},new ItemStack[]{recipeMap.get(itemStack).clone()}));
            }
            autoTableSawRecipes.put(sfItem.getId(),result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public static int lcm(int a, int b)
    {
        return (a / gcd(a, b)) * b;
    }
    /**
     *from Slimefun RC-35
    **/
    public static boolean isBucket(@Nullable ItemStack item) {
        if (item == null) {
            return false;
        }
        ItemStackWrapper wrapper = ItemStackWrapper.wrap(item);
        Material type = wrapper.getType();
        return type.equals(Material.LAVA_BUCKET)
                || type.equals(Material.WATER_BUCKET)
                || type.equals(Material.MILK_BUCKET)
                || type.equals(Material.COD_BUCKET)
                || type.equals(Material.PUFFERFISH_BUCKET)
                || type.equals(Material.TROPICAL_FISH_BUCKET)
                || SlimefunUtils.isItemSimilar(wrapper, SlimefunItems.FUEL_BUCKET, true) 
                || SlimefunUtils.isItemSimilar(wrapper, SlimefunItems.OIL_BUCKET, true);
    }
    public static void tryAddHeadSkin(ItemStack showItem, ItemStack headWithSkinMeta, String name, List<String> lore){
        if (headWithSkinMeta.getType().equals(Material.PLAYER_HEAD)){
            SkullMeta showItemMeta = (SkullMeta) headWithSkinMeta.getItemMeta();
            if (showItemMeta != null){
                showItemMeta = showItemMeta.clone();
                SkullMeta fakeMeta =(SkullMeta)new CustomItemStack(
                        showItem.getType()
                        , name
                        , lore
                ).getItemMeta();
                fakeMeta.setDisplayName(name);
                fakeMeta.setLore(lore);
                showItem.setItemMeta(fakeMeta);
            }
        }
    }
    public static int[] IntegerArrAsIntArr(Integer[] integers){
        int[] result = new int[integers.length];
        for (int i=0;i<integers.length;i++){
            result[i] = integers[i];
        }
        return result;
    }

}
