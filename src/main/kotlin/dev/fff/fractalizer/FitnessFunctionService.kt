package dev.fff.fractalizer

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import org.springframework.stereotype.Service

@Service
class FitnessFunctionService(private val fitnessFunctionHandlers: List<FitnessFunctionHandler>) {

    private val handlerMap: Map<String, FitnessFunctionHandler> by lazy {
        fitnessFunctionHandlers.map { it.getType() to it }.toMap()
    }

    fun evaluateFitnessFunction(fitnessFunction: FitnessFunction): FitnessFunction {
        fitnessFunction.children = fitnessFunction.children?.map { evaluateFitnessFunction(it) }
        if (fitnessFunction.children?.any { !it.okay } == true) {
            fitnessFunction.okay = false
        } else if (fitnessFunction.type != null) {
            fitnessFunction.okay = handlerMap.getValue(fitnessFunction.type).checkFitnessFunction(fitnessFunction)
        }
        return fitnessFunction
    }

}
