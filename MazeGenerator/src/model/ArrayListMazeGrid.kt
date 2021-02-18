package model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class ArrayListMazeGrid(val mazeWidth: Int, val mazeHeight: Int) {

    // 2 extras cells for board walls
    val boardWidth = mazeWidth + 2
    val boardHeight = mazeHeight + 2

    val board by lazy {
            val board = ArrayList<ArrayList<MazeCell>>()

        for (x in 0..(boardWidth - 1)) {
            board.add(ArrayList())

            for (y in 0..(boardHeight - 1)) {
                val cell = ArrayListMazeCell(x, y, board)

                // Immediately define board walls and corners
                if (x == 0 && y == 0
                    || x == 0 && y == boardHeight - 1
                    || x == boardWidth - 1 && y == 0
                    || x == boardWidth - 1 && y == boardHeight - 1) {
                    cell.type = MazeCell.CellType.BOARD_CORNER
                } else if (x == 0 || x == boardWidth - 1 || y == 0 || y == boardHeight - 1) {
                    cell.type = MazeCell.CellType.BOARD_WALL
                }

                board[x].add(cell)
            }
        }

        board
    }

    fun toImage(): Image {
        val writableImg = WritableImage(boardWidth * MazeCell.width, boardHeight * MazeCell.height)

        for (x in 0..(boardWidth - 1)) {
            for (y in 0..(boardHeight - 1)) {
                val cellImage = board[x][y].toImage()

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