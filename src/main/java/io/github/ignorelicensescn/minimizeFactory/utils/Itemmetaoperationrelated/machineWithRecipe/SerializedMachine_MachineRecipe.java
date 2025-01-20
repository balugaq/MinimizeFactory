package io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe;

import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.DataTypeMethods;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.PersistentSerializedMachineRecipeType;
import io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion.MachineBlockRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.compatibilities.InfinityExpansion.SingularityRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.TweakedMachineFuel;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.MachineRecipeInTicksWithExpectations;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.MachineRecipeOutEntity;
import io.github.ignorelicensescn.minimizeFactory.utils.localmachinerecipe.MachineRecipeWithExpectations;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.IntegerRational;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.PersistentSerializedMachineRecipeType.SERIALIZED_MACHINE_RECIPE;
import static io.github.ignorelicensescn.minimizeFactory.utils.itemstackrelated.ItemStackUtil.itemStackArrayEquals;
import static io.github.ignorelicensescn.minimizeFactory.utils.recipesupport.InfoScan.isBucket;

public class SerializedMachine_MachineRecipe extends SerializedMachineWithRecipe{
    @Nullable
    public String machineRecipeClassName = null;
    public int ticks = 1;
//    public int[] expectation;//MachineRecipeWithExpectations
    @Nullable
    public ItemStack[] inputs = null;
    @Nullable
    public ItemStack[] outputs = null;
    @Nullable
    public IntegerRational[] outputExpectations = null;
    @Nullable
    public World.Environment env = null;
    @Nullable
    public Biome biome = null;
    @Nullable
    public String entityClassName = null;//MachineRecipeOutEntity
    public int Singularity_Material_amount = -1;
    public long energyPerTick = 0;//GEOTHERMAL:Environment.NORMAL
    public long energyPerTickAtNight = 0;//GEOTHERMAL:Environment.NETHER
    //rule:half of the time (not nether or end) and receive skylight -> day
    //nether and receive skylight -> day
    //end -> night
    @Nullable
    public ItemStack[] extraItems = null;
    public SerializedMachine_MachineRecipe(ItemStack itemStack, MachineRecipe machineRecipe, long energyPerTick){
        super(itemStack.clone());
        this.energyPerTick = energyPerTick;
        this.energyPerTickAtNight = this.energyPerTick;
        this.ticks = machineRecipe.getTicks();
        this.inputs = machineRecipe.getInput();
        this.outputs = machineRecipe.getOutput();
        if (machineRecipe instanceof MachineRecipeWithExpectations RwE){
            this.machineRecipeClassName = MachineRecipeWithExpectations.class.getName();

            this.outputExpectations = new IntegerRational[this.outputs.length];
            System.arraycopy(RwE.expectations, 0, this.outputExpectations, 0, this.outputs.length);
//            this.expectation = new IntegerRational(((MachineRecipeWithExpectations) machineRecipe).numerator,((MachineRecipeWithExpectations) machineRecipe).denominator);
        }
        else if (machineRecipe instanceof MachineRecipeInTicksWithExpectations RwE){
            this.machineRecipeClassName = MachineRecipeInTicksWithExpectations.class.getName();
            this.outputExpectations = new IntegerRational[this.outputs.length];
            System.arraycopy(RwE.expectations, 0, this.outputExpectations, 0, this.outputs.length);
//            this.expectation = new IntegerRational(((MachineRecipeInTicksWithExpectations) machineRecipe).numerator,((MachineRecipeInTicksWithExpectations) machineRecipe).denominator);
        }
        else if (machineRecipe instanceof MachineRecipeOutEntity){
            this.machineRecipeClassName = MachineRecipeOutEntity.class.getName();
            entityClassName = ((MachineRecipeOutEntity) machineRecipe).getOutputEntityClass().getName();
        }else if (machineRecipe instanceof MachineBlockRecipe){
            this.machineRecipeClassName = MachineBlockRecipe.class.getName();
        }
        else {
            this.machineRecipeClassName = MachineRecipe.class.getName();
        }
    }
    public SerializedMachine_MachineRecipe(ItemStack itemStack,
                                           SingularityRecipe singularityRecipe,
                                           int ticks,
                                           long energyPerTick,
                                           int speed){
        super(itemStack.clone());
        this.energyPerTick = energyPerTick;
        this.energyPerTickAtNight = this.energyPerTick;
        this.ticks = ticks;
        this.inputs = new ItemStack[]{singularityRecipe.input()};
        this.outputs = new ItemStack[]{singularityRecipe.output()};
        this.Singularity_Material_amount = singularityRecipe.amount();
    }
    public SerializedMachine_MachineRecipe(ItemStack itemStack, MachineRecipe machineRecipe, @Nullable World.Environment env, long energyPerTick, int speed, @Nullable IntegerRational[] outputExpectations){
        super(itemStack.clone());
        this.outputExpectations = outputExpectations;
        this.energyPerTick = energyPerTick;
        this.energyPerTickAtNight = this.energyPerTick;
        this.env = env;
        this.ticks = machineRecipe.getTicks();
        this.inputs = machineRecipe.getInput();
        this.outputs = machineRecipe.getOutput();
        if (machineRecipe instanceof MachineRecipeWithExpectations RwE){
            this.machineRecipeClassName = MachineRecipeWithExpectations.class.getName();

            this.outputExpectations = new IntegerRational[this.outputs.length];
            System.arraycopy(RwE.expectations, 0, this.outputExpectations, 0, this.outputs.length);
//            this.expectation = new IntegerRational(((MachineRecipeWithExpectations) machineRecipe).numerator,((MachineRecipeWithExpectations) machineRecipe).denominator);
        }
        else if (machineRecipe instanceof MachineRecipeInTicksWithExpectations RwE){
            this.machineRecipeClassName = MachineRecipeInTicksWithExpectations.class.getName();
            this.outputExpectations = new IntegerRational[this.outputs.length];
            System.arraycopy(RwE.expectations, 0, this.outputExpectations, 0, this.outputs.length);
//            this.expectation = new IntegerRational(((MachineRecipeInTicksWithExpectations) machineRecipe).numerator,((MachineRecipeInTicksWithExpectations) machineRecipe).denominator);
        }
        else if (machineRecipe instanceof MachineRecipeOutEntity){
            this.machineRecipeClassName = MachineRecipeOutEntity.class.getName();
            entityClassName = ((MachineRecipeOutEntity) machineRecipe).getOutputEntityClass().getName();
        }
        else if (machineRecipe instanceof MachineBlockRecipe) {
            this.machineRecipeClassName = MachineBlockRecipe.class.getName();
        }
        else {
            this.machineRecipeClassName = MachineRecipe.class.getName();
        }
    }
    public SerializedMachine_MachineRecipe(ItemStack itemStack, MachineRecipe machineRecipe, long energyPerTick, int speed, @Nullable IntegerRational[] outputExpectations){
        super(itemStack.clone());
        this.outputExpectations = outputExpectations;
        this.energyPerTick = energyPerTick;
        this.energyPerTickAtNight = this.energyPerTick;
        this.ticks = machineRecipe.getTicks();
        this.inputs = machineRecipe.getInput();
        this.outputs = machineRecipe.getOutput();
        if (machineRecipe instanceof MachineRecipeWithExpectations RwE){
            this.machineRecipeClassName = MachineRecipeWithExpectations.class.getName();

            this.outputExpectations = new IntegerRational[this.outputs.length];
            System.arraycopy(RwE.expectations, 0, this.outputExpectations, 0, this.outputs.length);
//            this.expectation = new IntegerRational(((MachineRecipeWithExpectations) machineRecipe).numerator,((MachineRecipeWithExpectations) machineRecipe).denominator);
        }
        else if (machineRecipe instanceof MachineRecipeInTicksWithExpectations RwE){
            this.machineRecipeClassName = MachineRecipeInTicksWithExpectations.class.getName();
            this.outputExpectations = new IntegerRational[this.outputs.length];
            System.arraycopy(RwE.expectations, 0, this.outputExpectations, 0, this.outputs.length);
//            this.expectation = new IntegerRational(((MachineRecipeInTicksWithExpectations) machineRecipe).numerator,((MachineRecipeInTicksWithExpectations) machineRecipe).denominator);
        }
        else if (machineRecipe instanceof MachineRecipeOutEntity){
            this.machineRecipeClassName = MachineRecipeOutEntity.class.getName();
            entityClassName = ((MachineRecipeOutEntity) machineRecipe).getOutputEntityClass().getName();
        }
        else if (machineRecipe instanceof MachineBlockRecipe) {
            this.machineRecipeClassName = MachineBlockRecipe.class.getName();
        }
        else {
            this.machineRecipeClassName = MachineRecipe.class.getName();
        }
    }
    public SerializedMachine_MachineRecipe(ItemStack itemStack,MachineFuel machineRecipe, long energyPerTick){
        super(itemStack);
        this.energyPerTick = energyPerTick;
        this.energyPerTickAtNight = this.energyPerTick;
        this.ticks = machineRecipe.getTicks();
        this.inputs = new ItemStack[]{machineRecipe.getInput().clone()};
        if (isBucket(machineRecipe.getInput())){
            this.outputs = new ItemStack[]{machineRecipe.getOutput().clone(),new ItemStack(Material.BUCKET)};
        }else {
            if (machineRecipe.getOutput() != null && machineRecipe.getOutput().getType() != Material.AIR){
                this.outputs = new ItemStack[]{machineRecipe.getOutput().clone()};
            }
        }
        this.machineRecipeClassName = MachineFuel.class.getName();
    }
    public SerializedMachine_MachineRecipe(ItemStack itemStack, TweakedMachineFuel machineRecipe, long energyPerTick){
        super(itemStack);
        this.energyPerTick = energyPerTick;
        this.energyPerTickAtNight = this.energyPerTick;
        this.ticks = machineRecipe.getTicks();
        this.inputs = machineRecipe.getInput();
        this.outputs = machineRecipe.getOutput();
        this.machineRecipeClassName = MachineFuel.class.getName();
    }
    public SerializedMachine_MachineRecipe(){
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SerializedMachine_MachineRecipe another){
            if (!Objects.equals(this.machineRecipeClassName,another.machineRecipeClassName)){return false;}
            if (!Objects.equals(this.ticks,another.ticks)){return false;}
            if (!itemStackArrayEquals(this.inputs, another.inputs)){return false;}
            if (!itemStackArrayEquals(this.outputs, another.outputs)){return false;}
            if (!Arrays.deepEquals(this.outputExpectations, another.outputExpectations)){return false;}
            if (!Objects.equals(this.env,another.env)){return false;}
            if (!Objects.equals(this.biome,another.biome)){return false;}
            if (!Objects.equals(this.entityClassName,another.entityClassName)){return false;}
            if (!Objects.equals(this.Singularity_Material_amount, another.Singularity_Material_amount)){return false;}
            if (!Objects.equals(this.energyPerTick,another.energyPerTick)){return false;}
            if (!Objects.equals(this.energyPerTickAtNight,another.energyPerTickAtNight)){return false;}
            return itemStackArrayEquals(this.extraItems, another.extraItems);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (machineRecipeClassName != null ? machineRecipeClassName.hashCode() : 0);
        result = 31 * result + ticks;
        result = 31 * result + Arrays.hashCode(inputs);
        result = 31 * result + Arrays.hashCode(outputs);
        result = 31 * result + Arrays.deepHashCode(outputExpectations);
        result = 31 * result + (env != null ? env.ordinal() : 0);
        result = 31 * result + (biome != null ? biome.ordinal() : 0);
        result = 31 * result + (entityClassName != null ? entityClassName.hashCode() : 0);
        result = 31 * result + Singularity_Material_amount;
        result = 31 * result + Long.hashCode(energyPerTick);
        result = 31 * result + Long.hashCode(energyPerTickAtNight);
        result = 31 * result + Arrays.hashCode(extraItems);
        return result;
    }

    @Override
    public String toString() {
        return "SerializedMachine_MachineRecipe{" +
                "\nmachineRecipeClassName='" + machineRecipeClassName + '\'' +
                "\n, ticks=" + ticks +
                "\n, inputs=" + Arrays.toString(inputs) +
                "\n, outputs=" + Arrays.toString(outputs) +
                "\n, outputExpectations=" + Arrays.toString(outputExpectations) +
                "\n, env=" + env +
                "\n, biome=" + biome +
                "\n, entityClassName='" + entityClassName + '\'' +
                "\n, Singularity_Material_amount=" + Singularity_Material_amount +
                "\n, energyPerTick=" + energyPerTick +
                "\n, energyPerTickAtNight=" + energyPerTickAtNight +
                "\n, extraItems=" + Arrays.toString(extraItems) +
                "\n}";
    }

    public static Optional<SerializedMachine_MachineRecipe> retrieveFromItemStack(ItemStack stack){
        if (stack == null){return Optional.empty();}
        if (!stack.hasItemMeta()){return Optional.empty();}
        ItemMeta meta = stack.getItemMeta();
        if (meta == null){return Optional.empty();}
        return DataTypeMethods.getOptionalCustom(meta,SERIALIZED_MACHINE_RECIPE, PersistentSerializedMachineRecipeType.TYPE);
    }
}
