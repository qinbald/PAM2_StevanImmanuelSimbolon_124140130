package com.andro.minggu2_stevan_immanuel_simbolon_124140130

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

data class News(val id: Int, val title: String, val category: String)
data class DisplayNews(val id: Int, val displayString: String)

class NewsFeedSimulator {
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    // 1. Flow simulasi berita tiap 2 detik
    val newsFlow: Flow<News> = flow {
        var id = 1
        val categories = listOf("Tech", "Sports", "Politics")
        while (true) {
            emit(News(id, "Berita $id", categories.random()))
            id++
            delay(2000)
        }
    }

    // 2 & 3. Filter dan Transform
    fun getFilteredAndTransformedNews(category: String): Flow<DisplayNews> {
        return newsFlow
            .filter { it.category == category || category == "All" }
            .map { DisplayNews(it.id, "[${it.category}] ${it.title}") }
    }

    // 4. Update StateFlow
    fun markAsRead() {
        _readCount.value++
    }

    // 5. Async fetch detail
    suspend fun fetchNewsDetailAsync(newsId: Int): String = coroutineScope {
        val deferred = async {
            delay(1000) // simulasi network
            "Detail lengkap untuk berita $newsId. Diambil secara async."
        }
        deferred.await()
    }
}
