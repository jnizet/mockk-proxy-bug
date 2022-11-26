package com.example

import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.reflect.Proxy

interface ExampleRepository {
    fun find(id: String): String
}

class ExampleRepositoryImpl: ExampleRepository {
    override fun find(id: String) = "Hello $id"
}

class ProxyBugTest {
    val realRepo = ExampleRepositoryImpl()

    val proxyRepo = Proxy.newProxyInstance(
        this::class.java.classLoader,
        arrayOf(ExampleRepository::class.java)) { _, _, _ ->
        "Test"
    } as ExampleRepository

    @Test
    // passes
    fun `real repo should work fine`() {
        assertEquals("Hello world", realRepo.find("world"))
    }

    @Test
    // passes
    fun `spied real repo should work fine`() {
        val spiedRealRepo = spyk(realRepo)
        assertEquals("Hello world", spiedRealRepo.find("world"))
    }

    @Test
    // passes
    fun `proxy repo should work fine`() {
        assertEquals("Test", proxyRepo.find("world"))
    }

    @Test
    // fails
    fun `spied proxy repo should work fine`() {
        val spiedProxyRepo = spyk(proxyRepo)
        assertEquals("Test", spiedProxyRepo.find("world"))
    }
}
