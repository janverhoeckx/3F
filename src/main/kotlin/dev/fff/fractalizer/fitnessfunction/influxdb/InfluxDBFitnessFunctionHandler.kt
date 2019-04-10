package dev.fff.fractalizer.fitnessfunction.influxdb

import dev.fff.fractalizer.FitnessFunctionHandler
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

        val url = fitnessFunction.properties["url"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'url'")
            return false
        }
        val username = fitnessFunction.properties["username"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'username'")
            return false
        }
        val password = fitnessFunction.properties["password"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'password'")
            return false
        }
        val database = fitnessFunction.properties["database"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'database'")
            return false
        }
        val query = fitnessFunction.properties["query"] ?: run {
            LOGGER.warn("Expected fitness function ${fitnessFunction.name} to have a property 'query'")
            return false
        }


        try {

            val influxDB = influxDBFactoryAdapter.getInfluxDBConnection(url, username, password);

            val queryResult: QueryResult = influxDB.query(Query(query, database))

            queryResult.error ?: run {
                LOGGER.warn("Influx query result for ${fitnessFunction.name} is ${queryResult.error}")
                return false
            }

            if (queryResult.results.size != 1) {
                LOGGER.warn("Influx query result for ${fitnessFunction.name} is not a single value")
                return false
            }

            LOGGER.debug("Influx query result for ${fitnessFunction.name} : ${queryResult.results[0]}")

            //FIXME evaluate result eg:
            /*
            return when (resultExpression) {
                    "==" -> 0
                    "<" -> 1
                    ">" -> 2
                    else -> throw IllegalArgumentException("invalid resultExpression")
                }
             */


        } catch (e: Throwable) {
            LOGGER.info("Failed to connect to ${fitnessFunction.properties["url"]} influxdb for fitness function ${fitnessFunction.name}", e)
            return false
        }
        return false;
    }

    override fun getType(): String {
        return "InfluxDBCheck"
    }

}
