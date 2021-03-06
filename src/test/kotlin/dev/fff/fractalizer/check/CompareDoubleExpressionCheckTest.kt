package dev.fff.fractalizer.check

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class CompareDoubleExpressionCheckTest
{

    @Rule
    @JvmField
    var expectedException = ExpectedException.none()

    private fun createFitnessFunction(fitnessFunctionProperties: Map<String, String>?): FitnessFunction
    {
        return FitnessFunction("name", this.toString(), false, null, "Check", fitnessFunctionProperties)
    }

    @Test
    fun `Validate configuration - no properties`()
    {
        expectedException.expectMessage("Expected fitness function name to have a property 'expression'")
        CompareDoubleExpressionCheck(createFitnessFunction(null))
    }

    @Test
    fun `Validate configuration - empty properties`()
    {
        expectedException.expectMessage("Expected fitness function name to have a property 'expression'")
        CompareDoubleExpressionCheck(createFitnessFunction(emptyMap()))
    }

    @Test
    fun `Validate configuration - empty expression property`()
    {
        expectedException.expectMessage("Error in fitness function name,  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc")
        CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "")))
    }

    @Test
    fun `Validate configuration - expression property contains to much whitespace`()
    {
        expectedException.expectMessage("Error in fitness function name,  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc")
        CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "> 1 invalid")))
    }

    @Test
    fun `Validate configuration - expression property second part is not a number`()
    {
        expectedException.expectMessage("Error in fitness function name,  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc")
        CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "> not_a_number")))
    }

    @Test
    fun `Validate configuration - expression property first part is not a valid operator`()
    {
        assertFalse(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "ooo 10"))).compareWithExpression(0.0))
    }

    @Test
    fun `Validate equals`()
    {
        assertFalse(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "== 3"))).compareWithExpression(10.0))
        assertTrue(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "== 3"))).compareWithExpression(3.0))
    }

    @Test
    fun `Validate not equals`()
    {
        assertFalse(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "!= 13"))).compareWithExpression(13.0))
        assertTrue(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "!= 13"))).compareWithExpression(10.0))
    }

    @Test
    fun `Validate more then`()
    {
        assertFalse(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "> 10"))).compareWithExpression(9.0))
        assertFalse(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "> 10"))).compareWithExpression(10.0))
        assertTrue(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "> 10"))).compareWithExpression(11.0))
    }

    @Test
    fun `Validate less then`()
    {
        assertFalse(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "< 42"))).compareWithExpression(47.0))
        assertFalse(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "< 42"))).compareWithExpression(42.0))
        assertTrue(CompareDoubleExpressionCheck(createFitnessFunction(mapOf("expression" to "< 42"))).compareWithExpression(-44.0))
    }
}