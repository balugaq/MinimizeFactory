package io.github.ignorelicensescn.minimizeFactory.utils.enumsets;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

/**
 * costs memory but really fast.
 */
@NotThreadSafe
public class MaterialSet extends AbstractSet<Material> {
    private final boolean[] containsValueFlags = new boolean[Material.values().length];
    private int size = 0;
    public MaterialSet(){
        Arrays.fill(containsValueFlags,false);
    }

    /**
     * i don't suggest it.
     */
    @Nonnull
    @Override
    public Iterator<Material> iterator() {
        List<Material> containsMaterials = new LinkedList<>();
        for (Material m:Material.values()){
            if (containsValueFlags[m.ordinal()]){
                containsMaterials.add(m);
            }
        }
        return containsMaterials.iterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Material m){
            return containsValueFlags[m.ordinal()];
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o:c){
            if (o instanceof Material m){
                if (!containsValueFlags[m.ordinal()]){
                    return false;
                }
            }else {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean remove(Object o) {
        if (o instanceof Material m){
            //get and set
            boolean removedFlag = containsValueFlags[m.ordinal()];
            if (removedFlag){
                size -= 1;
                containsValueFlags[m.ordinal()] = false;
            }
            return removedFlag;
        }
        return false;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removedFlag = false;
        for (Object o:c){
            if (remove(o)){
                removedFlag = true;
            }
        }
        return removedFlag;
    }

    @Override
    public boolean add(Material m) {
        if (m != null){
            //get and set
            boolean addedFlag = containsValueFlags[m.ordinal()];
            if (!addedFlag){
                size += 1;
                containsValueFlags[m.ordinal()] = true;
            }
            return addedFlag;
        }
        return false;
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends Material> c) {
        //yes "==" equals
        if (c == this){return true;}
        boolean addedFlag = false;
        for (Material m:c){
            if (add(m)){
                addedFlag = true;
            }
        }
        return addedFlag;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(containsValueFlags);
    }
}
