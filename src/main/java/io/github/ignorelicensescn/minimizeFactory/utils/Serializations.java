package io.github.ignorelicensescn.minimizeFactory.utils;

import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.math.BigInteger;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.logger;

public class Serializations {
    public static String LocationToString(Location location){
        JSONObject jsonObject = new JSONObject();
        if (location.getWorld() != null){
            jsonObject.put("world", location.getWorld().getUID().toString());
        }else {
            jsonObject.put("world","NULL");
        }
        jsonObject.put("x",location.getX());
        jsonObject.put("y",location.getY());
        jsonObject.put("z",location.getZ());
        jsonObject.put("yaw",location.getYaw());
        jsonObject.put("pitch",location.getPitch());
        return jsonObject.toString();
    }

    public static Location StringToLocation(String locationString){
        try {
            JSONObject jsonObject = new JSONObject(locationString);
            World world = Bukkit.getWorld(UUID.fromString(jsonObject.getString("world")));
            double x = jsonObject.getDouble("x");
            double y = jsonObject.getDouble("y");
            double z = jsonObject.getDouble("z");
            float yaw = jsonObject.getFloat("yaw");
            float pitch = jsonObject.getFloat("pitch");
            return new Location(world, x, y, z, yaw, pitch);
        }catch (Exception e){
            return null;
        }
    }
    public static String ItemStackToString(ItemStack what){
        return BukkitSerializableToString(what);
    }
    public static ItemStack StringToItemStack(String data){
        return (ItemStack) StringToBukkitSerializable(data);
    }
    public static String ItemStackArrayToString(ItemStack[] what){
        String[] serialized = new String[what.length];
        for (int i=0;i<what.length;i++){
            serialized[i] = ItemStackToString(what[i]);//we can only hope it (Serializable)
        }
        return SerializableToString(serialized);
    }
    public static ItemStack[] StringToItemStackArray(String data){
        String[] serialized = (String[]) StringToSerializable(data);
        ItemStack[] result = new ItemStack[serialized.length];
        for (int i=0;i<serialized.length;i++){
            result[i] = StringToItemStack(serialized[i]);
        }
        return result;
    }
    public static String BigIntegerDividingsToString(BigInteger[][] what){

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(what);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static BigInteger[][] StringToBigIntegerDividings(String data){
        try {
            return (BigInteger[][]) new ObjectInputStream(
                    new ByteArrayInputStream(
                            Base64.getDecoder().decode(data)
                    )
            ).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String SerializableToString(Serializable what){

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(what);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Serializable StringToSerializable(String data){
        try {
            return (Serializable) new ObjectInputStream(
                    new ByteArrayInputStream(
                            Base64.getDecoder().decode(data)
                    )
            ).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String BukkitSerializableToString(ConfigurationSerializable what){

        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            BukkitObjectOutputStream so = new BukkitObjectOutputStream(bo);
            so.writeObject(what);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ConfigurationSerializable StringToBukkitSerializable(String data){
        try {
            return (ConfigurationSerializable) new BukkitObjectInputStream(
                    new ByteArrayInputStream(
                            Base64.getDecoder().decode(data)
                    )
            ).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
