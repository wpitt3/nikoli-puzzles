package dulcinea.nikoli.regionsum

import dulcinea.nikoli.builder.Grid
import dulcinea.nikoli.builder.GridBuilder
import spock.lang.Specification

class RegionSumCalculatorTest extends Specification {
    
    
    void "Box with single overflowing cell"() {
        List regions = [
                [22,2,0,3,0,4,0,5,0],
                [26,3,1,3,2,4,1,5,1,4,2,5,2]
        ]
        given:
          Grid grid = grid(regions)
        
        when:
          RegionSumCalculator.checkForRegionsUncontainedByRegion(grid.regions[19])
        
        then:
          grid.getCell(2,0).value == 3
          !grid.getCell(4,0).possibles.contains(3)
    }
    
    void "Box with 2 overflowing cells, one with value set"() {
        List regions = [
                [23,2,0,3,0,4,0,5,0,6,0],
                [26,3,1,3,2,4,1,5,1,4,2,5,2]
        ]
        given:
          Grid grid = grid(regions)
          grid.getCell(6,0).setValue(1)
        
        when:
          RegionSumCalculator.checkForRegionsUncontainedByRegion(grid.regions[19])
        
        then:
          grid.getCell(2,0).value == 3
          !grid.getCell(4,0).possibles.contains(3)
    }
    
    void "Box with single underflowing cell"() {
        List regions = [
                [12,1,0,2,0,3,0],
                [10,4,0,5,0],
                [26,3,1,3,2,4,1,5,1,4,2,5,2]
        ]
        given:
          Grid grid = grid(regions)
        
        when:
          RegionSumCalculator.checkForRegionsUncontainedByRegion(grid.regions[19])
        
        then:
          grid.getCell(3,0).value == 9
          !grid.getCell(4,0).possibles.contains(9)
    }
    
    void "Box with 2 underflowing cells, one with set value"() {
        List regions = [
                [12,1,0,2,0,3,0],
                [10,4,0,5,0],
                [20,3,1,4,1,5,1,3,2,4,2],
                [8,5,2,6,2,7,2]
        ]
        given:
          Grid grid = grid(regions)
          grid.getCell(5,2).setValue(6)
        
        when:
          RegionSumCalculator.checkForRegionsUncontainedByRegion(grid.regions[19])
        
        then:
          grid.getCell(3,0).value == 9
          !grid.getCell(4,0).possibles.contains(9)
    }
    
    void "Box with underflowing and overflowing cells, underflowing value is set"() {
        List regions = [
                [45,2,0,3,0,4,0,5,0,3,1,4,1,5,1,3,2,4,2],
                [8,5,2,6,2,7,2]
        ]
        given:
          Grid grid = grid(regions)
          grid.getCell(5,2).setValue(6)
        
        when:
          RegionSumCalculator.checkForRegionsUncontainedByRegion(grid.regions[19])
        
        then:
          grid.getCell(2,0).value == 6
          !grid.getCell(1,0).possibles.contains(6)
    }
    void "Box with underflowing and overflowing cells, overflowing value is set"() {
        List regions = [
                [45,2,0,3,0,4,0,5,0,3,1,4,1,5,1,3,2,4,2],
                [8,5,2,6,2,7,2]
        ]
        
        given:
          Grid grid = grid(regions)
          grid.getCell(2,0).setValue(6)
        
        when:
          RegionSumCalculator.checkForRegionsUncontainedByRegion(grid.regions[19])
        
        then:
          grid.getCell(5,2).value == 6
          !grid.getCell(7,2).possibles.contains(6)
    }
    
    Grid grid(List regions) {
        return GridBuilder.build(9, 9, regions, [], true)
    }
}
