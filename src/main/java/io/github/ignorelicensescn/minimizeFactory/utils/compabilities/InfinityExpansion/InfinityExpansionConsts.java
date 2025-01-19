package io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion;

import io.github.acdeasdff.infinityCompress.items.blocks.TweakedGEOQuarry;
import io.github.acdeasdff.infinityCompress.items.blocks.TweakedGEOQuarry_Filter;
import io.github.ignorelicensescn.minimizeFactory.utils.ItemStackUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.Approximation;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimpleFour;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimpleTri;
import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.infinitylib.machines.MachineBlock;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.generators.Generators;
import io.github.mooy1.infinityexpansion.items.generators.InfinityReactor;
import io.github.mooy1.infinityexpansion.items.machines.*;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinityexpansion.items.mobdata.MobData;
import io.github.mooy1.infinityexpansion.items.mobdata.MobDataCard;
import io.github.mooy1.infinityexpansion.items.mobdata.MobDataTier;
import io.github.mooy1.infinityexpansion.items.quarries.Oscillator;
import io.github.mooy1.infinityexpansion.items.quarries.Quarry;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.RandomizedSet;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

import static com.google.common.math.IntMath.gcd;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.logger;
import static io.github.ignorelicensescn.minimizeFactory.utils.mathutils.DividingsOperation.sumOfDividings;
import static io.github.mooy1.infinityexpansion.items.blocks.Blocks.INFINITY_FORGE;
import static io.github.mooy1.infinityexpansion.items.machines.Machines.*;

//TODO:rewrite
/**
 * things here would be mad and {@link sun.misc.Unsafe}
 * */
public class InfinityExpansionConsts {
    public static SimplePair<ItemStack[],ItemStack>[] INFINITY_WORKBENCH_RECIPES = new SimplePair[0];
    public static Map<String, SimpleTri<ItemStack[],IntegerRational[],Long>> CARDS_INFO = new HashMap<>();//(outputs,expectations,energyPerTick),ignoresXP
    public static List<SimpleFour<String, ItemStack[],IntegerRational[],Long>> CARDS_INFO_LIST = new ArrayList<>();
    public static double XP_MULTIPLIER = 1.;
    public static int CHAMBER_INTERVAL = 20;
    public static int CHAMBER_BUFFER = 15000;
    public static int CHAMBER_ENERGY = 150;
    public static int INFUSER_ENERGY = 20000;
    public static ItemStack[] emptyItemStackArray = new ItemStack[0];
    public static Material[] emptyMaterialArray = new Material[0];

    public static Class<?> RandomizedItemStackClass = null;
    public static ItemStack airItemStack = new ItemStack(Material.AIR);
    public static final List<SingularityRecipe> SINGULARITY_RECIPES = new ArrayList<>();
    public static int HYDRO_ENERGY = -5;
    public static int ADVANCED_HYDRO_ENERGY = -45;
    public static int GEO_ENERGY = -35;
    public static int ADVANCED_GEO_ENERGY = -210;
    public static int BASIC_SOLAR_ENERGY = -9;
    public static int ADVANCED_SOLAR_ENERGY = -150;
    public static int CELESTIAL_ENERGY = -750;
    public static int VOID_ENERGY = -3000;
    public static int INFINITY_ENERGY = -60000;
    public static int INFINITY_REACTOR_ENERGY = -120_000;

    public static int INFINITY_INTERVAL = 196000;
    public static int VOID_INTERVAL = 32000;
    public static double COST_MULTIPLIER = 1;
    public static boolean ALLOW_NETHER_IN_OVERWORLD = false;
    public static  int INTERVAL = 1;
    public static SimplePair<ItemStack[],ItemStack>[] GEAR_TRANSFORMER_RECIPES = new SimplePair[0];
    /**
     * Map< SlimefunItemID,machineBlockInfo >
     */
    public static Map<String,MachineBlockInfo> machineBlockInfo = new HashMap<>();
    /**
     * < slimefunID, ticksPerOutput} >
     */
    public static Map<String, Integer> geoQuarryInfo = new HashMap<>();
    public static Map<String, Integer> growingMachineInfo = new HashMap<>();
    public static Map<String, SimplePair<ItemStack,ItemStack[]>[]> growingMachineRecipes = new HashMap<>();
    /**
     * slimefunID, < speed, chance, < < material,numerator,denominator >[],Oscillator, environment> >
     */
    public static Map<String, SimpleTri<Integer, Integer, SimpleTri<SimplePair<Material,IntegerRational>[],Oscillator, World.Environment>[]>> quarryInfo = new HashMap<>();
    public static List<SimplePair<MachineRecipeInTicks,String[]>> stoneworksFactoryRecipes = new ArrayList<>();
    public static List<StoneworksFactoryEnumInstance> stoneworksFactoryEnumInstances = new ArrayList<>();
    public static Map<String, Oscillator> Oscillators = new HashMap<>();
    public static Oscillator[] OscillatorArray = new Oscillator[0];
    public static Map<World.Environment,Map<Oscillator,SimplePair<Material[], IntegerRational[]>>> quarryRecipeMap = new HashMap<>();
    public static Map<World.Environment,SimplePair<Material[],IntegerRational[]>> quarryRecipeMap_NoOscillator = new HashMap<>();
    /**
     * slimefunID, ListOf< material,numerator,denominator ,Oscillator, environment> >
     */
    public static Map<String, List<SimpleFour<Material,IntegerRational,Oscillator, World.Environment>>> quarryOutputListMap = new HashMap<>();
    public static Map<String, long[]> voidHarvesterEnergyInfo = new HashMap<>();
    public static int VOID_HARVESTER_TIME = 1024;
    public static long[] getVoidHarvesterEnergyInfo(VoidHarvester sfItem){
        if (voidHarvesterEnergyInfo.containsKey(sfItem.getId())){return voidHarvesterEnergyInfo.get(sfItem.getId());}
        long speed = Long.MAX_VALUE;
        try {
            speed = getIntInUnsafe(sfItem,sfItem.getClass().getDeclaredField("speed"));
        }catch (Exception e){
            e.printStackTrace();
        }
        long[] result = new long[]{sfItem.getCapacity()/2,speed,sfItem.getCapacity()};
        voidHarvesterEnergyInfo.put(sfItem.getId(),result);
        return result;
    }
    public static int SingularityConstructorSpeed(String slimefunItemId){
        if (slimefunItemId.equals(INFINITY_CONSTRUCTOR.getItemId())){
            return 64;
        }else if (slimefunItemId.equals(SINGULARITY_CONSTRUCTOR.getItemId())){
            return 1;
        }else {
            return 1;
        }
    }

    /**
      I paid hours and hours to figure this way out in order to avoid updating frequently,
      finally found that it should be executed after server loaded.
     **/
    public static <T> void getInfinitySingularities() throws Exception {
        logger.log(Level.INFO,"Loading Infinity Expansion Singularities.");
        SingularityConstructor fakeSingularityConstructor = new SingularityConstructor(Groups.INFINITY_CHEAT, INFINITY_CONSTRUCTOR, InfinityWorkbench.TYPE, new ItemStack[] {
                null, Materials.MACHINE_PLATE, Materials.MACHINE_PLATE, Materials.MACHINE_PLATE, Materials.MACHINE_PLATE, null,
                null, Materials.VOID_INGOT, Materials.INFINITE_CIRCUIT, Materials.INFINITE_CIRCUIT, Materials.VOID_INGOT, null,
                null, Materials.VOID_INGOT, SINGULARITY_CONSTRUCTOR, SINGULARITY_CONSTRUCTOR, Materials.VOID_INGOT, null,
                null, Materials.VOID_INGOT, SINGULARITY_CONSTRUCTOR, SINGULARITY_CONSTRUCTOR, Materials.VOID_INGOT, null,
                null, Materials.INFINITE_INGOT, Materials.INFINITE_CORE, Materials.INFINITE_CORE, Materials.INFINITE_INGOT, null,
                Materials.INFINITE_INGOT, Materials.INFINITE_INGOT, Materials.INFINITE_INGOT, Materials.INFINITE_INGOT, Materials.INFINITE_INGOT, Materials.INFINITE_INGOT
        });

        Class<T> RecipeClass = null;
        Field RecipeListField = fakeSingularityConstructor.getClass().getDeclaredField("RECIPE_LIST");
        for (Class<?> checkClass:fakeSingularityConstructor.getClass().getDeclaredClasses()){
            if (checkClass.getSimpleName().equals("Recipe")){
                RecipeClass = (Class<T>) checkClass;
                break;
            }
        }

        VarHandle varHandle = MethodHandles
                .privateLookupIn(fakeSingularityConstructor.getClass(), MethodHandles.lookup())
                .findStaticVarHandle(fakeSingularityConstructor.getClass(),"RECIPE_LIST", List.class);
        varHandle.accessModeType(VarHandle.AccessMode.GET);

        List<T> RecipeList = ((List<T>) getInUnsafe_static(RecipeListField));
        for (T t:RecipeList){
            Field fInput = t.getClass().getDeclaredField("input");
            Field fOutput = t.getClass().getDeclaredField("output");
            Field fAmount = t.getClass().getDeclaredField("amount");
            fInput.setAccessible(true);
            fOutput.setAccessible(true);
            fAmount.setAccessible(true);

            SINGULARITY_RECIPES.add(new SingularityRecipe(
                    ((ItemStack) getInUnsafe(t,fInput)).clone()
                    , ((ItemStack) getInUnsafe(t,fOutput)).clone()
                    , getIntInUnsafe(t,fAmount)));
            fInput.setAccessible(false);
            fOutput.setAccessible(false);
            fAmount.setAccessible(false);
        }
        logger.log(Level.INFO,"Infinity Expansion Singularities loaded.");
    }

    public static <T> MachineBlockInfo getMachineBlockInfo(@Nonnull MachineBlock machineBlock) throws Exception{
        if (!machineBlockInfo.containsKey(machineBlock.getId())){
            Field fRecipes = MachineBlock.class.getDeclaredField("recipes");
            Field fTicksPerOut = MachineBlock.class.getDeclaredField("ticksPerOutput");
            int ticksPerOut = getIntInUnsafe(machineBlock, fTicksPerOut);
            List<T> Recipes = (List<T>) getInUnsafe(machineBlock,fRecipes);
            List<MachineBlockRecipe> machineBlockRecipes = new ArrayList<>();
            for (T t:Recipes){
                Field fOutput = t.getClass().getDeclaredField("output");
                Field fAmount = t.getClass().getDeclaredField("amounts");
                Field fStrings = t.getClass().getDeclaredField("strings");
                ItemStack output = (ItemStack) getInUnsafe(t,fOutput);
                if (RandomizedItemStackClass == null){try{getRandomizedItemStackClass();} catch (Exception e){e.printStackTrace();}}
                if (RandomizedItemStackClass.isInstance(output)){
                    output = new InfinityExpansion_RandomizedItemStack(
                            getItemStacksFromRandomizedItemStack(output)
                    );
                }
                int[] amount = (int[]) getInUnsafe(t,fAmount);
                String[] strings = (String[]) getInUnsafe(t,fStrings);
                machineBlockRecipes.add(new MachineBlockRecipe(new ItemStack[]{output},strings,amount,ticksPerOut, new ItemStack[strings.length]));
            }
            machineBlockInfo.put(machineBlock.getId(),new MachineBlockInfo(ticksPerOut,machineBlockRecipes));
        }

        return machineBlockInfo.get(machineBlock.getId());
    }

    public static int getGeoQuarryTicksPerOutput(@Nonnull GeoQuarry geoQuarry) throws Exception{
        if (!geoQuarryInfo.containsKey(geoQuarry.getId())){
            Field fTicksPerOut = GeoQuarry.class.getDeclaredField("ticksPerOutput");
            fTicksPerOut.setAccessible(true);
            int ticksPerOut = getIntInUnsafe(geoQuarry, fTicksPerOut);
            geoQuarryInfo.put(geoQuarry.getId(),ticksPerOut);
            fTicksPerOut.setAccessible(false);
        }
        return geoQuarryInfo.get(geoQuarry.getId());
    }
    public static int getGrowingMachineTicksPerOutput(@Nonnull GrowingMachine growingMachine) throws Exception{
        if (!growingMachineInfo.containsKey(growingMachine.getId())){
            Field fTicksPerOut = GrowingMachine.class.getDeclaredField("ticksPerOutput");
            fTicksPerOut.setAccessible(true);
            int ticksPerOut = getIntInUnsafe(growingMachine, fTicksPerOut);
            growingMachineInfo.put(growingMachine.getId(),ticksPerOut);
            fTicksPerOut.setAccessible(false);
        }
        return growingMachineInfo.get(growingMachine.getId());
    }
    public static SimplePair<ItemStack,ItemStack[]>[] getGrowingMachineOutput(GrowingMachine growingMachine){
        try {
            if (growingMachine == null){
                return new SimplePair[0];
            }
            if (growingMachineRecipes.containsKey(growingMachine.getId())){return growingMachineRecipes.get(growingMachine.getId());}
            EnumMap<Material,ItemStack[]> recipes = (EnumMap<Material, ItemStack[]>) getInUnsafe(growingMachine,growingMachine.getClass().getDeclaredField("recipes"));
            SimplePair<ItemStack,ItemStack[]>[] result = new SimplePair[recipes.size()];
            int counter = 0;
            for (Map.Entry<Material,ItemStack[]> recipe:recipes.entrySet()){
                result[counter] = new SimplePair<>(new ItemStack(recipe.getKey()),recipe.getValue().clone());
                counter += 1;
            }
            growingMachineRecipes.put(growingMachine.getId(),result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return new SimplePair[0];
        }
    }

    /**
     * speed,chance,materialArray.
     * <p> </p>
     * get material array, calculate Expectation,then reformat
     * <p> </p>
     * holy ****
     */
    public static SimpleTri<Integer, Integer, SimpleTri<SimplePair<Material,IntegerRational>[],Oscillator, World.Environment>[]> quarryInfo(@Nonnull Quarry quarry) throws NoSuchFieldException {
        if (!quarryInfo.containsKey(quarry.getId())){
            Field fSpeed = Quarry.class.getDeclaredField("speed");
            Field fChance = Quarry.class.getDeclaredField("chance");
            Field fMaterial = Quarry.class.getDeclaredField("outputs");
            fSpeed.setAccessible(true);
            fChance.setAccessible(true);
            fMaterial.setAccessible(true);
            int speed = getIntInUnsafe(quarry, fSpeed);
            int chance = getIntInUnsafe(quarry, fChance);
            Material[] materialArray = (Material[]) getInUnsafe(quarry, fMaterial);
            int basicMaterialTriArrSize = materialArray.length + 1;//+ 1 for cobblestone
            int cobbleCount = 1;
            int triArrSize = Oscillators.size() + 1;
            if (!ALLOW_NETHER_IN_OVERWORLD){
                triArrSize *= 2;
                for (Material m:materialArray){
                    if (isMaterialNetherResources(m)){
                        cobbleCount += 1;
                    }
                }
            }

            SimpleTri<SimplePair<Material,IntegerRational>[],Oscillator, World.Environment>[] quarryTirThird = new SimpleTri[triArrSize];
            World.Environment env = World.Environment.NETHER;
            //oscillator
            for (int i=0;i<OscillatorArray.length;i+=1){
                Oscillator o = OscillatorArray[i];

                IntegerRational oscillatorChanceNumeratorAndDenominator = Approximation.find((float) o.chance);
                int numerator = oscillatorChanceNumeratorAndDenominator.numerator();
                int denominator = oscillatorChanceNumeratorAndDenominator.denominator();

                int specificNumerator = 1;
                int specificDenominator = materialArray.length;

                SimplePair<Material,IntegerRational>[] materialTris = new SimplePair[basicMaterialTriArrSize];
                for (int j=0;j<materialArray.length;j+=1){
                    Material m=materialArray[j];
                    if (o.getItem().getType().equals(m)){
                        int num = numerator;
                        int den = (chance) * denominator;
                        int[] diving = sumOfDividings(new int[]{
                                        num,
                                        den
                                },
                                new int[]{
                                specificNumerator*(denominator - numerator),
                                specificDenominator *denominator *chance
                                });
                        num = diving[0];
                        den = diving[1];
                        int GCD = gcd(num,den);
                        num /= GCD;
                        den /= GCD;
                        materialTris[j] = new SimplePair<>(
                                m
                                ,new IntegerRational(num,den)
                        );
                    }else {
                        int num = (denominator - numerator)*specificNumerator;
                        int den = chance * denominator*specificDenominator;
                        int GCD = gcd(num,den);
                        num /= GCD;
                        den /= GCD;
                        materialTris[j] = new SimplePair<>(
                                m
                                ,new IntegerRational(num,den)
                        );
                    }
                }
                materialTris[materialArray.length] = new SimplePair<>(Material.COBBLESTONE,new IntegerRational(chance - 1,chance));
                materialTris = reformatMaterialTris(materialTris);
                
                if (quarryRecipeMap.containsKey(env)){
                    Map<Oscillator, SimplePair<Material[], IntegerRational[]>> newMap = quarryRecipeMap.get(env);
                    if (!newMap.containsKey(o)){
                        Material[] outs = new Material[materialTris.length];
                        IntegerRational[] outExpectations = new IntegerRational[materialTris.length];
                        for (int i2=0;i2<materialTris.length;i2+=1){
                            outs[i2] = materialTris[i2].first;
                            outExpectations[i2] = materialTris[i2].second;
                        }
                        newMap.put(o,new SimplePair<>(outs,outExpectations));
                    }
                }
                else {
                    Map<Oscillator, SimplePair<Material[], IntegerRational[]>> newMap = new HashMap<>();
                    Material[] outs = new Material[materialTris.length];
                    IntegerRational[] outExpectations = new IntegerRational[materialTris.length];
                    for (int i2=0;i2<materialTris.length;i2+=1){
                        outs[i2] = materialTris[i2].first;
                        outExpectations[i2] = materialTris[i2].second;
                    }
                    newMap.put(o,new SimplePair<>(outs,outExpectations));
                    quarryRecipeMap.put(env, newMap);
                }
                
                quarryTirThird[i] = new SimpleTri<>(materialTris,o,env);
            }
            //no oscillator
            {
                int basicNumerator = 1;
                int basicDenominator = (materialArray.length) * (chance);
                SimplePair<Material, IntegerRational>[] materialTris = new SimplePair[materialArray.length + 1];
                for (int j = 0; j < materialArray.length; j+=1) {
                    materialTris[j] = new SimplePair<>(materialArray[j], new IntegerRational(basicNumerator, basicDenominator));
                }
                materialTris[materialArray.length] = new SimplePair<>(Material.COBBLESTONE, new IntegerRational(chance - 1, chance));
                materialTris = reformatMaterialTris(materialTris);

                Material[] outs = new Material[materialTris.length];
                IntegerRational[] outExpectations = new IntegerRational[materialTris.length];
                for (int i2=0;i2<materialTris.length;i2+=1){
                    outs[i2] = materialTris[i2].first;
                    outExpectations[i2] = materialTris[i2].second;
                }
                quarryRecipeMap_NoOscillator.put(env,new SimplePair<>(outs,outExpectations));

                quarryTirThird[OscillatorArray.length] = new SimpleTri<>(materialTris, null, env);
            }
            IntegerRational[] cobbleArr = new IntegerRational[cobbleCount];
            cobbleArr[cobbleCount - 1] = new IntegerRational(chance - 1,chance);
            int cobbleArrIndex = 0;

            if (!ALLOW_NETHER_IN_OVERWORLD){
                env = World.Environment.NORMAL;//not nether
                //oscillator
                for (int i=0;i<OscillatorArray.length;i+=1){
                    Oscillator o = OscillatorArray[i];
                    IntegerRational oscillatorChanceNumeratorAndDenominator = Approximation.find((float) o.chance);
                    int numerator = oscillatorChanceNumeratorAndDenominator.numerator();
                    int denominator = oscillatorChanceNumeratorAndDenominator.denominator();

                    int specificNumerator = 1;
                    int specificDenominator = materialArray.length;

                    SimplePair<Material,IntegerRational>[] materialTris = new SimplePair[materialArray.length + 1 - cobbleCount];
                    int index = 0;
                    for (Material m : materialArray) {
                        if (o.getItem().getType().equals(m)) {
                            IntegerRational diving = new IntegerRational(numerator, (chance) * denominator).add(
                                    new IntegerRational(specificNumerator*(denominator - numerator),
                                            specificDenominator *(chance) * denominator)).simplify();
                            if (!isMaterialNetherResources(m)) {
                                materialTris[index] = new SimplePair<>(m,diving);
                            } else {
                                cobbleArr[cobbleArrIndex] = new IntegerRational(numerator, denominator);
                                cobbleArrIndex += 1;
                                continue;
                            }
                        } else {
                            int num = (denominator - numerator)*specificNumerator;
                            int den = chance * denominator*specificDenominator;
                            int GCD = gcd(num,den);
                            num /= GCD;
                            den /= GCD;
                            if (!isMaterialNetherResources(m)) {
                                materialTris[index] = new SimplePair<>(m, new IntegerRational(num, den));
                            } else {
                                cobbleArr[cobbleArrIndex] = new IntegerRational(num, den);
                                cobbleArrIndex += 1;
                                continue;
                            }
                        }
                        index += 1;
                    }
                    cobbleArrIndex = 0;
                    IntegerRational numAndDen = sumOfDividings(cobbleArr);
                    materialTris[materialTris.length - 1] = new SimplePair<>(Material.COBBLESTONE,numAndDen);
                    materialTris = reformatMaterialTris(materialTris);
                    if (quarryRecipeMap.containsKey(env)){
                        Map<Oscillator, SimplePair<Material[], IntegerRational[]>> newMap = quarryRecipeMap.get(env);
                        if (!newMap.containsKey(o)){
                            Material[] outs = new Material[materialTris.length];
                            IntegerRational[] outExpectations = new IntegerRational[materialTris.length];
                            for (int i2=0;i2<materialTris.length;i2+=1){
                                outs[i2] = materialTris[i2].first;
                                outExpectations[i2] = materialTris[i2].second;
                            }
                            newMap.put(o,new SimplePair<>(outs,outExpectations));
                        }
                    }
                    else {
                        Map<Oscillator, SimplePair<Material[], IntegerRational[]>> newMap = new HashMap<>();
                        Material[] outs = new Material[materialTris.length];
                        IntegerRational[] outExpectations = new IntegerRational[materialTris.length];
                        for (int i2=0;i2<materialTris.length;i2+=1){
                            outs[i2] = materialTris[i2].first;
                            outExpectations[i2] = materialTris[i2].second;
                        }
                        newMap.put(o,new SimplePair<>(outs,outExpectations));
                        quarryRecipeMap.put(env, newMap);
                    }
                    quarryTirThird[OscillatorArray.length + 1 + i] = new SimpleTri<>(materialTris,o,env);
                }
                //no oscillator
                cobbleArr = new IntegerRational[cobbleCount];
                cobbleArr[cobbleCount - 1] = new IntegerRational(chance - 1,chance);
                int basicNumerator = 1;
                int basicDenominator = materialArray.length *  (chance);
                SimplePair<Material,IntegerRational>[] materialTris = new SimplePair[materialArray.length + 1 - cobbleCount];
                int index = 0;
                for (Material material : materialArray) {
                    if (!isMaterialNetherResources(material)) {
                        materialTris[index] = new SimplePair<>(material, new IntegerRational(basicNumerator, basicDenominator));
                        index += 1;
                    }else {
                        cobbleArr[cobbleArrIndex] = new IntegerRational(basicNumerator,basicDenominator);
                        cobbleArrIndex += 1;
                    }
                }
                IntegerRational cobbleDividing = sumOfDividings(cobbleArr);
                materialTris[materialTris.length - 1] = new SimplePair<>(Material.COBBLESTONE, cobbleDividing);
                materialTris = reformatMaterialTris(materialTris);

                Material[] outs = new Material[materialTris.length];
                IntegerRational[] outExpectations = new IntegerRational[materialTris.length];
                for (int i2=0;i2<materialTris.length;i2+=1){
                    outs[i2] = materialTris[i2].first;
                    outExpectations[i2] = materialTris[i2].second;
                }
                quarryRecipeMap_NoOscillator.put(env,new SimplePair<>(outs,outExpectations));

                quarryTirThird[quarryTirThird.length - 1] = new SimpleTri<>(materialTris,null,env);
            }
            fSpeed.setAccessible(false);
            fChance.setAccessible(false);
            fMaterial.setAccessible(false);

            quarryInfo.put(quarry.getId(), new SimpleTri<>(speed,chance,quarryTirThird));

            List<SimpleFour<Material,IntegerRational,Oscillator, World.Environment>> quarryOutList = new ArrayList<>();
            for (SimpleTri<SimplePair<Material,IntegerRational>[],Oscillator, World.Environment> t:quarryTirThird){
                for (SimplePair<Material,IntegerRational> t1: t.first){
                    quarryOutList.add(new SimpleFour<>(t1.first,t1.second,t.second,t.third));
                }
            }
            quarryOutputListMap.put(quarry.getId(),quarryOutList);
        }
        return quarryInfo.get(quarry.getId());
    }

    public static SimplePair<Material,IntegerRational>[] reformatMaterialTris(SimplePair<Material,IntegerRational>[] materialTris){
        HashMap<Material,IntegerRational> materialTable = new HashMap<>();
        for (SimplePair<Material,IntegerRational> mTri:materialTris){
            materialTable.put(mTri.first,materialTable.getOrDefault(mTri.first,IntegerRational.ZERO).add(mTri.second));
        }
        SimplePair<Material,IntegerRational>[] result = new SimplePair[materialTable.keySet().size()];
        int index = 0;
        for (Material m:materialTable.keySet()){
            IntegerRational dividing = materialTable.get(m);
            result[index] = new SimplePair<>(m,dividing);
            index += 1;
        }
        return result;
    }

    public static void getOscillators() throws Exception{
        Field OscillatorMap = Oscillator.class.getDeclaredField("OSCILLATORS");
        OscillatorMap.setAccessible(true);
        Oscillators = (Map<String, Oscillator>) getInUnsafe_static(OscillatorMap);
        OscillatorArray = Oscillators.values().toArray(new Oscillator[0]);
        OscillatorMap.setAccessible(false);
    }
    public static int getTweakedGeoQuarryItemMultiplier(@Nonnull TweakedGEOQuarry geoQuarry) throws Exception{
        if (!geoQuarryInfo.containsKey(geoQuarry.getId())){
            Field fItemMultiplier = TweakedGEOQuarry.class.getDeclaredField("itemMultiplier");
            int itemMultiplier = getIntInUnsafe(geoQuarry, fItemMultiplier);
            geoQuarryInfo.put(geoQuarry.getId(),itemMultiplier);
        }
        return geoQuarryInfo.get(geoQuarry.getId());
    }
    public static int getTweakedGeoQuarryItemMultiplier(@Nonnull TweakedGEOQuarry_Filter geoQuarry) throws Exception{
        if (!geoQuarryInfo.containsKey(geoQuarry.getId())){
            Field fTicksPerOut = TweakedGEOQuarry_Filter.class.getDeclaredField("itemMultiplier");
            int itemMultiplier = getIntInUnsafe(geoQuarry, fTicksPerOut);
            geoQuarryInfo.put(geoQuarry.getId(),itemMultiplier);
        }
        return geoQuarryInfo.get(geoQuarry.getId());
    }
    public static <T> void getStoneworksFactoryRecipes() throws Exception{
        logger.log(Level.INFO,"Loading StoneworksFactory recipes.");
        Class<T> stoneworksEnum = null;
        for (Class<?> cls:StoneworksFactory.class.getDeclaredClasses()){
            if (cls.getSimpleName().equals("Choice")){
                stoneworksEnum = (Class<T>) cls;
                break;
            }
        }
        for (T t:stoneworksEnum.getEnumConstants()){
            Field fIn = t.getClass().getDeclaredField("inputs");
            Field fOut = t.getClass().getDeclaredField("outputs");
            String name = t.toString();
            fIn.setAccessible(true);
            fOut.setAccessible(true);
            Material[] input = (Material[]) getInUnsafe(t,fIn);
            Material[] output = (Material[]) getInUnsafe(t,fOut);
            stoneworksFactoryEnumInstances.add(new StoneworksFactoryEnumInstance(name,input.clone(),output.clone()));
            fIn.setAccessible(false);
            fOut.setAccessible(false);
        }
        for (int i=0;i<stoneworksFactoryEnumInstances.size();i+=1){
            StoneworksFactoryEnumInstance s = stoneworksFactoryEnumInstances.get(i);
            if (s.name().equals("NONE")){
                stoneworksFactoryEnumInstances.remove(i);
                break;
            }
        }

        stoneworksFactoryRecipes.add(new SimplePair<>(new MachineRecipeInTicks(1,emptyItemStackArray,new ItemStack[]{new ItemStack(Material.COBBLESTONE)}),new String[]{"NONE"}));
        for (StoneworksFactoryEnumInstance s1:stoneworksFactoryEnumInstances){
            int ticks;
            for (int i1=0;i1< s1.inputs().length;i1+=1){
                ItemStack input = null;
                ItemStack[] inputArray;
                if (s1.inputs()[i1].equals(Material.COBBLESTONE)){
                    ticks = 2;
                    inputArray = emptyItemStackArray;
                }else {
                    ticks = 1;
                    input = new ItemStack(s1.inputs()[i1]);
                    inputArray = new ItemStack[]{input};
                }
                MachineRecipeInTicks machineRecipe = new MachineRecipeInTicks(
                        ticks
                        ,inputArray
                        ,new ItemStack[]{new ItemStack(s1.outputs()[i1])}
                );
                stoneworksFactoryRecipes.add(new SimplePair<>(machineRecipe,new String[]{s1.name()}));
                for (StoneworksFactoryEnumInstance s2:stoneworksFactoryEnumInstances){
                    for (int i2=0;i2<s2.inputs().length;i2+=1){
                        if (input == null){
                            ticks = 3;
                        }else {
                            ticks = 2;
                        }
                        if (s2.inputs()[i2].equals(s1.outputs()[i1])){
                            machineRecipe = new MachineRecipeInTicks(
                                    ticks
                                    ,inputArray
                                    ,new ItemStack[]{new ItemStack(s2.outputs()[i2])}
                            );
                            stoneworksFactoryRecipes.add(new SimplePair<>(machineRecipe,new String[]{s1.name(),s2.name()}));
                        }else {continue;}
                        for (StoneworksFactoryEnumInstance s3:stoneworksFactoryEnumInstances){
                            if (input == null){
                                ticks = 4;
                            }else {
                                ticks = 3;
                            }
                            for (int i3=0;i3<s3.inputs().length;i3+=1){
                                if (s3.inputs()[i3].equals(s2.outputs()[i2])){
                                    machineRecipe = new MachineRecipeInTicks(
                                            ticks
                                            ,inputArray
                                            ,new ItemStack[]{new ItemStack(s3.outputs()[i3])}
                                    );
                                    stoneworksFactoryRecipes.add(
                                            new SimplePair<>(machineRecipe, 
                                                    new String[]{
                                                            s1.name(),
                                                            s2.name(),
                                                            s3.name(),
                                            }));
                                }
                            }
                        }
                    }
                }
            }
        }
        logger.log(Level.INFO,"StoneworksFactory recipes loaded.");
    }

    public static <T> void getRandomizedItemStackClass(){
        logger.log(Level.INFO,"Loading Infinity Expansion RandomizedStack class.");
        for (Class<?> cls: Machines.class.getDeclaredClasses())
        {
            if (cls.getSimpleName().equals("RandomizedItemStack")){
                RandomizedItemStackClass = cls;
                logger.log(Level.INFO,"Infinity Expansion RandomizedStack class loaded.");
                return;
            }
        }
        logger.log(Level.WARNING,"RandomizedItemStack Class NOT FOUND!");
    }

    public static ItemStack[] getItemStacksFromRandomizedItemStack(ItemStack output) throws Exception{
        Field targetField = null;
        for (Field f:output.getClass().getDeclaredFields()){
            if (f.getName().equals("items")){
                targetField = f;
            }
        }
        ItemStack[] result = null;
        if (targetField != null){
            result = (ItemStack[]) getInUnsafe(output,targetField);
        }
        return result;
    }

    public static void getEnergyConsts() throws Exception{
        logger.log(Level.INFO,"Loading Infinity Expansion energy constants.");
        COST_MULTIPLIER = InfinityExpansion.config().getDouble("balance-options.singularity-cost-multiplier", 0.1, 100);
        ALLOW_NETHER_IN_OVERWORLD = InfinityExpansion.config().getBoolean("quarry-options.output-nether-materials-in-overworld");
        INTERVAL = InfinityExpansion.config().getInt("quarry-options.ticks-per-output", 1, 100);
        XP_MULTIPLIER = InfinityExpansion.config().getDouble("mob-simulation-options.xp-multiplier", 0, 1000);
        CHAMBER_INTERVAL = InfinityExpansion.config().getInt("mob-simulation-options.ticks-per-output", 1, 1000);
        for (String s:new String[]{
                "HYDRO_ENERGY","ADVANCED_HYDRO_ENERGY","GEO_ENERGY","ADVANCED_GEO_ENERGY",
                "BASIC_SOLAR_ENERGY","ADVANCED_SOLAR_ENERGY","CELESTIAL_ENERGY","VOID_ENERGY",
                "INFINITY_ENERGY","INFINITY_REACTOR_ENERGY"
        }){
            Field f = Generators.class.getDeclaredField(s);
            f.setAccessible(true);
            InfinityExpansionConsts.class.getDeclaredField(s).setInt(InfinityExpansionConsts.class,getIntInUnsafe_static(f) * (-1));
            f.setAccessible(false);
        }
        for (String s:new String[]{
                "INFINITY_INTERVAL","VOID_INTERVAL"
        }){
            Field f = InfinityReactor.class.getDeclaredField(s);
            InfinityExpansionConsts.class.getDeclaredField(s).setInt(InfinityExpansionConsts.class,getIntInUnsafe_static(f));
        }
        for (String s:new String[]{
                "CHAMBER_BUFFER","CHAMBER_ENERGY","INFUSER_ENERGY"
        }){
            Field f = MobData.class.getDeclaredField(s);
            InfinityExpansionConsts.class.getDeclaredField(s).setInt(MobData.class,getIntInUnsafe_static(f));
        }
        VOID_HARVESTER_TIME = getIntInUnsafe_static(VoidHarvester.class.getDeclaredField("TIME"));
        Map<String, MobDataCard> CARDS = ((Map<String, MobDataCard>) getInUnsafe_static(MobDataCard.class.getDeclaredField("CARDS")));
        for (String s:CARDS.keySet()){
            MobDataCard card = CARDS.get(s);

            MobDataTier tier = (MobDataTier)getInUnsafe(card,card.getClass().getDeclaredField("tier"));
            int energyPerTick = getIntInUnsafe(tier,tier.getClass().getDeclaredField("energy"));

            RandomizedSet<ItemStack> drops = (RandomizedSet<ItemStack>) getInUnsafe(card,card.getClass().getDeclaredField("drops"));
            Map<ItemStack,Float> dropMap = drops.toMap();
            Set<ItemStack> keys = dropMap.keySet();
            ItemStack[] output = new ItemStack[keys.size()];
            IntegerRational[] expectation = new IntegerRational[keys.size()];
            int counter = 0;
            for (ItemStack i:keys){
                output[counter] = i.clone();
                expectation[counter] = Approximation.find(dropMap.get(i));
                counter += 1;
            }
            CARDS_INFO.put(card.getId(),new SimpleTri<>(output,expectation,(long)energyPerTick));
            CARDS_INFO_LIST.add(new SimpleFour<>(card.getId(),output,expectation,(long)energyPerTick));
        }
        logger.log(Level.INFO,"Infinity Expansion energy constants loaded.");
    }
    public static void initInfinityWorkbenchRecipes() throws NoSuchFieldException {
        InfinityWorkbench fake = new InfinityWorkbench(Groups.MAIN_MATERIALS, INFINITY_FORGE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.VOID_INGOT, Materials.MACHINE_PLATE, Materials.VOID_INGOT,
                SlimefunItems.ENERGIZED_CAPACITOR, new ItemStack(Material.CRAFTING_TABLE), SlimefunItems.ENERGIZED_CAPACITOR,
                Materials.VOID_INGOT, Materials.MACHINE_PLATE, Materials.VOID_INGOT
        }, 10000000);
        Field f = fake.getClass().getSuperclass().getDeclaredField("recipes");
        List<io.github.mooy1.infinityexpansion.infinitylib.machines.CraftingBlockRecipe> recipes = (List<io.github.mooy1.infinityexpansion.infinitylib.machines.CraftingBlockRecipe>) getInUnsafe(fake,f);
        INFINITY_WORKBENCH_RECIPES = new SimplePair[recipes.size()];
        for (int i=0;i<recipes.size();i+=1){
            ItemStack[] inputs;
            try {
                inputs = (ItemStack[]) getInUnsafe(recipes.get(i),recipes.get(i).getClass().getDeclaredField("recipe"));
            }catch (NoSuchFieldException nof){
                inputs = (ItemStack[]) getInUnsafe(recipes.get(i),recipes.get(i).getClass().getDeclaredField("inputs"));
            }
            ItemStack[] inputsClone = new ItemStack[inputs.length];
            for (int j=0;j<inputs.length;j+=1){
                if (inputs[j] != null && inputs[j].getType() != Material.AIR){
                    inputsClone[j] = new ItemStack(inputs[j].getType());
                    if (inputs[j].hasItemMeta()) {
                        inputsClone[j].setItemMeta(inputs[j].getItemMeta());
                    }
                }
            }
            ItemStack output = (ItemStack) getInUnsafe(recipes.get(i),recipes.get(i).getClass().getDeclaredField("output"));
            INFINITY_WORKBENCH_RECIPES[i] = new SimplePair<>(ItemStackUtil.collapseItems(inputsClone),output);
        }
    }
    public static void initGearTransformerRecipes() throws Exception{
        logger.log(Level.INFO,"Loading GearTransformer recipes");
        GearTransformer fake = new GearTransformer(Groups.ADVANCED_MACHINES, GEAR_TRANSFORMER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL_PLATE,
                Materials.MACHINE_CIRCUIT, new ItemStack(Material.SMITHING_TABLE), Materials.MACHINE_CIRCUIT,
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL_PLATE
        }, 12000);
        ItemStack[] toolRecipes = (ItemStack[]) getInUnsafe_static(fake.getClass().getDeclaredField("TOOL_RECIPE"));
        ItemStack[] armorRecipes = (ItemStack[]) getInUnsafe_static(fake.getClass().getDeclaredField("ARMOR_RECIPE"));
        String[] armorTypes = (String[]) getInUnsafe_static(fake.getClass().getDeclaredField("ARMOR_TYPES"));
        String[] toolTypes = (String[]) getInUnsafe_static(fake.getClass().getDeclaredField("TOOL_TYPES"));
        String[] armorMaterials = (String[]) getInUnsafe_static(fake.getClass().getDeclaredField("ARMOR_MATERIALS"));
        String[] toolMaterials = (String[]) getInUnsafe_static(fake.getClass().getDeclaredField("TOOL_MATERIALS"));
        int armorCount = armorTypes.length*armorMaterials.length;
        int toolCount = toolTypes.length*toolMaterials.length;
        int armorRecipeCount = armorCount * armorMaterials.length - armorCount;
        int toolRecipeCount = toolCount * toolMaterials.length - toolCount;
        GEAR_TRANSFORMER_RECIPES = new SimplePair[toolRecipeCount + armorRecipeCount];
        int counter = 0;
        for (String type:armorTypes){
            for (String material:armorMaterials){
                for (int i=0;i<armorMaterials.length;i+=1){
                    String inMaterial=armorMaterials[i];
                    if (material.equals(inMaterial)){continue;}
                    GEAR_TRANSFORMER_RECIPES[counter] = new SimplePair<>(new ItemStack[]{
                            new ItemStack(Material.getMaterial(  material + type)),armorRecipes[i].clone()}
                            ,new ItemStack(Material.getMaterial(inMaterial + type))
                    );
                    counter += 1;
                }
            }
        }
        for (String type:toolTypes){
            for (String material:toolMaterials){
                for (int i=0;i<toolMaterials.length;i+=1){
                    String inMaterial=toolMaterials[i];
                    if (material.equals(inMaterial)){continue;}
                    GEAR_TRANSFORMER_RECIPES[counter] = new SimplePair<>(new ItemStack[]{
                            new ItemStack(Material.getMaterial(  material + type)),toolRecipes[i].clone()}
                            ,new ItemStack(Material.getMaterial(inMaterial + type))
                    );
                    counter += 1;
                }
            }
        }
        logger.log(Level.INFO,"GearTransformer recipes loaded.");
    }

    public static boolean isMaterialNetherResources(Material m){
        return m.equals(Material.NETHERRACK) || m.equals(Material.QUARTZ) || m.equals(Material.NETHERITE_INGOT);
    }

    static Unsafe unsafe;
    static {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //maybe i need to use native methods to extract the fields.
    public static Object getInUnsafe_static(Field field) {
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);
        return unsafe.getObject(fieldBase, fieldOffset);
    }
    public static Object getInUnsafe(Object o,Field field) {
        long fieldOffset = unsafe.objectFieldOffset(field);
        return unsafe.getObject(o, fieldOffset);
    }
    public static int getIntInUnsafe(Object o,Field field) {
        long fieldOffset = unsafe.objectFieldOffset(field);
        return unsafe.getInt(o, fieldOffset);
    }

    public static int getIntInUnsafe_static(Field field) {
        Object fieldBase = unsafe.staticFieldBase(field);
        long fieldOffset = unsafe.staticFieldOffset(field);
        return unsafe.getInt(fieldBase, fieldOffset);
    }

    public static Object createNew(Class<?> cls) throws Exception{
        return unsafe.allocateInstance(cls);
    }
}
