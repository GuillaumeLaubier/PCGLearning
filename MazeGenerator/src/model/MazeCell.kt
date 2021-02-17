package model

import javafx.scene.image.Image
import javafx.scene.image.PixelWriter
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import java.lang.Exception
import kotlin.math.absoluteValue

class MazeCell(val positionX: Int, val positionY: Int, val parentGrid: MazeGrid) {

    companion object {
        const val width = 40
        const val height = 40
    }

    enum class NeighbourPosition {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        NONE
    }

    var isVisited = false

    var hasTopWall = true
    var hasBottomWall = true
    var hasLeftWall = true
    var hasRightWall = true

    fun getTopNeighbour(): MazeCell? {
        return try {
            parentGrid[positionX, positionY - 1]
        } catch (_: Exception) {
            null
        }
    }

    fun getBottomNeighbour(): MazeCell? {
        return try {
            parentGrid[positionX, positionY + 1]
        } catch (_: Exception) {
            null
        }
    }

    fun getLeftNeighbour(): MazeCell? {
        return try {
            parentGrid[positionX - 1, positionY]
        } catch (_: Exception) {
            null
        }
    }

    fun getRightNeighbour(): MazeCell? {
        return try {
            parentGrid[positionX + 1, positionY]
        } catch (_: Exception) {
            null
        }
    }

    fun getUnvisitedNeighbour(): ArrayList<MazeCell> {
        val list = ArrayList<MazeCell>()

        getTopNeighbour()?.let {
            if (!it.isVisited) {
                list.add(it)
            }
        }

        getBottomNeighbour()?.let {
            if (!it.isVisited) {
                list.add(it)
            }
        }

        getLeftNeighbour()?.let {
            if (!it.isVisited) {
                list.add(it)
            }
        }

        getRightNeighbour()?.let {
            if (!it.isVisited) {
                list.add(it)
            }
        }

        return list
    }

    fun removeWallWith(cell: MazeCell) {
        when (whichNeighbour(cell)) {
            NeighbourPosition.TOP -> {
                hasTopWall = false
                cell.hasBottomWall = false
            }
            NeighbourPosition.BOTTOM -> {
                hasBottomWall = false
                cell.hasTopWall = false
            }
            NeighbourPosition.LEFT -> {
                hasLeftWall = false
                cell.hasRightWall = false
            }
            NeighbourPosition.RIGHT -> {
                hasRightWall = false
                cell.hasLeftWall = false
            }
            else -> {}
        }
    }

    private fun whichNeighbour(cell: MazeCell): NeighbourPosition {
        // Check if this is a real neighbour
        return if ((positionX - cell.positionX).absoluteValue == 1 && positionY - cell.positionY == 0
                || positionX - cell.positionX == 0 && (positionY - cell.positionY).absoluteValue == 1) {
            if (positionX - cell.positionX == 1 && positionY - cell.positionY == 0) {
                NeighbourPosition.LEFT
            } else if (positionX - cell.positionX == -1 && positionY - cell.positionY == 0) {
                NeighbourPosition.RIGHT
            } else if (positionX - cell.positionX == 0 && positionY - cell.positionY == 1) {
                NeighbourPosition.TOP
            } else {
                NeighbourPosition.BOTTOM
            }
        } else {
            return NeighbourPosition.NONE
        }
    }

    fun getImage(): Image {
        val writableImage = WritableImage(width, height)

        val color = if (isVisited) { Color.WHITE } else { Color.BLACK }
        draw(writableImage.pixelWriter, 0, width - 1, 0, height - 1, color)

        drawCorners(writableImage.pixelWriter, Color.BLACK)

        if (hasTopWall) {
            draw(writableImage.pixelWriter, 0, width - 1, 0, (height / 4) - 1, Color.BLACK)
        }

        if (hasBottomWall) {
            draw(writableImage.pixelWriter, 0, width - 1, (height / 4) * 3, height - 1, Color.BLACK)
        }

        if (hasLeftWall) {
            draw(writableImage.pixelWriter, 0, (width / 4) - 1, 0, height - 1, Color.BLACK)
        }

        if (hasRightWall) {
            draw(writableImage.pixelWriter, (width / 4) * 3, width - 1, 0, height - 1, Color.BLACK)
        }

        return writableImage
    }

    private fun draw(pixelWriter: PixelWriter, xStart: Int, xEnd: Int, yStart: Int, yEnd: Int, color: Color) {
        for (x in xStart..xEnd) {
            for (y in yStart..yEnd) {
                pixelWriter.setColor(x, y, color)
            }
        }
    }

    private fun drawCorners(pixelWriter: PixelWriter, color: Color) {
        // Top left corner
        draw(pixelWriter, 0, (width / 4) - 1, 0, (height / 4) - 1, color)

        // Top right corner
        draw(pixelWriter, (width / 4) * 3, width -1, 0, (height / 4) - 1, color)

        // Bottom left corner
        draw(pixelWriter, 0, (width / 4) - 1, (height / 4) * 3, height - 1, color)

        // Bottom right corner
        draw(pixelWriter, (width / 4) * 3, width - 1, (height / 4) * 3, height - 1, color)
    }
}