package pathfinder

import model.MazeTile
import writeImage
import java.util.ArrayList

class BreadthFirstPathFinder : MazePathFinder() {

    private class PathStep(val tile: MazeTile, var previousStep: PathStep?)

    override fun resolveMaze(startTile: MazeTile, endTile: MazeTile?) {
        assert(startTile.type == MazeTile.TileType.START)

        val corridors = ArrayList<PathStep>()


        startTile.getAdjacentTiles().filter { it.isSearchable() }.forEach {
            corridors.add(PathStep(it, null))
        }

        while (corridors.isNotEmpty()) {
            val currentStep = corridors.first()
            val currentTile = currentStep.tile

            currentTile.type = MazeTile.TileType.SEARCHED

            if (currentTile.getSpecificAdjacentTiles(MazeTile.TileType.FINISH).any()) {
                // Now draw the path
                var step: PathStep? = currentStep

                do {
                    step?.let {
                        it.tile.type = MazeTile.TileType.VALIDATED
                        writeImage(it.tile.parentGridImage(), "jikstra")

                        step = it.previousStep
                    }
                } while (step != null)

                break
            }

            currentTile.getAdjacentTiles().filter { it.isSearchable() }.forEach {
                val nextStep = PathStep(it, currentStep)
                corridors.add(nextStep)
            }

            corridors.removeAt(0)

            writeImage(currentTile.parentGridImage(), "jikstra")
        }
    }


}