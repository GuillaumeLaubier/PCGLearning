package pathfinder

import model.MazeTile
import kotlin.test.assert

class RightFirstPathFinder : MazePathFinder() {

    enum class Direction {
        NORTH, SOUTH, EAST, WEST
    }

    override fun resolveMaze(startTile: MazeTile) {
        assert(startTile.type == MazeTile.TileType.START)

        recursiveRightFirst(startTile.getSpecificAdjacentTiles(MazeTile.TileType.CORRIDOR).first())
    }

    /**
     * Right first pathfinding order:
     * - right tile
     * - bottom tile
     * - left tile
     * - top tile
     */
    private fun recursiveRightFirst(tile: MazeTile): Boolean {

        val searchingOrder = arrayListOf(Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH)

        when (tile.type) {
            MazeTile.TileType.CORRIDOR -> {
                tile.type = MazeTile.TileType.SEARCHED

                while (searchingOrder.isNotEmpty()) {
                    val nextTile = when (searchingOrder.first()) {
                        Direction.EAST -> tile.getRightNeighbour()
                        Direction.SOUTH -> tile.getBottomNeighbour()
                        Direction.WEST -> tile.getLeftNeighbour()
                        Direction.NORTH -> tile.getTopNeighbour()
                    }

                    if (nextTile?.type == MazeTile.TileType.CORRIDOR) {
                        return recursiveRightFirst(nextTile)
                    } else {
                        searchingOrder.removeAt(0)
                    }
                }

                return false
            }

            MazeTile.TileType.FINISH -> return true
            else -> return false
        }
    }
}