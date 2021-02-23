package pathfinder

import model.MazeGrid
import model.MazeTile
import java.util.ArrayList

abstract class MazePathFinder {

    abstract fun resolveMaze(startTile: MazeTile, endTile: MazeTile? = null)

}