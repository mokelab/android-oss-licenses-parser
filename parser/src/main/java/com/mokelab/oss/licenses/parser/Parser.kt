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

    /**
     * Loads the body of a library.
     *
     * This method can be overridden to provide a custom implementation for loading
     * the body of a library based on its [Library] object.
     *
     * @param library The [Library] object for which to load the body.
     * @return The body of the library as a [String].
     */
    suspend fun loadBody(library: Library): String
}