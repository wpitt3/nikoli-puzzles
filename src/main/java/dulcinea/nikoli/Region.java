package dulcinea.nikoli;

import java.util.ArrayList;
import java.util.List;

public class Region {
    private List<Cell> cells;
    private Integer total;

    public Region(Integer total) {
        this.cells = new ArrayList<>();
        this.total = total;
    }

    public Region(Integer total, List<Cell> cells) {
        this.total = total;
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}