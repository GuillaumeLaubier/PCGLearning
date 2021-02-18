package model

import extension.fillUpWithColor
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

abstract class MazeCell(val positionX: Int, val positionY: Int) {

    companion object {
        const val width: Int = 20
        const val height: Int = 20
    }

    var isVisited = false

    enum class CellType {
        BOARD_CORNER, BOARD_WALL, CORRIDOR, WALL, START, END, UNDEFINED
    }

    var type: CellType = CellType.UNDEFINED

    abstract fun getNeighbours(): List<MazeCell>

    fun getUndefinedNeighbours() = getNeighbours().filter { cell -> cell.type != CellType.UNDEFINED }

    fun toImage(): Image {
        val writableImg = WritableImage(width, height)

        val color = when (type) {
            CellType.CORRIDOR -> Color.WHITE
            CellType.WALL, CellType.BOARD_WALL, CellType.BOARD_CORNER -> Color.BLACK
            CellType.UNDEFINED -> Color.WHITE
            CellType.START -> Color.GREEN
            CellType.END -> Color.RED
        }

        writableImg.fillUpWithColor(color)

        return writableImg
    }
}