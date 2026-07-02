package com.example.loantracker

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey

@Serializable data object DebtsLendingList : NavKey

@Serializable data class RecordDetail(val id: Long) : NavKey

@Serializable data class EmiDetail(val id: Long) : NavKey

@Serializable data class AddEditRecord(val id: Long = -1, val type: String = "DEBT") : NavKey

@Serializable data class AddEditEmi(val id: Long = -1) : NavKey
