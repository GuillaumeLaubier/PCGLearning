package model

import extension.fillUpWithColor
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

/**
 * Tile are the smallest component of the maze board. It can either be a corridor or a wall.
 * Any MazeCell is composed of several MazeTile
 */
abstract class MazeTile(val positionX: Int, val positionY: Int) {

    companion object {
        const val width: Int = 20
        const val height: Int = 20
    }

    var isVisited = false

    enum class TileType {
        BOARD_CORNER, BOARD_WALL, CORRIDOR, WALL, START, FINISH, UNDEFINED
    }

    var type: TileType = TileType.UNDEFINED

    // Related MazeCells
    var topCell: MazeCell? = null
    var bottomCell: MazeCell? = null
    var leftCell: MazeCell? = null
    var rightCell: MazeCell? = null
    var currentCell: MazeCell? = null

    abstract fun getAdjacentTiles(): List<MazeTile>

    fun getSpecificAdjacentTiles(type: TileType) = getAdjacentTiles().filter { cell -> cell.type == type }

    fun toImage(): Image {
        val writableImg = WritableImage(width, height)

        val color = when (type) {
            TileType.CORRIDOR -> Color.WHITE
            TileType.WALL, TileType.BOARD_WALL, TileType.BOARD_CORNER -> Color.BLACK
            TileType.UNDEFINED -> Color.WHITE
            TileType.START -> Color.GREEN
            TileType.FINISH -> Color.RED
        }

        writableImg.fillUpWithColor(color)

        return writableImg
    }
}