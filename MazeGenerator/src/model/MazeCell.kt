package model

/**
 * A MazeCell represent a bunch of Mazetile. Generally, the center of the MazeCell is a corridor, and all other tiles
 * cann be either wall or other corridors.
 */
class MazeCell(val centerTile: MazeTile, val grid: ArrayListMazeGrid) {

    val topTile = grid[centerTile.positionX, centerTile.positionY - 1]
    val bottomTile = grid[centerTile.positionX, centerTile.positionY + 1]
    val leftTile = grid[centerTile.positionX - 1, centerTile.positionY]
    val rightTile = grid[centerTile.positionX + 1, centerTile.positionY]

    init {
        topTile?.bottomCell = this
        bottomTile?.topCell = this
        leftTile?.rightCell = this
        rightTile?.leftCell = this
        centerTile.currentCell = this
    }

    fun getAdjacentCells(): ArrayList<MazeCell> {
        val list = ArrayList<MazeCell>()

        topTile?.topCell?.let {
            list.add(it)
        }

        bottomTile?.bottomCell?.let {
            list.add(it)
        }

        leftTile?.leftCell?.let {
            list.add(it)
        }

        rightTile?.rightCell?.let {
            list.add(it)
        }

        return list
    }

    // Other cell must be adjacent!
    fun removeWallWithCell(otherCell: MazeCell) {
        if (centerTile.positionX == otherCell.centerTile.positionX) {
            if (centerTile.positionY < otherCell.centerTile.positionY) {
                bottomTile?.type = MazeTile.TileType.CORRIDOR
            } else {
                topTile?.type = MazeTile.TileType.CORRIDOR
            }
        } else {
            if (centerTile.positionX < otherCell.centerTile.positionX) {
                rightTile?.type = MazeTile.TileType.CORRIDOR
            } else {
                leftTile?.type = MazeTile.TileType.CORRIDOR
            }
        }
    }
}