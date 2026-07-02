package com.example.loantracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loantracker.data.DataRepository
import com.example.loantracker.data.EmiEntity
import com.example.loantracker.data.PaymentEntity
import com.example.loantracker.data.RecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardState(
    val netBalance: Double = 0.0,
    val totalBorrowed: Double = 0.0,
    val totalLent: Double = 0.0,
    val totalMonthlyEmi: Double = 0.0,
    val recentRecords: List<RecordEntity> = emptyList(),
    val activeEmis: List<EmiEntity> = emptyList(),
    val activeItems: List<RecordEntity> = emptyList()
)

class TrackerViewModel(private val repository: DataRepository) : ViewModel() {

    val records: StateFlow<List<RecordEntity>> = repository.getRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val emis: StateFlow<List<EmiEntity>> = repository.getEmis()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payments: StateFlow<List<PaymentEntity>> = repository.getAllPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dashboardState: StateFlow<DashboardState> = combine(records, emis, payments) { recordList, emiList, paymentList ->
        val activeRecords = recordList.filter { !it.isCompleted }
        val activeEmis = emiList.filter { !it.isCompleted }
        val activeItems = recordList.filter { it.isPhysicalItem && !it.isCompleted }

        var borrowed = 0.0
        var lent = 0.0

        activeRecords.forEach { record ->
            if (!record.isPhysicalItem) {
                val recordPayments = paymentList.filter { it.parentId == record.id && it.parentType == "RECORD" }
                val remainingBalance = if (record.type == "KGOA_LOAN") {
                    com.example.loantracker.ui.screens.KgoaCalculator.calculateLedger(record, recordPayments).currentBalance
                } else {
                    val totalPaid = recordPayments.sumOf { it.amount }
                    (record.principalAmount - totalPaid).coerceAtLeast(0.0)
                }

                if (record.type == "DEBT" || record.type == "LOAN" || record.type == "KGOA_LOAN") {
                    borrowed += remainingBalance
                } else if (record.type == "LEND") {
                    lent += remainingBalance
                }
            }
        }

        val totalMonthlyEmi = activeEmis.sumOf { it.monthlyPayment }

        DashboardState(
            netBalance = lent - borrowed,
            totalBorrowed = borrowed,
            totalLent = lent,
            totalMonthlyEmi = totalMonthlyEmi,
            recentRecords = recordList.take(5),
            activeEmis = activeEmis,
            activeItems = activeItems
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardState())

    // Records
    fun insertRecord(record: RecordEntity, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertRecord(record)
            onComplete(id)
        }
    }

    fun updateRecord(record: RecordEntity) {
        viewModelScope.launch {
            repository.updateRecord(record)
        }
    }

    fun deleteRecord(record: RecordEntity) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }

    fun getRecordById(id: Long): Flow<RecordEntity?> = repository.getRecordById(id)

    // EMIs
    fun insertEmi(emi: EmiEntity, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertEmi(emi)
            onComplete(id)
        }
    }

    fun updateEmi(emi: EmiEntity) {
        viewModelScope.launch {
            repository.updateEmi(emi)
        }
    }

    fun deleteEmi(emi: EmiEntity) {
        viewModelScope.launch {
            repository.deleteEmi(emi)
        }
    }

    fun getEmiById(id: Long): Flow<EmiEntity?> = repository.getEmiById(id)

    // Payments
    fun getPayments(parentId: Long, parentType: String): Flow<List<PaymentEntity>> =
        repository.getPayments(parentId, parentType)

    fun insertPayment(payment: PaymentEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertPayment(payment)
            
            // Adjust payments count in EMI or Record if needed
            if (payment.parentType == "EMI") {
                val emi = repository.getEmiByIdOneShot(payment.parentId)
                if (emi != null) {
                    val updatedEmi = emi.copy(
                        monthsPaid = (emi.monthsPaid + 1).coerceAtMost(emi.totalMonths),
                        isCompleted = emi.monthsPaid + 1 >= emi.totalMonths
                    )
                    repository.updateEmi(updatedEmi)
                }
            }
            onComplete()
        }
    }

    fun deletePayment(payment: PaymentEntity, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deletePayment(payment)
            
            // Revert EMI payments count
            if (payment.parentType == "EMI") {
                val emi = repository.getEmiByIdOneShot(payment.parentId)
                if (emi != null) {
                    val updatedEmi = emi.copy(
                        monthsPaid = (emi.monthsPaid - 1).coerceAtLeast(0),
                        isCompleted = false
                    )
                    repository.updateEmi(updatedEmi)
                }
            }
            onComplete()
        }
    }
}
