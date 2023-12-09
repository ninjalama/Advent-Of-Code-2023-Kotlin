package puzzles.day01

data class CalibrationDocument(val document: String) {
    fun getCalibrationLines(): List<String> {
        return document.split("\n")
    }
}
