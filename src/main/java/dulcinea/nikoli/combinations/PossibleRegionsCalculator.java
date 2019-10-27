package dulcinea.nikoli.combinations;

import dulcinea.nikoli.builder.Cell;
import dulcinea.nikoli.builder.Region;
import dulcinea.nikoli.combinations.CombinationStore.Combination;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PossibleRegionsCalculator {
    private static CombinationStore combinationStore = new CombinationStore();

    public static void removeImpossibleCombinationsFromRegion(Region region) {
        if (region.getTotal() == 45) {
            return;
        }
        List<Combination> combinations = combinationStore.getCombinations(region.size(), region.getTotal());

        List<Integer> allPossible = calculateAllPossibleInRegion(region);
        List<Integer> existingValues = region.getCells().stream().map(cell -> cell.getValue()).filter(it -> it != null).collect(Collectors.toList());

        combinations = combinations.stream().filter(combo ->
            existingValues.stream().allMatch( existingValue ->
                combo.getValues().contains(existingValue)
        )).collect(Collectors.toList());

        combinations = filterCombinationsByPossibleValues(combinations, allPossible);

        List<Integer> impossibleValues = invertPossibles(calculateImpossibleValuesInRegion(combinations, existingValues));
        region.getCells().stream().forEach(cell -> setImpossibleValuesOnCell(impossibleValues, cell));

        if (region.getCells().size() == 2) {
            calculatePossibleComboPairs(region.getCells().get(0), region.getCells().get(1), combinations);
            calculatePossibleComboPairs(region.getCells().get(1), region.getCells().get(0), combinations);
        }
    }

    private static void calculatePossibleComboPairs(Cell cellA, Cell cellB, List<Combination> combinations) {
        List<Integer> z = cellA.getPossibles();
        List<Integer> otherCellPossibles = combinations.stream().map(Combination::getValues).filter(combo -> z.contains(combo.get(0))).map(combo -> combo.get(1)).collect(Collectors.toList());
        otherCellPossibles.addAll(combinations.stream().map(Combination::getValues).filter(combo -> z.contains(combo.get(1))).map(combo -> combo.get(0)).collect(Collectors.toList()));
        setImpossibleValuesOnCell(invertPossibles(otherCellPossibles), cellB);
    }

    private static List<Integer> calculateImpossibleValuesInRegion(List<Combination> combinations, List<Integer> existingValues) {
        return combinations.stream()
                .map(Combination::getValues)
                .flatMap(List::stream)
                .filter( it -> !existingValues.contains(it))
                .collect(Collectors.toList());
    }

    private static List<Integer> invertPossibles(List<Integer> toInvert) {
        return IntStream.range(1, 10).boxed().filter(it -> !toInvert.contains(it)).collect(Collectors.toList());
    }

    private static void setImpossibleValuesOnCell(List<Integer> impossibleValues, Cell cell) {
        if (cell.getValue() == null ) {
            impossibleValues.stream().forEach( toRemove -> cell.setImpossible(toRemove));
        }
    }

    private static List<Combination> filterCombinationsByPossibleValues(List<Combination> combinations, List<Integer> allPossible) {
        return combinations
                .stream()
                .filter(combination -> combination.getValues().stream().allMatch( it -> allPossible.contains(it)))
                .collect(Collectors.toList());
    }

    private static List<Integer> calculateAllPossibleInRegion(Region region) {
        return region.getCells().stream()
            .map(cell -> cell.getPossibles())
            .flatMap(List::stream)
            .distinct()
            .collect(Collectors.toList());
    }
}
