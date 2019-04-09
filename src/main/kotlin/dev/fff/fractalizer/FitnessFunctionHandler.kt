package dev.fff.fractalizer

import dev.fff.fractalizer.fitnessfunction.FitnessFunction

interface FitnessFunctionHandler {
    fun checkFitnessFunction(fitnessFunction: FitnessFunction): Boolean

    fun getType(): String
}
