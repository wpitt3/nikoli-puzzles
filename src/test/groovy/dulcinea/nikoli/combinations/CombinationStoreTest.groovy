package dulcinea.nikoli.combinations

import dulcinea.nikoli.builder.Cell
import dulcinea.nikoli.builder.Region
import spock.lang.Specification

class CombinationStoreTest extends Specification {
    static CombinationStore combinationStore
    
    def setup() {
        combinationStore = new CombinationStore()
    }
    

    void "3 total, 2 cells, 1 option "() {
        when:
          List result = combinationStore.getCombinations(2, 3)

        then:
          result.size() == 1
          result[0].values.size() == 2
          result[0].values == [1, 2]
        
    }
    
    void "5 total, 2 cells, 2 options"() {
        when:
          List result = combinationStore.getCombinations(2, 5)
        
        then:
          result.values.size() == 2
          result.values.contains([1, 4])
          result.values.contains([2, 3])
    }
    
    void "18 total, 4 cells, 11 options"() {
        when:
          List result = combinationStore.getCombinations(4, 18)
        
        then:
          result.size() == 11
          result.values.contains([1, 2, 6, 9])
    }
}
