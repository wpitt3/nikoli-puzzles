package dulcinea.integration

import dulcinea.nikoli.builder.Grid
import dulcinea.nikoli.builder.GridBuilder

class PuzzleReader {
    
    static Grid readPuzzle(String fileName) {
        List<String> puzzleFileLines = new File(PuzzleReader.class.getResource(fileName).toURI()).readLines()
        return readPuzzle(puzzleFileLines)
    }
    
    static Grid readPuzzle(List<String> puzzleFileLines) {
        int commaCount = puzzleFileLines[0].split(',').length -1
        
        if (commaCount < 1) {
            return readSudoku(puzzleFileLines)
        }
        if (commaCount == 1) {
            return readKakuro(puzzleFileLines)
        }
        if (commaCount > 1) {
            return readKiller(puzzleFileLines)
        }
    }
    
    static Grid readSudoku(List<String> lines) {
        List values = (0..8).inject([]){ result, y ->
            String[] line = lines[y].split('')
            return result + (0..8).findAll{x -> line.length > x && line[x].trim()}.collect { x ->
                return [x, y, Integer.parseInt(line[x].trim())]
            }
        }
        return GridBuilder.build(9, 9, [], values, true)
    }
    
    static Grid readKakuro(List<String> lines) {
        List<Integer> size = lines[0].split(',').collect{Integer.parseInt(it)}
        List regions = lines.drop(1).collect{it.split(',')}.collect{ region ->
            Integer value = Integer.parseInt(region.first())
            region = region.drop(1).collect{it.split('-').collect{Integer.parseInt(it)}}
                    .collect{ it.size() == 2 ? (it[0]..it[1]) : it}
            return [value] + region[1].collect{ y ->
                region[0].collect{ x -> [x, y] }
            }.flatten()
        }
        return GridBuilder.build(size[0], size[1], regions, [], false)
    }
    
    static Grid readKiller(List<String> lines) {
        List<List<Integer>> regions = lines.collect{it.split(',').collect{Integer.parseInt(it)}}
        List coords = regions.inject([]){result, it -> result + it.drop(1).collate(2)}
        List coordsUnique = coords.clone().unique()
        if (coordsUnique.size() != 81) {
            println coordsUnique.size()
            (0..<coords.size()).each { index ->
                if(coordsUnique[index] != coords[index]) {
                    println coords[index]
                }
            }
            throw new Exception();
        }
        
        
        return GridBuilder.build(9, 9, regions, [], true)
    }
}
