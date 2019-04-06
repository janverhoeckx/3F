package dev.fff.fractalizer.handler

import dev.fff.fractalizer.model.FitnessFunction
import org.springframework.stereotype.Service
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.slf4j.LoggerFactory
import java.net.ConnectException


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
        fitnessFunction.properties["url"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'url'")
            return false
        }

        try {
            val response = httpClient.newCall(Request.Builder()
                    .url(fitnessFunction.properties["url"])
                    .build()).execute()

            if (!response.isSuccessful) {
                return false
            }

            return fitnessFunction.properties["expectedResult"]?.contains(response.body().string()) ?: true
        } catch (e: ConnectException) {
            LOGGER.info("Failed to connect to ${fitnessFunction.properties["url"]} for fitness function ${fitnessFunction.name}")
            return false
        }
    }

    override fun getType(): String {
        return "HttpCheck"
    }

}
