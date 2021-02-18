package model

import extension.fillUpWithColor
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

abstract class MazeCell(val positionX: Int, val positionY: Int, val width: Int = 20, val height: Int = 20) {

    var isVisited = false

    enum class CellType {
        CORRIDOR, WALL, UNDEFINED
    }

    var type: CellType = CellType.UNDEFINED

    abstract fun getNeighbours(): List<MazeCell>

    fun getUnvisitedNeighbours() = getNeighbours().filter { cell -> !cell.isVisited }

    fun toImage(): Image {
        val writableImg = WritableImage(width, height)

        val color = when (type) {
            CellType.CORRIDOR -> Color.WHITE
            CellType.WALL -> Color.BLACK
            CellType.UNDEFINED -> Color.DARKGRAY
        }

        writableImg.fillUpWithColor(color)

        return writableImg
    }
}