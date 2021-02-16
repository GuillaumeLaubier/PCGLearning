import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

fun main() {
    val writableImage = WritableImage(500, 500)

    for (x in 0..499) {
        for (y in 0..499) {
            writableImage.pixelWriter.setColor(x, y, Color.WHITE)
        }
    }

    for (x in 100..299) {
        for (y in 50..399) {
            writableImage.pixelWriter.setColor(x, y, Color.RED)
        }
    }

    val byteOutput = ByteArrayOutputStream()

    val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val file = File("results/$timestamp.png")

    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", byteOutput)

    file.writeBytes(byteOutput.toByteArray())
}