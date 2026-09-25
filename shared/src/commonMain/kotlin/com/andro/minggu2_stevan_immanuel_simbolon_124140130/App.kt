package com.andro.minggu2_stevan_immanuel_simbolon_124140130

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun App() {
    val simulator = remember { NewsFeedSimulator() }
    val scope = rememberCoroutineScope()

    val categories = listOf("All", "Tech", "Sports", "Politics")
    var selectedCategory by remember { mutableStateOf("All") }

    // Kumpulkan berita dari Flow yang sudah difilter & ditransform
    val newsList = remember { mutableStateListOf<DisplayNews>() }

    // Fitur 4 - StateFlow: jumlah berita dibaca
    val readCount by simulator.readCount.collectAsState()

    // Detail berita async
    var detailText by remember { mutableStateOf("") }
    var isLoadingDetail by remember { mutableStateOf(false) }

    // Fitur 1 - Collect Flow setiap kali selectedCategory berubah
    LaunchedEffect(selectedCategory) {
        newsList.clear()
        simulator.getFilteredAndTransformedNews(selectedCategory).collect { news ->
            newsList.add(0, news) // terbaru di atas
            if (newsList.size > 20) newsList.removeAt(newsList.lastIndex)
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding()
                .padding(12.dp)
        ) {
            // Header
            Text(
                "News Feed Simulator",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Fitur 4 - StateFlow: penghitung berita dibaca
            Text(
                "Berita dibaca: $readCount",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Fitur 2 - Filter kategori
            Text("Filter Kategori:", fontSize = 13.sp)
            Row(
                modifier = Modifier.padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

            // Fitur 5 - Detail async result
            if (detailText.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        detailText,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 13.sp
                    )
                }
            }
            if (isLoadingDetail) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            // Fitur 1 & 3 - Daftar berita (Flow + Transform)
            Text(
                "Berita terbaru (${newsList.size}):",
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(newsList, key = { it.id }) { news ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Fitur 3: displayString hasil transform
                            Text(
                                news.displayString,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                // Fitur 4: tandai dibaca -> update StateFlow
                                Button(
                                    onClick = { simulator.markAsRead() },
                                    contentPadding = PaddingValues(
                                        horizontal = 8.dp, vertical = 4.dp
                                    )
                                ) {
                                    Text("Baca", fontSize = 11.sp)
                                }
                                // Fitur 5: fetch detail async via coroutines
                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            isLoadingDetail = true
                                            detailText =
                                                simulator.fetchNewsDetailAsync(news.id)
                                            isLoadingDetail = false
                                        }
                                    },
                                    contentPadding = PaddingValues(
                                        horizontal = 8.dp, vertical = 4.dp
                                    )
                                ) {
                                    Text("Detail", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}