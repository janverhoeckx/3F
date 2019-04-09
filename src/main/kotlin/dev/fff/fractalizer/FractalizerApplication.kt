package dev.fff.fractalizer

import com.squareup.okhttp.OkHttpClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc



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

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
    }
}
