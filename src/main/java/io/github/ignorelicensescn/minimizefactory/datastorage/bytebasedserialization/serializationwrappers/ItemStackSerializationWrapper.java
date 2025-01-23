package io.github.ignorelicensescn.minimizefactory.datastorage.bytebasedserialization.serializationwrappers;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static io.github.thebusybiscuit.slimefun4.libraries.commons.lang.ArrayUtils.EMPTY_BYTE_ARRAY;

public class ItemStackSerializationWrapper {

    private byte[] bytes;
    //for kryo
    private ItemStackSerializationWrapper(){}
    private ItemStackSerializationWrapper(byte[] bytes){
        this.bytes = bytes;
    }

    public ItemStack toItemStack(){
        try (BukkitObjectInputStream so = new BukkitObjectInputStream(new ByteArrayInputStream(bytes))){
            return (ItemStack) so.readObject();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStackSerializationWrapper fromItemStack(ItemStack from){
        from = new ItemStack(from);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream so = new BukkitObjectOutputStream(byteArrayOutputStream)){
            so.writeObject(from);
            so.flush();
            return new ItemStackSerializationWrapper(byteArrayOutputStream.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
