package generator

import kotlin.random.Random

class TopographicArrayGenerator(val nbRows: Int = 500, val nbColumns: Int = 500) {

    fun generateTotallyRandom(): TopographicArray {
        return TopographicArray(Array(nbRows) { FloatArray(nbColumns) { Random.nextFloat() } })
    }

    fun generate(): TopographicArray {
        // First set of rules
        // The first pixel is randomly set.
        val topographicArray = TopographicArray(Array(nbRows) { FloatArray(nbColumns) { -1.0f } })

        // The first pixel is randomly set
        // Then the next one is .05 higher or .05 less higher. Randomly.
        for (x in 0..(nbRows - 1)) {
            for (y in 0..(nbColumns - 1)) {

                val leftOfCurrent = topographicArray.leftOf(x, y)
                val aboveCurrent = topographicArray.above(x, y)

                if (leftOfCurrent < 0 && aboveCurrent < 0) {
                    topographicArray[x, y] = Random.nextFloat()
                } else if (leftOfCurrent >= 0) {
                    topographicArray[x, y] = if (Random.nextBoolean()) {
                        leftOfCurrent + 0.05f
                    } else {
                        leftOfCurrent - 0.05f
                    }
                } else {
                    topographicArray[x, y] = if (Random.nextBoolean()) {
                        aboveCurrent + 0.05f
                    } else {
                        aboveCurrent - 0.05f
                    }
                }
            }
        }

        return topographicArray
    }

}