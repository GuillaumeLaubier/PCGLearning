package model

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class ArrayListMazeGrid(val mazeWidth: Int, val mazeHeight: Int) {

    // 2 extras cells for board walls
    val boardWidth = mazeWidth + 2
    val boardHeight = mazeHeight + 2

    val board by lazy {
        val board = ArrayList<MazeTile>()

        for (x in 0..(boardWidth - 1)) {

            for (y in 0..(boardHeight - 1)) {
                val cell = ArrayListMazeTile(x, y, this)

                // Immediately define board walls and corners
                if (x == 0 && y == 0
                    || x == 0 && y == boardHeight - 1
                    || x == boardWidth - 1 && y == 0
                    || x == boardWidth - 1 && y == boardHeight - 1
                ) {
                    cell.type = MazeTile.CellType.BOARD_CORNER
                } else if (x == 0 || x == boardWidth - 1 || y == 0 || y == boardHeight - 1) {
                    cell.type = MazeTile.CellType.BOARD_WALL
                }

                board.add(cell)
            }
        }

        board
    }

    operator fun get(x: Int, y: Int): MazeTile? = board.firstOrNull { it.positionX == x && it.positionY == y }

    fun defineStartAndFinish(isRandom: Boolean = true) {
        if (isRandom) {
            defineRandomStartAndFinish()
        } else {
            defineClassicStartAndFinish()
        }
    }

    private fun defineRandomStartAndFinish() {
        board.filter {
            it.type == MazeTile.CellType.BOARD_WALL
                    && it.getNeighbours().any { neighbour ->
                neighbour.type == MazeTile.CellType.CORRIDOR
            }
        }.random().type = MazeTile.CellType.START

        board.filter {
            it.type == MazeTile.CellType.BOARD_WALL
                    && it.getNeighbours().any { neighbour ->
                neighbour.type == MazeTile.CellType.CORRIDOR
            }
        }.random().type = MazeTile.CellType.FINISH
    }

    private fun defineClassicStartAndFinish() {
        this[1, 0]?.type = MazeTile.CellType.START
        this[mazeWidth, boardHeight - 1]?.type = MazeTile.CellType.FINISH
    }

    fun toImage(): Image {
        val writableImg = WritableImage(boardWidth * MazeTile.width, boardHeight * MazeTile.height)

        for (x in 0..(boardWidth - 1)) {
            for (y in 0..(boardHeight - 1)) {
                val cellImage = this[x, y]!!.toImage()

                for (ix in 0..(MazeTile.width - 1)) {
                    for (iy in 0..(MazeTile.height - 1)) {
                        writableImg.pixelWriter.setColor(
                            x * MazeTile.width + ix,
                            y * MazeTile.height + iy,
                            cellImage.pixelReader.getColor(ix, iy)
                        )
                    }
                }
            }
        }

        return writableImg
    }

}