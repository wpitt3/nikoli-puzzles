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

        List<Cell> cellsWithSharedRegion = regionsWithinRegion.stream()
                .map(Region::getCells)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        calculateOverflowCellValue(regionsWithinRegion, cellsWithSharedRegion, region);

        List<Region> regionsOnBorder = cellsWithSharedRegion.stream()
                .filter(cell -> !cell.getRegions().contains(region))
                .map(Cell::getRegions)
                .flatMap(List::stream)
                .distinct()
                .filter(r -> r.getTotal() != 45).collect(Collectors.toList());

        List<Region> regionsNotOnBorder = new ArrayList<>(regionsWithinRegion);
        regionsNotOnBorder.removeAll(regionsOnBorder);

        List<Cell> cellsWithNoSharedRegions = regionsNotOnBorder.stream()
                .map(Region::getCells)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        calculateUnderflowCellValue(cellsWithNoSharedRegions, regionsOnBorder, regionsNotOnBorder, region);
    }

    private static void calculateOverflowCellValue(List<Region> regionsWithInRegion, List<Cell> cellsWithSharedRegion, Region region) {
        if (cellsWithSharedRegion.size() == 10) {
            Integer total = regionsWithInRegion.stream().map(Region::getTotal).mapToInt(Integer::intValue).sum();
            Cell cellOutSideMainRegion = cellsWithSharedRegion.stream().filter(cell -> !cell.getRegions().contains(region)).findFirst().get();
            cellOutSideMainRegion.setValue(total - 45);
            cellOutSideMainRegion.updateCellsInSameRegion();
        }
    }

    private static void calculateUnderflowCellValue(List<Cell> cellsWithNoSharedRegions, List<Region> regionsOnBorder, List<Region> regionsNotOnBorder, Region region) {
        if (cellsWithNoSharedRegions.size() == 8) {
            Cell cellWithinRegionAndSharedRegion = regionsOnBorder.stream()
                    .map(Region::getCells)
                    .flatMap(List::stream)
                    .distinct()
                    .filter(cell -> cell.getRegions().contains(region)).findFirst().get();
            Integer total = regionsNotOnBorder.stream().map(Region::getTotal).mapToInt(Integer::intValue).sum();
            cellWithinRegionAndSharedRegion.setValue(45 - total);
            cellWithinRegionAndSharedRegion.updateCellsInSameRegion();
        }
    }
}
