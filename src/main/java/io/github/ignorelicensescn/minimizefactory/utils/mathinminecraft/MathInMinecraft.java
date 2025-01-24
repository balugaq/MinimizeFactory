package io.github.ignorelicensescn.minimizefactory.utils.mathinminecraft;

import io.github.ignorelicensescn.minimizefactory.MinimizeFactory;
import io.github.ignorelicensescn.minimizefactory.utils.mathutils.BigRational;
import org.bukkit.World;

import javax.annotation.Nonnull;

import static org.bukkit.GameRule.RANDOM_TICK_SPEED;

public class MathInMinecraft {
    public static final int SUB_CHUNK_SIZE = 16*16*16;
    public static final BigRational MEET_SPECIFIC_RANDOM_TICK_CHANCE = new BigRational(1,SUB_CHUNK_SIZE);
    public static final BigRational NO_MEET_SPECIFIC_RANDOM_TICK_CHANCE = new BigRational(SUB_CHUNK_SIZE-1,SUB_CHUNK_SIZE);

    public static BigRational chanceToReceiveRandomTickPerTick(int randomTickSpeed){
        return BigRational.ONE.subtract(NO_MEET_SPECIFIC_RANDOM_TICK_CHANCE.pow(randomTickSpeed));
    }
    //hint:binomial distribution
    public static BigRational expectationReceiveRandomTicksPerTick(int randomTickSpeed){
        return MEET_SPECIFIC_RANDOM_TICK_CHANCE.multiply(randomTickSpeed);
    }

    public static int getRandomTickSpeed(@Nonnull World world){
        Integer result = world.getGameRuleValue(RANDOM_TICK_SPEED);
        return result==null?3:result;
    }

}
