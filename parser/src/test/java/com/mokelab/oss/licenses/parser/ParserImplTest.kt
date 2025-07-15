package com.mokelab.oss.licenses.parser

import android.content.Context
import android.content.res.Resources
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayInputStream

class ParserImplTest : StringSpec({
    "loadBody reads correct offset and length with real InputStream" {
        val context = mockk<Context>(relaxed = true)
        val resources = mockk<Resources>(relaxed = true)
        val data = "HelloWorld".toByteArray(Charsets.UTF_8)
        val inputStream = ByteArrayInputStream(data)

        every { context.applicationContext } returns context
        every { context.resources } returns resources
        every { resources.openRawResource(123) } returns inputStream

        val library = Library(offset = 5, length = 5, name = "lib")

        val parser = ParserImpl(context, 0, 123)
        val result = runBlocking { parser.loadBody(library) }
        result shouldBe "World"
    }

    "loadBody returns empty string if nothing read with real InputStream" {
        val context = mockk<Context>(relaxed = true)
        val resources = mockk<Resources>(relaxed = true)
        val data = ByteArray(0)
        val inputStream = ByteArrayInputStream(data)

        every { context.applicationContext } returns context
        every { context.resources } returns resources
        every { resources.openRawResource(123) } returns inputStream

        val library = Library(offset = 0, length = 4, name = "lib")

        val parser = ParserImpl(context, 0, 123)
        val result = runBlocking { parser.loadBody(library) }
        result shouldBe ""
    }

    "loadBody reads from third_party_licenses content" {
        val context = mockk<Context>(relaxed = true)
        val resources = mockk<Resources>(relaxed = true)
        // 2行分のライセンステキストを用意
        val licensesText = """
            http://www.apache.org/licenses/LICENSE-2.0.txt
            https://www.apache.org/licenses/LICENSE-2.0.txt
        """.trimIndent().replace("\n", System.lineSeparator())
        val data = licensesText.toByteArray(Charsets.UTF_8)
        val inputStream = ByteArrayInputStream(data)

        every { context.applicationContext } returns context
        every { context.resources } returns resources
        every { resources.openRawResource(123) } returns inputStream

        // 1行目の先頭から46文字を読む（実際の third_party_license_metadata の例に合わせる）
        val library = Library(offset = 0, length = 46, name = "Saved State")

        val parser = ParserImpl(context, 0, 123)
        val result = runBlocking { parser.loadBody(library) }
        // 期待値は1行目のURL
        val expected = "http://www.apache.org/licenses/LICENSE-2.0.txt"
        result shouldBe expected
    }
})
