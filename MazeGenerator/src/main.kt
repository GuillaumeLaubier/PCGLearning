import javafx.embed.swing.SwingFXUtils
import model.MazeTile
import pathfinder.AStarPathFinder
import pathfinder.BreadthFirstPathFinder
import pathfinder.RightFirstPathFinder
import java.awt.image.BufferedImage
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

fun main() {

    val grid = MazeGenerator().generateByRecursiveDivisionMaze(151, 151)
    grid.defineStartAndFinish(true)

    AStarPathFinder().resolveMaze(grid.board.first { it.type == MazeTile.TileType.START },
        grid.board.first { it.type == MazeTile.TileType.FINISH })
    writeImage(grid.toImage(), "astar")

    grid.reset()

    RightFirstPathFinder().resolveMaze(grid.board.first { it.type == MazeTile.TileType.START }, null)
    writeImage(grid.toImage(), "rightfirst")

    grid.reset()

    BreadthFirstPathFinder().resolveMaze(grid.board.first { it.type == MazeTile.TileType.START }, null)
    writeImage(grid.toImage(), "jikstra")
}




fun writeImage(image: BufferedImage, name: String = "") {
    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val actualName = if (name.isNotEmpty()) {
        "$name/"
    } else {
        name
    }
    val file = File("results/tests/$name/$timestamp.png")

    ImageIO.write(image, "png", file)
}