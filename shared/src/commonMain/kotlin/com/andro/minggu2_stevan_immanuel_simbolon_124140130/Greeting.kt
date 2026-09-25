package com.andro.minggu2_stevan_immanuel_simbolon_124140130

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}