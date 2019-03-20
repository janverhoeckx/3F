package dev.fff.fractalizer.dataloader

import org.junit.Test

import org.junit.Assert.*

class JsonFitnessFunctionLoaderTest {

    @Test
    fun loadFitnessFunctions() {
        val result = JsonFitnessFunctionLoader("/test.json").loadFitnessFunctions()

        assertEquals("Example webapp", result.name)
        assertEquals("Architecture okay?", result.description)
        assertFalse(result.okay)
        assertEquals("Functionality", result.children!![0].name)
        assertTrue(result.children!![0].okay)
        assertNull(result.children!![0].description)
        assertEquals("Security", result.children!![0].children!![0].name)
        assertTrue(result.children!![0].children!![0].okay)
        assertNull(result.children!![0].children!![0].children)
        assertNull(result.children!![0].children!![0].description)
        assertEquals("Usability (UX)", result.children!![1].name)
        assertNull(result.children!![1].description)
        assertNull(result.children!![1].children)
        assertTrue(result.children!![1].okay)
    }
}
