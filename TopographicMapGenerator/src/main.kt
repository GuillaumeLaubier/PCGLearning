import generator.TopographicArrayGenerator
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

fun main() {
    writeImage(TopographicArrayGenerator().generate().toImage())
}

fun writeImage(image: Image) {
    val byteOutput = ByteArrayOutputStream()

    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val file = File("results/$timestamp.png")

    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput)

    file.writeBytes(byteOutput.toByteArray())
}