package dulcinea.nikoli;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Grid;
import dulcinea.nikoli.builder.Region;
import dulcinea.nikoli.combinations.PossibleRegionsCalculator;
import dulcinea.nikoli.regionsum.RegionSumCalculator;
import dulcinea.nikoli.subregion.HiddenXFinder;
import dulcinea.nikoli.subregion.NakedXFinder;

import java.util.List;
import java.util.stream.Collectors;

public class GridResolver {

    public static Grid resolveGrid(Grid grid) {
        setupGrid(grid);
        grid.getRegions().forEach(region ->
            RegionSumCalculator.checkForRegionsUncontainedByRegion(region)
        );

        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region ->
            PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        );
        exastivelyCheckForSingles(getAllCells(grid));
        for (int i = 0; i< 30; i++) {
            grid.getRegions().forEach(region -> {
                NakedXFinder.checkForNakedX(region, 2);
                NakedXFinder.checkForNakedX(region, 3);
                NakedXFinder.checkForNakedX(region, 4);
                HiddenXFinder.checkForHiddenX(region, 2);
                HiddenXFinder.checkForHiddenX(region, 3);
                HiddenXFinder.checkForHiddenX(region, 4);
                PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region);
            });
            exastivelyCheckForSingles(getAllCells(grid));
        }
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

        updated.addAll(getSharedRegions(updatedCells).stream().map( region ->
            HiddenXFinder.checkForHiddenSingles(region)
        ).flatMap(List::stream).collect(Collectors.toList()));

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
