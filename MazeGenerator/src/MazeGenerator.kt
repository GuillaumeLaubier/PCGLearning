import javafx.scene.image.Image
import model.FirstTestMazeCell
import model.FirstTestMazeGrid
import kotlin.random.Random

class MazeGenerator{

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



}