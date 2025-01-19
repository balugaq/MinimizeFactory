package io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion;

import io.github.ignorelicensescn.minimizeFactory.utils.records.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.NameUtil;
import io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.TweakedMachineFuel;
import io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityCompress.InfinityCompressSerializedMachineRecipes;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.MachineRecipeInTicksWithExpectations;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimpleFour;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimpleTri;
import io.github.mooy1.infinityexpansion.infinitylib.machines.MachineBlock;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.generators.EnergyGenerator;
import io.github.mooy1.infinityexpansion.items.generators.InfinityReactor;
import io.github.mooy1.infinityexpansion.items.machines.*;
import io.github.mooy1.infinityexpansion.items.mobdata.MobSimulationChamber;
import io.github.mooy1.infinityexpansion.items.quarries.Oscillator;
import io.github.mooy1.infinityexpansion.items.quarries.Quarry;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static io.github.acdeasdff.infinityCompress.items.blocks.Blocks.INFINITY_INFINITE_PANEL;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.InfoScan.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.Slimefun.SlimefunConsts.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.*;
import static io.github.mooy1.infinityexpansion.items.generators.Generators.*;
import static io.github.mooy1.infinityexpansion.items.materials.Materials.VOID_BIT;

public class InfinityExpansionSerializedMachineRecipes {
    private static boolean initFlag = false;
    public static void init(){
        if (initFlag){return;}
        if (InfinityExpansionFlag){
            initFlag = true;
            registerSerializedRecipeProvider_byClassName(MaterialGenerator.class.getName(),
                    new SerializedRecipeProvider<MaterialGenerator>(){

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable MaterialGenerator m) {
                            if (m == null){return new ArrayList<>();}
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> recipes = new ArrayList<>();
                            for (MachineRecipe r:findRecipes_MaterialGenerator(m)){
                                recipes.add(
                                        new SimplePair<>(
                                                new SerializedMachine_MachineRecipe(
                                                        m.getItem(),
                                                        r,
                                                        findEnergyInfo_InfinityCompress_MaterialGenerator(m)[0]
                                                ),
                                                null
                                        )
                                );
                            }
                            return recipes;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull MaterialGenerator m) {
                            long[] energyInfo = findEnergyInfo_InfinityCompress_MaterialGenerator(m);
                            return new String[]{properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                    + energyInfo[0]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")};
                        }
                    });
            registerSerializedRecipeProvider_byClassName(ResourceSynthesizer.class.getName(),
                    new SerializedRecipeProvider<ResourceSynthesizer>(){

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable ResourceSynthesizer m) {
                            if (m == null){return new ArrayList<>();}
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> recipes = new ArrayList<>();
                            for (MachineRecipe r:findRecipes_ResourceSynthesizer(m)){
                                recipes.add(
                                        new SimplePair<>(
                                                new SerializedMachine_MachineRecipe(
                                                        m.getItem(),
                                                        r,
                                                        findEnergyInfo_InfinityExpansion_ResourceSynthesizer(m)[0]
                                                ),
                                                null
                                        )
                                );
                            }
                            return recipes;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull ResourceSynthesizer m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_ResourceSynthesizer(m);
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            registerSerializedRecipeProvider_byClassName(SingularityConstructor.class.getName(),
                    new SerializedRecipeProvider<SingularityConstructor>() {

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable SingularityConstructor m)
                        {
                            if (m == null){return new ArrayList<>();}
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> recipes = new ArrayList<>();
                            for (SimplePair<SingularityRecipe,Double> r:findRecipes_SingularityConstructor(m)){
                                recipes.add(
                                        new SimplePair<>(
                                                new SerializedMachine_MachineRecipe(
                                                        m.getItem(),
                                                        r.first,
                                                        r.second == null
                                                                ?0
                                                                :r.second.intValue(),
                                                        findEnergyInfo_InfinityExpansion_SingularityConstructor(m)[0],
                                                        1),
                                                null
                                        )
                                );
                            }
                            return recipes;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull SingularityConstructor m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_SingularityConstructor(m);
                            return new String[]{properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                    + energyInfo[0]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            registerSerializedRecipeProvider_byClassName(MachineBlock.class.getName(),
                    new SerializedRecipeProvider<MachineBlock>() {

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable MachineBlock m) {
                            if (m == null){return new ArrayList<>();}
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> recipes = new ArrayList<>();
                            for (MachineRecipeInTicks r:findRecipes_MachineBlock(m)){
                                SerializedMachine_MachineRecipe serialized;
                                if (r instanceof MachineRecipeInTicksWithExpectations){
                                    StringBuilder inputName = new StringBuilder();

                                    if (r.getInput().length == 0){
                                        inputName.append("NULL");}
                                    else {
                                        for (ItemStack i1:r.getInput()) {
                                            inputName.append(NameUtil.findNameWithAmount(i1));
                                        }
                                    }

                                    SimplePair<ItemStack[],IntegerRational[]> pair = machineBlockRecipeMapWithExpectation.get(m.getId()).get(inputName.toString());
                                    serialized = new SerializedMachine_MachineRecipe(
                                            m.getItem(),
                                            new MachineRecipeInTicks(
                                                    r.getTicks(),
                                                    r.getInput(),
                                                    pair.first)
                                            , findEnergyInfo_InfinityExpansion_MachineBlock(m)[0]
                                            , 1
                                            ,pair.second);
                                }else {
                                    serialized = new SerializedMachine_MachineRecipe(
                                            m.getItem(),
                                            r,
                                            findEnergyInfo_InfinityExpansion_MachineBlock(m)[0]
                                    );
                                }
                                recipes.add(new SimplePair<>(serialized,null));
                            }
                            return recipes;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull MachineBlock m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_MachineBlock(m);
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                            + energyInfo[1]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            registerSerializedRecipeProvider_byClassName(GeoQuarry.class.getName(),
                    new SerializedRecipeProvider<GeoQuarry>() {
                        @Override
                        @Nonnull
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable GeoQuarry m) {
                            if (m == null){return new ArrayList<>();}
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_GeoQuarry(m);
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> recipes = new ArrayList<>();
                            for (SimplePair<BiomeAndEnvironment,SimplePair<ItemStack[],IntegerRational[]>> r
                                    :geoMinerResourcesInfo_BiomeAndEnvironmentKey_List){
                                BiomeAndEnvironment bioAndEnv = r.first;
                                SimplePair<ItemStack[],IntegerRational[]> outputPair = r.second;
                                SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                        m.getItem(),
                                        new MachineRecipeInTicks(
                                                (int) energyInfo[1],
                                                null,
                                                outputPair.first),
                                        energyInfo[0],
                                        1,
                                        outputPair.second
                                );
                                serialized.env = bioAndEnv.environment();
                                serialized.biome = bioAndEnv.biome();
                                recipes.add(new SimplePair<>(serialized,null));
                            }
                            return recipes;
                        }

//                        @Override
//                        public SimplePair<String,List<String>> getNameAndLoreForRecipe(@Nullable GeoQuarry m, SimplePair<SerializedMachine_MachineRecipe,ItemStack> recipe,int index)
//                        {
//                            long[] energyInfo = findEnergyInfo_InfinityExpansion_GeoQuarry(m);
//                            int ticksPerOutput = (int) energyInfo[1];
//
//                            assert recipe.first.outputs != null;
//                            SimplePair<BiomeAndEnvironment,
//                                            SimplePair<ItemStack[], IntegerRational[]>> result
//                                    =  geoMinerResourcesInfo_BiomeAndEnvironmentKey_List.get(index);
//
//                            List<String> lore = new ArrayList<>();
//                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
//                            ItemStack showItemTemplate = recipe.first.outputs[0];
//                            lore.add(ChatColor.WHITE + NameUtil.findName(showItemTemplate));
//
//                            lore.add(ChatColor.GREEN
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_MainWorld") + " "
//                                    + ChatColor.RED
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_Nether") + " "
//                                    + ChatColor.LIGHT_PURPLE
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_End") + " "
//                                    + ChatColor.GRAY
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CUSTOM")
//                            );
//                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_LocationAndExpectation"));
//                            String[] locations = new String[canBeFoundAt.size() / GEOMINER_BIOME_EVERY_LINE
//                                    + (canBeFoundAt.size() % GEOMINER_BIOME_EVERY_LINE == 0 ? 0 : 1)];
//                            Arrays.fill(locations, "");
//                            for (int j=0;j<canBeFoundAt.size();j+=1){
//                                SimplePair<BiomeAndEnvironment, IntegerRational> tri = canBeFoundAt.get(j);
//                                StringBuilder sb = new StringBuilder();
//                                if (tri.first.environment().equals(World.Environment.NORMAL)){
//                                    sb.append(ChatColor.GREEN);
//                                }
//                                else if (tri.first.environment().equals(World.Environment.NETHER)){
//                                    sb.append(ChatColor.RED);
//                                }
//                                else if (tri.first.environment().equals(World.Environment.THE_END)){
//                                    sb.append(ChatColor.LIGHT_PURPLE);
//                                }
//                                else{
//                                    sb.append(ChatColor.GRAY);
//                                }
//                                sb.append(NameUtil.nameForBiome(tri.first.biome()))
//                                        .append(" ")
//                                        .append(properties.getReplacedProperty("Test_InfoProvider_Info_ExpectationColor"))
//                                        .append(tri.second);
//                                locations[j / GEOMINER_BIOME_EVERY_LINE] += sb.toString();
//                            }
//                            lore.addAll(List.of(locations));
//                            String name = properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
//                                    + (ticksPerOutput)
//                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit");
//                            return new SimplePair<>(name,lore);
//                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull GeoQuarry m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_GeoQuarry(m);
                            return new String[]{
                                properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                        + energyInfo[0]
                                        + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                        properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                                + energyInfo[1]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                                + energyInfo[2]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")

                            };
                        }
                    });
            registerSerializedRecipeProvider_byClassName(Quarry.class.getName(),
                    new SerializedRecipeProvider<Quarry>() {
                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull Quarry m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_Quarry(m);
                            String speedStr = properties.getReplacedProperty("Test_InfoProvider_Info_Speed_CANNOT_GET");
                            try {
                                SimpleTri<Integer,Integer, SimpleTri<SimplePair<Material,IntegerRational>[], Oscillator, World.Environment>[]> info = quarryInfo(m);
                                speedStr = String.valueOf(info.first);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_Speed")
                                            + speedStr
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Speed_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable Quarry m) {
                            if (m == null){return new ArrayList<>();}
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_Quarry(m);
                            SimpleTri<Integer,Integer, SimpleTri<SimplePair<Material,IntegerRational>[], Oscillator, World.Environment>[]> info = null;
                            try {
                                info = quarryInfo(m);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            int speed = info == null
                                    ?1
                                    :info.first;
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> recipes = new ArrayList<>();
                            for (Map.Entry<World.Environment, Map<Oscillator, SimplePair<Material[], IntegerRational[]>>> oreInWorld:quarryRecipeMap.entrySet()){
                                for (Map.Entry<Oscillator, SimplePair<Material[], IntegerRational[]>> oresWithOscillator:oreInWorld.getValue().entrySet()){
                                    SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                            m.getItem(),
                                            new MachineRecipeInTicks(INTERVAL, emptyItemStackArray,
                                                    MachineRecipeInTicks.material2Stack(oresWithOscillator.getValue().first, speed)),
                                            oreInWorld.getKey(),
                                            energyInfo[0]
                                            , 1
                                            , oresWithOscillator.getValue().second);
                                    Oscillator oscillator = oresWithOscillator.getKey();
                                    recipes.add(new SimplePair<>(serialized,oscillator==null?null:oscillator.getItem()));
                                }
                            }for (Map.Entry<World.Environment,  SimplePair<Material[], IntegerRational[]>> oreInWorld:quarryRecipeMap_NoOscillator.entrySet()){
                                SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                        m.getItem(),
                                        new MachineRecipeInTicks(INTERVAL, emptyItemStackArray,
                                                MachineRecipeInTicks.material2Stack(oreInWorld.getValue().first, speed)),
                                        oreInWorld.getKey(),
                                        energyInfo[0]
                                        , 1
                                        , oreInWorld.getValue().second);
                                recipes.add(new SimplePair<>(serialized,null));
                            }
                            return recipes;
                        }

                        @Override
                        public Material getShowingMaterial(@Nullable Quarry m, SimplePair<SerializedMachine_MachineRecipe, ItemStack> serialized, int index) {
                            if (serialized.second != null){
                                return serialized.second.getType();
                            }
                            if (Objects.equals(serialized.first.env, World.Environment.NETHER)){
                                return Material.NETHERITE_PICKAXE;
                            }
                            return Material.DIAMOND_PICKAXE;
                        }

                        @Override
                        public SimplePair<String,List<String>> getNameAndLoreForRecipe(@Nullable Quarry m, SimplePair<SerializedMachine_MachineRecipe,ItemStack> serializedPair,int index)
                        {
                            if (m == null){return null;}
                            SerializedMachine_MachineRecipe serialized = serializedPair.first;
                            SimpleTri<Integer,Integer, SimpleTri<SimplePair<Material,IntegerRational>[], Oscillator, World.Environment>[]> info = null;
                            try {
                                info = quarryInfo(m);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            int speed = info == null ?1 :info.first;
//                            SimpleFour<Material,IntegerRational, Oscillator, World.Environment> recipe = quarryOutputListMap.get(m.getId()).get(index);
                            ChatColor colorForNetherExpectation =
                                    !ALLOW_NETHER_IN_OVERWORLD
                                            ? ChatColor.RED
                                            : ChatColor.GRAY;
                            ChatColor colorForOtherExpectation = ChatColor.GRAY;
                            List<String> lore = new ArrayList<>();
                            lore.add(
                                    colorForNetherExpectation
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Nether") + " "
                                            + colorForOtherExpectation
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_Other") + " "
                            );
                            ChatColor colorForOutput = Objects.equals(serialized.env,World.Environment.NETHER)?colorForNetherExpectation:colorForOtherExpectation;
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Oscillator_Material"));
                            ItemStack oscillator = serializedPair.second;
                            lore.add(oscillator == null
                                            ?properties.getReplacedProperty("Test_InfoProvider_Info_NoItem")
                                            :NameUtil.nameForMaterial(oscillator.getType()));

                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));

                            int counter = 0;
                            assert serialized.outputs != null;
                            for (ItemStack out:serialized.outputs){
                                assert serialized.outputExpectations != null;
                                lore.add(NameUtil.nameForMaterial(out.getType()) + " "
                                        + properties.getReplacedProperty("Test_InfoProvider_Info_Output_Unit")
                                        + speed
                                        + " "
                                        + colorForOutput
                                        + properties.getReplacedProperty("Test_InfoProvider_Info_Recipe_Expectation")
                                        + serialized.outputExpectations[counter]
                                );
                                counter += 1;
                            }
                            return new SimplePair<>(properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                    + INTERVAL
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit")
                                    ,lore);
                        }
                    });
            registerSerializedRecipeProvider_byClassName(StoneworksFactory.class.getName(),
                    new SerializedRecipeProvider<StoneworksFactory>(){
                        @Nonnull
                        @Override
                        public SimplePair<String, List<String>> getNameAndLoreForRecipe(@Nullable StoneworksFactory m, SimplePair<SerializedMachine_MachineRecipe,ItemStack> serializedMachineMachineRecipe, int index) {
                            SimplePair<MachineRecipeInTicks, String[]> recipePair = findRecipes_StoneworksFactory(m).get(index);
                            MachineRecipeInTicks recipe = recipePair.first;
                            List<String> lore = new ArrayList<>();
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Input"));
                            for (ItemStack itemStack:recipe.getInput()){
                                lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(itemStack));
                            }
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
                            for (ItemStack itemStack:recipe.getOutput()){
                                lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(itemStack));
                            }
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_StoneworksFactory_Steps"));
                            for (String s:recipePair.second){
                                lore.add(ChatColor.GRAY + s);
                            }
                            return new SimplePair<>(properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                    + recipe.getTicks()
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                                    lore);
                        }

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable StoneworksFactory m) {
                            if (m == null){return new ArrayList<>();}
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_StoneWorksFactory(m);
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> recipes = new ArrayList<>();
                            List<SimplePair<MachineRecipeInTicks, String[]>> stoneworksFactoryRecipes = findRecipes_StoneworksFactory(m);
                            for (SimplePair<MachineRecipeInTicks, String[]> rPair:stoneworksFactoryRecipes){
                                SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(m.getItem(),rPair.first, energyInfo[0]);
                                recipes.add(new SimplePair<>(serialized, null));
                            }
                            return recipes;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull StoneworksFactory m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_StoneWorksFactory(m);
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                            + energyInfo[1]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            registerSerializedRecipeProvider_byClassName(VoidHarvester.class.getName(),
                    new SerializedRecipeProvider<VoidHarvester>() {
                @Nonnull
                @Override
                public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable VoidHarvester m) {
                    if (m == null){return Collections.emptyList();}
                    long[] energyInfo = getVoidHarvesterEnergyInfo(m);
                    SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                            m.getItem()
                            ,new MachineRecipeInTicks((int) (VOID_HARVESTER_TIME / energyInfo[1]),new ItemStack[0], new ItemStack[]{VOID_BIT}
                    ),energyInfo[0]);
                    return Collections.singletonList(new SimplePair<>(serialized,VOID_BIT));
                }

                @Nonnull
                @Override
                public String[] getEnergyInfoStrings(@Nonnull VoidHarvester m) {
                    long[] energyInfo = getVoidHarvesterEnergyInfo(m);
                    return new String[]{properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                            + energyInfo[0]
                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                            properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                    + (energyInfo[1] != Long.MAX_VALUE
                                    ? String.valueOf((VOID_HARVESTER_TIME / energyInfo[1]))
                                    : properties.getReplacedProperty("Test_InfoProvider_Info_Speed_CANNOT_GET"))
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                            properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                    + energyInfo[2]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                    };
                }
            });
            registerSerializedRecipeProvider_byClassName(GrowingMachine.class.getName(),
                    new SerializedRecipeProvider<GrowingMachine>() {
                        @Nullable
                        @Override
                        public SimplePair<String, List<String>> getNameAndLoreForRecipe(@Nullable GrowingMachine m, SimplePair<SerializedMachine_MachineRecipe,ItemStack> serializedMachineMachineRecipe, int index) {
                            if (m == null){return null;}
                            SimplePair<ItemStack,ItemStack[]>[] recipes = getGrowingMachineOutput(m);
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_GrowingMachine(m);
                            SimplePair<ItemStack,ItemStack[]> recipe = recipes[index];
                            List<String> lore = new ArrayList<>();
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Seed"));
                            lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(recipe.first));
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
                            for (ItemStack itemStack:recipe.second){
                                if (itemStack != null && !itemStack.getType().equals(Material.AIR)){
                                    lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(itemStack));
                                }else{
                                    lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_NoItem"));
                                }
                            }
                            String name = properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                    + (int) energyInfo[1]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit");
                            return new SimplePair<>(name,lore);
                        }

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable GrowingMachine m) {
                            if (m == null){return Collections.emptyList();}
                            SimplePair<ItemStack,ItemStack[]>[] recipes = getGrowingMachineOutput(m);
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
                            for (var recipe:recipes){
                                long[] energyInfo = findEnergyInfo_InfinityExpansion_GrowingMachine(m);
                                MachineRecipeInTicks machineRecipeInTicks =
                                        new MachineRecipeInTicks(
                                                (int) energyInfo[1],
                                                new ItemStack[]{recipe.first},
                                                recipe.second);
                                SerializedMachine_MachineRecipe serialized =
                                        new SerializedMachine_MachineRecipe(
                                                m.getItem(),
                                                machineRecipeInTicks,
                                                energyInfo[0]
                                        );
                                result.add(new SimplePair<>(serialized,null));
                            }
                            return result;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull GrowingMachine m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_GrowingMachine(m);
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                    + energyInfo[0]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                            + (VOID_HARVESTER_TIME / energyInfo[1])
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            registerSerializedRecipeProvider_byClassName(EnergyGenerator.class.getName(),
                    new SerializedRecipeProvider<EnergyGenerator>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable EnergyGenerator m) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_EnergyGenerator(m);
                            String id = m.getId();
                            if (Objects.equals(GEOTHERMAL.getItemId(),id) || Objects.equals(REINFORCED_GEOTHERMAL.getItemId(),id)){
                                List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
                                SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe();
                                serialized.sfItem = m;
                                serialized.sfItemStack = m.getItem();
                                serialized.energyPerTick = energyInfo[1];
                                serialized.env = World.Environment.NETHER;
                                result.add(new SimplePair<>(serialized,null));
                                serialized = new SerializedMachine_MachineRecipe();
                                serialized.sfItem = m;
                                serialized.sfItemStack = m.getItem();
                                serialized.energyPerTick = energyInfo[0];
                                serialized.env = World.Environment.NORMAL;
                                result.add(new SimplePair<>(serialized,null));
                                serialized = new SerializedMachine_MachineRecipe();
                                serialized.sfItem = m;
                                serialized.sfItemStack = m.getItem();
                                serialized.energyPerTick = energyInfo[0];
                                serialized.env = World.Environment.THE_END;
                                result.add(new SimplePair<>(serialized,null));
                                serialized = new SerializedMachine_MachineRecipe();
                                serialized.sfItem = m;
                                serialized.sfItemStack = m.getItem();
                                serialized.energyPerTick = energyInfo[0];
                                serialized.env = World.Environment.CUSTOM;
                                result.add(new SimplePair<>(serialized,null));
                                return result;
//                                serialized.energyPerTickAtNight = energyInfo[1];
                            }else if(
                                    Objects.equals(BASIC_PANEL.getItemId(),id)
                                            || Objects.equals(ADVANCED_PANEL.getItemId(),id)
                                            || Objects.equals(CELESTIAL_PANEL.getItemId(),id)
                                            || Objects.equals(VOID_PANEL.getItemId(),id)
                                            || Objects.equals(INFINITE_PANEL.getItemId(),id)
                            ){

                                SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe();
                                serialized.sfItem = m;
                                serialized.sfItemStack = m.getItem();
                                serialized.energyPerTick = energyInfo[0];
                                serialized.energyPerTickAtNight = energyInfo[1];
                                return Collections.singletonList(new SimplePair<>(serialized,null));
                            }
                            else if(InfinityCompressFlag){
                                if (Objects.equals(INFINITY_INFINITE_PANEL.getItemId(),id)){
                                    SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe();
                                    serialized.sfItem = m;
                                    serialized.sfItemStack = m.getItem();
                                    serialized.energyPerTick = energyInfo[0];
                                    serialized.energyPerTickAtNight = energyInfo[1];
                                    return Collections.singletonList(new SimplePair<>(serialized,null));
                                }
                            }
                            return Collections.emptyList();
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull EnergyGenerator m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_EnergyGenerator(m);
                            String id = m.getId();
                            if (Objects.equals(GEOTHERMAL.getItemId(),id) || Objects.equals(REINFORCED_GEOTHERMAL.getItemId(),id)){
                                return new String[]{
                                        properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy")
                                                + energyInfo[0]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Unit"),
                                        properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Nether")
                                                + energyInfo[1]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_GeoEnergy_Unit"),
                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                                + energyInfo[2]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                                };
                            }else {
                                return new String[]{
                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDay")
                                                + energyInfo[0]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNight")
                                                + energyInfo[1]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                        properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                                + energyInfo[2]
                                                + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                                };
                            }
                        }
                    });
            registerSerializedRecipeProvider_byClassName(InfinityReactor.class.getName(),
                    new SerializedRecipeProvider<InfinityReactor>() {
                @Nonnull
                @Override
                public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable InfinityReactor m) {
                    if (m == null){return Collections.emptyList();}
                    long[] energyInfo = findEnergyInfo_InfinityExpansion_InfinityReactor(m);
                    Set<TweakedMachineFuel> machineFuels = findFuels_InfinityExpansion_InfinityReactor(m);
                    List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
                    for (TweakedMachineFuel recipe:machineFuels){
                        SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(m.getItem(),recipe, energyInfo[0]);
                        result.add(new SimplePair<>(serialized,null));
                    }
                    return result;
                }

                @Nonnull
                @Override
                public String[] getEnergyInfoStrings(@Nonnull InfinityReactor m) {
                    long[] energyInfo = findEnergyInfo_InfinityExpansion_InfinityReactor(m);
                    return new String[]{
                            properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                    + energyInfo[0]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                            properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                    + energyInfo[2]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                    };
                }
            });
            registerSerializedRecipeProvider_byClassName(MobSimulationChamber.class.getName(),
                    new SerializedRecipeProvider<MobSimulationChamber>() {
                        @Nonnull
                        @Override
                        public SimplePair<String, List<String>> getNameAndLoreForRecipe(@Nullable MobSimulationChamber m, SimplePair<SerializedMachine_MachineRecipe,ItemStack> serialized, int index) {
                            SimpleFour<String, ItemStack[],IntegerRational[],Long> recipe = CARDS_INFO_LIST.get(index);

                            List<String> lore = new ArrayList<>();
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick") + recipe.d);
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_MobSimulationCard"));
                            SlimefunItem card = SlimefunItem.getById(recipe.a);
                            ItemStack cardItem = card==null?null:card.getItem();
                            lore.add(NameUtil.findName(cardItem));
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
                            for (int j=0;j<recipe.b.length;j+=1){
                                lore.add(ChatColor.WHITE + NameUtil.findNameWithAmount(recipe.b[j]) + properties.getReplacedProperty("Test_InfoProvider_Info_Recipe_Expectation") + recipe.c[j]);
                            }
                            return new SimplePair<>(properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                    + CHAMBER_INTERVAL
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                                    lore);
                        }

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable MobSimulationChamber m) {
                            if (m == null){return Collections.emptyList();}
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
                            for (SimpleFour<String, ItemStack[],IntegerRational[],Long> cardInfo:CARDS_INFO_LIST){
                                SerializedMachine_MachineRecipe recipe= new SerializedMachine_MachineRecipe(
                                        m.getItem(),
                                        new MachineRecipeInTicks(CHAMBER_INTERVAL, emptyItemStackArray, cardInfo.b),
                                        cardInfo.d
                                        , 1
                                        , cardInfo.c);
                                SlimefunItem card = SlimefunItem.getById(cardInfo.a);
                                result.add(new SimplePair<>(recipe, card == null?null:card.getItem()));
                            }
                            return result;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull MobSimulationChamber m) {
                            long[] energyInfo = new long[]{1145141919810L,1,m.getCapacity()};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_Depends_On_Recipe"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                            + (CHAMBER_INTERVAL)
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            registerSerializedRecipeProvider_byClassName(InfinityWorkbench.class.getName(),
                    new SerializedRecipeProvider<InfinityWorkbench>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable InfinityWorkbench m) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{m.getCapacity(),1,m.getCapacity()};
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
                            for (SimplePair<ItemStack[], ItemStack> workBenchRecipe:INFINITY_WORKBENCH_RECIPES){
                                MachineRecipeInTicks machineRecipeInTicks = new MachineRecipeInTicks(
                                        1,
                                        workBenchRecipe.first,
                                        new ItemStack[]{workBenchRecipe.second}
                                );
                                SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                        m.getItem(),
                                        machineRecipeInTicks,
                                        energyInfo[0]
                                );
                                result.add(new SimplePair<>(serialized,null));
                            }
                            return result;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull InfinityWorkbench m) {
                            long[] energyInfo = new long[]{m.getCapacity(),1,m.getCapacity()};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            //IDK y we need this.↓
            registerSerializedRecipeProvider_byClassName(GearTransformer.class.getName(),
                    new SerializedRecipeProvider<GearTransformer>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable GearTransformer m) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{m.getCapacity(),1,m.getCapacity()};
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>();
                            for (SimplePair<ItemStack[], ItemStack> recipeInfo : GEAR_TRANSFORMER_RECIPES) {
                                MachineRecipeInTicks machineRecipeInTicks = new MachineRecipeInTicks(
                                        1,
                                        recipeInfo.first,
                                        new ItemStack[]{recipeInfo.second});
                                SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                        m.getItem(),
                                        machineRecipeInTicks,
                                        energyInfo[0]
                                );
                                result.add(new SimplePair<>(serialized,null));
                            }
                            return result;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull GearTransformer m) {
                            long[] energyInfo = new long[]{m.getCapacity(),1,m.getCapacity()};
                            return new String[]{
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick")
                                            + energyInfo[0]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"),
                                    properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                            + energyInfo[2]
                                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit")
                            };
                        }
                    });
            InfinityCompressSerializedMachineRecipes.init();
        }
    }
}
