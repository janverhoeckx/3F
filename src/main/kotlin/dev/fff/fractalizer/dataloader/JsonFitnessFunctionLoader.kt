package dev.fff.fractalizer.dataloader

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.fff.fractalizer.model.FitnessFunction
import org.slf4j.LoggerFactory

@Service
class JsonFitnessFunctionLoader(
        @Value("\${jsonFile}")
        val jsonDataPath: String
) : FitnessFunctionDataLoader {

    private val LOGGER = LoggerFactory.getLogger(javaClass)

    init {
        LOGGER.info("Using json file: $jsonDataPath")
    }

    override fun loadFitnessFunctions(): FitnessFunction = jacksonObjectMapper().readValue(JsonFitnessFunctionLoader::class.java.getResource(jsonDataPath).readText())

}
