package dulcinea

import dulcinea.nikoli.builder.Grid
import dulcinea.nikoli.builder.GridBuilder
import dulcinea.nikoli.GridResolver
import spock.lang.Specification

class GridResolverTest extends Specification {

    void "Grid with value remove possibles from all cells in same region"() {
        when:
          Grid grid = grid()
          grid.cells[0][0].value = 3
          Grid result = GridResolver.resolveGrid(grid)
        
        then:
          result.cells[0][0].value == 3
          result.cells[0][1].possibles.contains(1)
          result.cells[0][1].possibles.contains(2)
          ! result.cells[0][1].possibles.contains(3)
    }
    
    void "Cell with only one value possible is set to value"() {
        when:
          Grid grid = grid()
  
          grid.cells[0][3].value = 1
          grid.cells[0][4].value = 2
          grid.cells[0][5].value = 3
          grid.cells[0][6].value = 8
        
          grid.cells[3][0].value = 4
          grid.cells[4][0].value = 5
          grid.cells[5][0].value = 6
          grid.cells[6][0].value = 7
        
          Grid result = GridResolver.resolveGrid(grid)
        
        then:
          result.cells[0][0]
          result.cells[0][0].value == 9
    }
    
    void "Region with value only available in one cell"() {
        when:
          Grid grid = grid()
          
          grid.cells[1][4].value = 1
          grid.cells[2][7].value = 1
          grid.cells[4][1].value = 1
          grid.cells[7][2].value = 1
        
          Grid result = GridResolver.resolveGrid(grid)
          
        then:
          result.cells[0][0].value == 1
        
    }
    
    Grid grid() {
        return GridBuilder.build(9, 9, [], [], true)
    }
}
