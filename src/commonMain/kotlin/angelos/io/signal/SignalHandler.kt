package angelos.io.signal

class SignalHandler internal constructor(
    val signals: List<Int>,
    private val queue: kotlinx.coroutines.channels.Channel<Int>
){

}