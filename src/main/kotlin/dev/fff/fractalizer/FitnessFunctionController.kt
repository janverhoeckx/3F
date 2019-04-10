package dev.fff.fractalizer

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

//FIXME everything should be renamed to Fractalizer inclusing strings... accept for the fitnessfunction itself

@RestController
class FitnessFunctionController(val fitnessFunctionDataLoader: FitnessFunctionDataLoader, val fitnessFunctionService: FitnessFunctionService) {

    @GetMapping("/fitnessfunction")
    fun getFitnessFunctionResult() = fitnessFunctionService.evaluateFitnessFunction(fitnessFunctionDataLoader.loadFitnessFunctions())

}
