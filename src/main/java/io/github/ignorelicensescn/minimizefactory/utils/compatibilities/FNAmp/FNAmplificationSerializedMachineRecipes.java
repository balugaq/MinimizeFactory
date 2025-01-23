package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.FNAmp;

import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils;
import ne.fnfal113.fnamplifications.materialgenerators.implementations.CustomMaterialGenerator;
import ne.fnfal113.fnamplifications.powergenerators.implementation.CustomPowerGen;
import ne.fnfal113.fnamplifications.powergenerators.implementation.CustomSolarGen;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags.FNAmplificationsFlag;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.FNAmp.FNAmpConsts.getMaterialGeneratorsOutput;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.InfoScan.*;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils.fromSolarGen;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;
import static io.github.ignorelicensescn.minimizefactory.utils.searchregistries.SearchRegistries.registerOnScannedSlimefunItemInstanceListener;
import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY;

public class FNAmplificationSerializedMachineRecipes {

    private static boolean initFlag = false;
    public static void init(){
        if (initFlag){return;}
        if (FNAmplificationsFlag){
            initFlag = true;
//            it has duration,i don't want to add it for now.
//            registerSerializedRecipeProvider_byClassName(CustomMaterialGenerator.class,
//                    new SerializedRecipeProvider<CustomMaterialGenerator>() {
//                    })
            registerSerializedRecipeProvider_byClassName(CustomSolarGen.class.getName(),
                    new SerializedRecipeProvider<CustomSolarGen>() {
                @Nonnull
                @Override
                public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable CustomSolarGen m) {
                    if (m == null){return Collections.emptyList();}
                    long[] energyInfo = findEnergyInfo_FNAmp_CustomSolarGen(m);
                    return fromSolarGen(m,energyInfo[0],energyInfo[1]);
                }

                @Nonnull
                @Override
                public String[] getEnergyInfoStrings(@Nonnull CustomSolarGen m) {
                    long[] energyInfo = findEnergyInfo_FNAmp_CustomSolarGen(m);
                    List<String> lore = new ArrayList<>(Arrays.asList(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNightOrEnd").split("\n")));
                    lore.set(lore.size() - 1,lore.get(lore.size()-1) + energyInfo[1]
                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
                    lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                            + energyInfo[2]
                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"));
                    lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDayOrNether")
                            + energyInfo[0]
                            + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
                    return lore.toArray(EMPTY_STRING_ARRAY);
                }
            });
            registerSerializedRecipeProvider_byClassName(CustomPowerGen.class.getName(),
                    new SerializedRecipeProvider<CustomPowerGen>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable CustomPowerGen m) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_FNAmp_CustomPowerGen(m);
                            return fromSolarGen(m,energyInfo[0],energyInfo[1]);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull CustomPowerGen m) {
                            long[] energyInfo = findEnergyInfo_FNAmp_CustomPowerGen(m);
                            List<String> lore = new ArrayList<>(Arrays.asList(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtNightOrEnd").split("\n")));
                            lore.set(lore.size() - 1,lore.get(lore.size()-1) + energyInfo[1]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity")
                                    + energyInfo[2]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyCapacity_Unit"));
                            lore.add(properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTickAtDayOrNether")
                                    + energyInfo[0]
                                    + properties.getReplacedProperty("Test_InfoProvider_Info_EnergyPerTick_Unit"));
                            return lore.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
                        }
                    });

            registerOnScannedSlimefunItemInstanceListener(CustomMaterialGenerator.class,
                    sfItem -> {
                        try {
                            getMaterialGeneratorsOutput((CustomMaterialGenerator) sfItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
            });
        }
    }
}
