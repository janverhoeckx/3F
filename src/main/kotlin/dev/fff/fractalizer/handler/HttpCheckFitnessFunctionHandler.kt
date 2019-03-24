package dev.fff.fractalizer.handler

import dev.fff.fractalizer.model.FitnessFunction
import org.springframework.stereotype.Service

@Service
class HttpCheckFitnessFunctionHandler : FitnessFunctionHandler {

    override fun checkFitnessFunction(fitnessFunction: FitnessFunction): Boolean {
        //TODO: Implement http check
        return true
    }

    override fun getType(): String {
        return "HttpCheck"
    }

}
