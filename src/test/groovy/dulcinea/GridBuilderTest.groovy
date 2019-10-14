package dulcinea

import dulcinea.nikoli.builder.Cell
import dulcinea.nikoli.builder.Grid
import dulcinea.nikoli.builder.GridBuilder
import spock.lang.Specification

class GridBuilderTest extends Specification {

    void "empty grid"() {
        when:
          Grid result = GridBuilder.build(6, 4, [], [], false)

        then:
          result.width == 6
          result.height == 4
          result.cells.size() == 6
          result.cells.every( { row -> row.size() == 4 })
          result.cells.every( { row -> row.every{ it == null }} )
          result.regions == []
    }
    
    void "basic sudoku grid"() {
        when:
          Grid result = GridBuilder.build(9, 9, [], [], true)
        
        then:
          result.width == 9
          result.height == 9
          result.cells.size() == 9
          result.cells.every( { row -> row.size() == 9 })
          result.cells.every( { row -> row.every{ Cell cell -> cell.value == null}} )
          result.regions.every( { region -> region.total == 45 })
          result.regions.every( { region -> region.cells.size() == 9 })
          result.regions.size() == 27
          // regions 0-8 are rows
          result.regions[0].cells[0] == result.cells[0][0]
          result.regions[0].cells[0] != result.cells[0][1] // checking two null cells are not the same
          result.regions[0].cells[8] == result.cells[8][0]
          // regions 9-17 are columns
          result.regions[9].cells[0] == result.cells[0][0]
          result.regions[9].cells[8] == result.cells[0][8]
          // regions 18-26 are boxes
          // box '0' is the top left corner
          result.regions[18].cells[0] == result.cells[0][0]
          result.regions[18].cells[8] == result.cells[2][2]
          // box '0' is the bottom right corner
          result.regions[26].cells[0] == result.cells[6][6]
          result.regions[26].cells[8] == result.cells[8][8]
          // box '1' is to the right of box '0'
          result.regions[19].cells[0] == result.cells[3][0]
          // cell '1' is to the right of cell '0'
          result.regions[19].cells[1] == result.cells[4][0]
    }
    
    void "Sudoku grid with values"() {
        when:
          List values = [
              [0, 3, 9],
              [4, 5, 1],
          ]
          Grid result = GridBuilder.build(9, 9, [], values, true)
        
        then:
          result.width == 9
          result.height == 9
          result.cells.size() == 9
          result.cells.every( { row -> row.size() == 9 })
          result.regions.every( { region -> region.total == 45 })
          result.regions.every( { region -> region.cells.size() == 9 })
          result.regions.size() == 27
          result.cells[0][3].value == 9
          result.cells[4][5].value == 1
    }
    
    void "Sudoku grid with regions"() {
        when:
          List regions = [[10, 0, 0, 0, 1, 0, 2], [4, 2, 1, 2, 2]]
          Grid result = GridBuilder.build(9, 9, regions, [], true)
        
        then:
          result.width == 9
          result.height == 9
          result.cells.size() == 9
          result.cells.every( { row -> row.size() == 9 })
  
          result.regions.size() == 29
          result.regions[27].total == 10
          result.regions[27].cells[0] == result.cells[0][0]
          result.regions[27].cells[1] == result.cells[0][1]
          result.regions[27].cells[2] == result.cells[0][2]
          result.regions[28].total == 4
          result.regions[28].cells[0] == result.cells[2][1]
          result.regions[28].cells[1] == result.cells[2][2]
    }
    
    void "Kakuro with regions"(){
        when:
          List regions = [
              [4, 0, 0, 1, 0],
              [3, 0, 1, 1, 1],
              [7, 0, 0, 0, 1],
              [6, 1, 0, 1, 1]
          ]
          Grid result = GridBuilder.build(2, 2, regions, [], false)
    
        then:
          result.width == 2
          result.height == 2
          result.cells.size() == 2
          result.cells.every( { row -> row.size() == 2 })
          result.regions[0].total == 4
          result.regions[0].cells[0] == result.cells[0][0]
          result.regions[0].cells[1] == result.cells[1][0]
    }
}
