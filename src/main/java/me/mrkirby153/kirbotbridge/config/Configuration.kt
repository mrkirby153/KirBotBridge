package me.mrkirby153.kirbotbridge.config

import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader

object Configuration {

    var BOT_HOST_DEFAULT = "localhost"
    var BOT_PORT_DEFAULT: Int = 7563
    var BOT_PASSWORD_DEFAULT = "<CHANGE ME>"


    var BOT_HOST: String = "localhost"
    var BOT_PORT: Int = 7563
    var BOT_PASSWORD: String = "<CHANGE ME>"

    lateinit var configurationLoader: ConfigurationLoader<CommentedConfigurationNode>

    fun loadConfig() {
        val rootNode = configurationLoader.createEmptyNode()
        BOT_HOST = rootNode.getNode("bot", "host").getString(BOT_HOST_DEFAULT)
        BOT_PORT = rootNode.getNode("bot", "port").getInt(BOT_PORT_DEFAULT)
        BOT_PASSWORD = rootNode.getNode("bot", "password").getString(BOT_PASSWORD_DEFAULT)
        saveConfig()
    }

    fun saveConfig() {
        val rootNode = configurationLoader.createEmptyNode()
        rootNode.getNode("bot", "host").value = BOT_HOST
        rootNode.getNode("bot", "port").value = BOT_PORT
        rootNode.getNode("bot", "password").value = BOT_PASSWORD
        configurationLoader.save(rootNode)
    }
}