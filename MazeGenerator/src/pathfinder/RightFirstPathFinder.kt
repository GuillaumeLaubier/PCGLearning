package pathfinder

import model.MazeTile
import writeImage
import kotlin.test.assert

class RightFirstPathFinder : MazePathFinder() {

    enum class Direction {
        NORTH, SOUTH, EAST, WEST
    }

    override fun resolveMaze(startTile: MazeTile, endTile: MazeTile?) {
        assert(startTile.type == MazeTile.TileType.START)

        recursiveRightFirst(startTile.getAdjacentTiles().first { it.isSearchable() })
    }

    /**
     * Right first pathfinding order:
     * - right tile
     * - bottom tile
     * - left tile
     * - top tile
     */
    private fun recursiveRightFirst(tile: MazeTile): Boolean {

        writeImage(tile.parentGridImage())

        val searchingOrder = arrayListOf(Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH)

        when (tile.type) {
            MazeTile.TileType.CORRIDOR, MazeTile.TileType.UNDEFINED -> {
                tile.type = MazeTile.TileType.SEARCHED

                while (searchingOrder.isNotEmpty()) {
                    val nextTile = when (searchingOrder.first()) {
                        Direction.EAST -> tile.getRightNeighbour()
                        Direction.SOUTH -> tile.getBottomNeighbour()
                        Direction.WEST -> tile.getLeftNeighbour()
                        Direction.NORTH -> tile.getTopNeighbour()
                    }

                    nextTile?.let {
                        if (it.isSearchable() && recursiveRightFirst(nextTile)) {
                            tile.type = MazeTile.TileType.VALIDATED
                            writeImage(it.parentGridImage())
                            return true
                        }
                    }
                    searchingOrder.removeAt(0)
                }

            }

            MazeTile.TileType.FINISH -> return true
            else -> return false
        }

        tile.type = MazeTile.TileType.BACKTRACKED
        writeImage(tile.parentGridImage())
        return false
    }
}