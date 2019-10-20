package dulcinea.nikoli

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
    
    void "Region with naked double, all other cells remove double"() {
        when:
          Grid grid = grid()
  
          grid.cells[0][0].possibles = [1,2]
          grid.cells[0][1].possibles = [1,2]
          
          Grid result = GridResolver.resolveGrid(grid)
        
        then:
          result.cells[0][0].possibles == [1,2]
          result.cells[0][2].possibles == [3,4,5,6,7,8,9]
    }
    
    void "Region with naked triple, all other cells remove triple"() {
        when:
          Grid grid = grid()
  
          grid.cells[0][0].possibles = [1,2,3]
          grid.cells[0][1].possibles = [1,2,3]
          grid.cells[0][2].possibles = [1,2,3]
          
          Grid result = GridResolver.resolveGrid(grid)
        
        then:
          result.cells[0][0].possibles == [1,2,3]
          result.cells[0][3].possibles == [4,5,6,7,8,9]
    }
    
    void "Region with naked triple withSubset, all other cells remove triple"() {
        when:
          Grid grid = grid()
          
          grid.cells[0][0].possibles = [1,2]
          grid.cells[0][1].possibles = [1,3]
          grid.cells[0][2].possibles = [2,3]
          
          Grid result = GridResolver.resolveGrid(grid)
        
        then:
          result.cells[0][0].possibles == [1,2]
          result.cells[0][3].possibles == [4,5,6,7,8,9]
    }
    
    void "Region with naked quadruple withSubset, all other cells remove quadruple"() {
        when:
          Grid grid = grid()
          
          grid.cells[0][0].possibles = [1,2]
          grid.cells[0][1].possibles = [2,3]
          grid.cells[0][2].possibles = [3,4]
          grid.cells[0][3].possibles = [1,4]
          
          Grid result = GridResolver.resolveGrid(grid)
        
        then:
          result.cells[0][0].possibles == [1,2]
          result.cells[0][4].possibles == [5,6,7,8,9]
    }
    
    void "Region with hidden double, hidden double cells remove double"() {
        when:
          Grid grid = grid()
          
          (2..8).each { x ->
              grid.cells[0][x].possibles = [1,2,3,4,5,6,7]
          }
          
          Grid result = GridResolver.resolveGrid(grid)
        
        then:
          result.cells[0][0].possibles == [8,9]
          result.cells[0][1].possibles == [8,9]
    }
    
    Grid grid() {
        return GridBuilder.build(9, 9, [], [], true)
    }
}
