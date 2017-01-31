package me.mrkirby153.kirbotbridge.listener

import com.google.gson.Gson
import me.mrkirby153.kirbotbridge.KirBotBridge
import me.mrkirby153.kirbotbridge.config.Configuration
import me.mrkirby153.kirbotbridge.net.BridgeMessage
import me.mrkirby153.kirbotbridge.net.NetworkMessage
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.message.MessageChannelEvent
import java.util.*

class MainListener(val instance: KirBotBridge) {
    val gson = Gson()

    @Listener
    fun onChat(event: MessageChannelEvent.Chat, @First player: Player) {
        println("Chat event by ${player.name}")
        if (!event.channel.isPresent)
            return
        val rawMessage = event.rawMessage.toPlain()
        val bridgeMessage = BridgeMessage(Configuration.config.getNode("bridge", "channel").string, player.name, rawMessage)
        val m = instance.networkConnection.write(NetworkMessage(Random().nextInt().toString(), Configuration.config.getNode("bridge", "guild").string, null, "minecraftbridge", gson.toJson(bridgeMessage)))
    }
}