package dulcinea.nikoli;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Grid;
import dulcinea.nikoli.builder.Region;
import dulcinea.nikoli.combinations.PossibleRegionsCalculator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GridResolver {

    public static Grid resolveGrid(Grid grid) {
        setupGrid(grid);

        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region ->
            PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        );
        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region ->
                PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        );
        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region ->
                PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        );
        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region -> {
            NakedXFinder.checkForNakedX(region, 2);
        });
        grid.getRegions().forEach( region -> {
            NakedXFinder.checkForNakedX(region, 3);
        });
        grid.getRegions().forEach( region -> {
            NakedXFinder.checkForNakedX(region, 4);
        });
        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region ->
                PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        );
        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region -> {
            NakedXFinder.checkForNakedX(region, 3);
        });
        exastivelyCheckForSingles(getAllCells(grid));
        return grid;
    }

    private static List<Cell> getAllCells(Grid grid) {
        return grid.getFlatCells().stream().filter(cell -> cell != null).collect(Collectors.toList());
    }

    private static void setupGrid(Grid grid) {
        getAllCells(grid).forEach(cell -> {
            if (cell.getValue() != null) {
                cell.updateCellsInSameRegion();
            }
        });
    }

    private static void exastivelyCheckForSingles(List<Cell> updated) {
        while (updated.size() > 0) {
            updated = checkForSingles(updated);
        }
    }

    private static List<Cell> checkForSingles(List<Cell> updatedCells) {
        List<Cell> updated = NakedXFinder.checkForNakedSingles(updatedCells);
        updated.addAll(HiddenXFinder.checkForHiddenSingles(getSharedRegions(updatedCells)));
        return updated.stream().distinct().collect(Collectors.toList());
    }

    private static List<Region> getSharedRegions(List<Cell> cells) {
        return cells.stream()
                .map(Cell::getRegions)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

}
