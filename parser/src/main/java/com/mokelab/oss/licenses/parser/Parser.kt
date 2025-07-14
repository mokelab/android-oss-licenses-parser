package com.mokelab.oss.licenses.parser

/**
 * Interface for parsing libraries from a source.
 *
 * Implementations of this interface should provide the logic to parse
 * libraries and return a list of [Library] objects.
 */
interface Parser {
    /**
     * Parses the source and returns a list of libraries.
     *
     * @return A list of [Library] objects representing the parsed libraries.
     */
    suspend fun parse(): List<Library>
}