package com.blocker.core.common

import java.text.Normalizer
import java.util.Locale

/**
 * Normalizes Persian, Arabic and English text
 * before word detection and comparison.
 */
object TextNormalizer {

    private val arabicCharacters = mapOf(
        'ي' to 'ی',
        'ى' to 'ی',
        'ك' to 'ک',
        'أ' to 'ا',
        'إ' to 'ا',
        'آ' to 'ا',
        'ٱ' to 'ا',
        'ؤ' to 'و',
        'ئ' to 'ی'
    )

    private val persianDigits = mapOf(
        '۰' to '0',
        '۱' to '1',
        '۲' to '2',
        '۳' to '3',
        '۴' to '4',
        '۵' to '5',
        '۶' to '6',
        '۷' to '7',
        '۸' to '8',
        '۹' to '9'
    )

    private val arabicDigits = mapOf(
        '٠' to '0',
        '١' to '1',
        '٢' to '2',
        '٣' to '3',
        '٤' to '4',
        '٥' to '5',
        '٦' to '6',
        '٧' to '7',
        '٨' to '8',
        '٩' to '9'
    )

    /**
     * Normalizes text for reliable comparison.
     */
    fun normalize(text: String): String {

        val unicodeNormalized = Normalizer.normalize(
            text,
            Normalizer.Form.NFKC
        )

        val result = StringBuilder()

        for (character in unicodeNormalized) {

            when {

                // Arabic to Persian character conversion
                arabicCharacters.containsKey(character) -> {
                    result.append(arabicCharacters[character])
                }

                // Persian digits to English digits
                persianDigits.containsKey(character) -> {
                    result.append(persianDigits[character])
                }

                // Arabic digits to English digits
                arabicDigits.containsKey(character) -> {
                    result.append(arabicDigits[character])
                }

                // Remove ZWNJ and zero-width characters
                character == '\u200C' ||
                character == '\u200B' ||
                character == '\uFEFF' -> {
                    // Intentionally ignored
                }

                // Remove Arabic tatweel
                character == '\u0640' -> {
                    // Intentionally ignored
                }

                // Remove Arabic diacritics
                Character.getType(character) ==
                    Character.NON_SPACING_MARK -> {
                    // Intentionally ignored
                }

                // Normalize whitespace
                character.isWhitespace() -> {
                    result.append(' ')
                }

                // English case normalization
                else -> {
                    result.append(character)
                }
            }
        }

        return result
            .toString()
            .lowercase(Locale.ROOT)
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    /**
     * Normalizes a word or phrase entered by the user.
     */
    fun normalizeTerm(term: String): String {
        return normalize(term)
    }

    /**
     * Checks whether two texts are equal after normalization.
     */
    fun areEqual(first: String, second: String): Boolean {
        return normalize(first) == normalize(second)
    }

    /**
     * Checks whether normalized text contains
     * the normalized search phrase.
     *
     * Whole-word matching will be handled
     * by the word-filter engine.
     */
    fun containsNormalized(
        text: String,
        searchTerm: String
    ): Boolean {

        val normalizedText = normalize(text)
        val normalizedTerm = normalize(searchTerm)

        if (normalizedTerm.isBlank()) {
            return false
        }

        return normalizedText.contains(normalizedTerm)
    }
}
