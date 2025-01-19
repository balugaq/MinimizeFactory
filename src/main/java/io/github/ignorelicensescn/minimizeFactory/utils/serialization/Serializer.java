package io.github.ignorelicensescn.minimizeFactory.utils.serialization;

import io.github.ignorelicensescn.minimizeFactory.utils.mathutils.BigRational;

import java.io.*;
import java.math.BigInteger;
import java.util.Base64;

//T:Serializable
public interface Serializer<T extends Serializable> extends UnifiedSerializer<T> {
    Serializer<BigRational> BIG_RATIONAL_SERIALIZER = new Serializer<>() {};
    Serializer<BigRational[]> BIG_RATIONAL_ARRAY_SERIALIZER = new Serializer<>() {};
    Serializer<BigInteger> BIG_INTEGER_SERIALIZER = new Serializer<>() {};
    Serializer<BigInteger[]> BIG_INTEGER_ARRAY_SERIALIZER = new Serializer<>() {};
    Serializer<String[]> STRING_ARRAY_SERIALIZER = new Serializer<>() {};

    @Override
    default String SerializableToString(T what){

        try (ByteArrayOutputStream bo = new ByteArrayOutputStream();
             ObjectOutputStream so = new ObjectOutputStream(bo)){
            so.writeObject(what);
            so.flush();
            return Base64.getEncoder().encodeToString(bo.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    default T StringToSerializable(String data){
        try (ObjectInputStream stream = new ObjectInputStream(
                new ByteArrayInputStream(
                        Base64.getDecoder().decode(data)
                )
        )
        ){
            return (T) stream.readObject();
        }catch (ClassNotFoundException | IOException | ClassCastException e){
            e.printStackTrace();
        }
        return null;
    }
}
