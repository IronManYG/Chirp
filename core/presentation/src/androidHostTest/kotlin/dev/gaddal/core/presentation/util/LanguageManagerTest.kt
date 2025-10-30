package dev.gaddal.core.presentation.util

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LanguageManagerTest {

    @Test
    fun `isSupported respects normalization`() {
        val manager = LanguageManager(supportedLanguages = setOf("en", "ar"), defaultLanguage = "en")
        assertTrue(manager.isSupported("EN"))
        assertTrue(manager.isSupported("ar-EG"))
        assertFalse(manager.isSupported("fr"))
    }

    @Test
    fun `setLanguage normalizes and applies`() {
        val manager = LanguageManager(supportedLanguages = setOf("en", "ar"), defaultLanguage = "en")
        val okUpper = manager.setLanguage("EN")
        assertTrue(okUpper)
        assertEquals("en", manager.currentLanguage)

        val okRegional = manager.setLanguage("ar-EG")
        assertTrue(okRegional)
        assertEquals("ar", manager.currentLanguage)
    }

    @Test
    fun `setLanguage rejects unsupported and keeps current`() {
        val manager = LanguageManager(supportedLanguages = setOf("en", "ar"), defaultLanguage = "en")
        val before = manager.currentLanguage
        val ok = manager.setLanguage("fr")
        assertFalse(ok)
        assertEquals(before, manager.currentLanguage)
    }

    @Test
    fun `initialLanguage respected when valid and normalized when needed`() {
        val managerLower = LanguageManager(
            supportedLanguages = setOf("en", "ar"),
            defaultLanguage = "en",
            initialLanguage = "AR"
        )
        assertEquals("ar", managerLower.currentLanguage)

        val managerFallback = LanguageManager(
            supportedLanguages = setOf("en", "ar"),
            defaultLanguage = "en",
            initialLanguage = null
        )
        assertEquals("en", managerFallback.currentLanguage)
    }
}
