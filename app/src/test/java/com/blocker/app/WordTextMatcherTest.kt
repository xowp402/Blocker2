package com.blocker.app

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WordTextMatcherTest {
    @Test
    fun matchesPersianAndArabicLetterVariants() {
        assertTrue(WordTextMatcher.containsRule("كلمه ي", "کلمه ی"))
    }

    @Test
    fun ignoresArabicDiacritics() {
        assertTrue(WordTextMatcher.containsRule("سَلَام", "سلام"))
    }

    @Test
    fun treatsPersianHalfSpaceAsOptional() {
        assertTrue(WordTextMatcher.containsRule("میروم", "می‌روم"))
        assertTrue(WordTextMatcher.containsRule("می‌روم", "میروم"))
    }

    @Test
    fun doesNotMatchInsideLongerWord() {
        assertFalse(WordTextMatcher.containsRule("سلامت", "سلام"))
    }

    @Test
    fun matchesAtPunctuationBoundaries() {
        assertTrue(WordTextMatcher.containsRule("متن (سلام!) تمام شد", "سلام"))
    }

    @Test
    fun matchesEnglishWithoutCaseSensitivity() {
        assertTrue(WordTextMatcher.containsRule("OpenAI is here", "openai"))
    }

    @Test
    fun matchesPhrasesWithWhitespace() {
        assertTrue(WordTextMatcher.containsRule("این یک عبارت آزمایشی است", "عبارت آزمایشی"))
    }
}