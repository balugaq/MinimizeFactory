package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress;

import io.github.acdeasdff.infinityCompress.items.Multiblock_Autocrafter;
import io.github.acdeasdff.infinityCompress.items.blocks.*;
import io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.BiomeAndEnvironment;
import io.github.ignorelicensescn.minimizefactory.utils.datastructures.records.ItemStacksToStackRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.namemateriallore.NameUtil;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.localmachinerecipe.MachineRecipeInTicks;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.InfoScan.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Slimefun.SlimefunConsts.geoResourcesInfo;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Slimefun.SlimefunConsts.geoResourcesInfo_ResourcesList;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityCompress.InfinityCompressConsts.getMultiblockAutocrafterRecipes;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.GEAR_TRANSFORMER_RECIPES;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.INFINITY_WORKBENCH_RECIPES;

public class InfinityCompressSerializedMachineRecipes {
    private static boolean initFlag = false;
//    it tried to reduce lag,but cannot fight against exponential-growing complexity of calculation.
//    then here we are.A plugin provides black-boxes should help.We don't need to calculate too much.
//    before:machines -> calculate tick by tick with laaaaaaaaaag ->result
//    we know the calculation result is a function about time t and solved this function,so:
//    after:machines -> result,no lag.
    public static void init(){
        if (initFlag){return;}
        if (PluginEnabledFlags.InfinityCompressFlag){
            initFlag = true;

            registerSerializedRecipeProvider_byClassName(TweakedGenerator.class.getName(),
                    new SerializedRecipeProvider<TweakedGenerator>() {

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable TweakedGenerator m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_InfinityCompress_TweakedGenerator(m);
                            return fromSolarGen(m,energyInfo[0],energyInfo[0]);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull TweakedGenerator m) {
                            long[] energyInfo = findEnergyInfo_InfinityCompress_TweakedGenerator(m);
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
            registerSerializedRecipeProvider_byClassName(TweakedMaterialGenerator.class.getName(),
                    new SerializedRecipeProvider<TweakedMaterialGenerator>() {

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable TweakedMaterialGenerator m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_InfinityCompress_TweakedMaterialGenerator( m);
                            List<MachineRecipe> machineRecipes = findRecipes_TweakedMaterialGenerator(m);
                            return SerializeMachineRecipeUtils.fromMachineRecipe(machineRecipes,m,energyInfo[0],1);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull TweakedMaterialGenerator m) {
                            long[] energyInfo = findEnergyInfo_InfinityCompress_TweakedMaterialGenerator( m);
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
            registerSerializedRecipeProvider_byClassName(TweakedGEOQuarry.class.getName(),
                    new SerializedRecipeProvider<TweakedGEOQuarry>() {
                        @Nonnull
                        @Override
                        public SimplePair<String, List<String>> getNameAndLoreForRecipe(@Nullable TweakedGEOQuarry m, SimplePair<SerializedMachine_MachineRecipe,ItemStack> serialized, int index) {
                            SimplePair<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>> recipe = geoResourcesInfo_ResourcesList.get(index);
                            List<String> lore = new ArrayList<>(10);

                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
                            ItemStack itemStack = recipe.first;
                            lore.add(ChatColor.WHITE + NameUtil.findName(itemStack));

                            lore.add(ChatColor.GREEN
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_MainWorld") + " "
                                    + ChatColor.RED
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_Nether") + " "
                                    + ChatColor.LIGHT_PURPLE
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_End") + " "
                                    + ChatColor.GRAY
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CUSTOM")
                            );
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_LocationAndExpectation"));
                            String[] locations = new String[recipe.second.size() / GEOMINER_BIOME_EVERY_LINE + (recipe.second.size() % GEOMINER_BIOME_EVERY_LINE == 0 ? 0 : 1)];
                            Arrays.fill(locations, "");
                            for (int j=0;j<recipe.second.size();j+=1){
                                SimplePair<BiomeAndEnvironment, IntegerRational> tri = recipe.second.get(j);
                                StringBuilder sb = new StringBuilder();
                                if (tri.first.environment().equals(World.Environment.NORMAL)){
                                    sb.append(ChatColor.GREEN);
                                }
                                else if (tri.first.environment().equals(World.Environment.NETHER)){
                                    sb.append(ChatColor.RED);
                                }
                                else if (tri.first.environment().equals(World.Environment.THE_END)){
                                    sb.append(ChatColor.LIGHT_PURPLE);
                                }
                                else{
                                    sb.append(ChatColor.GRAY);
                                }
                                sb.append(NameUtil.nameForBiome(tri.first.biome()))
                                        .append(" ")
                                        .append(properties.getReplacedProperty("Test_InfoProvider_Info_ExpectationColor"))
                                        .append(tri.second)
                                        .append(" ");
                                locations[j / GEOMINER_BIOME_EVERY_LINE] += sb.toString();
                            }
                            lore.addAll(List.of(locations));
                            String name = properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                    + 1
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit");
                            return new SimplePair<>(name,lore);
                        }

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable TweakedGEOQuarry m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_TweakedGeoQuarry(m);
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(geoResourcesInfo_ResourcesList.size()*5);
                            for (SimplePair<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>> outputPair
                                    : geoResourcesInfo_ResourcesList) {

                                for (SimplePair<BiomeAndEnvironment, IntegerRational> outputInfo:outputPair.second){
                                    BiomeAndEnvironment bioAndEnv = outputInfo.first;
                                    SimplePair<ItemStack[],IntegerRational[]> pair = geoResourcesInfo
                                            .get(bioAndEnv.environment())
                                            .get(bioAndEnv.biome());
                                    pair.first = pair.first.clone();
                                    for (int j = 0; j < pair.first.length; j+=1) {
                                        pair.first[j] = pair.first[j].clone();
                                        pair.first[j].setAmount((int) energyInfo[1]);
                                    }
                                    SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                            m.getItem()
                                            ,new MachineRecipeInTicks(1,null,pair.first)
                                            ,energyInfo[0]
                                            ,1
                                            ,pair.second
                                    );
                                    serialized.env = bioAndEnv.environment();
                                    serialized.biome = bioAndEnv.biome();
                                    result.add(new SimplePair<>(serialized,null));
                                }
                            }
                            return result;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull TweakedGEOQuarry m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_TweakedGeoQuarry(m);
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
            registerSerializedRecipeProvider_byClassName(TweakedGEOQuarry_Filter.class.getName(),
                    new SerializedRecipeProvider<TweakedGEOQuarry_Filter>() {
                        @Nonnull
                        @Override
                        public SimplePair<String, List<String>> getNameAndLoreForRecipe(@Nullable TweakedGEOQuarry_Filter m, SimplePair<SerializedMachine_MachineRecipe,ItemStack> serialized, int index) {
                            SimplePair<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>> recipe = geoResourcesInfo_ResourcesList.get(index);
                            int locationLines = recipe.second.size() / GEOMINER_BIOME_EVERY_LINE + (recipe.second.size() % GEOMINER_BIOME_EVERY_LINE == 0 ? 0 : 1);
                            List<String> lore = new ArrayList<>(5+locationLines);

                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_Material_Output"));
                            ItemStack itemStack = recipe.first;
                            lore.add(ChatColor.WHITE + NameUtil.findName(itemStack));

                            lore.add(ChatColor.GREEN
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_MainWorld") + " "
                                    + ChatColor.RED
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_Nether") + " "
                                    + ChatColor.LIGHT_PURPLE
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_End") + " "
                                    + ChatColor.GRAY
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_ColoredBlock")
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CUSTOM")
                            );
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_LocationAndExpectation"));
                            String[] locations = new String[locationLines];
                            Arrays.fill(locations, "");
                            for (int j=0;j<recipe.second.size();j+=1){
                                SimplePair<BiomeAndEnvironment, IntegerRational> tri = recipe.second.get(j);
                                StringBuilder sb = new StringBuilder();
                                if (tri.first.environment().equals(World.Environment.NORMAL)){
                                    sb.append(ChatColor.GREEN);
                                }
                                else if (tri.first.environment().equals(World.Environment.NETHER)){
                                    sb.append(ChatColor.RED);
                                }
                                else if (tri.first.environment().equals(World.Environment.THE_END)){
                                    sb.append(ChatColor.LIGHT_PURPLE);
                                }
                                else{
                                    sb.append(ChatColor.GRAY);
                                }
                                sb.append(NameUtil.nameForBiome(tri.first.biome()))
                                        .append(" ")
                                        .append(properties.getReplacedProperty("Test_InfoProvider_Info_ExpectationColor"))
                                        .append(tri.second)
                                        .append(" ");
                                locations[j / GEOMINER_BIOME_EVERY_LINE] += sb.toString();
                            }
                            lore.addAll(List.of(locations));
                            String name = properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime")
                                    + 1
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_CraftingTime_Unit");
                            return new SimplePair<>(name,lore);
                        }

                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable TweakedGEOQuarry_Filter m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_TweakedGeoQuarry(m);
                            List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> result = new ArrayList<>(geoResourcesInfo_ResourcesList.size()*5);
                            for (SimplePair<ItemStack, List<SimplePair<BiomeAndEnvironment, IntegerRational>>> outputPair
                                    : geoResourcesInfo_ResourcesList) {

                                for (SimplePair<BiomeAndEnvironment, IntegerRational> outputInfo:outputPair.second){
                                    BiomeAndEnvironment bioAndEnv = outputInfo.first;
                                    SimplePair<ItemStack[],IntegerRational[]> pair = geoResourcesInfo
                                            .get(bioAndEnv.environment())
                                            .get(bioAndEnv.biome());
                                    pair.first = pair.first.clone();
                                    for (int j = 0; j < pair.first.length; j+=1) {
                                        pair.first[j] = pair.first[j].clone();
                                        pair.first[j].setAmount((int) energyInfo[1]);
                                    }
                                    SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe(
                                            m.getItem()
                                            ,new MachineRecipeInTicks(1,null,pair.first)
                                            ,energyInfo[0]
                                            ,1
                                            ,pair.second
                                    );
                                    serialized.env = bioAndEnv.environment();
                                    serialized.biome = bioAndEnv.biome();
                                    result.add(new SimplePair<>(serialized,null));
                                }
                            }
                            return result;
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull TweakedGEOQuarry_Filter m) {
                            long[] energyInfo = findEnergyInfo_InfinityExpansion_TweakedGeoQuarry(m);
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
            registerSerializedRecipeProvider_byClassName(Multiblock_Autocrafter.class.getName(),
                    new SerializedRecipeProvider<Multiblock_Autocrafter>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable Multiblock_Autocrafter m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{Multiblock_Autocrafter.ENERGY_CONSUMPTION,1,Multiblock_Autocrafter.CAPACITY};
                            ItemStacksToStackRecipe[] machineRecipes = getMultiblockAutocrafterRecipes(m);
                            return fromInputsAndSingleOutput(machineRecipes,m,
                                    energyInfo[0],1);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull Multiblock_Autocrafter m) {
                            long[] energyInfo = new long[]{Multiblock_Autocrafter.ENERGY_CONSUMPTION,1,Multiblock_Autocrafter.CAPACITY};
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
            registerSerializedRecipeProvider_byClassName(AutoInfinityWorkbench.class.getName(),
                    new SerializedRecipeProvider<AutoInfinityWorkbench>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AutoInfinityWorkbench m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{m.getCapacity(),1,m.getCapacity()};
                            return fromInputsAndSingleOutput(INFINITY_WORKBENCH_RECIPES,m,energyInfo[0],1);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull AutoInfinityWorkbench m) {
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
            registerSerializedRecipeProvider_byClassName(TweakedGenerator.class.getName(),
                    new SerializedRecipeProvider<TweakedGenerator>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable TweakedGenerator m ,@Nullable ItemStack stack) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = new long[]{m.getCapacity(),1,m.getCapacity()};
                            return fromCraftingTableLikeRecipes(GEAR_TRANSFORMER_RECIPES,m.getItem(),null,energyInfo[0]);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull TweakedGenerator m) {
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
        }
    }
}
