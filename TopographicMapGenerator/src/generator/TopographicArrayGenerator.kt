package generator

import kotlin.random.Random

class TopographicArrayGenerator(val nbRows: Int = 500, val nbColumns: Int = 500) {

    fun generate(): TopographicArray {
        return TopographicArray(Array(nbRows) { FloatArray(nbColumns) { Random.nextFloat() } })
    }

}