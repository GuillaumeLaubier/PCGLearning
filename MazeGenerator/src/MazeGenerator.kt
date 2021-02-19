import javafx.scene.image.Image
import model.*
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

        firstTryRecursiveDepthFirst(grid[0, 0])

        return grid.getImage()
    }

    private fun firstTryRecursiveDepthFirst(currentCell: FirstTestMazeCell) {
        currentCell.isVisited = true

        while (currentCell.getUnvisitedNeighbour().size > 0) {
            val randomIndex = try {
                (Random.nextFloat() * currentCell.getUnvisitedNeighbour().size).toInt()
            } catch (_: Exception) {
                0
            }

            val nextCell = currentCell.getUnvisitedNeighbour()[randomIndex]
            currentCell.removeWallWith(nextCell)

            firstTryRecursiveDepthFirst(nextCell)
        }
    }

    fun generateDepthFirstMaze(width: Int, height: Int): MazeGrid {
        val grid = MazeGrid(width, height, MazeTile.TileType.WALL)

        recursiveDepthFirst(grid.cellBoard.first())

        return grid
    }

    private fun recursiveDepthFirst(cell: MazeCell) {
        cell.centerTile.type = MazeTile.TileType.CORRIDOR

        //writeImage(cell.grid.toImage())

        while (cell.getAdjacentCells().any { it.centerTile.type == MazeTile.TileType.WALL }) {
            val unvisitedCell = cell.getAdjacentCells().filter { it.centerTile.type == MazeTile.TileType.WALL }
            val randomIndex = (Random.nextFloat() * unvisitedCell.size).toInt()
            val nextCell = unvisitedCell[randomIndex]

            cell.removeWallWithCell(nextCell)
            recursiveDepthFirst(nextCell)
        }
    }

    fun generateByRecursiveDivisionMaze(width: Int, height: Int): MazeGrid {
        val grid = MazeGrid(width, height)

        recursiveDivision(grid, 1, 1, width, height)

        return grid
    }

    private fun recursiveDivision(grid: MazeGrid, startX: Int, startY: Int, width: Int, height: Int) {

        if (height == 0 || width == 0) {
            return
        }

        var firstSubdivision = {}
        var secondSubdivision = {}

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

            // Define random Y coordinate for the wall. Make sure it is even.
            val wallY = (startY..endY).toList().filter { it % 2 == 0 }.random()

            // Define random X coordinate for the hole. Make sure it is odd.
            val holeX = (startX..endX).toList().filter { it % 2 == 1 }.random()

            // Define horizontal wall
            grid.board.filter { it.positionY == wallY && it.positionX in startX..endX }.forEach {
                it.type = MazeTile.TileType.WALL
            }

            // Dig a hole in this wall
            grid[holeX, wallY]?.type = MazeTile.TileType.CORRIDOR

            // Repeat for the 2 new subdivisions
            // Top subdivision
            firstSubdivision = { recursiveDivision(grid, startX, startY, width, wallY - startY) }

            // Bottom subdivision
            secondSubdivision = { recursiveDivision(grid, startX, wallY + 1, width, endY - wallY) }

        } else {

            // Not enough space
            if (width < 3) {
                return
            }

            // Define random X coordinate for the wall. Make sure it is even.
            val wallX = (startX..endX).toList().filter { it % 2 == 0 }.random()

            // Define random Y coordinate for the hole. Make sure it is odd.
            val holeY = (startY..endY).toList().filter { it % 2 == 1 }.random()

            // Define vertical wall
            grid.board.filter { it.positionX == wallX && it.positionY in startY..endY }.forEach {
                it.type = MazeTile.TileType.WALL
            }

            // Dig a hole in this wall
            grid[wallX, holeY]?.type = MazeTile.TileType.CORRIDOR

            // Repeat for the 2 new subdivisions
            // Left subdivision
            firstSubdivision = { recursiveDivision(grid, startX, startY, wallX - startX, height) }

            // Right subdivision
            secondSubdivision = { recursiveDivision(grid, wallX + 1, startY, endX - wallX, height) }
        }

        // Randomly choose between the 2 next subdivision the first to start with
        if (Random.nextBoolean()) {
            firstSubdivision()
            secondSubdivision()
        } else {
            secondSubdivision()
            firstSubdivision()
        }
    }

    fun generatePrimsMaze(width: Int, height: Int): MazeGrid {
        val grid = MazeGrid(width, height, MazeTile.TileType.WALL)

        primsGeneration(grid)

        return grid
    }

    // Might be useful later for pathfinding
    private fun primsGeneration(grid: MazeGrid, wallStack: ArrayList<Pair<MazeTile, MazeCell>> = ArrayList()) {
        if (!grid.board.any { it.type == MazeTile.TileType.CORRIDOR }) {
            // This is the first iteration

            val randomCell = grid.cellBoard.filter { it.centerTile.type == MazeTile.TileType.WALL }.random()
            randomCell.centerTile.type = MazeTile.TileType.CORRIDOR

            randomCell.centerTile.getSpecificAdjacentTiles(MazeTile.TileType.WALL)
                .forEach { wallStack.add(Pair(it, randomCell)) }

            //writeImage(grid.toImage())
        }

        while (!wallStack.isEmpty()) {
            val randomPair = wallStack.random()

            // Possible new corridor
            val tile = randomPair.first

            // Retrieving the calling corridor
            val callingCell = randomPair.second

            val oppositeCell = when (callingCell) {
                tile.topCell -> tile.bottomCell
                tile.bottomCell -> tile.topCell
                tile.leftCell -> tile.rightCell
                tile.rightCell -> tile.leftCell
                else -> null
            }

            oppositeCell?.let { cell ->
                if (cell.centerTile.type != MazeTile.TileType.CORRIDOR) {
                    tile.type = MazeTile.TileType.CORRIDOR
                    cell.centerTile.type = MazeTile.TileType.CORRIDOR
                    cell.centerTile.getSpecificAdjacentTiles(MazeTile.TileType.WALL).forEach {
                        wallStack.add(Pair(it, cell))
                    }

                    //writeImage(grid.toImage())
                }
            }

            wallStack.remove(randomPair)
        }
    }

    // Might be useful later for pathfinding
    private fun brokenPrimsGeneration(
        grid: MazeGrid,
        wallStack: ArrayList<Pair<MazeTile, MazeTile>> = ArrayList()
    ) {
        if (!grid.board.any { it.type == MazeTile.TileType.CORRIDOR }) {
            // This is the first iteration

            val randomTile = grid.board.filter { it.type == MazeTile.TileType.WALL }.random()
            randomTile.type = MazeTile.TileType.CORRIDOR

            randomTile.getSpecificAdjacentTiles(MazeTile.TileType.WALL).forEach { wallStack.add(Pair(it, randomTile)) }
            //writeImage(grid.toImage())
        }

        while (!wallStack.isEmpty()) {
            val randomPair = wallStack.random()

            // Possible new corridor
            val tile = randomPair.first

            // Retrieving the calling corridor
            val callingCorridor = randomPair.second

            // Retrieve opposite of corridor from randomTile POV
            val otherSide = grid[
                    tile.positionX + (tile.positionX - callingCorridor.positionX),
                    tile.positionY + (tile.positionY - callingCorridor.positionY)
            ]

            if (otherSide?.type != MazeTile.TileType.CORRIDOR) {
                tile.type = MazeTile.TileType.CORRIDOR
                tile.getSpecificAdjacentTiles(MazeTile.TileType.WALL).forEach { wallStack.add(Pair(it, tile)) }

                //writeImage(grid.toImage())
            }

            wallStack.remove(randomPair)
        }
    }

    fun generateAldousBroderMaze(width: Int, height: Int): MazeGrid {
        val grid = MazeGrid(width, height, MazeTile.TileType.WALL)

        aldousBroderGeneration(grid)

        return grid
    }

    private fun aldousBroderGeneration(grid: MazeGrid) {

        var cell = grid.cellBoard.random()

        while (grid.cellBoard.any { it.centerTile.type != MazeTile.TileType.CORRIDOR }) {

            cell.centerTile.type = MazeTile.TileType.CORRIDOR

            val nextCell = cell.getAdjacentCells().random()

            if (nextCell.centerTile.type != MazeTile.TileType.CORRIDOR) {
                cell.removeWallWithCell(nextCell)
                //writeImage(grid.toImage())
            }

            cell = nextCell
        }

    }

    fun generateWilsonMaze(width: Int, height: Int): MazeGrid {
        val grid = MazeGrid(width, height, MazeTile.TileType.WALL)

        wilsonFullRandomGeneration(grid)

        return grid
    }

    private fun wilsonFullRandomGeneration(grid: MazeGrid) {
        grid.cellBoard.random().centerTile.type = MazeTile.TileType.CORRIDOR

        while (grid.cellBoard.any { it.centerTile.type != MazeTile.TileType.CORRIDOR }) {

            val candidatesList = ArrayList<MazeCell>()

             var cell = grid.cellBoard.filter { it.centerTile.type != MazeTile.TileType.CORRIDOR }.random()

            cell.centerTile.type = MazeTile.TileType.CANDIDATE
            candidatesList.add(cell)

            while (!candidatesList.isEmpty()) {
                writeImage(grid.toImage())

                // Randomly define next cell
                val nextCell = cell.getAdjacentCells().random()

                when (nextCell.centerTile.type) {

                    // If it is maze cell, we validate the whole candidates list
                    MazeTile.TileType.CORRIDOR -> {
                        cell.removeWallWithCell(nextCell)
                        nextCell.centerTile.type = MazeTile.TileType.CORRIDOR

                        cell = nextCell
                        while (!candidatesList.isEmpty()) {
                            cell.removeWallWithCell(candidatesList[candidatesList.size - 1])
                            cell = candidatesList[candidatesList.size - 1]
                            cell.centerTile.type = MazeTile.TileType.CORRIDOR
                            candidatesList.removeAt(candidatesList.size - 1)
                        }
                    }

                    // If it is candidate cell, we restart the loop from here
                    MazeTile.TileType.CANDIDATE -> {
                        val index = candidatesList.indexOf(nextCell)
                        while (candidatesList.size > index + 1) {
                            candidatesList[index + 1].centerTile.type = MazeTile.TileType.WALL
                            candidatesList.removeAt(index + 1)
                        }

                        cell = nextCell
                    }

                    // If it is nothing particular, we carry on
                    else -> {
                        nextCell.centerTile.type = MazeTile.TileType.CANDIDATE
                        candidatesList.add(nextCell)

                        cell = nextCell
                    }
                }
            }
        }
    }

}