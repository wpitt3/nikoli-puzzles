package dulcinea.nikoli;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Region;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HiddenXFinder {

    public static List<Cell> checkForHiddenSingles(Region region) {
        if (region.getTotal() != 45) { return new ArrayList<>(); }

        List<Integer> hiddenSingleValues = findHiddenXValue(region, 1);
        return hiddenSingleValues.stream().map( hiddenSingleValue -> {
            Cell cellWithValue = region.getCells().stream().filter(cell -> cell.getPossibles().contains(hiddenSingleValue)).findFirst().get();
            cellWithValue.setValue(hiddenSingleValue);
            return cellWithValue.updateCellsInSameRegion();
        }).flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public static void checkForHiddenX(Region region, Integer x) {
        if (region.getTotal() != 45) { return; }


        List<Integer> hiddenXValues = findHiddenXValue(region, x);
        Map<Integer,List<Cell>> hiddenValueToCells = hiddenXValues.stream().collect( Collectors.toMap(Function.identity(), hiddenSingleValue -> {
            return region.getCells().stream().filter(cell -> cell.getPossibles().contains(hiddenSingleValue)).collect(Collectors.toList());
        }));
        List<Integer> hiddenValues = Helper.findHiddenTinRofSizeX(hiddenValueToCells, new ArrayList<>(), new ArrayList<>(), x);
        List<Integer> invertedValues = invertIntegerRange(hiddenValues);
        region.getCells().stream()
            .filter(cell -> hiddenValues.stream()
                .anyMatch( hiddenValue -> cell.getPossibles().contains(hiddenValue))
            )
            .forEach(cell -> {
                invertedValues.stream().forEach(value -> cell.setImpossible(value));
        });
    }

    private static List<Integer> findHiddenXValue(Region region, Integer x) {
        Integer[] possiblesFrequency = new Integer[]{0,0,0,0,0,0,0,0,0,0};
        region.getCells().stream().filter(cell -> cell.getValue() == null)
                .forEach(cell -> cell.getPossibles().forEach(possible -> possiblesFrequency[possible] += 1));
        return IntStream.range(1,10)
                .filter(y -> possiblesFrequency[y] <= x && possiblesFrequency[y] > 0)
                .mapToObj(y -> y)
                .collect(Collectors.toList());
    }

    private static List<Integer> invertIntegerRange(List<Integer> original) {
        return IntStream.range(1,10).boxed().filter( it -> ! original.contains(it)).collect(Collectors.toList());
    }
}
