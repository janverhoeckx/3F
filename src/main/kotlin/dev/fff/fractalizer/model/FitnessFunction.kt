package dev.fff.fractalizer.model

import com.fasterxml.jackson.annotation.JsonInclude

class FitnessFunction(
        val name: String,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val description: String?,
        var okay: Boolean,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        var children: List<FitnessFunction>?,
        val type: String
)
