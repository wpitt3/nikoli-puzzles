package dulcinea.nikoli.regionsum;

import com.google.common.collect.Lists;
import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        List<Region> regionsOnBorder = cellsWithinSharedRegion.stream()
                .filter(cell -> !cell.getRegions().contains(region))
                .map(Cell::getRegions)
                .flatMap(List::stream)
                .distinct()
                .filter(r -> r.getTotal() != 45).collect(Collectors.toList());

        List<Region> regionsNotOnBorder = negateList(regionsWithinRegion, regionsOnBorder);

        List<Unresolved> unresolved = regionsOnBorder.stream().map( regionOnBorder -> {
            Map<CellStatus, List<Cell>> cellStatusToCells = calculateCellStatusToCells(region, regionOnBorder);

            List<Cell> assignedOverflowingCells = cellStatusToCells.get(new CellStatus(false, true));
            List<Cell> assignedUnderflowingCells = cellStatusToCells.get(new CellStatus(true, true));
            List<Cell> unassignedOverflowingCells = cellStatusToCells.get(new CellStatus(false, false));
            List<Cell> unassignedUnderflowingCells = cellStatusToCells.get(new CellStatus(true, false));

            if (!unassignedUnderflowingCells.isEmpty()) {
                if (!unassignedOverflowingCells.isEmpty()) {
                    if (unassignedOverflowingCells.size() == 1) {
                        return new Unresolved(unassignedOverflowingCells.get(0), - assignedOverflowingCells.stream().map(Cell::getValue).mapToInt(Integer::intValue).sum());
                    }
                    if (unassignedUnderflowingCells.size() == 1) {
                        return new Unresolved(unassignedUnderflowingCells.get(0), assignedUnderflowingCells.stream().map(Cell::getValue).mapToInt(Integer::intValue).sum());
                    }
                } else {
                    return new Unresolved(null, regionOnBorder.getTotal() - assignedOverflowingCells.stream().map(Cell::getValue).mapToInt(Integer::intValue).sum());
                }
            }
            if (!unassignedOverflowingCells.isEmpty() && unassignedUnderflowingCells.isEmpty()) {
                return new Unresolved(null, assignedUnderflowingCells.stream().map(Cell::getValue).mapToInt(Integer::intValue).sum());
            }
            return null;
        }).collect(Collectors.toList());

        List<Cell> unresolvedCells = unresolved.stream().filter(it -> it != null && it.getCell() != null).map(it -> it.getCell()).collect(Collectors.toList());
        if (unresolved.stream().anyMatch(it -> it == null) || unresolvedCells.size() > 1) {
            return; //region cannot be resolved
        }
        if (unresolvedCells.size() == 1) {
            Integer total = unresolved.stream().filter(it -> it != null && it.getInteriorDifference() != null).map(it -> it.getInteriorDifference()).mapToInt(it -> (Integer)it).sum() + regionsNotOnBorder.stream().map(Region::getTotal).mapToInt(Integer::intValue).sum();
            Cell cellToResolve = unresolvedCells.get(0);
            if (cellToResolve.getRegions().contains(region)) {
                cellToResolve.setValue(45 - total);
            } else {
                cellToResolve.setValue(cellToResolve.getRegions().stream().filter(it -> it.getTotal() != 45).findFirst().get().getTotal() + total - 45);
            }
            cellToResolve.updateCellsInSameRegion();
        }
    }

    private static Map<CellStatus, List<Cell>> calculateCellStatusToCells(Region region, Region regionOnBorder) {
        Map<CellStatus, List<Cell>> cellStatusToCells = new HashMap<>();
        cellStatusToCells.put(new CellStatus(false, true), new ArrayList<>());
        cellStatusToCells.put(new CellStatus(false, false), new ArrayList<>());
        cellStatusToCells.put(new CellStatus(true, true), new ArrayList<>());
        cellStatusToCells.put(new CellStatus(true, false), new ArrayList<>());
        cellStatusToCells.putAll(regionOnBorder.getCells().stream().collect(Collectors.toMap(
                cell -> new CellStatus(cell.getRegions().contains(region), cell.getValue() != null),
                cell -> Lists.newArrayList(cell),
                (cellsA, cellsB) -> {
                    cellsA.addAll(cellsB);
                    return cellsA;
                })));
        return cellStatusToCells;
    }

    private static <T> List<T> negateList(List<T> x, List<T> y) {
        List<T> newX = new ArrayList<>(x);
        newX.removeAll(y);
        return newX;
    }

    private static final class Unresolved {
        Cell cell;
        Integer interiorDifference;

        public Unresolved(Cell cell, Integer interiorDifference) {
            this.cell = cell;
            this.interiorDifference = interiorDifference;
        }

        public Cell getCell() {
            return cell;
        }

        public Integer getInteriorDifference() {
            return interiorDifference;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Unresolved that = (Unresolved) o;

            if (cell != null ? !cell.equals(that.cell) : that.cell != null) return false;
            return interiorDifference != null ? interiorDifference.equals(that.interiorDifference) : that.interiorDifference == null;
        }

        @Override
        public int hashCode() {
            int result = cell != null ? cell.hashCode() : 0;
            result = 31 * result + (interiorDifference != null ? interiorDifference.hashCode() : 0);
            return result;
        }
    }

    private static final class CellStatus {
        boolean insideRegion;
        boolean isAssigned;

        public CellStatus(boolean insideRegion, boolean isAssigned) {
            this.insideRegion = insideRegion;
            this.isAssigned = isAssigned;
        }

        public boolean isInsideRegion() {
            return insideRegion;
        }

        public boolean isAssigned() {
            return isAssigned;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CellStatus that = (CellStatus) o;

            return insideRegion == that.insideRegion && isAssigned == that.isAssigned;
        }

        @Override
        public int hashCode() {
            int result = (insideRegion ? 1 : 0);
            result = 31 * result + (isAssigned ? 1 : 0);
            return result;
        }
    }
}
