package model

class ArrayListMazeTile(X: Int, Y: Int, private val parentGrid: ArrayListMazeGrid) : MazeTile(X, Y) {

    override fun getNeighbours(): List<MazeTile> {
        val neighbours = ArrayList<MazeTile>()

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

    private fun getTopNeighbour(): MazeTile? = parentGrid[positionX, positionY - 1]

    private fun getBottomNeighbour(): MazeTile? = parentGrid[positionX, positionY + 1]

    private fun getLeftNeighbour(): MazeTile? = parentGrid[positionX - 1, positionY]

    private fun getRightNeighbour(): MazeTile? = parentGrid[positionX + 1, positionY]

}