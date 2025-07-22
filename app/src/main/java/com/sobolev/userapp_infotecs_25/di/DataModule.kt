package com.sobolev.userapp_infotecs_25.di

import android.content.Context
import androidx.room.Room
import com.sobolev.userapp_infotecs_25.data.local.db.UsersDao
import com.sobolev.userapp_infotecs_25.data.local.db.UsersDataBase
import com.sobolev.userapp_infotecs_25.data.repository.UsersRepositoryImpl
import com.sobolev.userapp_infotecs_25.domain.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindUsersRepository(
        impl: UsersRepositoryImpl
    ): UsersRepository


    companion object {

        @Singleton
        @Provides
        fun provideDataBase(
            @ApplicationContext context: Context
        ): UsersDataBase {
            return Room.databaseBuilder(
                context = context,
                klass = UsersDataBase::class.java,
                name = "users.db"
            ).build()
        }

        @Singleton
        @Provides
        fun provideUsersDao(
            dataBase: UsersDataBase
        ): UsersDao {
            return dataBase.usersDao()
        }
    }
}