package dulcinea.nikoli.combinations;

import org.apache.commons.math3.util.Combinations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

class CombinationStore {

    private final Map<Integer, Map<Integer, List<Combination>>> combinations;

    CombinationStore() {
        combinations = IntStream.range(2,9).boxed()
                .collect( Collectors.toMap(Function.identity(), select ->
                        StreamSupport.stream(new Combinations(9, select).spliterator(), false)
                                .map(combo -> new Combination(combo))
                                .collect(Collectors.groupingBy(Combination::getTotal))
                ));
    }

    List<Combination> getCombinations(Integer regionSize, Integer regionTotal) {
        return combinations.get(regionSize).get(regionTotal);
    }

    static class Combination {
        private final List<Integer> values;
        private final Integer total;

        Combination(int[] values) {
            this.values = Arrays.stream(values).mapToObj(it -> it + 1).collect(Collectors.toList());
            this.total = Arrays.stream(values).sum() + values.length;
        }

        List<Integer> getValues() {
            return values;
        }

        Integer getTotal() {
            return total;
        }
    }
}
