package dev.fff.fractalizer

import dev.fff.fractalizer.controller.FitnessFunctionController
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
class FitnessFunctionControllerIT {

    companion object {
        private const val MOCK_SERVER_PORT = 1234
    }

    @Autowired
    private lateinit var fitnessFunctionController: FitnessFunctionController

    @Test
    fun testGetFitnessFunctionResultConnectExceptionReturnsFalse() {
        val result = fitnessFunctionController.getFitnessFunctionResult()

        Assert.assertTrue(result.okay)
        Assert.assertTrue(result.children!![0].okay)
        Assert.assertTrue(result.children!![1].okay)
        Assert.assertFalse(result.children!![0].children!![0].okay)
    }

    @Test
    fun testGetFitnessFunctionResultConnectHttpCheckSuccess() {
        val mockWebServer = MockWebServer()
        mockWebServer.start(MOCK_SERVER_PORT)

        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        val result = fitnessFunctionController.getFitnessFunctionResult()

        Assert.assertTrue(result.okay)
        Assert.assertTrue(result.children!![0].okay)
        Assert.assertTrue(result.children!![1].okay)
        Assert.assertTrue(result.children!![0].children!![0].okay)

        mockWebServer.shutdown()
    }

}
