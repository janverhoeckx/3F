package dev.fff.fractalizer.fitnessfunction.influxdb

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import dev.fff.fractalizer.fitnessfunction.FitnessFunctionConfigurationException
import org.influxdb.InfluxDB
import org.influxdb.InfluxDBException
import org.influxdb.dto.Query
import org.influxdb.dto.QueryResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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

    @Mock
    private lateinit var mockInfluxDB: InfluxDB

    @InjectMocks
    private lateinit var systemUnderTest: InfluxDBFitnessFunctionHandler

    private val properties: HashMap<String, String> = hashMapOf("url" to "http://example.com", "username" to "user", "password" to "p4word",
                                                                "database" to "test", "query" to "Select total from memory order by time desc",
                                                                "expression" to "> 42")


    private fun createFitnessFunction(fitnessFunctionProperties: Map<String, String>?): FitnessFunction {
        return FitnessFunction("test 1", "test description 1", false, null, "InfluxDBCheck", fitnessFunctionProperties)
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `InfluxDB check without url property should fail`()
    {
        systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "url" }))
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `InfluxDB check without username property should fail`()
    {
        systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "username" }))
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `InfluxDB check without password property should fail`()
    {
        systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "password" }))
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `InfluxDB check without datebase property should fail`()
    {
        systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "database" }))
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `InfluxDB check without query property should fail`()
    {
        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties.filterKeys { it != "query" })))
    }

    @Test
    fun `InfluxDB check without proper connection should return false`()
    {

        Mockito.`when`(mockInfluxDBFactoryAdapter.getInfluxDBConnection("http://example.com", "user", "p4word"))
            .thenThrow(InfluxDBException("error test"))

        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties)))
    }

    @Test
    fun `InfluxDB check with error response should return false`()
    {

        val queryResult = QueryResult()
        queryResult.error = "Error"

        Mockito.`when`(mockInfluxDBFactoryAdapter.getInfluxDBConnection("http://example.com", "user", "p4word"))
            .thenReturn(mockInfluxDB)
        Mockito.`when`(mockInfluxDB.query(Mockito.any(Query::class.java)))
            .thenReturn(queryResult)

        assertFalse(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties)))
    }

    @Test
    fun checkFitnessFunction()
    {

        val series = QueryResult.Series()
        series.values = listOf(listOf("", 43.0))

        val results = QueryResult.Result()
        results.series = listOf(series)

        val queryResult = QueryResult()
        queryResult.results = listOf(results)

        Mockito.`when`(mockInfluxDBFactoryAdapter.getInfluxDBConnection("http://example.com", "user", "p4word"))
            .thenReturn(mockInfluxDB)
        Mockito.`when`(mockInfluxDB.query(Mockito.any(Query::class.java)))
            .thenReturn(queryResult)

        assertTrue(systemUnderTest.checkFitnessFunction(createFitnessFunction(properties)))
    }


    @Test
    fun getType() {
        assertEquals("InfluxDBCheck", systemUnderTest.getType())
    }
}