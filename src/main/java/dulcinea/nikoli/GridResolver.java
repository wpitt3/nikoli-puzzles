package dulcinea.nikoli;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Grid;
import dulcinea.nikoli.builder.Region;
import dulcinea.nikoli.combinations.PossibleRegionsCalculator;

import java.util.List;
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
            checkForNakedX(region, 2);
        });
        grid.getRegions().forEach( region -> {
            checkForNakedX(region, 3);
        });
        grid.getRegions().forEach( region -> {
            checkForNakedX(region, 4);
        });
        exastivelyCheckForSingles(getAllCells(grid));
        grid.getRegions().forEach( region ->
                PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        );
        exastivelyCheckForSingles(getAllCells(grid));
        return grid;
    }

    private static List<Cell> getAllCells(Grid grid) {
        return grid.getFlatCells().stream().filter(cell -> cell != null).collect(Collectors.toList());
    }

    private static void setupGrid(Grid grid) {
        getAllCells(grid).forEach(cell -> {
            if (cell.getValue() != null) {
                onCellValueUpdate(cell);
            }
        });
    }

    private static void exastivelyCheckForSingles(List<Cell> updated) {
        while (updated.size() > 0) {
            updated = checkForSingles(updated);
        }
    }

    private static List<Cell> checkForSingles(List<Cell> updatedCells) {
        List<Cell> updated = checkForNakedSingles(updatedCells);
        updated.addAll(checkForHiddenSingles(getSharedRegions(updatedCells)));
        return updated.stream().distinct().collect(Collectors.toList());
    }

    private static List<Cell> checkForNakedSingles(List<Cell> cells) {
        return cells.stream()
            .filter(cell -> cell.getValue() == null && cell.getPossibles().size() == 1)
            .map(cell -> {
                cell.setValue(cell.getPossibles().get(0));
                return onCellValueUpdate(cell);
            }).flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    private static List<Cell> checkForHiddenSingles(List<Region> regions) {

        return regions.stream().filter(region -> region.getTotal() == 45).map(region -> {
            List<Integer> hiddenSingleValues = findHiddenSingleValue(region);
            return hiddenSingleValues.stream().map( hiddenSingleValue -> {
                Cell cellWithValue = region.getCells().stream().filter(cell -> cell.getPossibles().contains(hiddenSingleValue)).findFirst().get();
                cellWithValue.setValue(hiddenSingleValue);
                return onCellValueUpdate(cellWithValue);
            }).flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
        }).flatMap(List::stream)
        .distinct()
        .collect(Collectors.toList());
    }

    private static List<Integer> findHiddenSingleValue(Region region) {
        Integer[] possiblesFrequency = new Integer[]{0,0,0,0,0,0,0,0,0,0};
        region.getCells().stream().filter(cell -> cell.getValue() == null)
                .forEach(cell -> cell.getPossibles().forEach(possible -> possiblesFrequency[possible] += 1));
        return IntStream.range(1,10)
                .filter(y -> possiblesFrequency[y] == 1)
                .mapToObj(y -> y)
                .collect(Collectors.toList());
    }

    private static void checkForNakedX(Region region, Integer x) {
        List<Cell> cellsWithXPossible = region.getCells().stream()
                .filter(cell -> cell.getValue() == null && cell.getPossibles().size() <= x)
                .collect(Collectors.toList());
        cellsWithXPossible.stream().forEach( cellA -> {
            List<Cell> nakedDoubles = cellsWithXPossible.stream().filter(cellB -> cellA.getPossibles().equals(cellB.getPossibles())).collect(Collectors.toList());
            if (nakedDoubles.size() == x) {
                List<Integer> imPossibles = cellA.getPossibles();
                region.getCells().stream().filter(cell -> !cell.getPossibles().equals(imPossibles)).forEach(cell ->
                    imPossibles.stream().forEach( impossible -> cell.setImpossible(impossible))
                );
            }
        });
    }


    private static List<Cell> onCellValueUpdate(Cell updatedCell) {
        return getCellsInSameRegion(updatedCell)
            .stream()
            .filter(cell -> cell.getValue() == null && cell.setImpossible(updatedCell.getValue()))
            .collect(Collectors.toList());
    }

    private static List<Region> getSharedRegions(List<Cell> cells) {
        return cells.stream()
                .map(Cell::getRegions)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private static List<Cell> getCellsInSameRegion(Cell cell) {
        return cell.getRegions().stream()
                .map(Region::getCells)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

}
