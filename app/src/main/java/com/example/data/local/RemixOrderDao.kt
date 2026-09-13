package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RemixOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface RemixOrderDao {
    @Query("SELECT * FROM remix_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<RemixOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: RemixOrder): Long

    @Update
    suspend fun updateOrder(order: RemixOrder)
}
