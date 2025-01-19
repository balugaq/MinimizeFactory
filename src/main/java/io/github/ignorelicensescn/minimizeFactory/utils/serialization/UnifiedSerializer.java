package io.github.ignorelicensescn.minimizeFactory.utils.serialization;

import org.bukkit.inventory.ItemStack;

import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.BukkitSerializer.ITEM_STACK_SERIALIZER;
import static io.github.ignorelicensescn.minimizeFactory.utils.serialization.Serializer.STRING_ARRAY_SERIALIZER;

/**
 * all the data will be stored into a database
 * @param <T> supported serializable class
 */
public interface UnifiedSerializer<T> {

    UnifiedSerializer<ItemStack[]> ITEM_STACK_ARRAY_SERIALIZER = new UnifiedSerializer<>(){

        @Override
        public String SerializableToString(ItemStack[] from) {
            String[] toStringArray = new String[from.length];
            for (int i=0;i<from.length;i+=1){
                toStringArray[i] = ITEM_STACK_SERIALIZER.SerializableToString(from[i]);
            }
            return STRING_ARRAY_SERIALIZER.SerializableToString(toStringArray);
        }

        @Override
        public ItemStack[] StringToSerializable(String from) {
            String[] itemStackStrArray = STRING_ARRAY_SERIALIZER.StringToSerializable(from);
            ItemStack[] result = new ItemStack[itemStackStrArray.length];
            for (int i=0;i<itemStackStrArray.length;i+=1){
                result[i] = ITEM_STACK_SERIALIZER.StringToSerializable(itemStackStrArray[i]);
            }
            return result;
        }
    };

    /**
     * returns a string that can be converted into "from" object
     * @param from this object will be converted into string(can be uuid)
     * @return string that can be converted to "from" object(you can even return uuid if {@code StringToSerializable} can read)
     */
    String SerializableToString(T from);

    /**
     * convert string to object(inverse-mapping of SerializableToString)
     * @param from the string from {@code SerializableToString}
     * @return object before converted by {@code SerializableToString}
     */
    T StringToSerializable(String from);
}
