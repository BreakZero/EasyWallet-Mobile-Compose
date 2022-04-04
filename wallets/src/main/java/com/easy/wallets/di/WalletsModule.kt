package com.easy.wallets.di

import android.app.Application
import androidx.room.Room
import com.easy.core.common.SecurityUtil
import com.easy.wallets.data.WalletDatabase
import com.easy.wallets.repository.WalletRepositoryImpl
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
object WalletsModule {
    @Provides
    @Singleton
    fun provideWalletDatabase(
        app: Application
    ): WalletDatabase {
        val passPhrase = SQLiteDatabase.getBytes(
            "multi".toCharArray()
        )
        val supportFactory = SupportFactory(passPhrase)
        return Room.databaseBuilder(app, WalletDatabase::class.java, "easy_wallet.db")
            .openHelperFactory(supportFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideWalletRepository(
        database: WalletDatabase
    ): WalletRepositoryImpl {
        return WalletRepositoryImpl(database.walletDao)
    }
}
