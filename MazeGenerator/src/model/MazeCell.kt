package model

import javafx.scene.image.Image
import javafx.scene.image.PixelWriter
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class MazeCell {

    companion object {
        const val width = 40
        const val height = 40
    }

    var isVisited = false

    val hasTopWall = true
    val hasBottomWall = true
    val hasLeftWall = true
    val hasRightWell = true

    fun getImage(): Image {
        val writableImage = WritableImage(width, height)

        draw(writableImage.pixelWriter, 0, width - 1, 0, height - 1, Color.WHITE)

        if (hasTopWall) {
            draw(writableImage.pixelWriter, 0, width - 1, 0, (height / 4) - 1, Color.BLACK)
        }

        if (hasBottomWall) {
            draw(writableImage.pixelWriter, 0, width - 1, (height / 4) * 3, height - 1, Color.BLACK)
        }

        if (hasLeftWall) {
            draw(writableImage.pixelWriter, 0, (width / 4) - 1, 0, height - 1, Color.BLACK)
        }

        if (hasRightWell) {
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
}