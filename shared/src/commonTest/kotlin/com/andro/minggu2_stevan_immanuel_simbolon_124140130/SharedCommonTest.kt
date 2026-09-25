package com.andro.minggu2_stevan_immanuel_simbolon_124140130

import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedCommonTest {

    /**
     * Test 1: Menguji Flow pemancar berita, filter kategori, dan mapping teks display.
     */
    @Test
    fun testFlowFilterAndTransform() = runTest {
        val simulator = NewsFeedSimulator()
        // Ambil 1 item pertama dari kategori Tech
        val result = simulator.getFilteredAndTransformedNews("Tech").take(1).toList()
        
        assertTrue(result.isNotEmpty())
        assertTrue(result.first().displayString.contains("[Tech]"))
    }

    /**
     * Test 2: Menguji StateFlow saat markAsRead() dipanggil.
     */
    @Test
    fun testStateFlowMarkAsRead() {
        val simulator = NewsFeedSimulator()
        assertEquals(0, simulator.readCount.value)
        
        simulator.markAsRead()
        assertEquals(1, simulator.readCount.value)
    }

    /**
     * Test 3: Menguji coroutine async fetch detail berita jika sukses.
     */
    @Test
    fun testAsyncFetchDetailSuccess() = runTest {
        val simulator = NewsFeedSimulator()
        val detail = simulator.fetchNewsDetailAsync(1)
        assertTrue(detail.contains("Detail lengkap untuk berita 1"))
    }

    /**
     * Test 4: Menguji Exception Handling saat fetch detail berita menerima input tidak valid.
     */
    @Test
    fun testAsyncFetchDetailError() = runTest {
        val simulator = NewsFeedSimulator()
        val detail = simulator.fetchNewsDetailAsync(-1)
        assertTrue(detail.contains("Error mengambil detail"))
    }
}