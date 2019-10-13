package dulcinea.nikoli;

import java.util.List;

public class GridBuilder {

    public static Grid build(Integer width, Integer height, List<List<Integer>> regions, List<List<Integer>> values, boolean isSudoku) {

        return new Grid(width, height);
    }
}
