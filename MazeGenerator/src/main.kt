import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import model.MazeCell
import model.MazeGrid
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import kotlin.random.Random

fun main() {
    val grid = MazeGrid(20, 10)

    recursiveDepthFirst(grid[0, 0])

    writeImage(grid.getImage())
}

fun recursiveDepthFirst(currentCell: MazeCell) {
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

fun writeImage(image: Image) {
    val byteOutput = ByteArrayOutputStream()

    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val file = File("results/tests/$timestamp.png")

    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput)

    file.writeBytes(byteOutput.toByteArray())
}