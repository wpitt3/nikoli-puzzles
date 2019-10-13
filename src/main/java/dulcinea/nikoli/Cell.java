package dulcinea.nikoli;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Integer value;
    private boolean[] possibles;
    private List<Region> regions;

    public Cell() {
        this.value = null;
        this.possibles = new boolean[]{ true,true,true,true,true,true,true,true,true };
        this.regions = new ArrayList<>();
    }

    public Cell(Integer value) {
        this.value = value;
        this.possibles = null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        this.possibles = null;
    }

    public void setImpossible(Integer index) {
        this.possibles[index] = false;
    }

    public boolean[] getPossibles() {
        return possibles;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void addRegion(Region region) {
        this.regions.add(region);
    }
}