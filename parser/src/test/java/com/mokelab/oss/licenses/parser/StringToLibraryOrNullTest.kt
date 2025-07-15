package com.mokelab.oss.licenses.parser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StringToLibraryOrNullTest : StringSpec({
    "parses valid line" {
        val line = "10:20 library-name"
        val lib = line.toLibraryOrNull()
        lib?.offset shouldBe 10
        lib?.length shouldBe 20
        lib?.name shouldBe "library-name"
    }

    "returns null for missing space" {
        val line = "10:20library-name"
        line.toLibraryOrNull() shouldBe null
    }

    "returns null for missing colon" {
        val line = "1020 library-name"
        line.toLibraryOrNull() shouldBe null
    }

    "returns null for non-integer offset" {
        val line = "a:20 library-name"
        line.toLibraryOrNull() shouldBe null
    }

    "returns null for non-integer length" {
        val line = "10:b library-name"
        line.toLibraryOrNull() shouldBe null
    }

    "returns null for too few parts" {
        val line = "10:20"
        line.toLibraryOrNull() shouldBe null
    }

    "library name has a space" {
        val line = "1:2 my library"
        val lib = line.toLibraryOrNull()
        lib?.offset shouldBe 1
        lib?.length shouldBe 2
        lib?.name shouldBe "my library"
    }
})
