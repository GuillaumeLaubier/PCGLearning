package generator

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class TopographicArray(val algoName: String, val array: Array<FloatArray>) {

    val nbColumns = array.size
    val nbRows = array[0].size

    operator fun get(x: Int, y: Int): Float {
        if (x !in 0..(nbColumns - 1) || y !in 0..(nbRows - 1)) {
            return -1.0f
        }
        return array[x][y]
    }

    operator fun set(x: Int, y: Int, value: Float) {
        if (x in 0..(nbColumns - 1) && y in 0..(nbRows - 1)) {
            array[x][y] = value
        }
    }

    fun safeSet(x: Int, y: Int, value: Float) {
        val finalValue: Float = when {
            value < 0 -> 0.0f
            value >= 1 -> 1.0f
            else -> value
        }

        if (x in 0..(nbColumns - 1) && y in 0..(nbRows - 1)) {
            array[x][y] = finalValue
        }
    }

    fun above(x: Int, y: Int): Float {
        return get(x, y - 1)
    }

    fun below(x: Int, y: Int): Float {
        return get(x, y + 1)
    }

    fun leftOf(x: Int, y: Int): Float {
        return get(x - 1, y)
    }

    fun rightOf(x: Int, y: Int): Float {
        return get(x + 1, y)
    }

    fun topLeftCornerOf(x: Int, y: Int): Float {
        return get(x - 1, y - 1)
    }

    fun topRightCornerOf(x: Int, y: Int): Float {
        return get(x + 1, y - 1)
    }

    fun bottomLeftCornerOf(x: Int, y: Int): Float {
        return get(x - 1, y + 1)
    }

    fun bottomRightCornerOf(x: Int, y: Int): Float {
        return get(x + 1, y + 1)
    }

    fun toImage(): Image {
        val writableImage = WritableImage(nbColumns, nbRows)

        for (x in 0..(nbColumns - 1)) {
            for (y in 0..(nbRows - 1)) {

                val colorValue = if (array[x][y] >= 0) {
                    (255.0 * (1.0f - array[x][y]))
                } else {
                    .0
                }

                val color = Color.hsb(colorValue, .8, 1.0)

                writableImage.pixelWriter.setColor(x, y, color)
            }
        }

        return writableImage
    }
}