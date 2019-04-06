package dev.fff.fractalizer

import com.squareup.okhttp.OkHttpClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class FractalizerApplication

fun main(args: Array<String>) {
    runApplication<FractalizerApplication>(*args)
}

@Configuration
class Configuration {

    @Bean
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

}
