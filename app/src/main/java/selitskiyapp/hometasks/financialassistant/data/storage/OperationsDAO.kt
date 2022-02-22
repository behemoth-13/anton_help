package selitskiyapp.hometasks.financialassistant.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import selitskiyapp.hometasks.financialassistant.data.storage.models.OperationEntity
import selitskiyapp.hometasks.financialassistant.data.storage.models.OperationWithMoneyHolderEntity

@Dao
interface OperationsDAO {
//    @Query("SELECT*FROM operations")
//    fun getOperations(): Flow<List<OperationEntity>>
//    "SELECT * FROM book " +
//    "INNER JOIN loan ON loan.book_id = book.id " +
//    "INNER JOIN user ON user.id = loan.user_id " +
//    "WHERE user.name LIKE :userName"
//@Query(
//    "SELECT user.name AS userName, book.name AS bookName " +
//            "FROM user, book " +
//            "WHERE user.id = book.user_id"
//)
//@Query("""
//    SELECT * FROM tasks
//    INNER JOIN images as img_ ON tasks.id = img_.id
//""")
@Query("SELECT SUM(value) FROM operations")
fun getBalance(): Flow<Long>

    @Query("SELECT * FROM moneyHolder " +
            "JOIN operations as emb_ ON moneyHolder.moneyId = emb_.moneyHolderId"
    )
    fun getOperations(): Flow<List<OperationWithMoneyHolderEntity>>

    @Query("SELECT*FROM operations WHERE id = :id")
    fun getOperationById(id: Int): OperationEntity

    @Insert
    fun addOperations(operationEntity: OperationEntity)

    @Update
    fun updateOperation(operationEntity: OperationEntity)

    @Query("DELETE FROM operations WHERE id = :id")
    fun deleteOperations(id: Int)
}