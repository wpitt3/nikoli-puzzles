package dulcinea.nikoli.combinations;

import dulcinea.nikoli.builder.Region;
import dulcinea.nikoli.combinations.CombinationStore.Combination;

import org.apache.commons.math3.util.Combinations;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PossibleRegionsCalculator {
    private static CombinationStore combinationStore = new CombinationStore();

    public static boolean removeImpossibleCombinationsFromRegion(Region region) {
        if (region.getTotal() == 45) {
            return false;
        }
        List<Combination> combinations = combinationStore.getCombinations(region.size(), region.getTotal());

        List<Integer> allPossible = calculateAllPossibleInRegion(region);
        combinations = filterCombinationsByPossibleValues(combinations, allPossible);

        return calculateImpossibleValuesInRegion(combinations)
            .stream()
            .map( toRemove ->
                region.getCells()
                    .stream()
                    .map(cell ->
                        cell.setImpossible(toRemove)
                    ).reduce(Boolean::logicalOr).orElse(false)
            ).reduce(Boolean::logicalOr).orElse(true);
    }

    private static List<Integer> calculateImpossibleValuesInRegion(List<Combination> combinations) {
        List<Integer> possibleComboValues = combinations.stream().map(Combination::getValues).flatMap(List::stream).collect(Collectors.toList());
        return IntStream.range(1,10).boxed().filter( it -> ! possibleComboValues.contains(it)).collect(Collectors.toList());
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
