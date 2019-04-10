package dev.fff.fractalizer

import dev.fff.fractalizer.fitnessfunction.FitnessFunction

interface FitnessFunctionDataLoader {
    fun loadFitnessFunctions(): FitnessFunction
}
