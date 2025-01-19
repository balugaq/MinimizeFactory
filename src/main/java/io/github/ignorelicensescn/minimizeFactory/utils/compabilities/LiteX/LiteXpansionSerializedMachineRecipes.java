package io.github.ignorelicensescn.minimizeFactory.utils.compabilities.LiteX;

import dev.j3fftw.litexpansion.machine.generators.AdvancedSolarPanel;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedRecipeProvider;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.LiteXpansionFlag;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.properties;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.InfoScan.findEnergyInfo_LiteX_AdvancedSolarPanel;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.SerializedMachineRecipeFinder.registerSerializedRecipeProvider_byClassName;

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
                            SerializedMachine_MachineRecipe serialized = new SerializedMachine_MachineRecipe();
                            serialized.sfItem = m;
                            serialized.sfItemStack = m.getItem();
                            serialized.energyPerTick = energyInfo[0];
                            serialized.energyPerTickAtNight = energyInfo[1];
                            return Collections.singletonList(new SimplePair<>(serialized,null));
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
                            return lore.toArray(new String[0]);
                        }
                    });
        }
    }
}
