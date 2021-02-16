package generator

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

                topographicArray[x, y] = if (leftOfCurrent >= 0) {
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

                topographicArray[x, y] = if (leftOfCurrent >= 0 && aboveCurrent >= 0) {
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
            }
        }

        return topographicArray
    }

}