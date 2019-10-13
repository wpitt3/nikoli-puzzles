package dulcinea

import dulcinea.nikoli.Grid
import dulcinea.nikoli.GridBuilder
import spock.lang.Specification

class GridBuilderTest extends Specification {

    void "basic grid"() {
        when:
          Grid result = GridBuilder.build(9, 9, [], [], false)

        then:
          result.height == 9
          result.width == 9
          result.cells == []
          result.regions == []
    }
    
    

}
