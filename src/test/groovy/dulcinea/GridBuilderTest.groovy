package dulcinea

import dulcinea.nikoli.Cell
import dulcinea.nikoli.Grid
import dulcinea.nikoli.GridBuilder
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
          result.regions.size() == 27
    }

}
