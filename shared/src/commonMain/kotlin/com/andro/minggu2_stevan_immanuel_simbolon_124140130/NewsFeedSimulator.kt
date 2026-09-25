package com.andro.minggu2_stevan_immanuel_simbolon_124140130

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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

    // 2 & 3. Filter dan Transform + Bonus: Error Handling dengan .catch
    // .catch menangkap exception dari upstream flow (filter/map) tanpa mematikan stream,
    // lalu memancarkan (emit) fallback DisplayNews bertanda [Error]
    fun getFilteredAndTransformedNews(category: String): Flow<DisplayNews> {
        return newsFlow
            .filter { it.category == category || category == "All" }
            .map { DisplayNews(it.id, "[${it.category}] ${it.title}") }
            .catch { e -> emit(DisplayNews(-1, "[Error] Gagal memuat berita: ${e.message}")) }
    }

    // 4. Update StateFlow
    fun markAsRead() {
        _readCount.value++
    }

    // 5. Async fetch detail + Bonus: Exception Handling dengan try-catch
    // Menggunakan coroutineScope + async/await untuk fetch concurrent.
    // try-catch mengamankan coroutine bila terjadi network failure atau ID invalid (newsId < 0)
    suspend fun fetchNewsDetailAsync(newsId: Int): String = try {
        coroutineScope {
            val deferred = async {
                delay(1000) // simulasi network
                if (newsId < 0) throw IllegalArgumentException("ID tidak valid")
                "Detail lengkap untuk berita $newsId. Diambil secara async."
            }
            deferred.await()
        }
    } catch (e: Exception) {
        "Error mengambil detail: ${e.message}"
    }
}
