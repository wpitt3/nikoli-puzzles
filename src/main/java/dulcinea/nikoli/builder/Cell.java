package dulcinea.nikoli.builder;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Integer value;
    private List<Region> regions;
    private List<Integer> possibles;

    protected Cell() {
        this.value = null;
        this.possibles = Lists.newArrayList(1,2,3,4,5,6,7,8,9);
        this.regions = new ArrayList<>();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        this.possibles = Lists.newArrayList(value);
    }

    public boolean setImpossible(Integer value) {
        return this.possibles.remove(value);
    }

    public List<Integer> getPossibles() {
        return possibles;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void addRegion(Region region) {
        this.regions.add(region);
    }
}