package model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class ArrayListMazeGrid(val mazeWidth: Int, val mazeHeight: Int) {

    // 2 extras cells for board walls
    val boardWidth = mazeWidth + 2
    val boardHeight = mazeHeight + 2

    val board by lazy {
            val board = ArrayList<MazeCell>()

        for (x in 0..(boardWidth - 1)) {

            for (y in 0..(boardHeight - 1)) {
                val cell = ArrayListMazeCell(x, y, this)

                // Immediately define board walls and corners
                if (x == 0 && y == 0
                    || x == 0 && y == boardHeight - 1
                    || x == boardWidth - 1 && y == 0
                    || x == boardWidth - 1 && y == boardHeight - 1) {
                    cell.type = MazeCell.CellType.BOARD_CORNER
                } else if (x == 0 || x == boardWidth - 1 || y == 0 || y == boardHeight - 1) {
                    cell.type = MazeCell.CellType.BOARD_WALL
                }

                board.add(cell)
            }
        }

        // Once initialized, define random start and end within board_wall cells
        board.filter { it.type == MazeCell.CellType.BOARD_WALL }.random().type = MazeCell.CellType.START
        board.filter { it.type == MazeCell.CellType.BOARD_WALL }.random().type = MazeCell.CellType.END

        board
    }

    operator fun get(x: Int, y: Int): MazeCell? = board.firstOrNull { it.positionX == x && it.positionY == y }

    fun toImage(): Image {
        val writableImg = WritableImage(boardWidth * MazeCell.width, boardHeight * MazeCell.height)

        for (x in 0..(boardWidth - 1)) {
            for (y in 0..(boardHeight - 1)) {
                val cellImage = this[x, y]!!.toImage()

                for (ix in 0..(MazeCell.width - 1)) {
                    for (iy in 0..(MazeCell.height - 1)) {
                        writableImg.pixelWriter.setColor(
                            x * MazeCell.width + ix,
                            y * MazeCell.height + iy,
                            cellImage.pixelReader.getColor(ix, iy)
                        )
                    }
                }
            }
        }

        return writableImg
    }

}