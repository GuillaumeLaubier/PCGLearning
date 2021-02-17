package model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class MazeGrid(val width: Int, val height: Int) {

    private val grid = Array(width) { Array(height) { MazeCell() } }

    operator fun get(x: Int, y: Int): MazeCell {
        return grid[x][y]
    }

    fun getImage(): Image {
        val writableImage = WritableImage(width * MazeCell.width, height * MazeCell.height)

        for (x in 0..(width - 1)) {
            for (y in 0..(height - 1)) {

                val cellImage = grid[x][y].getImage()
                for (ix in 0..(MazeCell.width - 1)) {
                    for (iy in 0..(MazeCell.height - 1)) {
                        writableImage.pixelWriter.setColor(x * MazeCell.width + ix,
                            y * MazeCell.height + iy,
                            cellImage.pixelReader.getColor(ix, iy)
                        )
                    }
                }
            }
        }

        return writableImage
    }
}