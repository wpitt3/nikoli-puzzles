package dulcinea.nikoli;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GridBuilder {

    public static Grid build(Integer width, Integer height, List<List<Integer>> regions, List<List<Integer>> values, boolean isSudoku) {


        Grid grid = new Grid(width, height);
        List<List<Cell>> cells = generateCells(width, height, isSudoku ? y -> new Cell() : y -> null);
        List<Region> gridRegions = new ArrayList<>();
        if ( isSudoku ) {
            gridRegions.addAll(createSudokuRegions(width, height, cells));
        }

        grid.setCells(cells);
        grid.setRegions(gridRegions);
        return grid;
    }

    private static List<List<Cell>> generateCells(Integer width, Integer height, IntFunction<Cell> makeCell) {
        return runXFTimes( width, (x -> runXFTimes( height, makeCell )));
    }

    private static List<Region> createSudokuRegions(Integer width, Integer height, List<List<Cell>> cells){
        if ( width != height && width != 9) {
            throw new RuntimeException("Only allowing 9 x 9 sudokus due to funny indeterminable size boxes");
        }
        List<Region> gridRegions = new ArrayList<>();
        // generate row regions
        gridRegions.addAll( runXFTimes(height, y ->
                new Region(45, runXFTimes(width, x -> cells.get(x).get(y)))
        ));

        // generate column regions
        gridRegions.addAll( runXFTimes(width, x ->
                new Region(45, runXFTimes(height, y -> cells.get(x).get(y)))
        ));

        // generate box regions
        gridRegions.addAll(runXFTimesWithFlatten( 3, yBox -> runXFTimes(3, xBox -> {
            List<Cell> boxCells = runXFTimesWithFlatten( 3, y -> runXFTimes(3, x ->
                cells.get(xBox*3+x).get(yBox*3+y)
            ));
            return new Region(45, boxCells);
        })));
        return gridRegions;
    }

    private static <T> List<T> runXFTimes(int x, IntFunction<T> f) {
        return IntStream.range(0, x).mapToObj( f ).collect(Collectors.toList());
    }

    private static <T> List<T> runXFTimesWithFlatten(int x, IntFunction<List<T>> f) {
        return IntStream.range(0, x).mapToObj( f ).flatMap(List::stream).collect(Collectors.toList());
    }

}
