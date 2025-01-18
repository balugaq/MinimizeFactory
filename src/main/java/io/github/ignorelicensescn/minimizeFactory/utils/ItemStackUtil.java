package io.github.ignorelicensescn.minimizeFactory.utils;

import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.DividingsOperation;
import io.github.ignorelicensescn.minimizeFactory.utils.mathUtils.IntegerRational;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimpleFour;
import io.github.ignorelicensescn.minimizeFactory.utils.simpleStructure.SimplePair;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.*;

import static io.github.ignorelicensescn.minimizeFactory.utils.compabilities.InfinityExpansion.InfinityExpansionConsts.emptyItemStackArray;

public class ItemStackUtil {
    public static ItemStack[] collapseItems(ItemStack[] array,ItemStack... additional){
        ItemStack[] allTogether = new ItemStack[array.length + additional.length];
        System.arraycopy(array, 0, allTogether, 0, array.length);
        System.arraycopy(additional, 0, allTogether, array.length, additional.length);
        return collapseItems(allTogether);
    }
    public static ItemStack[] collapseItems(ItemStack[] array){
        Map<ItemStack,Integer> amounts = new HashMap<>();
        for (ItemStack i:array){
            if (i == null){continue;}
            i = i.clone();
            int amount = i.getAmount();
            i.setAmount(1);
            amounts.put(i,amounts.getOrDefault(i,0) + amount);
        }
        Set<Map.Entry<ItemStack,Integer>> entries = amounts.entrySet();
        int counter = 0;
        ItemStack[] inputs = new ItemStack[entries.size()];
        for (Map.Entry<ItemStack,Integer> itemAndAmount:entries){
            inputs[counter] = itemAndAmount.getKey();
            inputs[counter].setAmount(itemAndAmount.getValue());
            counter += 1;
        }
        return inputs;
    }
    public static final IntegerRational[] emptyDividingsArray = new IntegerRational[0];
    public static final BigInteger[][] emptyDividingsArray_BigInteger = new BigInteger[0][2];
    public static ItemStack[] reformatItemStackArray(ItemStack[] itemStacks){
            ItemStack[] itemCache = new ItemStack[itemStacks.length];
            int[] itemCounts = new int[itemStacks.length];
            int validItems = 0;
            for (ItemStack itemStack : itemStacks) {
                if (itemStack == null) {
                    continue;
                }
                itemStack = itemStack.clone();
                int amount = itemStack.getAmount();
                itemStack.setAmount(1);
                boolean continueFlag = false;
                for (int j = 0; j < validItems; j++) {
                    if (isItemStackSimilar(itemStack, itemCache[j])) {
                        itemCounts[j] = itemCounts[j] + amount;
                        continueFlag = true;
                        break;
                    }
                }
                if (continueFlag) {
                    continue;
                }
                itemCache[validItems] = itemStack;
                itemCounts[validItems] = amount;
                validItems += 1;
            }
            int length = 0;
            itemCache = Arrays.copyOfRange(itemCache, 0, validItems);
            itemCounts = Arrays.copyOfRange(itemCounts, 0, validItems);
            for (int j = 0; j < validItems; j++) {
                ItemStack itemStack = itemCache[j];
                int max = itemStack.getMaxStackSize();
                length += (itemCounts[j] / max)
                        + (
                                (itemCounts[j] % max) > 0 ? 1 : 0
                );
            }
            ItemStack[] iStacks = new ItemStack[length];
            int counter = 0;
            for (int i = 0; i < validItems; i++) {
                ItemStack itemStack = itemCache[i];
                int itemMax = itemStack.getMaxStackSize();
                for (int j = 0; j < itemCounts[i] / itemStack.getMaxStackSize(); j++) {
                    ItemStack i1 = itemStack.clone();
                    i1.setAmount(itemMax);
                    iStacks[counter] = i1;
                    counter += 1;
                }
                if (
                        (itemCounts[i] % itemStack.getMaxStackSize()) > 0) {
                    ItemStack i1 = itemStack.clone();
                    i1.setAmount(itemCounts[i] % itemStack.getMaxStackSize());
                    iStacks[counter] = i1;
                    counter += 1;
                }
            }
            return iStacks;

    }
    public static SimplePair<ItemStack[],int[]> reformatItemStackArrayWithAmount(ItemStack[] itemStacks){
        ItemStack[] itemCache = new ItemStack[itemStacks.length];
        int[] itemCounts = new int[itemStacks.length];
        int validItems = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }
            itemStack = itemStack.clone();
            int amount = itemStack.getAmount();
            itemStack.setAmount(1);
            boolean continueFlag = false;
            for (int j = 0; j < validItems; j++) {
                if (isItemStackSimilar(itemStack, itemCache[j])) {
                    itemCounts[j] = itemCounts[j] + amount;
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag) {
                continue;
            }
            itemCache[validItems] = itemStack;
            itemCounts[validItems] = amount;
            validItems += 1;
        }
        itemCache = Arrays.copyOfRange(itemCache, 0, validItems);
        itemCounts = Arrays.copyOfRange(itemCounts, 0, validItems);

        return new SimplePair<>(itemCache,itemCounts);

    }
    public static SimplePair<ItemStack[],IntegerRational[]> reformatItemStackArrayWithAmountDividing(ItemStack[] itemStacks){
        ItemStack[] itemCache = new ItemStack[itemStacks.length];
        IntegerRational[] itemCounts = new IntegerRational[itemStacks.length];
        int validItems = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }
            itemStack = itemStack.clone();
            int amount = itemStack.getAmount();
            itemStack.setAmount(1);
            boolean continueFlag = false;
            for (int j = 0; j < validItems; j++) {
                if (isItemStackSimilar(itemStack, itemCache[j])) {
                    itemCounts[j] = new IntegerRational(itemCounts[j].numerator() + amount,itemCounts[j].denominator());
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag) {
                continue;
            }
            itemCache[validItems] = itemStack;
            itemCounts[validItems] = new IntegerRational(amount,1);
            validItems += 1;
        }
        itemCache = Arrays.copyOfRange(itemCache, 0, validItems);
        itemCounts = Arrays.copyOfRange(itemCounts, 0, validItems);

        return new SimplePair<>(itemCache,itemCounts);

    }
    public static SimplePair<ItemStack[],IntegerRational[]> reformatItemStackArrayWithAmount(ItemStack[] itemStacks,IntegerRational[] amount){
        ItemStack[] itemCache = new ItemStack[itemStacks.length];
        IntegerRational[] itemCounts = new IntegerRational[itemStacks.length];
        int validItems = 0;
        for (int t=0;t<itemStacks.length;t++) {
            if (itemStacks[t] == null || amount[t].numerator() == 0) {
                continue;
            }
            ItemStack itemStack = itemStacks[t].clone();
            IntegerRational iAmount = amount[t];
            itemStack.setAmount(1);
            boolean continueFlag = false;
            for (int j = 0; j < validItems; j++) {
                if (isItemStackSimilar(itemStack, itemCache[j])) {
                    itemCounts[j] = itemCounts[j].add(iAmount);
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag) {
                continue;
            }
            itemCache[validItems] = itemStack;
            itemCounts[validItems] = iAmount;
            validItems += 1;
        }
        itemCache = Arrays.copyOfRange(itemCache, 0, validItems);
        itemCounts = Arrays.copyOfRange(itemCounts, 0, validItems);

        return new SimplePair<>(itemCache,itemCounts);

    }

    /**
     * u should reformat inputs and outputs first
     */
    public static SimpleFour<ItemStack[],IntegerRational[],ItemStack[],IntegerRational[]> reformatInputAndOutputs(ItemStack[] inputs, ItemStack[] outputs){
        IntegerRational[] outputAmount = new IntegerRational[outputs.length];
        for (int i=0;i<outputs.length;i++){
            outputAmount[i] = new IntegerRational(outputs[i].getAmount(),1);
        }
        return reformatInputAndOutputs(inputs,outputs, outputAmount);
    }
    public static SimpleFour<ItemStack[],IntegerRational[],ItemStack[],IntegerRational[]> reformatInputAndOutputs(ItemStack[] inputs, IntegerRational[] inputAmount, ItemStack[] outputs){
        IntegerRational[] outputAmount = new IntegerRational[outputs.length];
        for (int i=0;i<outputs.length;i++){
            outputAmount[i] = new IntegerRational(outputs[i].getAmount(),1);
        }
        return reformatInputAndOutputs(inputs,inputAmount,outputs, outputAmount);
    }
    public static SimpleFour<ItemStack[],IntegerRational[],ItemStack[],IntegerRational[]> reformatInputAndOutputs(ItemStack[] inputs, ItemStack[] outputs, IntegerRational[] outputAmount){
        IntegerRational[] inputAmount = new IntegerRational[inputs.length];
        for (int i=0;i<inputs.length;i++){
            inputAmount[i] = new IntegerRational(inputs[i].getAmount(),1);
        }
        return reformatInputAndOutputs(inputs,inputAmount, outputs, outputAmount);
    }
    public static SimpleFour<ItemStack[],IntegerRational[],ItemStack[],IntegerRational[]> reformatInputAndOutputs(ItemStack[] inputs, IntegerRational[] inputAmount, ItemStack[] outputs, IntegerRational[] outputAmount){
        IntegerRational[] inputAmountCache = inputAmount.clone();
        IntegerRational[] outputAmountCache = outputAmount.clone();
        List<ItemStack> inCache = new ArrayList<>();
        List<IntegerRational> inAmountCache = new ArrayList<>();
        List<ItemStack> outCache = new ArrayList<>();
        List<IntegerRational> outAmountCache = new ArrayList<>();
        for (int inIndex=0;inIndex<inputs.length;inIndex++){
            for (int outIndex=0;outIndex<outputs.length;outIndex++){
                if (isItemStackSimilar(inputs[inIndex],outputs[outIndex])){
                    if (inputAmountCache[inIndex].lessThan(outputAmountCache[outIndex])){
                        outputAmountCache[outIndex] = outputAmountCache[outIndex].sub(inputAmountCache[inIndex]);
                        inputAmountCache[inIndex] = IntegerRational.ZERO;
                    }else {
                        inputAmountCache[inIndex] = inputAmountCache[inIndex].sub(outputAmountCache[outIndex]);
                        outputAmountCache[outIndex] = IntegerRational.ZERO;
                    }
                }
            }
        }
        for (int i=0;i<inputAmountCache.length;i++){
            if (inputAmountCache[i].numerator() != 0){
                inCache.add(inputs[i]);
                inAmountCache.add(inputAmountCache[i]);
            }
        }
        for (int i=0;i<outputAmountCache.length;i++){
            if (outputAmountCache[i].numerator() != 0){
                outCache.add(outputs[i]);
                outAmountCache.add(outputAmountCache[i]);
            }
        }
        return new SimpleFour<>(inCache.toArray(emptyItemStackArray),
                inAmountCache.toArray(emptyDividingsArray),
                outCache.toArray(emptyItemStackArray),
                outAmountCache.toArray(emptyDividingsArray)
        );
    }
    public static SimplePair<ItemStack[],IntegerRational[]> addItemStacks(ItemStack[] itemStacks, ItemStack[] addItemStack){
        IntegerRational[] addItemAmounts = new IntegerRational[addItemStack.length];
        for (int i=0;i<addItemStack.length;i++){
            addItemAmounts[i] = new IntegerRational(addItemStack[i].getAmount(),1);
        }
        return addItemStacks(itemStacks,addItemStack,addItemAmounts);
    }
    public static SimplePair<ItemStack[],IntegerRational[]> addItemStacks(ItemStack[] itemStacks, IntegerRational[] itemAmounts, ItemStack[] addItemStack){
        IntegerRational[] addItemAmounts = new IntegerRational[addItemStack.length];
        for (int i=0;i<addItemStack.length;i++){
            addItemAmounts[i] = new IntegerRational(addItemStack[i].getAmount(),1);
        }
        return addItemStacks(itemStacks,itemAmounts,addItemStack,addItemAmounts);
    }
    public static SimplePair<ItemStack[],IntegerRational[]> addItemStacks(ItemStack[] itemStacks, ItemStack[] addItemStack,IntegerRational[] addItemAmounts){
        IntegerRational[] itemAmounts = new IntegerRational[itemStacks.length];
        for (int i=0;i<itemStacks.length;i++){
            itemAmounts[i] = new IntegerRational(itemStacks[i].getAmount(),1);
        }
        return addItemStacks(itemStacks,itemAmounts,addItemStack,addItemAmounts);
    }
    public static SimplePair<ItemStack[],IntegerRational[]> addItemStacks(ItemStack[] itemStacks, IntegerRational[] itemAmounts, ItemStack[] addItemStack,IntegerRational[] addItemAmounts){
        List<ItemStack> itemStacksCache = new ArrayList<>(Arrays.asList(itemStacks.clone()));
        IntegerRational[] itemAmountsCache = itemAmounts.clone();
        List<ItemStack> extraItems = new ArrayList<>();
        List<IntegerRational> extraAmounts = new ArrayList<>();
        for (int addIndex=0;addIndex < addItemStack.length;addIndex++){
            boolean continueFlag = false;
            for (int itemIndex=0;itemIndex < itemStacks.length;itemIndex++){
                if (isItemStackSimilar(itemStacks[itemIndex],addItemStack[addIndex])){
                    itemAmountsCache[itemIndex] = itemAmounts[itemIndex].add(addItemAmounts[addIndex]);
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag){continue;}
            extraItems.add(addItemStack[addIndex]);
            extraAmounts.add(addItemAmounts[addIndex]);
        }
        List<IntegerRational> resultAmount = new ArrayList<>(Arrays.asList(itemAmountsCache));
        resultAmount.addAll(extraAmounts);
        itemStacksCache.addAll(extraItems);
        return new SimplePair<>(itemStacksCache.toArray(emptyItemStackArray),resultAmount.toArray(emptyDividingsArray));
    }

    public static SimplePair<ItemStack[],BigInteger[]> reformatItemStackArrayWithAmount_BigInteger(ItemStack[] itemStacks){
        ItemStack[] itemCache = new ItemStack[itemStacks.length];
        BigInteger[] itemCounts = new BigInteger[itemStacks.length];
        int validItems = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }
            itemStack = itemStack.clone();
            int amount = itemStack.getAmount();
            itemStack.setAmount(1);
            boolean continueFlag = false;
            for (int j = 0; j < validItems; j++) {
                if (isItemStackSimilar(itemStack, itemCache[j])) {
                    itemCounts[j] = itemCounts[j].add(BigInteger.valueOf(amount));
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag) {
                continue;
            }
            itemCache[validItems] = itemStack;
            itemCounts[validItems] = BigInteger.valueOf(amount);
            validItems += 1;
        }
        itemCache = Arrays.copyOfRange(itemCache, 0, validItems);
        itemCounts = Arrays.copyOfRange(itemCounts, 0, validItems);

        return new SimplePair<>(itemCache,itemCounts);

    }
    public static SimplePair<ItemStack[],BigInteger[][]> reformatItemStackArrayWithAmountDividing_BigInteger(ItemStack[] itemStacks){
        ItemStack[] itemCache = new ItemStack[itemStacks.length];
        BigInteger[][] itemCounts = new BigInteger[itemStacks.length][2];
        int validItems = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) {
                continue;
            }
            itemStack = itemStack.clone();
            int amount = itemStack.getAmount();
            itemStack.setAmount(1);
            boolean continueFlag = false;
            for (int j = 0; j < validItems; j++) {
                if (isItemStackSimilar(itemStack, itemCache[j])) {
                    itemCounts[j][0] = itemCounts[j][0].add(BigInteger.valueOf(amount));
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag) {
                continue;
            }
            itemCache[validItems] = itemStack;
            itemCounts[validItems][0] = BigInteger.valueOf(amount);
            itemCounts[validItems][1] = BigInteger.ONE;
            validItems += 1;
        }
        itemCache = Arrays.copyOfRange(itemCache, 0, validItems);
        itemCounts = Arrays.copyOfRange(itemCounts, 0, validItems);

        return new SimplePair<>(itemCache,itemCounts);

    }
    public static SimplePair<ItemStack[],BigInteger[][]> reformatItemStackArrayWithAmount_BigInteger(ItemStack[] itemStacks,BigInteger[][] amount){
        ItemStack[] itemCache = new ItemStack[itemStacks.length];
        BigInteger[][] itemCounts = new BigInteger[itemStacks.length][2];
        int validItems = 0;
        for (int t=0;t<itemStacks.length;t++) {
            if (itemStacks[t] == null || Objects.equals(amount[t][0], BigInteger.ZERO)) {
                continue;
            }
            ItemStack itemStack = itemStacks[t].clone();
            BigInteger[] iAmount = amount[t];
            itemStack.setAmount(1);
            boolean continueFlag = false;
            for (int j = 0; j < validItems; j++) {
                if (isItemStackSimilar(itemStack, itemCache[j])) {
                    itemCounts[j] = DividingsOperation.sumOfDividings(itemCounts[j],iAmount);
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag) {
                continue;
            }
            itemCache[validItems] = itemStack;
            itemCounts[validItems] = DividingsOperation.simplify(iAmount);
            validItems += 1;
        }
        itemCache = Arrays.copyOfRange(itemCache, 0, validItems);
        itemCounts = Arrays.copyOfRange(itemCounts, 0, validItems);

        return new SimplePair<>(itemCache,itemCounts);

    }

    /**
     * u should reformat inputs and outputs first
     */
    public static SimpleFour<ItemStack[],BigInteger[][],ItemStack[],BigInteger[][]> reformatInputAndOutputs_BigInteger(ItemStack[] inputs, ItemStack[] outputs){
        BigInteger[][] outputAmount = new BigInteger[outputs.length][2];
        for (int i=0;i<outputs.length;i++){
            outputAmount[i] = new BigInteger[]{BigInteger.valueOf(outputs[i].getAmount()),BigInteger.ONE};
        }
        return reformatInputAndOutputs_BigInteger(inputs,outputs, outputAmount);
    }
    public static SimpleFour<ItemStack[],BigInteger[][],ItemStack[],BigInteger[][]> reformatInputAndOutputs_BigInteger(ItemStack[] inputs, BigInteger[][] inputAmount, ItemStack[] outputs){
        BigInteger[][] outputAmount = new BigInteger[outputs.length][2];
        for (int i=0;i<outputs.length;i++){
            outputAmount[i] = new BigInteger[]{BigInteger.valueOf(outputs[i].getAmount()),BigInteger.ONE};
        }
        return reformatInputAndOutputs_BigInteger(inputs,inputAmount,outputs, outputAmount);
    }
    public static SimpleFour<ItemStack[],BigInteger[][],ItemStack[],BigInteger[][]> reformatInputAndOutputs_BigInteger(ItemStack[] inputs, ItemStack[] outputs, BigInteger[][] outputAmount){
        BigInteger[][] inputAmount = new BigInteger[inputs.length][2];
        for (int i=0;i<inputs.length;i++){
            inputAmount[i] = new BigInteger[]{BigInteger.valueOf(inputs[i].getAmount()),BigInteger.ONE};
        }
        return reformatInputAndOutputs_BigInteger(inputs,inputAmount, outputs, outputAmount);
    }
    public static SimpleFour<ItemStack[],BigInteger[][],ItemStack[],BigInteger[][]> reformatInputAndOutputs_BigInteger(ItemStack[] inputs, BigInteger[][] inputAmount, ItemStack[] outputs, BigInteger[][] outputAmount){
        BigInteger[][] inputAmountCache = inputAmount.clone();
        BigInteger[][] outputAmountCache = outputAmount.clone();
        for (int i = 0; i < inputAmountCache.length; i++) {
            inputAmountCache[i] = inputAmountCache[i].clone();
        }
        for (int i = 0; i < outputAmountCache.length; i++) {
            outputAmountCache[i] = outputAmountCache[i].clone();
        }
        List<ItemStack> inCache = new ArrayList<>();
        List<BigInteger[]> inAmountCache = new ArrayList<>();
        List<ItemStack> outCache = new ArrayList<>();
        List<BigInteger[]> outAmountCache = new ArrayList<>();
        for (int inIndex=0;inIndex<inputs.length;inIndex++){
            for (int outIndex=0;outIndex<outputs.length;outIndex++){
                if (isItemStackSimilar(inputs[inIndex],outputs[outIndex])){
                    if (DividingsOperation.less(inputAmountCache[inIndex],outputAmountCache[outIndex])){
                        outputAmountCache[outIndex] = DividingsOperation.sub(outputAmountCache[outIndex],inputAmountCache[inIndex]);
                        inputAmountCache[inIndex][0] = BigInteger.ZERO;
                        break;
                    }else {
                        inputAmountCache[inIndex] = DividingsOperation.sub(inputAmountCache[inIndex],outputAmountCache[outIndex]);
                        outputAmountCache[outIndex][0] = BigInteger.ZERO;
                    }
                }
            }
        }
        for (int i=0;i<inputAmountCache.length;i++){
            if (!Objects.equals(inputAmountCache[i][0], BigInteger.ZERO)){
                inCache.add(inputs[i]);
                inAmountCache.add(inputAmountCache[i]);
            }
        }
        for (int i=0;i<outputAmountCache.length;i++){
            if (!Objects.equals(outputAmountCache[i][0], BigInteger.ZERO)){
                outCache.add(outputs[i]);
                outAmountCache.add(outputAmountCache[i]);
            }
        }
        return new SimpleFour<>(inCache.toArray(emptyItemStackArray),
                inAmountCache.toArray(emptyDividingsArray_BigInteger),
                outCache.toArray(emptyItemStackArray),
                outAmountCache.toArray(emptyDividingsArray_BigInteger)
        );
    }
    public static SimplePair<ItemStack[],BigInteger[][]> addItemStacks_BigInteger(ItemStack[] itemStacks, ItemStack[] addItemStack,BigInteger multiplier){
        BigInteger[][] addItemAmounts = new BigInteger[addItemStack.length][2];
        for (int i=0;i<addItemStack.length;i++){
            addItemAmounts[i] = new BigInteger[]{BigInteger.valueOf(addItemStack[i].getAmount()),BigInteger.ONE};
        }
        return addItemStacks_BigInteger(itemStacks,addItemStack,addItemAmounts,multiplier);
    }
    public static SimplePair<ItemStack[],BigInteger[][]> addItemStacks_BigInteger(ItemStack[] itemStacks, BigInteger[][] itemAmounts, ItemStack[] addItemStack,BigInteger multiplier){
        BigInteger[][] addItemAmounts = new BigInteger[addItemStack.length][2];
        for (int i=0;i<addItemStack.length;i++){
            addItemAmounts[i] = new BigInteger[]{BigInteger.valueOf(addItemStack[i].getAmount()).multiply(multiplier),BigInteger.ONE};
        }
        return addItemStacks_BigInteger(itemStacks,itemAmounts,addItemStack,addItemAmounts,multiplier);
    }
    public static SimplePair<ItemStack[],BigInteger[][]> addItemStacks_BigInteger(ItemStack[] itemStacks, ItemStack[] addItemStack,BigInteger[][] addItemAmounts,BigInteger multiplier){
        BigInteger[][] itemAmounts = new BigInteger[itemStacks.length][2];
        for (int i=0;i<itemStacks.length;i++){
            itemAmounts[i] = new BigInteger[]{BigInteger.valueOf(itemStacks[i].getAmount()),BigInteger.ONE};
        }
        return addItemStacks_BigInteger(itemStacks,itemAmounts,addItemStack,addItemAmounts,multiplier);
    }
    public static SimplePair<ItemStack[],BigInteger[][]> addItemStacks_BigInteger(ItemStack[] itemStacks, BigInteger[][] itemAmounts, ItemStack[] addItemStack,BigInteger[][] addItemAmounts,BigInteger multiplier){
        List<ItemStack> itemStacksCache = new ArrayList<>(Arrays.asList(itemStacks.clone()));
        BigInteger[][] itemAmountsCache = itemAmounts.clone();
        List<ItemStack> extraItems = new ArrayList<>();
        List<BigInteger[]> extraAmounts = new ArrayList<>();
        for (int addIndex=0;addIndex < addItemStack.length;addIndex++){
            boolean continueFlag = false;
            for (int itemIndex=0;itemIndex < itemStacks.length;itemIndex++){
                if (isItemStackSimilar(itemStacks[itemIndex],addItemStack[addIndex])){
                    itemAmountsCache[itemIndex] = DividingsOperation.sumOfDividings(itemAmounts[itemIndex],addItemAmounts[addIndex]);
                    continueFlag = true;
                    break;
                }
            }
            if (continueFlag){continue;}
            extraItems.add(addItemStack[addIndex]);
            extraAmounts.add(addItemAmounts[addIndex]);
        }
        List<BigInteger[]> resultAmount = new ArrayList<>(Arrays.asList(itemAmountsCache));
        resultAmount.addAll(extraAmounts);
        itemStacksCache.addAll(extraItems);
        return new SimplePair<>(itemStacksCache.toArray(emptyItemStackArray),resultAmount.toArray(emptyDividingsArray_BigInteger));
    }

    //does not check amount
    public static boolean isItemStackSimilar(ItemStack i1,ItemStack i2){
        return SlimefunUtils.isItemSimilar(i1, i2, false, false, true);
//        i1 = i1.clone();
//        i1.setAmount(1);
//        i2 = i2.clone();
//        i2.setAmount(1);
//        ItemMeta meta1 = i1.getItemMeta();
//        ItemMeta meta2 = i2.getItemMeta();
//        if (( (meta1 == null) && !(meta2 == null) )
//                || ( !(meta1 == null) && (meta2 == null) )
//        ){return false;}//different item meta stats
//
//        //same item meta stats
//        if (meta1 != null){//both have
//            SlimefunItem sfi1 = SlimefunItem.getByItem(i1);
//            SlimefunItem sfi2 = SlimefunItem.getByItem(i2);
//            if ((sfi1 != null) && (sfi2 != null))
//            {
//                return SlimefunUtils.isItemSimilar(i2, i1, false, false, true);
//            }
//            return Objects.equals(i1,i2);
//        }
//
//        return Objects.equals(i1,i2);

//        return i1.getType().equals(i2.getType());
    }

    public static ItemStack[] RecipeChoiceListToItemStackArray(List<RecipeChoice> recipeChoices){
        ItemStack[] itemStacks = new ItemStack[recipeChoices.size()];
        int counter = 0;
        for (RecipeChoice rc:recipeChoices){
            itemStacks[counter] = rc.getItemStack();
            counter += 1;
        }
        return itemStacks;
    }
    public static ItemStack[] RecipeChoiceListToItemStackArray_formated(List<RecipeChoice> recipeChoices){
        List<ItemStack> itemStacks = new ArrayList<>();
        for (RecipeChoice rc:recipeChoices){
            if (rc == null){continue;}
            itemStacks.add(rc.getItemStack());
        }
        return collapseItems(itemStacks.toArray(emptyItemStackArray));
    }

    public static SimplePair<ItemStack[], BigInteger[][]> addItemStacks_BigInteger(ItemStack[] first, BigInteger[][] second, ItemStack[] first1, BigInteger[][] second1) {
        return addItemStacks_BigInteger(first, second, first1, second1,BigInteger.ONE);
    }

    public static void setItemStackArrayAmount(ItemStack[] itemStacks,int amount){
        for (ItemStack i:itemStacks){
            i.setAmount(amount);
        }
    }
}
