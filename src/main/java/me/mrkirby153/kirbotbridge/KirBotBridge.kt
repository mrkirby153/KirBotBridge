package me.mrkirby153.kirbotbridge

import com.google.inject.Inject
import me.mrkirby153.kirbotbridge.config.Configuration
import me.mrkirby153.kirbotbridge.listener.MainListener
import me.mrkirby153.kirbotbridge.net.NetworkConnection
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.event.game.state.GameStoppedServerEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.plugin.Plugin
import java.nio.file.Path

@Plugin(id = "kirbotbridge", name = "KirBotBridge")
class KirBotBridge {

    @Inject val logger: Logger? = null

    @Inject
    @DefaultConfig(sharedRoot = true)
    private val defaultConfig: Path? = null

    @Inject
    @DefaultConfig(sharedRoot = true)
    lateinit var confgManager: ConfigurationLoader<CommentedConfigurationNode>


    lateinit var networkConnection: NetworkConnection


    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        instance = this
        logger!!.info("Loading Configuration")
        Configuration.init()
        Sponge.getEventManager().registerListeners(this, MainListener(this))
        networkConnection = NetworkConnection(this, Configuration.config.getNode("bridge", "host").string, Configuration.config.getNode("bridge", "port").int,
                Configuration.config.getNode("bridge", "password").string)
    }

    @Listener
    fun onServerStop(event: GameStoppedServerEvent) {
        networkConnection.shutdown()
    }


    @Listener
    fun onPlayerJoin(event: ClientConnectionEvent.Join, @Getter("getTargetEntity") player: Player) {

    }

    companion object {
        lateinit var instance: KirBotBridge
    }
}
