package com.alexander.rickmorty

import androidx.paging.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexander.rickmorty.database.AppDatabase
import com.alexander.rickmorty.database.CharacterDao
import com.alexander.rickmorty.database.RemoteKeyDao
import com.alexander.rickmorty.model.db.Character
import com.alexander.rickmorty.model.dto.CharacterDto
import com.alexander.rickmorty.model.dto.CharactersResponse
import com.alexander.rickmorty.model.dto.InfoResponse
import com.alexander.rickmorty.datasource.CharacterRemoteMediator
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import retrofit2.HttpException
import kotlin.test.assertTrue

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CharacterRemoteMediatorTest: AutoCloseKoinTest() {

  @MockK
  private val characterApi = mockk<CharacterApi>()

  @MockK
  private val characterDao = mockk<CharacterDao>()

  @MockK
  private val remoteKeyDao = mockk<RemoteKeyDao>()

  @MockK
  private val database = mockk<AppDatabase>()

  @MockK
  private val httpError = mockk<HttpException>()

  private val characterResponse = CharactersResponse(
    info = InfoResponse(3, null),
    results = listOf(
      CharacterDto(
        id = CharacterMapperTest.ID,
        name = CharacterMapperTest.NAME,
        status = CharacterMapperTest.STATUS,
        species = CharacterMapperTest.SPECIES,
        type = CharacterMapperTest.TYPE,
        gender = CharacterMapperTest.GENDER,
        episode = null,
        image = CharacterMapperTest.IMAGE,
        url = CharacterMapperTest.URL,
        created = CharacterMapperTest.CREATED
      ),
      CharacterDto(
        id = CharacterMapperTest.ID,
        name = CharacterMapperTest.NAME,
        status = CharacterMapperTest.STATUS,
        species = CharacterMapperTest.SPECIES,
        type = CharacterMapperTest.TYPE,
        gender = CharacterMapperTest.GENDER,
        episode = null,
        image = CharacterMapperTest.IMAGE,
        url = CharacterMapperTest.URL,
        created = CharacterMapperTest.CREATED
      )
  ))

  @Test
  fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
    coEvery { characterApi.getCharacters() } returns characterResponse
    coEvery { characterDao.insertAll(any()) } returns LongArray(2)

    val remoteMediator = CharacterRemoteMediator(
      characterApi,
      characterDao,
      remoteKeyDao,
      database
    )
    val pagingState = PagingState<Int, Character>(
      listOf(),
      null,
      PagingConfig(10),
      10
    )
    val result = remoteMediator.load(LoadType.REFRESH, pagingState)
    assertTrue { result is RemoteMediator.MediatorResult.Success }
    assertTrue { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }
  }

  @Test
  fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
    coEvery { characterApi.getCharacters() } returns characterResponse
    coEvery { characterDao.insertAll(any()) } returns LongArray(2)

    val remoteMediator = CharacterRemoteMediator(
      characterApi,
      characterDao,
      remoteKeyDao,
      database
    )
    val pagingState = PagingState<Int, Character>(
      listOf(),
      null,
      PagingConfig(10),
      10
    )
    val result = remoteMediator.load(LoadType.REFRESH, pagingState)
    assertTrue { result is RemoteMediator.MediatorResult.Success }
    assertTrue { (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached }
  }

  @Test
  fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runBlocking {
    coEvery { characterApi.getCharacters() } throws httpError
    coEvery { characterDao.insertAll(any()) } returns LongArray(2)

    val remoteMediator = CharacterRemoteMediator(
      characterApi,
      characterDao,
      remoteKeyDao,
      database
    )
    val pagingState = PagingState<Int, Character>(
      listOf(),
      null,
      PagingConfig(10),
      10
    )

    val result = remoteMediator.load(LoadType.REFRESH, pagingState)
    assertTrue {result is RemoteMediator.MediatorResult.Error }
  }
}