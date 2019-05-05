package dev.fff.fractalizer.fitnessfunction

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.File

data class FitnessFunction(
        val name: String,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val description: String?,
        var okay: Boolean,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var children: List<FitnessFunction>?,
        val type: String?,
        val properties: Map<String, String>?
)

fun getRequiredProperty(fitnessFunction: FitnessFunction, name: String): String
{
        val values = fitnessFunction.properties?.get(name)?.split("::", limit = 2) ?: run {
                throw FitnessFunctionConfigurationException("Expected fitness function ${fitnessFunction.name} to have a property '$name'")
        }

        if (values.size == 2)
        {
                when (values.first())
                {
                        "file" -> return getRequiredPropertyFromFile(values.last())
                        else   -> throw FitnessFunctionConfigurationException("Error reading property $name: unknown source: ${values.first()}")

                }
        }
        return values.first()
}

private fun getRequiredPropertyFromFile(file: String): String
{
        try
        {
                return File(file).readText(Charsets.UTF_8)
        }
        catch (t: Throwable)
        {
                throw FitnessFunctionConfigurationException("Error reading property from file $file", t);
        }
}
