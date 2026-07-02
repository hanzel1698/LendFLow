package com.example.loantracker.ui.screens

import com.example.loantracker.data.PaymentEntity
import com.example.loantracker.data.RecordEntity

data class KgoaLedgerRow(
    val paymentId: Long,
    val date: Long,
    val amount: Double,
    val periodLengthDays: Long,
    val periodicInterestRate: Double,
    val interestAccrued: Double,
    val principalComponent: Double,
    val newPrincipalBalance: Double,
    val notes: String
)

data class KgoaSummary(
    val currentBalance: Double,
    val totalInterestAccrued: Double,
    val totalPrincipalPaid: Double,
    val ledgerRows: List<KgoaLedgerRow>
)

object KgoaCalculator {
    fun calculateLedger(record: RecordEntity, payments: List<PaymentEntity>): KgoaSummary {
        // Sort payments ascending by date
        val sortedPayments = payments.sortedBy { it.date }
        
        val rows = mutableListOf<KgoaLedgerRow>()
        var previousDate = record.startDate
        var previousPrincipal = record.principalAmount
        var totalInterest = 0.0
        var totalPrincipalPaid = 0.0

        val dailyRate = (record.interestRate / 100.0) / 365.0

        for (payment in sortedPayments) {
            val diffMs = payment.date - previousDate
            val days = java.lang.Math.round(diffMs.toDouble() / (1000.0 * 60.0 * 60.0 * 24.0)).coerceAtLeast(0)

            val interestAccrued = previousPrincipal * dailyRate * days
            val principalComponent = payment.amount - interestAccrued
            val newPrincipal = previousPrincipal - principalComponent

            rows.add(
                KgoaLedgerRow(
                    paymentId = payment.id,
                    date = payment.date,
                    amount = payment.amount,
                    periodLengthDays = days,
                    periodicInterestRate = dailyRate,
                    interestAccrued = interestAccrued,
                    principalComponent = principalComponent,
                    newPrincipalBalance = newPrincipal,
                    notes = payment.notes
                )
            )

            totalInterest += interestAccrued
            totalPrincipalPaid += principalComponent
            
            previousDate = payment.date
            previousPrincipal = newPrincipal
        }

        return KgoaSummary(
            currentBalance = previousPrincipal,
            totalInterestAccrued = totalInterest,
            totalPrincipalPaid = totalPrincipalPaid,
            ledgerRows = rows
        )
    }
}
