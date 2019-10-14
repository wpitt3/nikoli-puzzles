package dulcinea.nikoli.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid {
    private Integer width;
    private Integer height;
    private List<Region> regions;
    private List<List<Cell>> cells;

    protected Grid(Integer width, Integer height, List<List<Cell>> cells) {
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

    public List<Cell> getFlatCells() {
        return cells.stream().flatMap(List::stream).collect(Collectors.toList());
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

    boolean isComplete() {
        return cells.stream().flatMap(List::stream).allMatch(cell -> cell == null || cell.getValue()!= null);
    }

    String printCells() {
        return "#" + runXFTimes(height, "#\n#", (y -> runXFTimes(width, "", ( x ->
                this.getCell(x, y) != null && this.getCell(x, y).getValue() != null ? this.getCell(x, y).getValue().toString() : " "
        )))) + "#";
    }

    String printPossibles() {
        return "#" + runXFTimes(height, "#\n#\n#", (y -> runXFTimes(3, "#\n#", (yPos -> runXFTimes(width, " ", ( x -> runXFTimes(3, "", (xPos -> {
            Integer value = 1 + yPos*3+xPos;
            return this.getCell(x, y).getPossibles().contains(value) ? value.toString() : " ";
        })))))))) + "#";
    }

    private static String runXFTimes(int x, String joiner, IntFunction<String> f) {
        return IntStream.range(0, x).mapToObj( f ).collect(Collectors.joining(joiner));
    }
}