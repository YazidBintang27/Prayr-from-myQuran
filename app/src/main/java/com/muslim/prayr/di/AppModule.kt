package com.muslim.prayr.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.muslim.prayr.data.remote.services.ApiService
import com.muslim.prayr.utils.ApiConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   @Provides
   @Singleton
   fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
      return ChuckerInterceptor.Builder(context).build()
   }

   @Provides
   @Singleton
   fun provideOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
      return OkHttpClient.Builder()
         .addInterceptor(chuckerInterceptor)
         .build()
   }

   @Provides
   @Singleton
   fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
      return Retrofit.Builder()
         .baseUrl(ApiConstant.BASE_URL)
         .client(okHttpClient)
         .addConverterFactory(GsonConverterFactory.create())
         .build()
   }

   @Provides
   @Singleton
   fun provideApiService(retrofit: Retrofit): ApiService {
      return retrofit.create(ApiService::class.java)
   }

   @Provides
   @Singleton
   fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
      return LocationServices.getFusedLocationProviderClient(context)
   }
}