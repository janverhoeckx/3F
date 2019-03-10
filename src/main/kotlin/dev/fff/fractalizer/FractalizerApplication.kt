package dev.fff.fractalizer

import com.squareup.okhttp.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.ConnectException

@SpringBootApplication
class FractalizerApplication

fun main(args: Array<String>) {
    runApplication<FractalizerApplication>(*args)
}


//FIXME  proxy fitness function to seperate package + renames?
@RestController
class Proxy {

    private val httpClient = OkHttpClient()

    @PostMapping("/fitnessfunction")
    fun getFitnessFunctionResult(@RequestBody fitnessFunction: FitnessFunction): FitnessFunctionResult {
        val request = Request.Builder()
                .url(fitnessFunction.url)
                .build()

        return try {
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body().string()
                if (fitnessFunction.expectedResultExpression != null) {
                    return if (body.contains(fitnessFunction.expectedResultExpression)) {
                        FitnessFunctionResult(true)
                    } else {
                        FitnessFunctionResult(false)
                    }
                }
                FitnessFunctionResult(true)
            } else {
                FitnessFunctionResult(false)
            }
        } catch (e: ConnectException) {
            FitnessFunctionResult(false)
        }
    }
}

data class FitnessFunctionResult(val passed: Boolean)

data class FitnessFunction(val url: String, val expectedResultExpression: String?)
