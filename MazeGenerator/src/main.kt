import javafx.embed.swing.SwingFXUtils
import model.MazeTile
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

    val grid = MazeGenerator().generateWilsonMaze(51, 51)
    grid.defineStartAndFinish(false)

    writeImage(grid.toImage())

    BreadthFirstPathFinder().resolveMaze(grid.board.first { it.type == MazeTile.TileType.START })

    writeImage(grid.toImage())
}



fun writeImage(image: BufferedImage) {
    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val file = File("results/tests/$timestamp.png")

    ImageIO.write(image, "png", file)
}