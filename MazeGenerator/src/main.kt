import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import model.MazeTile
import pathfinder.RightFirstPathFinder
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

fun main() {

    val grid = MazeGenerator().generateDepthFirstMaze(51, 51)
    grid.defineStartAndFinish(false)

    writeImage(grid.toImage())

    RightFirstPathFinder().resolveMaze(grid.board.first { it.type == MazeTile.TileType.START })

    writeImage(grid.toImage())
}



fun writeImage(image: Image) {
    val byteOutput = ByteArrayOutputStream()

    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val file = File("results/tests/$timestamp.png")

    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput)

    file.writeBytes(byteOutput.toByteArray())
}