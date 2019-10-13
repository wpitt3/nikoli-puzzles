package dulcinea.nikoli;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private Integer width;
    private Integer height;
    private List<Region> regions;
    private List<List<Cell>> cells;

    public Grid(Integer width, Integer height, List<List<Cell>> cells) {
        this.width = width;
        this.height = height;
        this.cells = cells;
        this.regions = new ArrayList<>();
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

    public void setCell(Integer x, Integer y, Cell cell) {
        cells.get(x).set(y, cell);
    }

    public void setCell(Integer x, Integer y, Integer value) {
        cells.get(x).get(y).setValue(value);
    }

    public Cell getCell(Integer x, Integer y) {
        return cells.get(x).get(y);
    }

}