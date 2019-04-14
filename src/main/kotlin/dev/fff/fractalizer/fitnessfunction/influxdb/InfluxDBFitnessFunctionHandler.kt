package dev.fff.fractalizer.fitnessfunction.influxdb

import dev.fff.fractalizer.FitnessFunctionHandler
import dev.fff.fractalizer.check.CompareDoubleExpressionCheck
import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import org.springframework.stereotype.Service

@Service
class InfluxDBFitnessFunctionHandler(val influxDBFactoryAdapter: InfluxDBFactoryAdapter) : FitnessFunctionHandler
{
    override fun checkFitnessFunction(fitnessFunction: FitnessFunction): Boolean {
        val questioner = InfluxDBSingleDoubleValueQuestioner(fitnessFunction, influxDBFactoryAdapter)
        val validator = CompareDoubleExpressionCheck(fitnessFunction)

        return try
        {
            validator.compareWithExpression(questioner.query())
        }
        catch (e: InfluxDBException)
        {
            return false
        }
    }

    override fun getType(): String {
        return "InfluxDBCheck"
    }

}
