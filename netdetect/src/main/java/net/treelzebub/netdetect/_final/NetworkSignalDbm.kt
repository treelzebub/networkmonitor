package net.treelzebub.netdetect._final

sealed class NetworkSignalDbm {

    companion object {

        val Edge = listOf( //TODO
            None(Int.MIN_VALUE..-110),
            Poor(-109..-101),
            Fair(-100..-86),
            Good(-85..-71),
            Great(-70..Int.MAX_VALUE)
        )

        val ThreeG = listOf(
            None(Int.MIN_VALUE..-110),
            Poor(-109..-101),
            Fair(-100..-86),
            Good(-85..-71),
            Great(-70..Int.MAX_VALUE)
        )

        val Lte = listOf(
            None(Int.MIN_VALUE..-120),
            Poor(-119..-111),
            Fair(-110..-106),
            Good(-105..-91),
            Great(-90..Int.MAX_VALUE)
        )

        val Wifi = listOf(
            None(Int.MIN_VALUE..-70),
            Poor(-119..-71),
            Fair(-70..-69),
            Good(-60..-51),
            Great(-50..Int.MAX_VALUE)
        )

        fun getRating(networkTypeOrdinal: Int, signalDbm: Int)
                = getRating(
            NetworkType.values()[networkTypeOrdinal],
            signalDbm
        )

        fun getRating(networkType: NetworkType, signalDbm: Int): NetworkSignalDbm {
            val ratings = when (networkType) {
                NetworkType.Wifi -> Wifi
                NetworkType.Edge -> Edge
                NetworkType.ThreeG -> ThreeG
                NetworkType.Lte -> Lte
            }
            return ratings.find { signalDbm in it.range }!!
        }
    }

    abstract val range: IntRange

    class None(override val range: IntRange) : NetworkSignalDbm() {
        override fun toString() = "None"
    }

    class Poor(override val range: IntRange) : NetworkSignalDbm() {
        override fun toString() = "Poor"
    }

    class Fair(override val range: IntRange) : NetworkSignalDbm() {
        override fun toString() = "Fair"
    }

    class Good(override val range: IntRange) : NetworkSignalDbm() {
        override fun toString() = "Good"
    }

    class Great(override val range: IntRange) : NetworkSignalDbm() {
        override fun toString() = "Great"
    }
}