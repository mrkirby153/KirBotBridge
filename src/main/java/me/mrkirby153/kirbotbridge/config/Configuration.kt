package me.mrkirby153.kirbotbridge.config

import me.mrkirby153.kirbotbridge.KirBotBridge
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader

object Configuration {
    private var loader: ConfigurationLoader<CommentedConfigurationNode> = KirBotBridge.instance.confgManager

    var config = loader.load()

    fun init() {
        if (config.getNode("bridge", "channel").isVirtual) {
            config.getNode("bridge", "channel").value = "<CHANGE ME>"
        }

        if (config.getNode("bridge", "guild").isVirtual) {
            config.getNode("bridge", "guild").value = "<CHANGE ME>"
        }

        if (config.getNode("bridge", "host").isVirtual) {
            config.getNode("bridge", "host").value = "localhost"
        }

        if (config.getNode("bridge", "port").isVirtual) {
            config.getNode("bridge", "port").value = 7563
        }

        if (config.getNode("bridge", "password").isVirtual) {
            config.getNode("bridge", "password").value = "<CHANGE ME>"
        }

        saveConfig()

    }

    fun saveConfig() {
        loader.save(config)
    }
}