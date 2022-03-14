package com.easy.intro.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.easy.intro.local.WalletDatabase
import com.easy.intro.repository.WalletRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WalletSingletonModule {
    @Provides
    @Singleton
    fun provideWalletDatabase(app: Application): WalletDatabase {
        // better to use the password from the user setup in the SP
        val supportFactory = SupportFactory(SQLiteDatabase.getBytes("multi".toCharArray()))
        return Room.databaseBuilder(app, WalletDatabase::class.java, "easy_wallet.db")
            .openHelperFactory(supportFactory)
            .build()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object WalletViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideWalletRepository(
        database: WalletDatabase
    ): WalletRepositoryImpl {
        return WalletRepositoryImpl(database.walletDao)
    }
}