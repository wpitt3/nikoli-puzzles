package dulcinea.nikoli;

public class Cell {
    private Integer value;

    public Cell() {
        this.value = null;
    }

    public Cell(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}