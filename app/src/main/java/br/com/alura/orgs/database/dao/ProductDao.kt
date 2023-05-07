package br.com.alura.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.alura.orgs.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun searchAll() : Flow<List<Product>>

    @Query("SELECT * FROM Product WHERE userId = :userId")
    fun searchAllUserProducts(userId: String) : Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg product: Product)

    @Delete
    suspend fun delete(vararg product: Product)

    @Query("SELECT * FROM Product WHERE uid = :id")
    suspend fun searchForId(id: Long) : Product?

    @Query("SELECT * FROM Product ORDER BY name ASC")
    suspend fun searchAllOrderByNameAsc() : List<Product>

    @Query("SELECT * FROM Product ORDER BY name DESC")
    suspend fun searchAllOrderByNameDesc() : List<Product>

    @Query("SELECT * FROM Product ORDER BY desc ASC")
    suspend fun searchAllOrderByDescAsc() : List<Product>

    @Query("SELECT * FROM Product ORDER BY desc DESC")
    suspend fun searchAllOrderByDescDesc() : List<Product>

    @Query("SELECT * FROM Product ORDER BY value ASC")
    suspend fun searchAllOrderByValueAsc() : List<Product>

    @Query("SELECT * FROM Product ORDER BY value DESC")
    suspend fun searchAllOrderByValueDesc() : List<Product>
}