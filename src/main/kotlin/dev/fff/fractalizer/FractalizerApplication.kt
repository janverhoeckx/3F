package dev.fff.fractalizer

import dev.fff.fractalizer.dataloader.FitnessFunctionDataLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class FractalizerApplication

fun main(args: Array<String>) {
    runApplication<FractalizerApplication>(*args)
}

@RestController
class FitnessFunctionController(val fitnessFunctionDataLoader: FitnessFunctionDataLoader, val fitnessFunctionService: FitnessFunctionService) {

    @GetMapping("/fitnessfunction")
    fun getFitnessFunctionResult() = fitnessFunctionService.evaluateFitnessFunction(fitnessFunctionDataLoader.loadFitnessFunctions())

}
