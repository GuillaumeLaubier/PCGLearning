package generator

import java.util.*
import kotlin.random.Random

class TopographicArrayGenerator(val nbRows: Int = 500, val nbColumns: Int = 500) {

    fun generateTotallyRandom(): TopographicArray {
        return TopographicArray("random", Array(nbRows) { FloatArray(nbColumns) { Random.nextFloat() } })
}

    /**
     * The current pixel will be:
     * - .05 higher or lower of the left pixel.
     * - .05 higher or lower of the pixel above, if left pixel is not set
     * - Fully random if left and above pixels are not set.
     */
    fun generateA(): TopographicArray {
        // First set of rules
        // The first pixel is randomly set.
        val topographicArray = TopographicArray("algo_a", Array(nbRows) { FloatArray(nbColumns) { -1.0f } })

        // The first pixel is randomly set
        // Then the next one is .05 higher or .05 less higher. Randomly.
        for (x in 0..(nbRows - 1)) {
            for (y in 0..(nbColumns - 1)) {

                val leftOfCurrent = topographicArray.leftOf(x, y)
                val aboveCurrent = topographicArray.above(x, y)

                val value = if (leftOfCurrent >= 0) {
                    if (Random.nextBoolean()) {
                        leftOfCurrent + 0.05f
                    } else {
                        leftOfCurrent - 0.05f
                    }
                } else if (aboveCurrent >= 0) {
                    if (Random.nextBoolean()) {
                        aboveCurrent + 0.05f
                    } else {
                        aboveCurrent - 0.05f
                    }
                } else {
                    Random.nextFloat()
                }

                topographicArray.safeSet(x, y, value)
            }
        }

        return topographicArray
    }

    /**
     * The current pixel will be:
     * - .05 higher or lower of the median value of left AND above pixels.
     * - .05 higher or lower of the left pixel, if above pixel is not set.
     * - .05 higher or lower of the pixel above, if left pixel is not set.
     * - Fully random if left and above pixels are not set.
     */
    fun generateB(): TopographicArray {
        // First set of rules
        // The first pixel is randomly set.
        val topographicArray = TopographicArray("algo_b", Array(nbRows) { FloatArray(nbColumns) { -1.0f } })

        // The first pixel is randomly set
        // Then the next one is .05 higher or .05 less higher. Randomly.
        for (x in 0..(nbRows - 1)) {
            for (y in 0..(nbColumns - 1)) {

                val leftOfCurrent = topographicArray.leftOf(x, y)
                val aboveCurrent = topographicArray.above(x, y)

                val value = if (leftOfCurrent >= 0 && aboveCurrent >= 0) {
                    if (Random.nextBoolean()) {
                        ((leftOfCurrent + aboveCurrent) / 2.0f) + 0.05f
                    } else {
                        ((leftOfCurrent + aboveCurrent) / 2.0f) - 0.05f
                    }
                } else if (leftOfCurrent >= 0) {
                    if (Random.nextBoolean()) {
                        leftOfCurrent + 0.05f
                    } else {
                        leftOfCurrent - 0.05f
                    }
                } else if (aboveCurrent >= 0) {
                    if (Random.nextBoolean()) {
                        aboveCurrent + 0.05f
                    } else {
                        aboveCurrent - 0.05f
                    }
                } else {
                    Random.nextFloat()
                }

                topographicArray.safeSet(x, y, value)
            }
        }

        return topographicArray
    }

    fun generatePerlinNoise(): TopographicArray {
        // Generate array with random number between -1 & 1. Representing whole array of 2 dimensionals vectors.
        val topographicArray = TopographicArray("perlin_noise", Array(nbRows) { FloatArray(nbColumns)})

        for (x in 0..(nbColumns - 1)) {
            for (y in 0..(nbRows - 1)) {
                topographicArray[x, y] = perlin(x, y).toFloat()
            }
        }

        return topographicArray
    }

    private fun interpolate(a0: Double, a1: Double, weight: Double): Double {
        return (a1 - a0) * weight + a0
    }

    // Create random gradient
    private fun randomGradient(): Pair<Double, Double> {
        return Pair(Math.cos(Random.nextDouble()), Math.sin(Random.nextDouble()))

    }

    // Computes the dot product of the distance and gradient vectors.
    private fun dotGridGradient(ix: Int, iy: Int, x: Int, y: Int): Double {
        val gradient = randomGradient()

        val dx = x - ix
        val dy = y - iy

        return dx * gradient.first + dy * gradient.second
    }

    private fun perlin(x: Int, y: Int): Double {
        val x0 = x
        val x1 = x + 1
        val y0 = y
        val y1 = y + 1

        // determine interpolation weights
        val weightX = (x - x0).toDouble()
        val weightY = (y - y0).toDouble()

        var n0 = dotGridGradient(x0, y0, x, y)
        var n1 = dotGridGradient(x1, y0, x, y)
        val ix0 = interpolate(n0, n1, weightX)

        n0 = dotGridGradient(x0, y1, x, y)
        n1 = dotGridGradient(x1, y1, x, y)
        val ix1 = interpolate(n0, n1, weightX)

        val value = interpolate(ix0, ix1, weightY)

        println("perlin: $value")
        return value
    }

}