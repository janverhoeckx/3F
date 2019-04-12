package dev.fff.fractalizer.check

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class SimpleExpressionValidatorTest
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
        CompareLongExpressionCheck(createFitnessFunction(null))
    }

    @Test
    fun `Validate configuration - empty properties`()
    {
        expectedException.expectMessage("Expected fitness function name to have a property 'expression'")
        CompareLongExpressionCheck(createFitnessFunction(emptyMap()))
    }

    @Test
    fun `Validate configuration - empty expression property`()
    {
        expectedException.expectMessage("Error in fitness function name,  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc")
        CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "")))
    }

    @Test
    fun `Validate configuration - expression property contains to much whitespace`()
    {
        expectedException.expectMessage("Error in fitness function name,  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc")
        CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "> 1 invalid")))
    }

    @Test
    fun `Validate configuration - expression property second part is not a number`()
    {
        expectedException.expectMessage("Error in fitness function name,  'expression' property should be like '< 10', '== 10', '!= 10', '> 10', etc")
        CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "> not_a_number")))
    }

    @Test
    fun `Validate configuration - expression property first part is not a valid operator`()
    {
        assertFalse(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "ooo 10"))).compareWithExpression(0))
    }

    @Test
    fun `Validate equals`()
    {
        assertFalse(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "== 3"))).compareWithExpression(10))
        assertTrue(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "== 3"))).compareWithExpression(3))
    }

    @Test
    fun `Validate not equals`()
    {
        assertFalse(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "!= 13"))).compareWithExpression(13))
        assertTrue(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "!= 13"))).compareWithExpression(10))
    }

    @Test
    fun `Validate more then`()
    {
        assertFalse(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "> 10"))).compareWithExpression(9))
        assertFalse(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "> 10"))).compareWithExpression(10))
        assertTrue(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "> 10"))).compareWithExpression(11))
    }

    @Test
    fun `Validate less then`()
    {
        assertFalse(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "< 42"))).compareWithExpression(47))
        assertFalse(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "< 42"))).compareWithExpression(42))
        assertTrue(CompareLongExpressionCheck(createFitnessFunction(mapOf("expression" to "< 42"))).compareWithExpression(-44))
    }
}