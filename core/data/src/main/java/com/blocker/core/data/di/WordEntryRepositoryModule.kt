package com.blocker.core.data.di

import com.blocker.core.data.repository.WordEntryRepository
import com.blocker.core.data.repository.WordEntryRepositoryImpl
import org.koin.dsl.module

val wordEntryRepositoryModule = module {

    single<WordEntryRepository> {
        WordEntryRepositoryImpl(
            dao = get()
        )
    }
}
