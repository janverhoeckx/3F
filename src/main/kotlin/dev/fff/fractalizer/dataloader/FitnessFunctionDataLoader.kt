package dev.fff.fractalizer.dataloader

import dev.fff.fractalizer.model.FitnessFunction

interface FitnessFunctionDataLoader {
    fun loadFitnessFunctions(): FitnessFunction
}
