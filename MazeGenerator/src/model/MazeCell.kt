package model

/**
 * A MazeCell represent a bunch of Mazetile. Generally, the center of the MazeCell is a corridor, and all other tiles
 * cann be either wall or other corridors.
 */
class MazeCell(val centerTile: MazeTile, grid: ArrayListMazeGrid) {

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
}