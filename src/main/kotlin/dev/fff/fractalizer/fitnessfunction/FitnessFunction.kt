package dev.fff.fractalizer.fitnessfunction

import com.fasterxml.jackson.annotation.JsonInclude

class FitnessFunction(
        val name: String,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val description: String?,
        var okay: Boolean,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var children: List<FitnessFunction>?,
        val type: String?,
        val properties: Map<String, String>?
)
