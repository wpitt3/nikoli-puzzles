package dulcinea.nikoli;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private Integer width;
    private Integer height;
    private List<Region> regions;
    private List<List<Cell>> cells;

    public Grid(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        this.regions = new ArrayList<>();
        this.cells = new ArrayList<>();
    }

    public Grid(Integer width, Integer height, List<Region> regions, List<List<Cell>> cells) {
        this.width = width;
        this.height = height;
        this.regions = regions;
        this.cells = cells;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

    public void setCells(List<List<Cell>> cells) {
        this.cells = cells;
    }
}