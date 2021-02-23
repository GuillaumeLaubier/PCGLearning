package pathfinder

import model.MazeTile
import writeImage
import kotlin.math.absoluteValue

class AStarPathFinder : MazePathFinder() {

    private class PathStep(val tile: MazeTile, var previousStep: PathStep?, val startCost: Int, val endCost: Int) {
        val totalCost = startCost + endCost
    }

    override fun resolveMaze(startTile: MazeTile, endTile: MazeTile?) {
        assert(startTile.type == MazeTile.TileType.START && endTile?.type == MazeTile.TileType.FINISH)

        endTile?.let { endTile ->

            val steps = ArrayList<PathStep>()

            startTile.getAdjacentTiles().filter { it.isSearchable() }.forEach {
                steps.add(PathStep(it, null, computeStartCost(startTile, it), computeEndCost(endTile, it)))
            }

            while (steps.isNotEmpty()) {
                val currentStep = steps
                    .sortedWith(compareBy<PathStep> { it.totalCost }.thenBy { it.endCost }.thenBy { it.startCost })
                    .first()
                val currentTile = currentStep.tile

                currentTile.type = MazeTile.TileType.SEARCHED

                if (currentTile.getSpecificAdjacentTiles(MazeTile.TileType.FINISH).any()) {
                    // Now draw the path
                    var step: PathStep? = currentStep

                    do {
                        step?.let {
                            it.tile.type = MazeTile.TileType.VALIDATED
                            writeImage(it.tile.parentGridImage())

                            step = it.previousStep
                        }
                    } while (step != null)

                    break
                }

                currentTile.getAdjacentTiles().filter { it.isSearchable() }.forEach {
                    val nextStep = PathStep(it, currentStep, computeStartCost(startTile, it), computeEndCost(endTile, it))
                    steps.add(nextStep)
                }

                steps.remove(currentStep)

                writeImage(currentTile.parentGridImage())
            }

        }
    }

    private fun computeStartCost(startTile: MazeTile, currentTile: MazeTile): Int {
        // 1 point for each tile that is between the start and the current tile.
        val distanceX = (currentTile.positionX - startTile.positionX).absoluteValue
        val distanceY = (currentTile.positionY - startTile.positionY).absoluteValue
        return distanceX + distanceY
    }

    private fun computeEndCost(endTile: MazeTile, currentTile: MazeTile): Int {
        // 1 point for each tile that is between the end and the current tile.
        val distanceX = (currentTile.positionX - endTile.positionX).absoluteValue
        val distanceY = (currentTile.positionY - endTile.positionY).absoluteValue
        return distanceX + distanceY
    }


}