package dulcinea.nikoli;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Region;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A naked value is a value on its own widthin a cell,
 * A naked single is the only value that is possible for a cell
 * A naked double is the only 2 values that are possible over two cells,
 * A naked X is the only X values that are possible over X cells
 */
public class NakedXFinder {

    public static List<Cell> checkForNakedSingles(List<Cell> cells) {
        return cells.stream()
                .filter(cell -> cell.getValue() == null && cell.getPossibles().size() == 1)
                .map(cell -> {
                    cell.setValue(cell.getPossibles().get(0));
                    return cell.updateCellsInSameRegion();
                }).flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public static void checkForNakedX(Region region, Integer x) {
        List<Cell> cellsWithXPossible = region.getCells().stream()
                .filter(cell -> cell.getValue() == null && cell.getPossibles().size() <= x)
                .collect(Collectors.toList());
        List<Cell> nakedXCells = findNakedXList(cellsWithXPossible, new ArrayList<Cell>(), new ArrayList<Integer>(), x);
        if( !nakedXCells.isEmpty()) {
            List<Integer> impossibles = nakedXCells.stream().map(cell -> cell.getPossibles()).flatMap(List::stream).collect(Collectors.toList());
            region.getCells().stream().filter(cell -> !nakedXCells.contains(cell)).forEach(cell ->
                    impossibles.stream().forEach( impossible -> cell.setImpossible(impossible))
            );
        }
    }

    private static List<Cell> findNakedXList(List<Cell> cellsWithXPossible, List<Cell> includedCells, List<Integer> currentPossibles, int x) {
        if (cellsWithXPossible.size() < x) {
            return new ArrayList<>();
        }
        List<Cell> cells = cellsWithXPossible.stream().filter(cell -> !includedCells.contains(cell)).collect(Collectors.toList());
        for (Cell cell : cells) {
            List<Integer> newPossibles = joinLists(currentPossibles, cell.getPossibles());
            if (newPossibles.size() <= x) {
                if (includedCells.size() == x - 1) {
                    return addToList(includedCells, cell);
                }
                List<Cell> result = findNakedXList(cellsWithXPossible, addToList(includedCells, cell), newPossibles, x);
                if (!result.isEmpty()) {
                    return result;
                }
            }
        }
        return new ArrayList<>();
    }

    private static <T> List<T> joinLists(List<T> a, List<T> b) {
        Set<T> newA = new LinkedHashSet<>(a);
        newA.addAll(b);
        return new ArrayList<>(newA);
    }

    private static <T> List<T> addToList(List<T> a, T x) {
        List<T> newA = new ArrayList<>(a);
        newA.add(x);
        return newA;
    }
}
