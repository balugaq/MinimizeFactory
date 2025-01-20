package io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.LiteX;

import static dev.j3fftw.litexpansion.machine.generators.AdvancedSolarPanel.*;

public class LiteXConsts {
    public static final long[] ADVANCED_RATE = new long[]{ADVANCED_DAY_RATE * (-1),ADVANCED_NIGHT_RATE * (-1),1};
    public static final long[] ULTIMATE_RATE = new long[]{ULTIMATE_DAY_RATE * (-1),ULTIMATE_NIGHT_RATE * (-1),1};
    public static final long[] HYBRID_RATE =  new long[]{HYBRID_DAY_RATE * (-1),HYBRID_NIGHT_RATE * (-1),1};
}
