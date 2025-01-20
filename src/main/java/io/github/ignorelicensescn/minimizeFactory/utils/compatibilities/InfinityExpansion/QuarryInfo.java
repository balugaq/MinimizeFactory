package io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion;

import io.github.mooy1.infinityexpansion.items.quarries.Oscillator;
import org.bukkit.Material;
import org.bukkit.World;

import static io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.OscillatorArray;

public class QuarryInfo {
    //first index:environment ordinal
    //second index:oscillator index in OscillatorArray
    public final MaterialRationalMapForQuarry[][] outputsInQuarry;
    public final int speed;
    public final int chance;

    public QuarryInfo(int speed, int chance, Material[] materialArray){
        this.speed = speed;
        this.chance = chance;
        outputsInQuarry = new MaterialRationalMapForQuarry[World.Environment.values().length][OscillatorArray.length + 1];
        for (World.Environment env: World.Environment.values()){
            for (int i=0;i<OscillatorArray.length;i+=1){
                Oscillator o = OscillatorArray[i];
                MaterialRationalMapForQuarry currentRationalMap = new MaterialRationalMapForQuarry(env,chance,materialArray,o);
                outputsInQuarry[env.ordinal()][i] = currentRationalMap;
            }
            outputsInQuarry[env.ordinal()][OscillatorArray.length] = new MaterialRationalMapForQuarry(env,chance,materialArray);
        }
    }
}
