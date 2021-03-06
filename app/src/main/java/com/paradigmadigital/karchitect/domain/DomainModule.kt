package com.paradigmadigital.karchitect.domain

import android.arch.persistence.room.Room
import android.content.Context
import com.paradigmadigital.karchitect.domain.db.ChannelsDao
import com.paradigmadigital.karchitect.domain.db.FeedDb
import com.paradigmadigital.karchitect.domain.db.ItemsDao
import com.paradigmadigital.karchitect.repository.ErrorLiveData
import com.paradigmadigital.karchitect.repository.FeedRepository
import com.paradigmadigital.karchitect.repository.RefreshUseCase
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class DomainModule() {

    @Singleton
    @Provides
    fun provideFeedDb(application: Context): FeedDb {
        return Room.databaseBuilder(application, FeedDb::class.java, "feed.db")
                .allowMainThreadQueries()
                .build()
    }

    @Singleton
    @Provides
    fun provideChannelsDao(db: FeedDb): ChannelsDao {
        return db.channelsDao()
    }

    @Singleton
    @Provides
    fun provideItemsDao(db: FeedDb): ItemsDao {
        return db.itemsDao()
    }

    @Singleton
    @Provides
    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    @Singleton
    @Provides
    fun provideRepository(itemsDao: ItemsDao,
                          channelsDao: ChannelsDao,
                          refreshUseCase: RefreshUseCase,
                          errorData: ErrorLiveData): FeedRepository {
        return FeedRepository(itemsDao, channelsDao, refreshUseCase, errorData)
    }
}
