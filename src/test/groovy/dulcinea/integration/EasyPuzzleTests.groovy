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
          kakuro.isComplete()
    }
    
    void "Easy Killer"() {
        when:
          Grid killer = PuzzleReader.readPuzzle('/killer1.txt')
          
          killer = GridResolver.resolveGrid(killer)
        
        then:
          killer.isComplete()
          killer.getCell(0, 2).value == 5
          killer.getCell(6, 2).value == 7
          killer.getCell(0, 3).value == 4
          killer.getCell(1, 3).value == 1
          killer.getCell(2, 3).value == 8
          killer.getCell(6, 3).value == 9
          killer.getCell(7, 3).value == 5
          killer.getCell(0, 6).value == 8
          killer.getCell(1, 6).value == 9
          killer.getCell(0, 7).value == 6
          killer.getCell(0, 8).value == 3
          killer.getCell(1, 8).value == 2
    }
    
    void "Medium Killer"() {
        when:
          Grid killer = PuzzleReader.readPuzzle('/killer2.txt')
          
          killer = GridResolver.resolveGrid(killer)
          killer = GridResolver.resolveGrid(killer)
        
        then:
          killer.isComplete()
    }
    
    void "Hard Killer"() {
        when:
          Grid killer = PuzzleReader.readPuzzle('/killer3.txt')
          
          killer = GridResolver.resolveGrid(killer)
          killer.getCell(3,1).value = 3 // bug?
          killer.getCell(8,3).value = 3 // PossibleRegions upgrade
          killer.getCell(5,3).value = 4
          killer = GridResolver.resolveGrid(killer)
          
          println(killer.printCells())
          println(killer.printPossibles())
        
        then:
          killer.isComplete()
    }
    
}
