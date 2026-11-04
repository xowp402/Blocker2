package com.blocker.core.common

import java.net.IDN
import java.net.URI
import java.util.Locale

/**
 * Normalizes website addresses before comparison and storage.
 *
 * Supports:
 * - Domains
 * - Subdomains
 * - HTTP and HTTPS URLs
 * - URLs containing paths and query parameters
 * - Internationalized domain names
 * - IPv4 and IPv6 literals
 */
object DomainNormalizer {

    /**
     * Converts a domain or URL into a normalized hostname.
     *
     * Returns null when the input cannot be interpreted
     * as a valid domain or IP address.
     */
    fun normalize(input: String): String? {

        val value = input.trim()

        if (value.isEmpty()) return null

        if (value.any { it.isWhitespace() }) {
            return null
        }

        val uri = parseUri(value) ?: return null

        val hostname = uri.host ?: return null

        return normalizeHostname(hostname)
    }

    /**
     * Normalizes a hostname without a URL path.
     */
    fun normalizeHostname(hostname: String): String? {

        var value = hostname.trim()
            .trimEnd('.')
            .lowercase(Locale.ROOT)

        if (value.isEmpty()) return null

        if (value.startsWith("[") && value.endsWith("]")) {
            value = value.substring(1, value.length - 1)
        }

        if (value.contains(':')) {
            return if (isValidIpv6(value)) value else null
        }

        if (isValidIpv4(value)) {
            return value
        }

        if (value.length > 253) return null

        return try {

            val ascii = IDN.toASCII(
                value,
                IDN.USE_STD3_ASCII_RULES
            ).lowercase(Locale.ROOT)

            if (!isValidDomain(ascii)) {
                null
            } else {
                ascii
            }

        } catch (_: IllegalArgumentException) {
            null
        }
    }

    /**
     * Parses a URL or a domain entered without a scheme.
     */
    private fun parseUri(value: String): URI? {

        return try {

            val candidate = when {

                value.startsWith("http://", true) ||
                value.startsWith("https://", true) -> value

                value.startsWith("//") -> "https:$value"

                else -> "https://$value"
            }

            val uri = URI(candidate)

            if (uri.rawUserInfo != null) {
                return null
            }

            uri

        } catch (_: Exception) {
            null
        }
    }

    /**
     * Validates a regular DNS hostname.
     */
    private fun isValidDomain(domain: String): Boolean {

        if (domain.isEmpty() || domain.length > 253) {
            return false
        }

        if (!domain.contains('.')) {
            return false
        }

        val labels = domain.split('.')

        return labels.all { label ->

            label.isNotEmpty() &&
                label.length <= 63 &&
                label.firstOrNull()?.isLetterOrDigit() == true &&
                label.lastOrNull()?.isLetterOrDigit() == true &&
                label.all {
                    it.isLetterOrDigit() || it == '-'
                }
        }
    }

    /**
     * Basic IPv4 validation.
     */
    private fun isValidIpv4(address: String): Boolean {

        val parts = address.split('.')

        if (parts.size != 4) return false

        return parts.all { part ->

            if (part.isEmpty() || part.length > 3) {
                return@all false
            }

            if (part.length > 1 && part.startsWith('0')) {
                return@all false
            }

            val number = part.toIntOrNull()
                ?: return@all false

            number in 0..255
        }
    }

    /**
     * Validates IPv6 using Android's InetAddress parser.
     */
    private fun isValidIpv6(address: String): Boolean {

        return try {

            java.net.InetAddress
                .getByName(address)
                .let {
                    it is java.net.Inet6Address
                }

        } catch (_: Exception) {
            false
        }
    }

    /**
     * Checks whether two hostnames are equal.
     */
    fun areEqual(
        first: String,
        second: String
    ): Boolean {

        val normalizedFirst = normalizeHostname(first)
        val normalizedSecond = normalizeHostname(second)

        return normalizedFirst != null &&
            normalizedFirst == normalizedSecond
    }

    /**
     * Checks whether a hostname belongs to a domain rule.
     */
    fun matches(
        hostname: String,
        ruleDomain: String,
        includeSubdomains: Boolean
    ): Boolean {

        val normalizedHost =
            normalizeHostname(hostname) ?: return false

        val normalizedRule =
            normalizeHostname(ruleDomain) ?: return false

        if (normalizedHost == normalizedRule) {
            return true
        }

        if (!includeSubdomains) {
            return false
        }

        return normalizedHost.endsWith(
            ".$normalizedRule"
        )
    }
}
