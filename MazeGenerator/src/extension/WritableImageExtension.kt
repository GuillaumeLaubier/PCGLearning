package extension

import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

fun WritableImage.fillUpWithColor(color: Color) {
    for (x in 0..(this.width - 1).toInt()) {
        for (y in 0..(this.height - 1).toInt()) {
            this.pixelWriter.setColor(x, y, color)
        }
    }
}