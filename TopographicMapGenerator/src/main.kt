import generator.TopographicArray
import generator.TopographicArrayGenerator
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

fun main() {
    val generator = TopographicArrayGenerator(1000, 1000)

    writeTopographicImage(generator.generateB())
}

fun writeTopographicImage(topographicArray: TopographicArray) {
    writeImage(topographicArray.toImage(), topographicArray.algoName)
}

fun writeImage(image: Image, algoName: String) {
    val byteOutput = ByteArrayOutputStream()

    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val file = File("results/$algoName/$timestamp.png")

    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput)

    file.writeBytes(byteOutput.toByteArray())
}