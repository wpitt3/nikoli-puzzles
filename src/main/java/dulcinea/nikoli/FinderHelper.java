package dulcinea.nikoli;

import java.util.*;

public class FinderHelper {

    public static <T,R> List<T> findHiddenTinRofSizeX(Map<T,List<R>> tsToRs, List<T> includedTs, List<R> includedRs, int x) {
        if (tsToRs.size() < x) {
            return new ArrayList<>();
        }
        for (Map.Entry<T,List<R>> tToRs : tsToRs.entrySet()) {
            List<R> newIncludedRs = joinLists(includedRs, tToRs.getValue());
            if(newIncludedRs.size() <= x && !includedTs.contains(tToRs.getKey())) {
                if (includedTs.size() == x - 1) {
                    return addToList(includedTs, tToRs.getKey());
                }
                List<T> result = findHiddenTinRofSizeX(tsToRs, addToList(includedTs, tToRs.getKey()), newIncludedRs, x);
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
