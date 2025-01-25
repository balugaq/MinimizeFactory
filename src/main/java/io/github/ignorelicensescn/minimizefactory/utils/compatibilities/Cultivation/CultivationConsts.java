package io.github.ignorelicensescn.minimizefactory.utils.compatibilities.Cultivation;

import dev.sefiraat.cultivation.implementation.slimefun.machines.GardenCloche;

import static dev.sefiraat.cultivation.implementation.slimefun.items.Machines.GARDEN_CLOCHE;
import static io.github.ignorelicensescn.minimizefactory.PluginEnabledFlags.CultivationFlag;
import static io.github.ignorelicensescn.minimizefactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.getIntInUnsafe_static;

public class CultivationConsts {

    public static void init(){
        if (CultivationFlag){
            try {
                GardenClocheConsts.POWER_REQUIREMENT = getIntInUnsafe_static(GardenCloche.class.getDeclaredField("POWER_REQUIREMENT"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static class GardenClocheConsts{
        public static int POWER_REQUIREMENT = 100;

        public static long[] getEnergyInfo(){
            return new long[]{POWER_REQUIREMENT,1,GARDEN_CLOCHE.getCapacity()};
        }
    }
}
