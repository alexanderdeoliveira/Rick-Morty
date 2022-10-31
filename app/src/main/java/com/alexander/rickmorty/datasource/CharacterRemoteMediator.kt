package com.alexander.rickmorty.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.alexander.rickmorty.CharacterApi
import com.alexander.rickmorty.database.AppDatabase
import com.alexander.rickmorty.database.CharacterDao
import com.alexander.rickmorty.database.RemoteKeyDao
import com.alexander.rickmorty.model.db.Character
import com.alexander.rickmorty.model.db.RemoteKey
import com.alexander.rickmorty.model.dto.toCharacter
import okio.IOException
import retrofit2.HttpException

@kotlin.OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
        private val characterApi: CharacterApi,
        private val characterDao: CharacterDao,
        private val remoteKeyDao: RemoteKeyDao,
        private val database: AppDatabase
) : RemoteMediator<Int, Character>() {

    private var nameFilter: String? = null
    private var statusFilter: String? = null

    override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, Character>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = nameFilter?.let { remoteKeyDao.remoteKeyByQuery(it) }
                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextKey
                }
            }

            val response = characterApi.getCharacters(page = loadKey)
            database.run {
                nameFilter?.let {
                    if (loadType == LoadType.REFRESH) {
                        remoteKeyDao.deleteByQuery(it)
                    }
                    remoteKeyDao.insertOrReplace(
                            RemoteKey(
                                    it,
                                    statusFilter,
                                    response.info.next?.substringAfter("page=")?.toInt()
                            )
                    )
                }

                characterDao.insertAll(response.results.map { it.toCharacter() })
            }

            MediatorResult.Success(
                    endOfPaginationReached = response.info.next == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    fun setFilters(name: String?, status: String?) {
        nameFilter = name
        statusFilter = status
    }
}
