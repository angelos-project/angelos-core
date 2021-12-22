import angelos.io.net.StreamServerSocket
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    println("Server: Hello World!")
    println("Program arguments: ${args.joinToString()}")

    val socket = StreamServerSocket("localhost", 8080)
    socket.open()
}