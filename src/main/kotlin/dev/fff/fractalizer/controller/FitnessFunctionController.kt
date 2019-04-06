package dev.fff.fractalizer.controller

import dev.fff.fractalizer.FitnessFunctionService
import dev.fff.fractalizer.dataloader.FitnessFunctionDataLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FitnessFunctionController(val fitnessFunctionDataLoader: FitnessFunctionDataLoader, val fitnessFunctionService: FitnessFunctionService) {

    @GetMapping("/fitnessfunction")
    fun getFitnessFunctionResult() = fitnessFunctionService.evaluateFitnessFunction(fitnessFunctionDataLoader.loadFitnessFunctions())

}
