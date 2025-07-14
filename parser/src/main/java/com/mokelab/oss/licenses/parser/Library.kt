package com.mokelab.oss.licenses.parser

/**
 * Represents a library.
 *
 * @property offset The starting position of the library in the source.
 * @property length The length of the library's license.
 * @property name The name of the library.
 */
data class Library(
    val offset: Int,
    val length: Int,
    val name: String,
)
