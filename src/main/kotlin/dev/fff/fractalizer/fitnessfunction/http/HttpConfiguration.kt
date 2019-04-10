package dev.fff.fractalizer.fitnessfunction.http

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpConfiguration {

    @Bean
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

}