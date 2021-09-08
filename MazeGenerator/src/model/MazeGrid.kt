package model

import java.awt.image.BufferedImage

class MazeGrid(val mazeWidth: Int, val mazeHeight: Int, initialType: MazeTile.TileType = MazeTile.TileType.UNDEFINED) {

    // 2 extras cells for board walls
    val boardWidth = mazeWidth + 2
    val boardHeight = mazeHeight + 2

    val board by lazy {
        val board = ArrayList<MazeTile>()

        for (x in 0..(boardWidth - 1)) {

            for (y in 0..(boardHeight - 1)) {
                val cell = MazeTile(x, y, this)

                // Immediately define board walls and corners
                if (x == 0 && y == 0
                    || x == 0 && y == boardHeight - 1
                    || x == boardWidth - 1 && y == 0
                    || x == boardWidth - 1 && y == boardHeight - 1
                ) {
                    cell.type = MazeTile.TileType.BOARD_CORNER
                } else if (x == 0 || x == boardWidth - 1 || y == 0 || y == boardHeight - 1) {
                    cell.type = MazeTile.TileType.BOARD_WALL
                } else {
                    cell.type = initialType
                }

                board.add(cell)
            }
        }

        board
    }

    val cellBoard by lazy {
        val cellBoard = ArrayList<MazeCell>()

        for (x in 1..(boardWidth - 1) step 2) {
            for (y in 1..(boardHeight - 1) step 2) {
                this[x, y]?.let {
                    if (it.type != MazeTile.TileType.BOARD_CORNER && it.type != MazeTile.TileType.BOARD_WALL) {
                        cellBoard.add(MazeCell(it, this))
                    }
                }
            }
        }

        cellBoard
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
            it.type == MazeTile.TileType.BOARD_WALL
                    && it.getAdjacentTiles().any { neighbour ->
                neighbour.type == MazeTile.TileType.CORRIDOR
            }
        }.random().type = MazeTile.TileType.START

        board.filter {
            it.type == MazeTile.TileType.BOARD_WALL
                    && it.getAdjacentTiles().any { neighbour ->
                neighbour.type == MazeTile.TileType.CORRIDOR
            }
        }.random().type = MazeTile.TileType.FINISH
    }

    private fun defineClassicStartAndFinish() {
        this[1, 0]?.type = MazeTile.TileType.START
        this[mazeWidth, boardHeight - 1]?.type = MazeTile.TileType.FINISH
    }

    fun toImage(): BufferedImage {
        val bufferedImg = BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_RGB)

        for (x in 0..(boardWidth - 1)) {
            for (y in 0..(boardHeight - 1)) {
                val tileImage = this[x, y]!!.toImage()

                bufferedImg.setRGB(x, y, tileImage.getRGB(0, 0))
            }
        }

        // Now scale up the image

        val finalWidth = boardWidth * MazeTile.width
        val finalHeight = boardHeight * MazeTile.height

        val finalImg = BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_INT_RGB)
        val graphics = finalImg.createGraphics()

        graphics.drawImage(bufferedImg, 0, 0, finalWidth, finalHeight, null)
        graphics.dispose()

        return finalImg
    }

    fun reset() {
        board.filter { it.type != MazeTile.TileType.WALL
                && it.type != MazeTile.TileType.BOARD_WALL
                && it.type != MazeTile.TileType.BOARD_CORNER
                && it.type != MazeTile.TileType.START
                && it.type != MazeTile.TileType.FINISH
        }.forEach { it.type = MazeTile.TileType.CORRIDOR }
    }

}