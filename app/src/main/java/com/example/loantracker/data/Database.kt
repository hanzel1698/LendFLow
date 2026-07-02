package com.example.loantracker.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "DEBT", "LEND", "LOAN"
    val title: String,
    val counterparty: String,
    val principalAmount: Double,
    val interestRate: Double,
    val startDate: Long,
    val dueDate: Long?,
    val notes: String,
    val isCompleted: Boolean = false,
    val isPhysicalItem: Boolean = false
)

@Entity(tableName = "emis")
data class EmiEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cardName: String,
    val purchaseItem: String,
    val totalAmount: Double,
    val monthlyPayment: Double,
    val totalMonths: Int,
    val monthsPaid: Int,
    val startDate: Long,
    val statementDate: Int,
    val notes: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val parentId: Long,
    val parentType: String, // "RECORD", "EMI"
    val amount: Double,
    val date: Long,
    val notes: String
)

@Dao
interface TrackerDao {
    @Query("SELECT * FROM records ORDER BY startDate DESC")
    fun getRecordsFlow(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE id = :id")
    fun getRecordByIdFlow(id: Long): Flow<RecordEntity?>

    @Query("SELECT * FROM records WHERE id = :id")
    suspend fun getRecordById(id: Long): RecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: RecordEntity): Long

    @Update
    suspend fun updateRecord(record: RecordEntity)

    @Delete
    suspend fun deleteRecord(record: RecordEntity)

    @Query("SELECT * FROM emis ORDER BY startDate DESC")
    fun getEmisFlow(): Flow<List<EmiEntity>>

    @Query("SELECT * FROM emis WHERE id = :id")
    fun getEmiByIdFlow(id: Long): Flow<EmiEntity?>

    @Query("SELECT * FROM emis WHERE id = :id")
    suspend fun getEmiById(id: Long): EmiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmi(emi: EmiEntity): Long

    @Update
    suspend fun updateEmi(emi: EmiEntity)

    @Delete
    suspend fun deleteEmi(emi: EmiEntity)

    @Query("SELECT * FROM payments WHERE parentId = :parentId AND parentType = :parentType ORDER BY date DESC")
    fun getPaymentsForParentFlow(parentId: Long, parentType: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE parentId = :parentId AND parentType = :parentType")
    suspend fun getPaymentsForParent(parentId: Long, parentType: String): List<PaymentEntity>

    @Query("SELECT * FROM payments ORDER BY date DESC")
    fun getAllPaymentsFlow(): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Delete
    suspend fun deletePayment(payment: PaymentEntity)
}

@Database(
    entities = [RecordEntity::class, EmiEntity::class, PaymentEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TrackerDatabase : RoomDatabase() {
    abstract val dao: TrackerDao

    companion object {
        @Volatile
        private var INSTANCE: TrackerDatabase? = null

        fun getDatabase(context: Context): TrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackerDatabase::class.java,
                    "tracker_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
