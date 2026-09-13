package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.MixingPack
import kotlinx.coroutines.flow.Flow

@Dao
interface MixingPackDao {
    @Query("SELECT * FROM mixing_packs ORDER BY isBestSeller DESC, id ASC")
    fun getAllPacks(): Flow<List<MixingPack>>

    @Query("SELECT * FROM mixing_packs WHERE category = :category ORDER BY id ASC")
    fun getPacksByCategory(category: String): Flow<List<MixingPack>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPack(pack: MixingPack): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(packs: List<MixingPack>)

    @Query("SELECT COUNT(*) FROM mixing_packs")
    suspend fun getPackCount(): Int
}
