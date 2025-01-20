package io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion;

import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.Approximation;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.mooy1.infinityexpansion.items.quarries.Oscillator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

import static io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion.InfinityExpansionConsts.ALLOW_NETHER_IN_OVERWORLD;

public class MaterialRationalMapForQuarry extends TreeMap<Integer,IntegerRational>{

    public MaterialRationalMapForQuarry(World.Environment env, int quarryChance, @Nonnull Material[] materials){
        this(env,quarryChance,materials,null);
    }
    public MaterialRationalMapForQuarry(World.Environment env,int quarryChance,@Nonnull Material[] materials,@Nullable Oscillator oscillator){
        IntegerRational noCobbleStoneChance = new IntegerRational(1,quarryChance);
        IntegerRational cobbleStoneChance = IntegerRational.ONE.sub(noCobbleStoneChance);
        add(Material.COBBLESTONE.ordinal(), cobbleStoneChance);
        IntegerRational defaultRational;
        if (oscillator == null){
            defaultRational = noCobbleStoneChance.divide(materials.length);
            for (Material m:materials){
                if (checkNetherAndPut(m,env,defaultRational)){continue;}
                add(m.ordinal(),defaultRational);
            }
        }
        else {
            IntegerRational oscillatorAffectChance = Approximation.find((float) oscillator.chance);
            oscillatorAffectChance = oscillatorAffectChance.multiply(noCobbleStoneChance);
            IntegerRational oscillatorNotAffectedChance = noCobbleStoneChance.sub(oscillatorAffectChance);
            defaultRational = oscillatorNotAffectedChance.divide(materials.length);
            int oscillatorKey = oscillator.getItem().getType().ordinal();
            put(oscillatorKey,oscillatorAffectChance);
            for (Material m:materials){
                if (checkNetherAndPut(m,env,defaultRational)){continue;}
                add(m.ordinal(),defaultRational);
            }
        }
    }

    public void add(Material m,IntegerRational value){
        add(m.ordinal(),value);
    }

    public void add(Integer materialOrdinal,IntegerRational value){
        put(materialOrdinal,getOrDefault(materialOrdinal,IntegerRational.ZERO).add(value).simplify());
    }

    public boolean checkNetherAndPut(Material m, World.Environment env,IntegerRational materialExpectation){
        if (m == Material.NETHERITE_INGOT || m == Material.NETHERRACK || m == Material.QUARTZ){
            if (env.ordinal() != World.Environment.NETHER.ordinal()
                    && !ALLOW_NETHER_IN_OVERWORLD
            ){
                add(Material.COBBLESTONE,materialExpectation);
                return true;
            }
        }
        return false;
    }

    public SimplePair<ItemStack[],IntegerRational[]> toOutputsAndExpectations(int multiplier){
        ItemStack[] outputs = new ItemStack[this.size()];
        IntegerRational[] outputExpectations = new IntegerRational[this.size()];
        int counter = 0;
        for (Map.Entry<Integer,IntegerRational> entry:this.entrySet()){
            Material m = Material.values()[entry.getKey()];
            IntegerRational expectation = entry.getValue().multiply(multiplier);
            outputs[counter] = new ItemStack(m);
            outputExpectations[counter] = expectation;
            counter+=1;
        }
        return new SimplePair<>(outputs,outputExpectations);
    }
}
