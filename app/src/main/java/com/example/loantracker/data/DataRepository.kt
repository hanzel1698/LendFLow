package com.example.loantracker.data

import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getRecords(): Flow<List<RecordEntity>>
    fun getRecordById(id: Long): Flow<RecordEntity?>
    suspend fun getRecordByIdOneShot(id: Long): RecordEntity?
    suspend fun insertRecord(record: RecordEntity): Long
    suspend fun updateRecord(record: RecordEntity)
    suspend fun deleteRecord(record: RecordEntity)

    fun getEmis(): Flow<List<EmiEntity>>
    fun getEmiById(id: Long): Flow<EmiEntity?>
    suspend fun getEmiByIdOneShot(id: Long): EmiEntity?
    suspend fun insertEmi(emi: EmiEntity): Long
    suspend fun updateEmi(emi: EmiEntity)
    suspend fun deleteEmi(emi: EmiEntity)

    fun getPayments(parentId: Long, parentType: String): Flow<List<PaymentEntity>>
    suspend fun getPaymentsOneShot(parentId: Long, parentType: String): List<PaymentEntity>
    suspend fun insertPayment(payment: PaymentEntity): Long
    suspend fun deletePayment(payment: PaymentEntity)
    fun getAllPayments(): Flow<List<PaymentEntity>>
}

class DefaultDataRepository(private val dao: TrackerDao) : DataRepository {
    override fun getRecords(): Flow<List<RecordEntity>> = dao.getRecordsFlow()
    override fun getRecordById(id: Long): Flow<RecordEntity?> = dao.getRecordByIdFlow(id)
    override suspend fun getRecordByIdOneShot(id: Long): RecordEntity? = dao.getRecordById(id)
    override suspend fun insertRecord(record: RecordEntity): Long = dao.insertRecord(record)
    override suspend fun updateRecord(record: RecordEntity) = dao.updateRecord(record)
    override suspend fun deleteRecord(record: RecordEntity) = dao.deleteRecord(record)

    override fun getEmis(): Flow<List<EmiEntity>> = dao.getEmisFlow()
    override fun getEmiById(id: Long): Flow<EmiEntity?> = dao.getEmiByIdFlow(id)
    override suspend fun getEmiByIdOneShot(id: Long): EmiEntity? = dao.getEmiById(id)
    override suspend fun insertEmi(emi: EmiEntity): Long = dao.insertEmi(emi)
    override suspend fun updateEmi(emi: EmiEntity) = dao.updateEmi(emi)
    override suspend fun deleteEmi(emi: EmiEntity) = dao.deleteEmi(emi)

    override fun getPayments(parentId: Long, parentType: String): Flow<List<PaymentEntity>> =
        dao.getPaymentsForParentFlow(parentId, parentType)
    override suspend fun getPaymentsOneShot(parentId: Long, parentType: String): List<PaymentEntity> =
        dao.getPaymentsForParent(parentId, parentType)
    override suspend fun insertPayment(payment: PaymentEntity): Long = dao.insertPayment(payment)
    override suspend fun deletePayment(payment: PaymentEntity) = dao.deletePayment(payment)
    override fun getAllPayments(): Flow<List<PaymentEntity>> = dao.getAllPaymentsFlow()
}
