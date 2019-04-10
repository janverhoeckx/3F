package dev.fff.fractalizer.fitnessfunction.http

import dev.fff.fractalizer.FitnessFunctionHandler
import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class HttpCheckFitnessFunctionHandler(val httpClient: OkHttpClient) : FitnessFunctionHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(HttpCheckFitnessFunctionHandler::class.java)
    }

    override fun checkFitnessFunction(fitnessFunction: FitnessFunction): Boolean {
        fitnessFunction.properties ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have properties")
            return false
        }
        val url = fitnessFunction.properties["url"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'url'")
            return false
        }

        try {
            val response = httpClient.newCall(Request.Builder()
                    .url(url)
                    .build()).execute()

            if (!response.isSuccessful) {
                return false
            }
            return fitnessFunction.properties["expectedResult"]?.contains((response.body()?.string() ?: "")) ?: true
        } catch (e: Throwable) {
            LOGGER.info("Failed to connect to ${fitnessFunction.properties["url"]} for fitness function ${fitnessFunction.name}")
            return false
        }
    }

    override fun getType(): String {
        return "HttpCheck"
    }

}
