import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import model.ArrayListMazeGrid
import model.FirstTestMazeCell
import model.FirstTestMazeGrid
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import kotlin.random.Random

fun main() {
    val grid = MazeGenerator().generatePrimsMaze(50, 50)
    writeImage(grid.toImage())
}



fun writeImage(image: Image) {
    val byteOutput = ByteArrayOutputStream()

    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val file = File("results/tests/$timestamp.png")

    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput)

    file.writeBytes(byteOutput.toByteArray())
}