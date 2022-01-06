import angelos.io.net.StreamClientSocket
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    println("Client: Hello World!")
    println("Program arguments: ${args.joinToString()}")

    val socket = StreamClientSocket("localhost", 8080)
    socket.connect()
}