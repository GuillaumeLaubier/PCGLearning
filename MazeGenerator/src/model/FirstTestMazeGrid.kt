package model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class FirstTestMazeGrid(val width: Int, val height: Int) {

    private val grid by lazy {
        val grid = ArrayList<ArrayList<FirstTestMazeCell>>()
        for (x in 0..(width - 1)) {
            grid.add(ArrayList())
            for (y in 0..(height - 1)) {
                grid[x].add(FirstTestMazeCell(x, y, this))
            }
        }

        grid
    }

    operator fun get(x: Int, y: Int): FirstTestMazeCell {
        return grid[x][y]
    }

    fun getImage(): Image {
        val writableImage = WritableImage(width * FirstTestMazeCell.width, height * FirstTestMazeCell.height)

        for (x in 0..(width - 1)) {
            for (y in 0..(height - 1)) {

                val cellImage = grid[x][y].getImage()
                for (ix in 0..(FirstTestMazeCell.width - 1)) {
                    for (iy in 0..(FirstTestMazeCell.height - 1)) {
                        writableImage.pixelWriter.setColor(
                            x * FirstTestMazeCell.width + ix,
                            y * FirstTestMazeCell.height + iy,
                            cellImage.pixelReader.getColor(ix, iy)
                        )
                    }
                }
            }
        }

        return writableImage
    }
}