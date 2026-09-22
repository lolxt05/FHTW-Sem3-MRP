package org.mrp.cache;

import java.util.List;
import java.util.TreeSet;

public class StringCache {
    private final TreeSet<String> set = new TreeSet<String>();

    public void massinsert(List<String> list){
        set.addAll(list);
    }

    public void insert(String str){
        set.add(str);
    }

    public boolean contains(String str){
        return this.set.contains(str);
    }

    public boolean remove(String str){
        return this.set.remove(str);
    }
}
