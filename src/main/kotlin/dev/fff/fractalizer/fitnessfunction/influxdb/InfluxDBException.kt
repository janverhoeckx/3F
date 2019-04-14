package dev.fff.fractalizer.fitnessfunction.influxdb

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class InfluxDBException(message: String?) : RuntimeException(message)
