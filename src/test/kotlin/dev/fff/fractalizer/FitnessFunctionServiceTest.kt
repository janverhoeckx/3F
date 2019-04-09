package dev.fff.fractalizer

import dev.fff.fractalizer.handler.FitnessFunctionHandler
import dev.fff.fractalizer.model.FitnessFunction
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FitnessFunctionServiceTest {

    private val mockFitnessFunctionHandler = mock(FitnessFunctionHandler::class.java)

    private val systemUnderTest = FitnessFunctionService(listOf(mockFitnessFunctionHandler))

    @Before
    fun setUp() {
        `when`(mockFitnessFunctionHandler.getType()).thenReturn("test")
    }

    @Test
    fun evaluateFitnessFunction() {
        val fitnessFunctionChild1 = FitnessFunction("test child 1", "test description child 1", false, null, "test", null)
        val fitnessFunctionChild1OfChild2 = FitnessFunction("test child 1 of child 2", "test description child 1 of child 2", false, null, "test", null)
        val fitnessFunctionChild2 = FitnessFunction("test child 2", "test description child 2", false, listOf(
                fitnessFunctionChild1OfChild2
        ), "test", null)
        val fitnessFunction = FitnessFunction("test parent", "test description parent", false, listOf(
                fitnessFunctionChild1,
                fitnessFunctionChild2
        ), "test", null)

        `when`(mockFitnessFunctionHandler.checkFitnessFunction(fitnessFunction)).thenReturn(false)
        `when`(mockFitnessFunctionHandler.checkFitnessFunction(fitnessFunctionChild1)).thenReturn(false)
        `when`(mockFitnessFunctionHandler.checkFitnessFunction(fitnessFunctionChild2)).thenReturn(true)
        `when`(mockFitnessFunctionHandler.checkFitnessFunction(fitnessFunctionChild1OfChild2)).thenReturn(true)

        val result = systemUnderTest.evaluateFitnessFunction(fitnessFunction)

        assertFalse(result.okay)
        assertFalse(result.children!![0].okay)
        assertTrue(result.children!![1].okay)
        assertTrue(result.children!![1].children!![0].okay)
    }

    @Test(expected = NoSuchElementException::class)
    fun evaluateFitnessFunctionUnknownTypeThrowsException() {
        val fitnessFunction = FitnessFunction("test parent", "test description parent", false, null, "unknowntype", null)

        systemUnderTest.evaluateFitnessFunction(fitnessFunction)
    }

}
