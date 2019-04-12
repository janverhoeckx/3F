package dev.fff.fractalizer.check

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import dev.fff.fractalizer.fitnessfunction.FitnessFunctionConfigurationException
import org.slf4j.LoggerFactory

class CompareLongExpressionCheck(fitnessFunction: FitnessFunction)
{
    companion object
    {
        private val LOGGER = LoggerFactory.getLogger(CompareLongExpressionCheck::class.java)
    }

    private val operator: String
    private val compareValue: Long

    init
    {
        val expression = fitnessFunction.properties?.get("expression") ?: run {
            val reason = "Expected fitness function ${fitnessFunction.name} to have a property 'expression'"
            CompareLongExpressionCheck.LOGGER.warn(reason)
            throw FitnessFunctionConfigurationException(reason)
        }

        val reason = "Error in fitness function ${fitnessFunction.name},  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc."

        operator = expression.split(" ", limit = 2).firstOrNull().orEmpty()
        if (operator.isEmpty())
        {
            CompareLongExpressionCheck.LOGGER.warn(reason)
            throw FitnessFunctionConfigurationException(reason)
        }
        compareValue = expression.split(" ", limit = 2).lastOrNull()?.toLongOrNull() ?: run {
            CompareLongExpressionCheck.LOGGER.warn(reason)
            throw FitnessFunctionConfigurationException(reason)
        }
    }

    fun compareWithExpression(value: Long): Boolean
    {
        return when (operator)
        {
            "==" -> value == compareValue
            "!=" -> value != compareValue
            "<"  -> value < compareValue
            ">"  -> value > compareValue
            else ->
            {
                CompareLongExpressionCheck.LOGGER.warn("Fitness function 'expression' operator is not correct: $operator")
                false
            }
        }
    }
}