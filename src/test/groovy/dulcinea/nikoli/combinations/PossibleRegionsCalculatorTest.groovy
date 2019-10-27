package dulcinea.nikoli.combinations

import dulcinea.nikoli.builder.Cell
import dulcinea.nikoli.builder.Region
import spock.lang.Specification

class PossibleRegionsCalculatorTest extends Specification {

    void "3 total, 2 cells, 1 option "() {
        given:
            Region region = region(3, 2)
        
        when:
          PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)

        then:
          region.cells.every{ cell ->
              cell.getPossibles() == [1, 2]
          }
    }
    
    void "5 total, 2 cells, 2 options"() {
        given:
          Region region = region(5, 2)
    
        when:
          PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          region.cells.every{ cell -> cell.getPossibles() == [1, 2, 3, 4]}
    }
    
    void "18 total, 4 cells, 11 options"() {
        given:
          Region region = region(18, 4)
    
        when:
          PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          region.cells.every{ cell -> cell.getPossibles() == [1, 2, 3, 4, 5, 6, 7, 8, 9]}
    }
    
    void "5 total, 2 cells, cannot contain value 1, 1 option"() {
        when:
          Region region = region(5, 2)
          region.cells.each{it.setImpossible(1)}
  
          PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          region.cells.every{ cell -> cell.getPossibles() == [2, 3]}
    }
    
    void "20 total, 4 cells, three have a given value"() {
        when:
          Region region = region(20, 4)
          region.cells[0].value = 7
          region.cells[2].value = 9
          region.cells[3].value = 1
          region.cells[0].updateCellsInSameRegion()
          region.cells[1].updateCellsInSameRegion()
          region.cells[2].updateCellsInSameRegion()
          
          
          PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          region.cells[0].possibles == [7]
          region.cells[1].possibles == [3]
    }
    
    void "6 total, 2 cells, one cell can not contain 1,2, 1 option"() {
        when:
          Region region = region(6, 2)
          region.cells[0].setImpossible(1)
          region.cells[0].setImpossible(2)
          
          PossibleRegionsCalculator.removeImpossibleCombinationsFromRegion(region)
        
        then:
          region.cells[0].getPossibles() == [4, 5]
          region.cells[1].getPossibles() == [1, 2]
    }
    
    Region region(Integer total, Integer noOfCells) {
        List<Cell> cells = (1..noOfCells).collect{new Cell()}
        return new Region(total, cells)
    }
}
