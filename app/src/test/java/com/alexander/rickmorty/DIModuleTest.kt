package com.alexander.rickmorty

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.alexander.rickmorty.di.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.koin.test.check.checkModules

@RunWith(RobolectricTestRunner::class)
class DIModuleTest : KoinComponent {

  private lateinit var application: Application

  @Before
  fun setUp() {
    application = ApplicationProvider.getApplicationContext()
    stopKoin()
  }

  @After
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun checkKoinDefinitions() {
    startKoin {
      androidContext(application)
      modules(listOf(
        daoModule,
        databaseModule,
        networkModule,
        viewModelModule,
        datasourceModule,
        repositoryModule
      ))
    }.checkModules()
  }
}