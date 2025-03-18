package com.muslim.prayr.di

import com.muslim.prayr.data.repository.Repository
import com.muslim.prayr.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

   @Binds
   @Singleton
   abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}