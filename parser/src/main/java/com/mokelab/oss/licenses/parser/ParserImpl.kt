package com.mokelab.oss.licenses.parser

import android.content.Context
import androidx.annotation.RawRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal fun String.toLibraryOrNull(): Library? {
    val parts = split(" ", limit = 2)
    if (parts.size != 2) return null

    val offsetLength = parts[0].split(":")
    if (offsetLength.size != 2) return null

    val offset = offsetLength[0].toIntOrNull() ?: return null
    val length = offsetLength[1].toIntOrNull() ?: return null

    return Library(offset = offset, length = length, name = parts[1])
}

class ParserImpl(
    context: Context,
    @param:RawRes private val metadataRes: Int,
    @param:RawRes private val bodyRes: Int,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : Parser {
    private val appContext = context.applicationContext

    override suspend fun parse(): List<Library> = withContext(dispatcher) {
        appContext.resources.openRawResource(metadataRes)
            .bufferedReader(Charsets.UTF_8)
            .lineSequence()
            .mapNotNull { it.toLibraryOrNull() }
            .toList()
    }

    override suspend fun loadBody(library: Library): String = withContext(dispatcher) {
        appContext.resources.openRawResource(bodyRes).use { inputStream ->
            inputStream.skip(library.offset.toLong())
            val buffer = ByteArray(library.length)
            val readBytes = inputStream.read(buffer, 0, library.length)
            if (readBytes <= 0) return@withContext ""
            String(buffer, 0, readBytes, Charsets.UTF_8)
        }
    }
}