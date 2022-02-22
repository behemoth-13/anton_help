package selitskiyapp.hometasks.financialassistant.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import selitskiyapp.hometasks.financialassistant.data.storage.models.MoneyHolderEntity

@Dao
interface MoneyHolderDao {
    @Query("SELECT*FROM moneyHolder")
    suspend fun getMoneyHolders(): List<MoneyHolderEntity>

    @Query("SELECT*FROM moneyHolder WHERE moneyId = :id")
    suspend fun getMoneyHolderById(id: Int): MoneyHolderEntity

    @Query("SELECT SUM(balance) FROM moneyHolder")
    fun getBalance(): Flow<Long>

    @Insert
    suspend fun addMoneyHolder(moneyHolderEntity: MoneyHolderEntity)

    @Update
    suspend fun updateMoneyHolder(moneyHolderEntity: MoneyHolderEntity)

    @Query("DELETE FROM moneyHolder WHERE moneyId = :id")
    suspend fun deleteMoneyHolder(id: Int)
}