package dev.fff.fractalizer.fitnessfunction.influxdb

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import org.influxdb.InfluxDBException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InfluxDBFitnessFunctionHandlerTest {

    @Mock
    private lateinit var mockInfluxDBFactoryAdapter: InfluxDBFactoryAdapter

    @InjectMocks
    private lateinit var systemUnderTest: InfluxDBFitnessFunctionHandler

    private val properties: HashMap<String, String> = hashMapOf("url" to "http://example.com", "username" to "user", "password" to "p4word",
                                                                "database" to "test", "query" to "Select total from memory order by time desc",
                                                                "expression" to "> 42")


    @Test
    fun checkFitnessFunctionNoProperties() {
        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(null)))
    }

    private fun createFitnessFunction(fitnessFunctionProperties: Map<String, String>?): FitnessFunction {
        return FitnessFunction("test 1", "test description 1", false, null, "InfluxDBCheck", fitnessFunctionProperties)
    }

    @Test
    fun checkFitnessFunctionEmptyProperties() {

        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(emptyMap())))
    }

    @Test
    fun checkFitnessFunctionNoURL() {
        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "url" })))
    }

    @Test
    fun checkFitnessFunctionNoUsername() {
        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "username" })))
    }

    @Test
    fun checkFitnessFunctionNoPassword() {
        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "password" })))
    }

    @Test
    fun checkFitnessFunctionNoDatabase() {
        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "database" })))
    }

    @Test
    fun checkFitnessFunctionNoQuery() {
        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "query" })))
    }

    @Test
    fun checkFitnessFunctionConnectionFail() {

        Mockito.`when`(mockInfluxDBFactoryAdapter.getInfluxDBConnection("http://example.com", "user", "p4word")).thenThrow(InfluxDBException("error test"))

        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties)))
    }


    @Test
    fun getType() {
        assertEquals("InfluxDBCheck", systemUnderTest.getType())
    }
}