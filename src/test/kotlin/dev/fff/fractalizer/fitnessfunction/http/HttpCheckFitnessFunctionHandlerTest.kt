package dev.fff.fractalizer.fitnessfunction.http

import dev.fff.fractalizer.fitnessfunction.FitnessFunction
import dev.fff.fractalizer.fitnessfunction.FitnessFunctionConfigurationException
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.net.ConnectException

@RunWith(MockitoJUnitRunner::class)
class HttpCheckFitnessFunctionHandlerTest {

    @Mock
    private lateinit var mockOkHttpClient: OkHttpClient

    @InjectMocks
    private lateinit var systemUnderTest: HttpCheckFitnessFunctionHandler

    @Test(expected = FitnessFunctionConfigurationException::class)
    fun testCheckFitnessFunctionMissingUrlPropertyReturnsFalse() {
        val fitnessFunction = FitnessFunction(name = "testName", okay = true, type = "HttpCheck", properties = emptyMap(), description = "", children = null)

        systemUnderTest.checkFitnessFunction(fitnessFunction)
    }

    @Test
    fun testCheckFitnessFunctionWithoutExpectedResultSuccess() {
        val fitnessFunction = FitnessFunction(name = "testName", okay = true, type = "HttpCheck", properties = mapOf(Pair("url", "http://test.nl")), description = "", children = null)
        val mockCall = mock(Call::class.java)
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "test")
        val mockResponse = Response.Builder()
                .request(Request.Builder().url("http://test.nl").build())
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("test")
                .body(responseBody)
                .build()

        `when`(mockOkHttpClient.newCall(any(Request::class.java))).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)

        val result = systemUnderTest.checkFitnessFunction(fitnessFunction)

        assertTrue(result)
    }

    @Test
    fun testCheckFitnessFunctionWithoutExpectedResultFailsReturnsFalse() {
        val fitnessFunction = FitnessFunction(name = "testName", okay = true, type = "HttpCheck", properties = mapOf(Pair("url", "http://test.nl")), description = "", children = null)
        val mockCall = mock(Call::class.java)
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "test")
        val mockResponse = Response.Builder()
                .request(Request.Builder().url("http://test.nl").build())
                .protocol(Protocol.HTTP_2)
                .code(500)
                .message("test")
                .body(responseBody)
                .build()

        `when`(mockOkHttpClient.newCall(any(Request::class.java))).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)

        val result = systemUnderTest.checkFitnessFunction(fitnessFunction)

        assertFalse(result)
    }

    @Test
    fun testCheckFitnessFunctionFailedToConnectReturnsFalse() {
        val fitnessFunction = FitnessFunction(name = "testName", okay = true, type = "HttpCheck", properties = mapOf(Pair("url", "http://test.nl")), description = "", children = null)
        val mockCall = mock(Call::class.java)

        `when`(mockOkHttpClient.newCall(any(Request::class.java))).thenReturn(mockCall)
        doThrow(ConnectException()).`when`(mockCall).execute()

        val result = systemUnderTest.checkFitnessFunction(fitnessFunction)

        assertFalse(result)
    }

    @Test
    fun testCheckFitnessFunctionWithExpectedResultSuccess() {
        val fitnessFunction = FitnessFunction(name = "testName", okay = true, type = "HttpCheck",
                properties = mapOf(Pair("url", "http://test.nl"), Pair("expectedResult", "test")),
                description = "",
                children = null)
        val mockCall = mock(Call::class.java)
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "test")
        val mockResponse = Response.Builder()
                .request(Request.Builder().url("http://test.nl").build())
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("test")
                .body(responseBody)
                .build()

        `when`(mockOkHttpClient.newCall(any(Request::class.java))).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)

        val result = systemUnderTest.checkFitnessFunction(fitnessFunction)

        assertTrue(result)
    }

    @Test
    fun testCheckFitnessFunctionWithExpectedResultFailsReturnsFalse() {
        val fitnessFunction = FitnessFunction(name = "testName", okay = true, type = "HttpCheck",
                properties = mapOf(Pair("url", "http://test.nl"), Pair("expectedResult", "test")),
                description = "",
                children = null)
        val mockCall = mock(Call::class.java)
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "wrong response")
        val mockResponse = Response.Builder()
                .request(Request.Builder().url("http://test.nl").build())
                .protocol(Protocol.HTTP_2)
                .code(200)
                .message("test")
                .body(responseBody)
                .build()

        `when`(mockOkHttpClient.newCall(any(Request::class.java))).thenReturn(mockCall)
        `when`(mockCall.execute()).thenReturn(mockResponse)

        val result = systemUnderTest.checkFitnessFunction(fitnessFunction)

        assertFalse(result)
    }

    @Test
    fun getType() {
        assertEquals("HttpCheck", systemUnderTest.getType())
    }
}
