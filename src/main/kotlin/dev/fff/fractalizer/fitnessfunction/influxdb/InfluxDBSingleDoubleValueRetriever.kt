package dev.fff.fractalizer.fitnessfunction.influxdb

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import dev.fff.fractalizer.fitnessfunction.getRequiredProperty
import org.influxdb.dto.Query
import org.influxdb.dto.QueryResult
import org.slf4j.LoggerFactory

internal class InfluxDBSingleDoubleValueQuestioner(private val fitnessFunction: FitnessFunction,
                                                   private val influxDBFactoryAdapter: InfluxDBFactoryAdapter)
{
    companion object
    {
        private val LOGGER = LoggerFactory.getLogger(InfluxDBSingleDoubleValueQuestioner::class.java)
    }

    private val url = getRequiredProperty(fitnessFunction, "url")
    private val username = getRequiredProperty(fitnessFunction, "username")
    private val password = getRequiredProperty(fitnessFunction, "password")
    private val database = getRequiredProperty(fitnessFunction, "database")
    private val query = getRequiredProperty(fitnessFunction, "query")

    fun query(): Double
    {
        try
        {
            val queryResult: QueryResult = influxDBFactoryAdapter.getInfluxDBConnection(url, username, password).query(Query(query, database))

            queryResult.error?.let {
                LOGGER.warn("Influx query result for ${fitnessFunction.name} is ${queryResult.error}")
                throw InfluxDBException("Influx query failed")
            }

            LOGGER.debug("Influx query result for ${fitnessFunction.name} : $queryResult")

            return queryResult.results.first().series.first().values.first().last().toString().toDouble()
        }
        catch (e: Throwable)
        {
            LOGGER.info("Failed to connect to influxdb $url for fitness function ${fitnessFunction.name}", e)
            throw InfluxDBException("Influx connection failed")
        }
    }
}