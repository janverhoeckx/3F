package dev.fff.fractalizer.fitnessfunction.http

import dev.fff.fractalizer.FitnessFunctionHandler
import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import dev.fff.fractalizer.fitnessfunction.getRequiredProperty
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
        val url = getRequiredProperty(fitnessFunction, "url")
        val expectedResult = fitnessFunction.properties?.get("expectedResult")

        return try
        {

            val response = httpClient.newCall(Request.Builder()
                    .url(url)
                    .build()).execute()

            response.isSuccessful && (expectedResult?.contains((response.body()?.string() ?: "")) ?: true)
        } catch (e: Throwable) {
            LOGGER.info("Failed to connect to $url for fitness function ${fitnessFunction.name}")
            return false
        }
    }

    override fun getType(): String {
        return "HttpCheck"
    }

}
