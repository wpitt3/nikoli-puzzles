package dulcinea.integration

import dulcinea.nikoli.GridResolver
import dulcinea.nikoli.builder.Grid
import spock.lang.Specification

class EasyPuzzleTests extends Specification {
    
    void "Easy Sudoku"() {
        when:
          Grid sudoku = PuzzleReader.readPuzzle('/sudoku.txt')
        
          sudoku = GridResolver.resolveGrid(sudoku)
        
        then:
          sudoku.isComplete()
    }
    
    void "Easy Kakuro"() {
        when:
          Grid kakuro = PuzzleReader.readPuzzle('/kakuro.txt')
        
          kakuro = GridResolver.resolveGrid(kakuro)
        
        then:
          !kakuro.isComplete()
    }
    
    void "Easy Killer"() {
        when:
          Grid killer = PuzzleReader.readPuzzle('/killer.txt')
          
          killer = GridResolver.resolveGrid(killer)
        
        then:
          !killer.isComplete()
    }
    
}
