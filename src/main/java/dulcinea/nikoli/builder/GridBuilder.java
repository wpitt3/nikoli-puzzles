package dulcinea.nikoli.builder;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GridBuilder {

    /**
     * @param width
     * @param height
     * @param regions a list of regions where each region is a value followed by a coords pairs
     *     e.g. 4, 3, 1, 3, 2 -> is a region of total count 4 with the two cells [3,1] and [3,2]
     * @param values a list of positions where each position is list of three values:
     *    xCoord  (0 indexed),
     *    yCoord  (0 indexed),
     *    value   (1 indexed),
     * @param isSudoku will decide whether rows, column and box regions are added to the grid
     * @return
     */
    public static Grid build(Integer width, Integer height, List<List<Integer>> regions, List<List<Integer>> values, boolean isSudoku) {
        List<List<Cell>> cells = generateCells(width, height, isSudoku ? y -> new Cell() : y -> null);
        Grid grid = new Grid(width, height, cells);

        List<Region> gridRegions = new ArrayList<>();
        if ( isSudoku ) {
            // add regions for rows columns and boxes
            gridRegions.addAll(createSudokuRegions(width, height, grid));
        } else {
            // only add cells in referenced locations
            calculateRegionCells(regions).forEach(regionCell ->
                grid.setCell(regionCell.get(0), regionCell.get(1), new Cell())
            );
        }

        gridRegions.addAll(createNamedRegions(grid, regions));

        values.forEach(cellValue -> {
            if (cellValue.get(0) < 0 || cellValue.get(0) > width
                    || cellValue.get(1) < 0 || cellValue.get(1) > height
                    || cellValue.get(2) < 1 || cellValue.get(2) > 9 ) {
                throw new RuntimeException("Cell value must be within grid and 0 < x < 10");
            }
            grid.setCell(cellValue.get(0), cellValue.get(1), cellValue.get(2));
        });

        // set back reference from cell to region
        gridRegions.forEach(region ->
            region.getCells().forEach( cell -> cell.addRegion(region))
        );

        grid.setRegions(gridRegions);
        return grid;
    }

    private static List<List<Cell>> generateCells(Integer width, Integer height, IntFunction<Cell> makeCell) {
        return runXFTimes( width, (x -> runXFTimes( height, makeCell )));
    }

    private static List<Region> createSudokuRegions(Integer width, Integer height, Grid grid) {
        if ( width != height && width != 9) {
            throw new RuntimeException("Only allowing 9 x 9 sudokus due to funny indeterminable size boxes");
        }
        List<Region> regions = new ArrayList<>();
        // generate row regions
        regions.addAll( runXFTimes(height, y ->
            new Region(45, runXFTimes(width, x -> grid.getCell(x, y)))
        ));

        // generate column regions
        regions.addAll( runXFTimes(width, x ->
            new Region(45, runXFTimes(height, y -> grid.getCell(x, y)))
        ));

        // generate box regions
        regions.addAll(runXFTimesWithFlatten( 3, yBox -> runXFTimes(3, xBox -> {
            List<Cell> boxCells = runXFTimesWithFlatten( 3, y -> runXFTimes(3, x ->
                grid.getCell(xBox*3+x, yBox*3+y)
            ));
            return new Region(45, boxCells);
        })));
        return regions;
    }

    private static List<Region> createNamedRegions(Grid grid, List<List<Integer>> namedRegions) {
        List<Region> regions = new ArrayList<>();
        namedRegions.forEach(region -> {
            if (region.get(0) < 1 || region.size() < 2 || region.size() % 2 != 1) {
                throw new RuntimeException("Region value must be positive and must have coords");
            }
            List<List<Integer>> coords = Lists.partition(region.subList(1, region.size()), 2);
            List<Cell> regionCells = coords.stream().map(coord ->
                grid.getCell(coord.get(0), coord.get(1))
            ).collect(Collectors.toList());

            regions.add(new Region(region.get(0), regionCells));
        });

        return regions;
    }

    private static List<List<Integer>> calculateRegionCells(List<List<Integer>> regions) {
        return regions.stream().map(region ->
                Lists.partition(region.subList(1, region.size()), 2)
        ).flatMap(List::stream).collect(Collectors.toList());
    }

    private static <T> List<T> runXFTimes(int x, IntFunction<T> f) {
        return IntStream.range(0, x).mapToObj( f ).collect(Collectors.toList());
    }

    private static <T> List<T> runXFTimesWithFlatten(int x, IntFunction<List<T>> f) {
        return IntStream.range(0, x).mapToObj( f ).flatMap(List::stream).collect(Collectors.toList());
    }

}
