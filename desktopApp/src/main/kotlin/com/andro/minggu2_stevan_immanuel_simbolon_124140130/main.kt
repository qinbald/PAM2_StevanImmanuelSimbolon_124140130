package com.andro.minggu2_stevan_immanuel_simbolon_124140130

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Minggu2_Stevan_Immanuel_Simbolon_124140130",
    ) {
        App()
    }
}