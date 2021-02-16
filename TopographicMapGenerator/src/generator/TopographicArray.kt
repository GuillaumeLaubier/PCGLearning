package generator

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class TopographicArray(val array: Array<FloatArray>) {

    val nbRows = array.size
    val nbColumns = array[0].size

    fun toImage(): Image {
        val writableImage = WritableImage(nbRows, nbColumns)

        for (x in 0..(nbRows - 1)) {
            for (y in 0..(nbColumns - 1)) {

                writableImage.pixelWriter.setColor(
                    x,
                    y,
                    Color.rgb((255 * array[x][y]).toInt(),
                        (255 * array[x][y]).toInt(),
                        (255 * array[x][y]).toInt()
                    )
                )

            }
        }

        return writableImage
    }
}