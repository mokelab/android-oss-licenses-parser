package com.mokelab.oss.licenses.parser

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideParser(
        @ApplicationContext context: Context,
    ): Parser {
        return ParserImpl(
            context = context,
            metadataRes = R.raw.third_party_license_metadata,
            bodyRes = R.raw.third_party_licenses,
            dispatcher = Dispatchers.IO,
        )
    }
}