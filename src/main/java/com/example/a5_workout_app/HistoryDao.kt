package com.example.a5_workout_app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.concurrent.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Query("select * from `history-table`")
    fun fetchAllDates(): kotlinx.coroutines.flow.Flow<List<HistoryEntity>>

}