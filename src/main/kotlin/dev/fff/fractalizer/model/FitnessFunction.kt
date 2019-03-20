package dev.fff.fractalizer.model

import com.fasterxml.jackson.annotation.JsonInclude

class FitnessFunction(
        val name: String,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val description: String?,
        val okay: Boolean,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val children: List<FitnessFunction>?
)
