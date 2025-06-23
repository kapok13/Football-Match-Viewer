package com.vb.footballmatchviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vb.footballmatchviewer.data.local.model.FootballEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FootballDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fixture: FootballEntity)
    @Delete
    suspend fun delete(fixture: FootballEntity)
    @Query("SELECT * FROM fixtures")
    fun getAll(): Flow<List<FootballEntity>>
    @Query("DELETE FROM fixtures")
    suspend fun clear()
}
