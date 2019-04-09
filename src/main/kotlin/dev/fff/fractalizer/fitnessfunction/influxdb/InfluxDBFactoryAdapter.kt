package dev.fff.fractalizer.fitnessfunction.influxdb

import org.influxdb.InfluxDB
import org.influxdb.InfluxDBFactory
import org.springframework.stereotype.Service

interface InfluxDBFactoryAdapter {
    fun getInfluxDBConnection(url: String, username: String, password: String): InfluxDB
}

@Service
class DefaultInfluxDBFactoryAdapter : InfluxDBFactoryAdapter {
    override fun getInfluxDBConnection(url: String, username: String, password: String): InfluxDB {
        return InfluxDBFactory.connect(url, username, password);
    }
}