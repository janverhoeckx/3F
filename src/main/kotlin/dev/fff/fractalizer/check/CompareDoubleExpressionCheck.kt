package dev.fff.fractalizer.check

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import dev.fff.fractalizer.fitnessfunction.FitnessFunctionConfigurationException
import dev.fff.fractalizer.fitnessfunction.getRequiredProperty
import org.slf4j.LoggerFactory

internal class CompareDoubleExpressionCheck(fitnessFunction: FitnessFunction)
{
    companion object
    {
        private val LOGGER = LoggerFactory.getLogger(CompareDoubleExpressionCheck::class.java)
    }

    private val operator: String
    private val compareValue: Double

    init
    {
        val expression = getRequiredProperty(fitnessFunction, "expression")

        val reason = "Error in fitness function ${fitnessFunction.name},  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc."

        operator = expression.split(" ", limit = 2).firstOrNull().orEmpty()
        if (operator.isEmpty())
        {
            CompareDoubleExpressionCheck.LOGGER.warn(reason)
            throw FitnessFunctionConfigurationException(reason)
        }
        compareValue = expression.split(" ", limit = 2).lastOrNull()?.toDoubleOrNull() ?: run {
            CompareDoubleExpressionCheck.LOGGER.warn(reason)
            throw FitnessFunctionConfigurationException(reason)
        }
    }

    fun compareWithExpression(value: Double): Boolean
    {
        return when (operator)
        {
            "==" -> value == compareValue
            "!=" -> value != compareValue
            "<"  -> value < compareValue
            ">"  -> value > compareValue
            else ->
            {
                CompareDoubleExpressionCheck.LOGGER.warn("Fitness function 'expression' operator is not correct: $operator")
                false
            }
        }
    }
}