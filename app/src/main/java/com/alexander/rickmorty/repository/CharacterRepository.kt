package com.alexander.rickmorty.repository

import androidx.paging.*
import com.alexander.rickmorty.database.CharacterDao
import com.alexander.rickmorty.datasource.CharacterRemoteMediator
import com.alexander.rickmorty.model.db.Character
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class CharacterRepository(
        private val remoteMediator: CharacterRemoteMediator,
        private val characterDao: CharacterDao
) {

    fun getCharacters(nameFilter: String?, statusFilter: String?): Flow<PagingData<Character>> {
        remoteMediator.setFilters(nameFilter, statusFilter)
        return Pager(
                config = PagingConfig(
                        pageSize = 2,
                        enablePlaceholders = false,
                        initialLoadSize = 1
                ),
                remoteMediator = remoteMediator
        ) {
            if (!nameFilter.isNullOrEmpty() && !statusFilter.isNullOrEmpty()) {
                characterDao.pagingSourceByQueryAndStatus(query = nameFilter, status = statusFilter)
            } else if (!nameFilter.isNullOrEmpty()) {
                characterDao.pagingSourceByQuery(query = nameFilter)
            } else if (!statusFilter.isNullOrEmpty()) {
                characterDao.pagingSourceByStatus(status = statusFilter)
            } else {
                characterDao.pagingSource()
            }
        }.flow
    }
}
