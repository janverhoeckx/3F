package dev.fff.fractalizer.handler

import dev.fff.fractalizer.model.FitnessFunction

interface FitnessFunctionHandler {
    fun checkFitnessFunction(fitnessFunction: FitnessFunction): Boolean

    fun getType(): String
}
