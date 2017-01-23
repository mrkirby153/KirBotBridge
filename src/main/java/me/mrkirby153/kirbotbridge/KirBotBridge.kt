package me.mrkirby153.kirbotbridge

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.plugin.Plugin

@Plugin(id = "kirbotbridge", name = "KirBotBridge")
class KirBotBridge {

    @Inject
    private val logger: Logger? = null


    @Listener
    fun onServerStart(event: GameStartedServerEvent) {

    }


    @Listener
    fun onPlayerJoin(event: ClientConnectionEvent.Join, @Getter("getTargetEntity") player: Player) {

    }

}
