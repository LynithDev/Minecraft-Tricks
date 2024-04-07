package com.example

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ExampleMod : ModInitializer {
    override fun onInitialize() {
        LOGGER.info("Hello Fabric world!")
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger("modid")
    }
}