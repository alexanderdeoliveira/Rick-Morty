package com.alexander.rickmorty.database

import androidx.paging.PagingSource
import androidx.room.*
import com.alexander.rickmorty.model.db.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>): LongArray

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%'")
    fun pagingSourceByQuery(query: String): PagingSource<Int, Character>

    @Query("SELECT * FROM characters WHERE status LIKE :status")
    fun pagingSourceByStatus(status: String): PagingSource<Int, Character>

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' AND status LIKE :status")
    fun pagingSourceByQueryAndStatus(query: String, status: String): PagingSource<Int, Character>

    @Query("SELECT * FROM characters")
    fun pagingSource(): PagingSource<Int, Character>

    @Query("DELETE FROM characters WHERE name LIKE :query")
    suspend fun deleteByQuery(query: String): Int
}
