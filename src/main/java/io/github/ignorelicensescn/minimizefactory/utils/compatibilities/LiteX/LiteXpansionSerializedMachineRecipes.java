package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.LiteX;

import dev.j3fftw.litexpansion.machine.generators.AdvancedSolarPanel;
import io.github.ignorelicensescn.minimizefactory.utils.itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizefactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags.LiteXpansionFlag;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.InfoScan.findEnergyInfo_LiteX_AdvancedSolarPanel;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializeMachineRecipeUtils.fromSolarGen;
import static io.github.ignorelicensescn.minimizefactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;

public class LiteXpansionSerializedMachineRecipes {
    private static boolean initFlag = false;
    public static void init(){
        if (initFlag){return;}
        if (LiteXpansionFlag){
            initFlag = true;
            registerSerializedRecipeProvider_byClassName(AdvancedSolarPanel.class.getName(),
                    new SerializedRecipeProvider<AdvancedSolarPanel>() {
                        @Nonnull
                        @Override
                        public List<SimplePair<SerializedMachine_MachineRecipe, ItemStack>> getSerializedRecipes(@Nullable AdvancedSolarPanel m) {
                            if (m == null){return Collections.emptyList();}
                            long[] energyInfo = findEnergyInfo_LiteX_AdvancedSolarPanel(m);
                            return fromSolarGen(m,energyInfo[0],energyInfo[1]);
                        }

                        @Nonnull
                        @Override
                        public String[] getEnergyInfoStrings(@Nonnull AdvancedSolarPanel m) {
                            long[] energyInfo = findEnergyInfo_LiteX_AdvancedSolarPanel(m);
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
        }
    }
}
