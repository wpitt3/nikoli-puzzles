package dulcinea.nikoli;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Region;

import java.util.*;
import java.util.function.Function;
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

        Map<Cell, List<Integer>> cellToPossibles = cellsWithXPossible.stream().collect(Collectors.toMap(Function.identity(), cell -> cell.getPossibles()));
        List<Cell> nakedXCells = FinderHelper.findHiddenTinRofSizeX(cellToPossibles, new ArrayList<>(), new ArrayList<>(), x);

        if( !nakedXCells.isEmpty()) {
            List<Integer> impossibles = nakedXCells.stream().map(cell -> cell.getPossibles()).flatMap(List::stream).collect(Collectors.toList());
            region.getCells().stream().filter(cell -> !nakedXCells.contains(cell)).forEach(cell ->
                    impossibles.stream().forEach( impossible -> cell.setImpossible(impossible))
            );
        }
    }
}
