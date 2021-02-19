import javafx.scene.image.Image
import model.ArrayListMazeGrid
import model.FirstTestMazeCell
import model.FirstTestMazeGrid
import model.MazeCell
import kotlin.random.Random

class MazeGenerator {

    enum class Direction {
        NORTH, SOUTH, EAST, WEST
    }

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

    fun generateFirstTestMaze(width: Int, height: Int): Image {
        val grid = FirstTestMazeGrid(15, 15)

        recursiveDepthFirst(grid[0, 0])

        return grid.getImage()
    }

    private fun recursiveDepthFirst(currentCell: FirstTestMazeCell) {
        currentCell.isVisited = true

        while (currentCell.getUnvisitedNeighbour().size > 0) {
            val randomIndex = try {
                (Random.nextFloat() * currentCell.getUnvisitedNeighbour().size).toInt()
            } catch (_: Exception) {
                0
            }

            val nextCell = currentCell.getUnvisitedNeighbour()[randomIndex]
            currentCell.removeWallWith(nextCell)

            recursiveDepthFirst(nextCell)
        }
    }


    fun generateByRecursiveDivision(width: Int, height: Int) {

        val grid = ArrayListMazeGrid(width, height)

        recursiveDivision(grid, 1, 1, width, height)

        writeImage(grid.toImage())
    }

    private fun recursiveDivision(grid: ArrayListMazeGrid, startX: Int, startY: Int, width: Int, height: Int) {

        if (height == 0 || width == 0) {
            return
        }

        val endX = startX + width - 1
        val endY = startY + height - 1

        // Define orientation
        val orientation = if (width < height) {
            Orientation.HORIZONTAL
        } else if (width > height) {
            Orientation.VERTICAL

            // If width == height, orientation is random
        } else if (Random.nextFloat() > 0.5) {
            Orientation.VERTICAL
        } else {
            Orientation.HORIZONTAL
        }

        if (orientation == Orientation.HORIZONTAL) {

            // Not enough space
            if (height < 3) {
                return
            }

            writeImage(grid.toImage())

            // Define random Y coordinate for the wall. Make sure it is even.
            val wallY = (startY..endY).toList().filter { it % 2 == 0 }.random()

            // Define random X coordinate for the hole. Make sure it is odd.
            val holeX = (startX..endX).toList().filter { it % 2 == 1 }.random()

            // Define horizontal wall
            grid.board.filter { it.positionY == wallY && it.positionX in startX..endX }.forEach {
                it.type = MazeCell.CellType.WALL
            }

            // Dig a hole in this wall
            grid[holeX, wallY]?.type = MazeCell.CellType.CORRIDOR

            // Repeat for the 2 new subdivisions
            // Top subdivision
            recursiveDivision(grid, startX, startY, width, wallY - startY)

            // Bottom subdivision
            recursiveDivision(grid, startX, wallY + 1, width, endY - wallY)

        } else {

            // Not enough space
            if (width < 3) {
                return
            }

            writeImage(grid.toImage())

            // Define random X coordinate for the wall. Make sure it is even.
            val wallX = (startX..endX).toList().filter { it % 2 == 0 }.random()

            // Define random Y coordinate for the hole. Make sure it is odd.
            val holeY = (startY..endY).toList().filter { it % 2 == 1 }.random()

            // Define vertical wall
            grid.board.filter { it.positionX == wallX && it.positionY in startY..endY }.forEach {
                it.type = MazeCell.CellType.WALL
            }

            // Dig a hole in this wall
            grid[wallX, holeY]?.type = MazeCell.CellType.CORRIDOR

            // Repeat for the 2 new subdivisions
            // Left subdivision
            recursiveDivision(grid, startX, startY, wallX - startX, height)

            // Right subdivision
            recursiveDivision(grid, wallX + 1, startY, endX - wallX, height)
        }
    }

}