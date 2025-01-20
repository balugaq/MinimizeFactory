package io.github.ignorelicensescn.minimizeFactory.utils.serialization;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public interface BukkitSerializer<T extends ConfigurationSerializable> extends UnifiedSerializer<T> {
    BukkitSerializer<ItemStack> ITEM_STACK_SERIALIZER = new BukkitSerializer<>(){};
    BukkitSerializer<Location> LOCATION_SERIALIZER = new BukkitSerializer<>() {
    };

    @Override
    default String SerializableToString(T what){
        try (ByteArrayOutputStream bo = new ByteArrayOutputStream();
             BukkitObjectOutputStream so = new BukkitObjectOutputStream(bo)){
            so.writeObject(what);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    default T StringToSerializable(String data){
        try (BukkitObjectInputStream stream = new BukkitObjectInputStream(
                new ByteArrayInputStream(
                        Base64.getDecoder().decode(data)
                )
        )){
            return (T) stream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
