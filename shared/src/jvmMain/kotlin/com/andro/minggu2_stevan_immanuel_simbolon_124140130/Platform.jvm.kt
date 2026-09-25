package com.andro.minggu2_stevan_immanuel_simbolon_124140130

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()