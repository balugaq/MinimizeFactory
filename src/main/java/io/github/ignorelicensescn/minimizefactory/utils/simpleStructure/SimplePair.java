package io.github.ignorelicensescn.minimizefactory.utils.simpleStructure;

import java.util.Objects;

public class SimplePair<A,B> {
    public A first;
    public final B second;
    public SimplePair(A first,B second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){return false;}
        if ((obj instanceof SimplePair<?,?> pair)){
            return Objects.equals(pair.first,this.first) && Objects.equals(pair.second,this.second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (first == null?0:first.hashCode() << 16)
                + (second == null?0:second.hashCode());
    }

    @Override
    public String toString() {
        return first + "|" + second;
    }
}
