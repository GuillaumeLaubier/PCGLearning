package model

class ArrayListMazeCell(X: Int, Y: Int, private val parentGrid: ArrayListMazeGrid) : MazeCell(X, Y) {

    override fun getNeighbours(): List<MazeCell> {
        val neighbours = ArrayList<MazeCell>()

        getTopNeighbour()?.let {
            neighbours.add(it)
        }

        getBottomNeighbour()?.let {
            neighbours.add(it)
        }

        getLeftNeighbour()?.let {
            neighbours.add(it)
        }

        getRightNeighbour()?.let {
            neighbours.add(it)
        }

        return neighbours
    }

    private fun getTopNeighbour(): MazeCell? = parentGrid[positionX, positionY - 1]

    private fun getBottomNeighbour(): MazeCell? = parentGrid[positionX, positionY + 1]

    private fun getLeftNeighbour(): MazeCell? = parentGrid[positionX - 1, positionY]

    private fun getRightNeighbour(): MazeCell? = parentGrid[positionX + 1, positionY]

}