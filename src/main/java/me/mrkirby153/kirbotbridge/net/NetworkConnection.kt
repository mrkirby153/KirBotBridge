package me.mrkirby153.kirbotbridge.net

import com.google.gson.Gson
import me.mrkirby153.kirbotbridge.KirBotBridge
import me.mrkirby153.kirbotbridge.config.Configuration
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.channel.MessageChannel
import org.spongepowered.api.text.format.TextColors
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CompletableFuture

class NetworkConnection(private val kirBotBridge: KirBotBridge, private val host: String, private val port: Int,
                        private val password: String) : Thread() {

    private val gson: Gson = Gson()

    private var running = true

    private lateinit var socket: Socket
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream

    private val sentMessages = mutableMapOf<String, CompletableFuture<NetworkMessage>>()


    init {
        name = "Network Connection"
        isDaemon = true
        start()
    }

    override fun run() {
        socket = Socket(host, port)
        inputStream = socket.inputStream
        outputStream = socket.outputStream
        kirBotBridge.logger?.info("Connected to robot!")

        write(NetworkMessage("-1", Configuration.config.getNode("bridge", "guild").string, null, "setServer", ""))

        while (running) {
            try {
                val networkMessage = read() ?: continue
                if (sentMessages[networkMessage.id] != null) {
                    val future = sentMessages[networkMessage.id] ?: continue
                    future.complete(networkMessage)
                    sentMessages.remove(networkMessage.id)
                    continue
                }
                // verify password
                if(networkMessage.password != password){
                    continue
                }
                if(networkMessage.messageType == "bridgeMessage"){
                    val data = gson.fromJson(networkMessage.data, BridgeMessage::class.java)
                    val text = Text.builder("[DISCORD] ").color(TextColors.LIGHT_PURPLE).append(Text.builder("<${data.user}> ${data.message}").build()).build()
                    MessageChannel.TO_ALL.send(text)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        socket.close()
        kirBotBridge.logger?.info("Output closed!")
    }

    fun shutdown() {
        socket.close()
        running = false
    }

    fun write(obj: NetworkMessage): CompletableFuture<NetworkMessage> {
        obj.password = this.password

        val message = gson.toJson(obj)
        val bytes = message.toByteArray()
        val size = ByteBuffer.allocate(4)
        size.order(ByteOrder.LITTLE_ENDIAN)
        size.putInt(bytes.size)

        val future = CompletableFuture<NetworkMessage>()
        sentMessages[obj.id] = future

        outputStream.write(size.array())
        outputStream.write(bytes)
        outputStream.flush()
        return future
    }

    fun read(): NetworkMessage? {
        val rawSize = ByteArray(4)
        if (inputStream.read(rawSize) == -1) {
            println("Reached end of stream")
            return null
        }
        val msgLenBuff = ByteBuffer.wrap(rawSize)
        msgLenBuff.order(ByteOrder.LITTLE_ENDIAN)
        msgLenBuff.rewind()

        val size = msgLenBuff.int

        val rawMessage = ByteArray(size)
        inputStream.read(rawMessage)

        val json = String(rawMessage)

        val networkMessage = gson.fromJson(json, NetworkMessage::class.java)
        return networkMessage
    }
}


data class NetworkMessage(var id: String, val guild: String, var password: String?, val messageType: String, val data: String)
data class BridgeMessage(val channel: String, val user: String, val message: String)