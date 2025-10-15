package dev.gaddal.chirp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform