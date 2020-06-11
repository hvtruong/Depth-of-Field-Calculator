package com.example.depthoffieldcalculator;

import java.util.ArrayList;
import java.util.Iterator;

public class LensManager implements Iterable<Lens>{
    private ArrayList<Lens> lens = new ArrayList<>();
    private static LensManager instance;
    private LensManager(){}
    public static LensManager getInstance(){
        if(instance == null)
            instance = new LensManager();
        return instance;
    }

    public boolean isEmpty(){
        if(lens.size() == 0)
            return true;
        return false;
    }

    public void add(Lens newLens) {
        lens.add(newLens);
    }

    public void remove(Lens aLens){
        lens.remove(aLens);
    }
    public int size()
    {
        return lens.size();
    }

    public Lens getLens(int index) {
        return lens.get(index);
    }

    @Override
    public Iterator<Lens> iterator() {
        return lens.iterator();
    }
}
