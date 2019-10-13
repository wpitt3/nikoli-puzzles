package dulcinea.nikoli;

import java.util.List;

public class Region {
    private List<Cell> cells;
    private Integer total;

    public Region(List<Cell> cells, Integer total) {
        this.cells = cells;
        this.total = total;
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