package dev.fff.fractalizer.fitnessfunction

import org.junit.Assert
import org.junit.Test

class FitnessFunctionTest
{

    private val fitnessFunction = FitnessFunction("name", "description", false, emptyList(), "type",
                                                  hashMapOf("simple" to "simplevalue",
                                                            "fromfile" to "file::" + FitnessFunctionTest::class.java.getResource("/test.txt").path,
                                                            "emptysource" to "file::",
                                                            "unknownsource" to "unknown::",
                                                            "filenotfound" to "file::unknown888.txt"))

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `Get simple property without properties should fail`()
    {
        getRequiredProperty(FitnessFunction("", "", false, emptyList(), "type", null), "")
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `Get simple property with empty properties should fail`()
    {

        getRequiredProperty(FitnessFunction("", "", false, emptyList(), "type", emptyMap()), "")
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `Get simple property with unknown property should fail`()
    {
        getRequiredProperty(fitnessFunction, "not found")
    }

    @Test
    fun `Get simple property test`()
    {
        Assert.assertEquals("simplevalue", getRequiredProperty(fitnessFunction, "simple"))
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `Get property from empty source should fail`()
    {
        getRequiredProperty(fitnessFunction, "emptysource")
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `Get property from unknown source should fail`()
    {
        getRequiredProperty(fitnessFunction, "unknownsource")
    }

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun `Get property from  file not found test`()
    {
        getRequiredProperty(fitnessFunction, "filenotfound")
    }

    @Test
    fun `Get property form file test`()
    {
        Assert.assertEquals("value from file", getRequiredProperty(fitnessFunction, "fromfile"))
    }
}