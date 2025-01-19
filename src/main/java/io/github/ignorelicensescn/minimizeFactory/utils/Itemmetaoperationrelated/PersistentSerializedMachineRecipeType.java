package io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated;


import de.jeff_media.morepersistentdatatypes.DataType;
import io.github.ignorelicensescn.minimizeFactory.utils.Itemmetaoperationrelated.machineWithRecipe.SerializedMachine_MachineRecipe;
import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.IntegerRational;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public class PersistentSerializedMachineRecipeType implements PersistentDataType<PersistentDataContainer, SerializedMachine_MachineRecipe> {

    public static final PersistentDataType<PersistentDataContainer, SerializedMachine_MachineRecipe> TYPE = new PersistentSerializedMachineRecipeType();

    public static final NamespacedKey SERIALIZED_MACHINE_RECIPE = Keys.newKey("mf_serialized_machine_recipe");
    public static final NamespacedKey BASED_ITEM = Keys.newKey("mf_based_item_stack");
    public static final NamespacedKey BASED_ITEM_META = Keys.newKey("mf_based_item_stack_metas");
    public static final NamespacedKey RECIPE_CLASS_STR = Keys.newKey("mf_recipe_class_str");
    public static final NamespacedKey ENTITY_CLASS_STR = Keys.newKey("mf_entity_class_str");
    public static final NamespacedKey BIOME_STR = Keys.newKey("mf_biome_str");
    public static final NamespacedKey ENVIRONMENT_STR = Keys.newKey("mf_env_str");
    public static final NamespacedKey INPUTS_STACK = Keys.newKey("mf_inputs_stack");
    public static final NamespacedKey INPUTS_STACK_AMOUNT = Keys.newKey("mf_inputs_stack_amount");
    public static final NamespacedKey INPUTS_STACK_META = Keys.newKey("mf_inputs_stack_meta");
    public static final NamespacedKey OUTPUTS_STACK = Keys.newKey("mf_outputs_stack");
    public static final NamespacedKey OUTPUTS_STACK_AMOUNT = Keys.newKey("mf_outputs_stack_amount");
    public static final NamespacedKey OUTPUTS_STACK_META = Keys.newKey("mf_outputs_stack_meta");
    public static final NamespacedKey TICKS_INT = Keys.newKey("mf_ticks_int");
    public static final NamespacedKey NUMERATOR_ARRAY = Keys.newKey("mf_numerator_array");
    public static final NamespacedKey DENOMINATOR_ARRAY = Keys.newKey("mf_denominator_array");
    public static final NamespacedKey ENERGY_PER_TICK_LONG = Keys.newKey("mf_energy_per_tick_long");
    public static final NamespacedKey ENERGY_PER_TICK_AT_NIGHT_LONG = Keys.newKey("mf_energy_per_tick_at_night_long");
    public static final NamespacedKey SINGULARITY_MATERIAL_AMOUNT_INT = Keys.newKey("mf_singularity_material_amount_int");
    public static final NamespacedKey EXTRA_ITEMS = Keys.newKey("mf_extra_items");
    public static final NamespacedKey EXTRA_ITEMS_META = Keys.newKey("mf_extra_items_metas");
    @Override
    @Nonnull
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    @Nonnull
    public Class<SerializedMachine_MachineRecipe> getComplexType() {
        return SerializedMachine_MachineRecipe.class;
    }

    @Override
    @Nonnull
    public PersistentDataContainer toPrimitive(@Nonnull SerializedMachine_MachineRecipe complex, @Nonnull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();

        if (complex.sfItemStack != null){
            ItemStack base = complex.sfItemStack.clone();
            container.set(BASED_ITEM, DataType.ITEM_STACK, new ItemStack(base.getType()));
            if (base.hasItemMeta()){
                container.set(BASED_ITEM_META, DataType.ITEM_META, base.getItemMeta());
            }
        }
        if (complex.extraItems != null){
            ItemStack[] itemsNoNBT = new ItemStack[complex.extraItems.length];
            ItemMeta[] itemMetas = new ItemMeta[complex.extraItems.length];
            for (int i=0;i<complex.extraItems.length;i+=1){
                itemsNoNBT[i] = new ItemStack(complex.extraItems[i].getType());
                itemMetas[i] = complex.extraItems[i].getItemMeta();
            }
            container.set(EXTRA_ITEMS,DataType.ITEM_STACK_ARRAY,itemsNoNBT);
            container.set(EXTRA_ITEMS_META,DataType.ITEM_META_ARRAY,itemMetas);
        }
        if (complex.machineRecipeClassName != null){
            container.set(RECIPE_CLASS_STR, DataType.STRING, complex.machineRecipeClassName);
        }
        if (complex.ticks != 0){
            container.set(TICKS_INT, DataType.INTEGER, complex.ticks);
        }
        if (complex.energyPerTick != 0){
            container.set(ENERGY_PER_TICK_LONG, DataType.LONG, complex.energyPerTick);
        }
        if (complex.energyPerTickAtNight != 0){
            container.set(ENERGY_PER_TICK_AT_NIGHT_LONG, DataType.LONG, complex.energyPerTickAtNight);
        }
        if (complex.entityClassName != null){
            container.set(ENTITY_CLASS_STR, DataType.STRING ,complex.entityClassName);
        }
        if (complex.biome != null){
            container.set(BIOME_STR,DataType.STRING,complex.biome.name());
        }
        if (complex.inputs != null){
            ItemStack[] itemsNoNBT = new ItemStack[complex.inputs.length];
            ItemMeta[] itemMetas = new ItemMeta[complex.inputs.length];
            int[] itemAmounts = new int[complex.inputs.length];
            for (int i=0;i<complex.inputs.length;i+=1){
                itemsNoNBT[i] = new ItemStack(complex.inputs[i].getType());
                itemMetas[i] = complex.inputs[i].getItemMeta();
                itemAmounts[i] = complex.inputs[i].getAmount();
            }
            container.set(INPUTS_STACK,DataType.ITEM_STACK_ARRAY,itemsNoNBT);
            container.set(INPUTS_STACK_META,DataType.ITEM_META_ARRAY,itemMetas);
            container.set(INPUTS_STACK_AMOUNT,DataType.INTEGER_ARRAY,itemAmounts);
        }
        if (complex.outputs != null){
            ItemStack[] itemsNoNBT = new ItemStack[complex.outputs.length];
            ItemMeta[] itemMetas = new ItemMeta[complex.outputs.length];
            int[] itemAmounts = new int[complex.outputs.length];
            for (int i=0;i<complex.outputs.length;i+=1){
                itemsNoNBT[i] = new ItemStack(complex.outputs[i].getType());
                itemMetas[i] = complex.outputs[i].getItemMeta();
                itemAmounts[i] = complex.outputs[i].getAmount();
            }
            container.set(OUTPUTS_STACK,DataType.ITEM_STACK_ARRAY,itemsNoNBT);
            container.set(OUTPUTS_STACK_META,DataType.ITEM_META_ARRAY,itemMetas);
            container.set(OUTPUTS_STACK_AMOUNT,DataType.INTEGER_ARRAY,itemAmounts);
        }
        if (complex.env != null){
            container.set(ENVIRONMENT_STR, DataType.STRING,complex.env.name());
        }
        if (complex.Singularity_Material_amount != -1){
            container.set(SINGULARITY_MATERIAL_AMOUNT_INT,DataType.INTEGER,complex.Singularity_Material_amount);
        }
        if (complex.outputExpectations != null){
            int[] numeratorArray = new int[complex.outputExpectations.length];
            int[] denominatorArray = new int[complex.outputExpectations.length];
            for (int i=0;i<complex.outputExpectations.length;i+=1){
                numeratorArray[i]=complex.outputExpectations[i].numerator();
                denominatorArray[i]=complex.outputExpectations[i].denominator();
            }
            container.set(NUMERATOR_ARRAY,DataType.INTEGER_ARRAY,numeratorArray);
            container.set(DENOMINATOR_ARRAY,DataType.INTEGER_ARRAY,denominatorArray);
        }
        return container;
    }

    @Override
    @Nonnull
    public SerializedMachine_MachineRecipe fromPrimitive(@Nonnull PersistentDataContainer primitive, @Nonnull PersistentDataAdapterContext context) {
        SerializedMachine_MachineRecipe result = new SerializedMachine_MachineRecipe();

        if (primitive.has(BASED_ITEM,DataType.ITEM_STACK)){
            result.sfItemStack = primitive.get(BASED_ITEM,DataType.ITEM_STACK);
            if (primitive.has(BASED_ITEM_META,DataType.ITEM_META)){
                result.sfItemStack.setItemMeta(primitive.get(BASED_ITEM_META,DataType.ITEM_META));
            }
            result.sfItem = SlimefunItem.getByItem(result.sfItemStack);
        }
        if (primitive.has(INPUTS_STACK,DataType.ITEM_STACK_ARRAY)){
            result.inputs = primitive.get(INPUTS_STACK,DataType.ITEM_STACK_ARRAY);
            ItemMeta[] itemMetas = primitive.get(INPUTS_STACK_META,DataType.ITEM_META_ARRAY);
            int[] amounts = primitive.get(INPUTS_STACK_AMOUNT,DataType.INTEGER_ARRAY);
            for (int i=0;i<itemMetas.length;i+=1){
                result.inputs[i].setItemMeta(itemMetas[i]);
                result.inputs[i].setAmount(amounts[i]);
            }
        }
        if (primitive.has(OUTPUTS_STACK,DataType.ITEM_STACK_ARRAY)){
            result.outputs = primitive.get(OUTPUTS_STACK,DataType.ITEM_STACK_ARRAY);
            ItemMeta[] itemMetas = primitive.get(OUTPUTS_STACK_META,DataType.ITEM_META_ARRAY);
            int[] amounts = primitive.get(OUTPUTS_STACK_AMOUNT,DataType.INTEGER_ARRAY);
            for (int i=0;i<itemMetas.length;i+=1){
                result.outputs[i].setItemMeta(itemMetas[i]);
                result.outputs[i].setAmount(amounts[i]);
            }
        }
        if (primitive.has(RECIPE_CLASS_STR,DataType.STRING)){
            result.machineRecipeClassName = primitive.get(RECIPE_CLASS_STR,DataType.STRING);
        }
        if (primitive.has(ENTITY_CLASS_STR,DataType.STRING)){
            result.entityClassName = primitive.get(ENTITY_CLASS_STR,DataType.STRING);
        }
        if (primitive.has(BIOME_STR,DataType.STRING)){
            result.biome = Biome.valueOf(primitive.get(BIOME_STR,DataType.STRING));
        }
        if (primitive.has(ENVIRONMENT_STR,STRING)){
            result.env = World.Environment.valueOf(primitive.get(ENVIRONMENT_STR,DataType.STRING));
        }
        if (primitive.has(ENERGY_PER_TICK_LONG,DataType.LONG)){
            result.energyPerTick = primitive.get(ENERGY_PER_TICK_LONG, DataType.LONG);
        }
        if (primitive.has(ENERGY_PER_TICK_AT_NIGHT_LONG,DataType.LONG)){
            result.energyPerTickAtNight = primitive.get(ENERGY_PER_TICK_AT_NIGHT_LONG, DataType.LONG);
        }
        if (primitive.has(SINGULARITY_MATERIAL_AMOUNT_INT,DataType.INTEGER)){
            result.Singularity_Material_amount = primitive.get(SINGULARITY_MATERIAL_AMOUNT_INT,DataType.INTEGER);
        }
        if (primitive.has(TICKS_INT,DataType.INTEGER)){
            result.ticks = primitive.get(TICKS_INT,DataType.INTEGER);
        }
        if (primitive.has(NUMERATOR_ARRAY,DataType.INTEGER_ARRAY)){
            int[] numeratorArray = primitive.get(NUMERATOR_ARRAY,DataType.INTEGER_ARRAY);
            int[] denominatorArray = primitive.get(DENOMINATOR_ARRAY,DataType.INTEGER_ARRAY);
            IntegerRational[] outExpectations = new IntegerRational[numeratorArray.length];
            for (int i=0;i<numeratorArray.length;i+=1){
                outExpectations[i] = new IntegerRational(numeratorArray[i],denominatorArray[i]);
            }
            result.outputExpectations = outExpectations;
        }
        if (primitive.has(EXTRA_ITEMS,DataType.ITEM_STACK_ARRAY)){
            result.extraItems = primitive.get(EXTRA_ITEMS,DataType.ITEM_STACK_ARRAY);
            ItemMeta[] itemMetas = primitive.get(EXTRA_ITEMS_META,DataType.ITEM_META_ARRAY);
            for (int i=0;i<itemMetas.length;i+=1){
                result.extraItems[i].setItemMeta(itemMetas[i]);
            }
        }
        return result;
    }
}
