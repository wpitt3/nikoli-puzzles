package dulcinea.nikoli.regionsum;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegionSumCalculator {

    public static void checkForRegionsUncontainedByRegion(Region region) {
        if (region.getTotal() != 45) {
            return;
        }

        List<Region> regionsWithinRegion = region.getCells().stream()
                .map(Cell::getRegions)
                .flatMap(List::stream)
                .distinct()
                .filter(r -> r.getTotal() != 45)
                .collect(Collectors.toList());

        List<Cell> cellsWithinSharedRegion = regionsWithinRegion.stream()
                .map(Region::getCells)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        calculateOverflowCellValue(regionsWithinRegion, cellsWithinSharedRegion, region);

        List<Region> regionsOnBorder = cellsWithinSharedRegion.stream()
                .filter(cell -> !cell.getRegions().contains(region))
                .map(Cell::getRegions)
                .flatMap(List::stream)
                .distinct()
                .filter(r -> r.getTotal() != 45).collect(Collectors.toList());

        List<Region> regionsNotOnBorder = negateList(regionsWithinRegion, regionsOnBorder);

        List<Cell> cellsWithNoSharedRegions = regionsNotOnBorder.stream()
                .map(Region::getCells)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        calculateUnderflowCellValue(cellsWithNoSharedRegions, regionsOnBorder, regionsNotOnBorder, region);
    }

    private static void calculateOverflowCellValue(List<Region> regionsWithInRegion, List<Cell> cellsWithSharedRegion, Region region) {
        List<Cell> overflowingCells = cellsWithSharedRegion.stream().filter(cell -> !cell.getRegions().contains(region)).collect(Collectors.toList());
        List<Cell> unassignedOverflowingCells = overflowingCells.stream().filter(cell -> cell.getValue() == null).collect(Collectors.toList());

        if (unassignedOverflowingCells.size() == 1) {
            Integer total = regionsWithInRegion.stream().map(Region::getTotal).mapToInt(Integer::intValue).sum();
            Integer cellValue = total - 45 - negateList(overflowingCells, unassignedOverflowingCells).stream().map(cell -> cell.getValue()).collect(Collectors.summingInt(Integer::intValue));
            unassignedOverflowingCells.get(0).setValue(cellValue);
            unassignedOverflowingCells.get(0).updateCellsInSameRegion();
        }
    }

    private static void calculateUnderflowCellValue(List<Cell> cellsWithNoSharedRegions, List<Region> regionsOnBorder, List<Region> regionsNotOnBorder, Region region) {
        List<Cell> underflowingCells = regionsOnBorder.stream()
                .map(Region::getCells)
                .flatMap(List::stream)
                .distinct()
                .filter(cell -> cell.getRegions().contains(region)).collect(Collectors.toList());

        List<Cell> unassignedUnderflowingCells = underflowingCells.stream().filter(cell -> cell.getValue() == null).collect(Collectors.toList());

        if (unassignedUnderflowingCells.size() == 1) {
            Integer total = regionsNotOnBorder.stream().map(Region::getTotal).mapToInt(Integer::intValue).sum();
            Integer cellValue = 45 - total - negateList(underflowingCells, unassignedUnderflowingCells).stream().map(cell -> cell.getValue()).collect(Collectors.summingInt(Integer::intValue));
            unassignedUnderflowingCells.get(0).setValue(cellValue);
            unassignedUnderflowingCells.get(0).updateCellsInSameRegion();
        }
    }

    private static <T> List<T> negateList(List<T> x, List<T> y) {
        List<T> newX = new ArrayList<>(x);
        newX.removeAll(y);
        return newX;
    }
}
