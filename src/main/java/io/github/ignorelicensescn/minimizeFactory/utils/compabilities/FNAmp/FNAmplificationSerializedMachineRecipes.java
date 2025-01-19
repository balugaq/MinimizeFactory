package io.github.ignorelicensescn.minimizeFactory.utils.compabilities.FNAmp;

import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import ne.fnfal113.fnamplifications.powergenerators.implementation.CustomPowerGen;
import ne.fnfal113.fnamplifications.powergenerators.implementation.CustomSolarGen;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.FNAmplificationsFlag;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.InfoScan.*;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;

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
                    SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe();
                    serialized.sfItem = m;
                    serialized.sfItemStack = m.getItem();
                    serialized.energyPerTick = energyInfo[0];
                    serialized.energyPerTickAtNight = energyInfo[1];
                    return Collections.singletonList(new SimplePair<>(serialized,null));
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
                    return lore.toArray(new String[0]);
                }
            });
            registerSerializedRecipeProvider_byClassName(CustomPowerGen.class.getName(),
                    new SerializedRecipeProvider<CustomPowerGen>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable CustomPowerGen m) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_FNAmp_CustomPowerGen(m);
                            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe();
                            serialized.sfItem = m;
                            serialized.sfItemStack = m.getItem();
                            serialized.energyPerTick = energyInfo[0];
                            serialized.energyPerTickAtNight = energyInfo[1];
                            return Collections.singletonList(new SimplePair<>(serialized,null));
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
                            return lore.toArray(new String[0]);
                        }
                    });
        }
    }
}
