package dulcinea.nikoli;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Region;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HiddenXFinder {

    public static List<Cell> checkForHiddenSingles(List<Region> regions) {
        return regions.stream().filter(region -> region.getTotal() == 45).map(region -> {
            List<Integer> hiddenSingleValues = findHiddenSingleValue(region);
            return hiddenSingleValues.stream().map( hiddenSingleValue -> {
                Cell cellWithValue = region.getCells().stream().filter(cell -> cell.getPossibles().contains(hiddenSingleValue)).findFirst().get();
                cellWithValue.setValue(hiddenSingleValue);
                return cellWithValue.updateCellsInSameRegion();
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
}
