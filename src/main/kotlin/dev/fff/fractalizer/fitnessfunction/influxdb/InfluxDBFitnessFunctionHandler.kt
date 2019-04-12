package dev.fff.fractalizer.fitnessfunction.influxdb

import dev.fff.fractalizer.FitnessFunctionHandler
import dev.fff.fractalizer.check.CompareDoubleExpressionCheck
import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import org.influxdb.dto.Query
import org.influxdb.dto.QueryResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class InfluxDBFitnessFunctionHandler(val influxDBFactoryAdapter: InfluxDBFactoryAdapter) : FitnessFunctionHandler {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(InfluxDBFitnessFunctionHandler::class.java)
    }

    override fun checkFitnessFunction(fitnessFunction: FitnessFunction): Boolean {


        fitnessFunction.properties ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have properties")
            return false
        }

        //FIXME refactor connection and query logic including configuration checks conform Expressionvalidator
        val url = fitnessFunction.properties["url"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'url'")
            //FIXME throw exceptions
            return false
        }
        val username = fitnessFunction.properties["username"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'username'")
            return false
        }
        //FIXME support for encrypted password / secret
        val password = fitnessFunction.properties["password"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'password'")
            return false
        }
        val database = fitnessFunction.properties["database"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'database'")
            return false
        }
        var query = fitnessFunction.properties["query"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'query'")
            return false
        }

        val validator = CompareDoubleExpressionCheck(fitnessFunction)


        try {

            val influxDB = influxDBFactoryAdapter.getInfluxDBConnection(url, username, password)

            val queryResult: QueryResult = influxDB.query(Query(query, database))

            queryResult.error?.let {
                LOGGER.warn("Influx query result for ${fitnessFunction.name} is ${queryResult.error}")
                return false
            }
            LOGGER.debug("Influx query result for ${fitnessFunction.name} : $queryResult")

            return validator.compareWithExpression(queryResult.results.first().series.first().values.first().last().toString().toDouble())

        } catch (e: Throwable) {
            LOGGER.info("Failed to connect to ${fitnessFunction.properties["url"]} influxdb for fitness function ${fitnessFunction.name}", e)
            return false
        }
    }

    override fun getType(): String {
        return "InfluxDBCheck"
    }

}
