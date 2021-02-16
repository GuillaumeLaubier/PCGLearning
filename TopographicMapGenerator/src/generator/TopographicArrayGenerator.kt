package generator

class TopographicArrayGenerator(val nbRows: Int = 500, val nbColumns: Int = 500) {

    fun generate(): TopographicArray {
        return TopographicArray(Array(nbRows) { FloatArray(nbColumns) { 0.0f } })
    }

}