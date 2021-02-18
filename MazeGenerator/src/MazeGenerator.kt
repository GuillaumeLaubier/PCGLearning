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
        if (width < 3 || height < 3) {
            return
        }

        writeImage(grid.toImage())

        val randomCell = grid.board.filter {
            it.type == MazeCell.CellType.UNDEFINED
                    && it.positionX in (startX + 1)..(startX + width - 2)
                    && it.positionY in (startY + 1)..(startY + height - 2)
        }.random()

        // Create horizontal wall
        grid.board.filter {
            it.positionY == randomCell.positionY
                    && it.positionX in startX..(startX + width - 1)
        }.forEach {
            it.type = MazeCell.CellType.WALL
        }

        // Create vertical wall
        grid.board.filter {
            it.positionX == randomCell.positionX
                    && it.positionY in startY..(startY + height - 1)
        }.forEach {
            it.type = MazeCell.CellType.WALL
        }

        // 3 of the 4 wall parts must have holes.
        val directionForHoles = arrayListOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
        directionForHoles.remove(directionForHoles.random())

        directionForHoles.forEach { direction ->
            when (direction) {
                Direction.NORTH -> grid.board.filter {
                    it.positionX == randomCell.positionX
                            && it.positionY in startY..(randomCell.positionY - 1)
                }.random().type = MazeCell.CellType.CORRIDOR

                Direction.SOUTH -> grid.board.filter {
                    it.positionX == randomCell.positionX
                            && it.positionY in (randomCell.positionY + 1)..(startY + height - 1)
                }.random().type = MazeCell.CellType.CORRIDOR

                Direction.EAST -> grid.board.filter {
                    it.positionY == randomCell.positionY
                            && it.positionX in (randomCell.positionX + 1)..(startX + width - 1)
                }.random().type = MazeCell.CellType.CORRIDOR

                Direction.WEST -> grid.board.filter {
                    it.positionY == randomCell.positionY
                            && it.positionX in startX..(randomCell.positionX - 1)
                }.random().type = MazeCell.CellType.CORRIDOR
            }
        }

        // Repeat for each subdivision
        // North West subdivision
        recursiveDivision(grid, startX, startY, randomCell.positionX - startX, randomCell.positionY - startY)

        // North East subdivision
        recursiveDivision(
            grid,
            randomCell.positionX + 1,
            startY,
            startX + width - 1 - randomCell.positionX,
            randomCell.positionY - startY
        )

        // South West Subdivision
        recursiveDivision(
            grid,
            startX,
            randomCell.positionY + 1,
            randomCell.positionX - startX,
            startY + height - 1 - randomCell.positionY
        )

        // South East Subdivision
        recursiveDivision(
            grid,
            randomCell.positionX + 1,
            randomCell.positionY + 1,
            startX + width - 1 - randomCell.positionX,
            startY + height - 1 - randomCell.positionY
        )
    }

}