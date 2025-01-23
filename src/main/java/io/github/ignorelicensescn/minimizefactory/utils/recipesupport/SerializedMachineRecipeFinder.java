package io.github.ignorelicensescn.minimizefactory.utils.recipesupport;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SerializedMachineRecipeFinder {
    private static final Map<String, SerializedRecipeProvider<?>> serializedMachineRecipes_byID = new HashMap<>();
    private static final Map<String, SerializedRecipeProvider<?>> serializedMachineRecipes_byClassName = new HashMap<>();
    private static final Map<String, SerializedRecipeProvider<?>> serializedMachineRecipes_byPackageName = new HashMap<>();

    public static void registerSerializedRecipeProvider_byID(@Nonnull String ID,@Nonnull SerializedRecipeProvider<?> value){
        serializedMachineRecipes_byID.put(ID,value);
    }
    public static void registerSerializedRecipeProvider_byClassName(@Nonnull String className,@Nonnull SerializedRecipeProvider<?> value){
        serializedMachineRecipes_byClassName.put(className,value);
    }
    public static void registerSerializedRecipeProvider_byPackageName(@Nonnull String packageName,@Nonnull SerializedRecipeProvider<?> value){
        serializedMachineRecipes_byPackageName.put(packageName,value);
    }
    public static @Nullable SerializedRecipeProvider<?> getSerializedRecipeProviderForMachine(@Nonnull SlimefunItem sfItem){
        if (sfItem instanceof SerializedRecipeProvider<?> rp){
            return rp;
        }
        SerializedRecipeProvider<?> result = serializedMachineRecipes_byID.getOrDefault(sfItem.getId(),null);
        if (result != null){return result;}
        if (Objects.equals(sfItem.getClass().getName(),SlimefunItem.class.getName())){
            return null;
        }
        Class<?> currentClass = sfItem.getClass();
        while (!Objects.equals(SlimefunItem.class,currentClass) && !Objects.equals(Object.class,currentClass)){
            result = serializedMachineRecipes_byClassName.getOrDefault(currentClass.getName(),null);
            if (result != null){
                return result;
            }
            currentClass = currentClass.getSuperclass();
        }
        currentClass = sfItem.getClass();
        while (!Objects.equals(SlimefunItem.class,currentClass) && !Objects.equals(Object.class,currentClass)){
            result = serializedMachineRecipes_byPackageName.getOrDefault(currentClass.getPackageName(),null);
            if (result != null){
                return result;
            }
            currentClass = currentClass.getSuperclass();
        }

        return null;
    }
}
