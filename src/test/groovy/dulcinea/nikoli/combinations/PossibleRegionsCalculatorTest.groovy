package dulcinea.nikoli.combinations

import dulcinea.nikoli.builder.Cell
import dulcinea.nikoli.builder.Region
import spock.lang.Specification

class PossibleRegionsCalculatorTest extends Specification {

    void "3 total, 2 cells, 1 option "() {
        given:
            Region region = region(3, 2)
        
        when:
          boolean result = PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)

        then:
          result
          region.cells.every{ cell ->
              cell.getPossibles() == [1, 2]
          }
    }
    
    void "5 total, 2 cells, 2 options"() {
        given:
          Region region = region(5, 2)
    
        when:
          boolean result = PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          result
          region.cells.every{ cell -> cell.getPossibles() == [1, 2, 3, 4]}
    }
    
    void "18 total, 4 cells, 11 options"() {
        given:
          Region region = region(18, 4)
    
        when:
          boolean result = PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          result
          region.cells.every{ cell -> cell.getPossibles() == [1, 2, 3, 4, 5, 6, 7, 8, 9]}
    }
    
    void "5 total, 2 cells, cannot contain value 1, 1 option"() {
        when:
          Region region = region(5, 2)
          region.cells.each{it.setImpossible(1)}
  
          boolean result = PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          result
          region.cells.every{ cell -> cell.getPossibles() == [2, 3]}
    }
    
    Region region(Integer total, Integer noOfCells) {
        List<Cell> cells = (1..noOfCells).collect{new Cell()}
        return new Region(total, cells)
    }
}
