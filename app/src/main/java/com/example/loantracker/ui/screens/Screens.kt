package com.example.loantracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.loantracker.AddEditEmi
import com.example.loantracker.AddEditRecord
import com.example.loantracker.DebtsLendingList
import com.example.loantracker.EmiDetail
import com.example.loantracker.RecordDetail
import com.example.loantracker.data.EmiEntity
import com.example.loantracker.data.PaymentEntity
import com.example.loantracker.data.RecordEntity
import com.example.loantracker.theme.ColorDebt
import com.example.loantracker.theme.ColorEmi
import com.example.loantracker.theme.ColorLend
import com.example.loantracker.theme.ColorLoan
import com.example.loantracker.ui.TrackerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Utilities
fun formatCurrency(value: Double): String = "₹%.2f".format(value)

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: TrackerViewModel,
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val dashboardState by viewModel.dashboardState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "LendFlow",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Net Balance Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Net Balance",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            formatCurrency(dashboardState.netBalance),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (dashboardState.netBalance >= 0) ColorLend else ColorDebt
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.TrendingUp,
                                        contentDescription = "Lent",
                                        tint = ColorLend,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Lent (Owed to me)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(
                                    formatCurrency(dashboardState.totalLent),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorLend
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(32.dp)
                                    .background(MaterialTheme.colorScheme.outline)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.TrendingDown,
                                        contentDescription = "Borrowed",
                                        tint = ColorDebt,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Owed (Debts/Loans)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(
                                    formatCurrency(dashboardState.totalBorrowed),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorDebt
                                )
                            }
                        }
                    }
                }
            }

            // Monthly EMI outflow summary card
            if (dashboardState.totalMonthlyEmi > 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CreditCard,
                                contentDescription = "EMI Outflow",
                                tint = ColorEmi,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Monthly EMI Outflow", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(formatCurrency(dashboardState.totalMonthlyEmi), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Quick Actions Title
            item {
                Text(
                    "Quick Actions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Quick Actions Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onNavigate(AddEditRecord(type = "DEBT")) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorDebt),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.TrendingDown, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Debt", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onNavigate(AddEditRecord(type = "LEND")) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorLend),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Lend", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onNavigate(AddEditEmi()) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorEmi),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add EMI", fontSize = 12.sp)
                    }
                }
            }

            // View all button
            item {
                OutlinedButton(
                    onClick = { onNavigate(DebtsLendingList) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View All Debts, Lending & EMIs")
                }
            }

            // Borrowed & Lent Items
            if (dashboardState.activeItems.isNotEmpty()) {
                item {
                    Text(
                        "Borrowed & Lent Items",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                items(dashboardState.activeItems) { record ->
                    RecordRow(record = record, onClick = { onNavigate(RecordDetail(record.id)) })
                }
            }

            // Recent items title
            item {
                Text(
                    "Recent Activity",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (dashboardState.recentRecords.isEmpty() && dashboardState.activeEmis.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No records found. Add some above!",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            items(dashboardState.recentRecords) { record ->
                RecordRow(record = record, onClick = { onNavigate(RecordDetail(record.id)) })
            }

            items(dashboardState.activeEmis) { emi ->
                EmiRow(emi = emi, onClick = { onNavigate(EmiDetail(emi.id)) })
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun RecordRow(record: RecordEntity, onClick: () -> Unit) {
    val (color, icon) = if (record.isPhysicalItem) {
        Pair(if (record.type == "LEND") ColorLend else ColorDebt, Icons.Default.Category)
    } else {
        when (record.type) {
            "LEND" -> Pair(ColorLend, Icons.Default.TrendingUp)
            "DEBT" -> Pair(ColorDebt, Icons.Default.TrendingDown)
            else -> Pair(ColorLoan, Icons.Default.Payments)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    record.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    record.counterparty,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    if (record.isPhysicalItem) "Qty: ${record.principalAmount.toInt()}" else formatCurrency(record.principalAmount),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = color
                )
                Text(
                    if (record.isPhysicalItem) {
                        if (record.type == "LEND") "LENT ITEM" else "BORROWED ITEM"
                    } else {
                        if (record.type == "KGOA_LOAN") "KGOA LOAN" else record.type
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = color.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun EmiRow(emi: EmiEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ColorEmi.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CreditCard, contentDescription = null, tint = ColorEmi, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    emi.purchaseItem,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    emi.cardName,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                val progress = if (emi.totalMonths > 0) emi.monthsPaid.toFloat() / emi.totalMonths else 0f
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = ColorEmi,
                    trackColor = ColorEmi.copy(alpha = 0.2f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    formatCurrency(emi.monthlyPayment),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = ColorEmi
                )
                Text(
                    "${emi.monthsPaid}/${emi.totalMonths} paid",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsLendingListScreen(
    viewModel: TrackerViewModel,
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val records by viewModel.records.collectAsState()
    val emis by viewModel.emis.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0 = Records, 1 = EMIs

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Records", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Debts, Lends & Loans") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Credit Card EMIs") }
                )
            }

            if (selectedTab == 0) {
                if (records.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No loans or debts logged", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(records) { record ->
                            RecordRow(record = record, onClick = { onNavigate(RecordDetail(record.id)) })
                        }
                    }
                }
            } else {
                if (emis.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No credit card EMIs logged", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(emis) { emi ->
                            EmiRow(emi = emi, onClick = { onNavigate(EmiDetail(emi.id)) })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDetailScreen(
    recordId: Long,
    viewModel: TrackerViewModel,
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val recordState by viewModel.getRecordById(recordId).collectAsState(initial = null)
    val payments by viewModel.getPayments(recordId, "RECORD").collectAsState(initial = emptyList())

    var showPaymentDialog by remember { mutableStateOf(false) }

    val record = recordState ?: return // Show nothing if loading or not found

    val isPhysicalItem = record.isPhysicalItem
    val (accentColor, icon) = if (isPhysicalItem) {
        Pair(if (record.type == "LEND") ColorLend else ColorDebt, Icons.Default.Category)
    } else {
        when (record.type) {
            "LEND" -> Pair(ColorLend, Icons.Default.TrendingUp)
            "DEBT" -> Pair(ColorDebt, Icons.Default.TrendingDown)
            else -> Pair(ColorLoan, Icons.Default.Payments)
        }
    }

    val isKgoa = record.type == "KGOA_LOAN"
    val kgoaSummary = remember(record, payments) {
        if (isKgoa) KgoaCalculator.calculateLedger(record, payments) else null
    }

    val totalPaid = payments.sumOf { it.amount }
    val remaining = if (isKgoa) kgoaSummary?.currentBalance ?: 0.0 else (record.principalAmount - totalPaid).coerceAtLeast(0.0)
    val progress = if (record.principalAmount > 0) {
        if (isKgoa) {
            val paidPrincipal = kgoaSummary?.totalPrincipalPaid ?: 0.0
            (paidPrincipal / record.principalAmount).toFloat().coerceIn(0f, 1f)
        } else {
            (totalPaid / record.principalAmount).toFloat()
        }
    } else 1f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(record.title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigate(AddEditRecord(id = record.id, type = record.type)) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = {
                        viewModel.deleteRecord(record)
                        onBack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorDebt)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (!isPhysicalItem && !record.isCompleted && remaining > 0) {
                FloatingActionButton(
                    onClick = { showPaymentDialog = true },
                    containerColor = accentColor,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Record Payment")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isPhysicalItem) {
                                    if (record.type == "LEND") "LENT ITEM" else "BORROWED ITEM"
                                } else if (isKgoa) {
                                    "KGOA LOAN"
                                } else {
                                    record.type
                                },
                                fontSize = 11.sp,
                                color = accentColor,
                                fontWeight = FontWeight.Bold
                            )
                            Text(record.counterparty, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(if (isPhysicalItem) "Quantity" else "Principal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(if (isPhysicalItem) record.principalAmount.toInt().toString() else formatCurrency(record.principalAmount), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        if (!isPhysicalItem) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Interest Rate", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${record.interestRate}% / year", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Status", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(if (record.isCompleted) "Completed (Returned)" else "Active", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (record.isCompleted) ColorLend else accentColor)
                            }
                        }
                    }

                    if (!isPhysicalItem) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Remaining Balance", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(formatCurrency(remaining), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total Paid", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(formatCurrency(totalPaid), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = accentColor,
                            trackColor = accentColor.copy(alpha = 0.2f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Start: ${formatDate(record.startDate)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        record.dueDate?.let {
                            Text("Due: ${formatDate(it)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    if (isKgoa && kgoaSummary != null && !isPhysicalItem) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Interest Accrued", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(formatCurrency(kgoaSummary.totalInterestAccrued), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorEmi)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Principal Component", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(formatCurrency(kgoaSummary.totalPrincipalPaid), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorLend)
                            }
                        }
                    }

                    if (record.notes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Notes: ${record.notes}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if ((remaining <= 0.0 || isPhysicalItem) && !record.isCompleted) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.updateRecord(record.copy(isCompleted = true)) },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorLend),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isPhysicalItem) "Mark as Returned" else "Mark as Completed")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!isPhysicalItem) {
                Text(if (isKgoa) "KGOA Amortization Ledger" else "Payment Logs", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                if (payments.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No payments recorded yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    if (isKgoa && kgoaSummary != null) {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(kgoaSummary.ledgerRows.reversed()) { row ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${formatDate(row.date)}  •  ${row.periodLengthDays} Days",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Paid: ${formatCurrency(row.amount)}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = accentColor
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                val originalPayment = payments.find { it.id == row.paymentId }
                                                if (originalPayment != null) {
                                                    IconButton(
                                                        onClick = { viewModel.deletePayment(originalPayment) },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Delete,
                                                            contentDescription = "Delete",
                                                            tint = ColorDebt,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text("Interest Accrued", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                Text(formatCurrency(row.interestAccrued), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorEmi)
                                            }
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("Principal Comp.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                Text(formatCurrency(row.principalComponent), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorLend)
                                            }
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text("New Balance", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                Text(formatCurrency(row.newPrincipalBalance), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accentColor)
                                            }
                                        }
                                        if (row.notes.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text("Notes: ${row.notes}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(payments) { payment ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                formatCurrency(payment.amount),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = accentColor
                                            )
                                            Text(
                                                formatDate(payment.date),
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            if (payment.notes.isNotEmpty()) {
                                                Text(payment.notes, fontSize = 11.sp)
                                            }
                                        }
                                        IconButton(onClick = { viewModel.deletePayment(payment) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorDebt)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Category,
                            contentDescription = null,
                            tint = accentColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (record.isCompleted) "This item has been returned." else "This item is currently active.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        if (showPaymentDialog) {
            AddPaymentDialog(
                parentId = recordId,
                parentType = "RECORD",
                onDismiss = { showPaymentDialog = false },
                onAdd = { amount, date, notes ->
                    viewModel.insertPayment(
                        PaymentEntity(
                            parentId = recordId,
                            parentType = "RECORD",
                            amount = amount,
                            date = date,
                            notes = notes
                        )
                    )
                    showPaymentDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmiDetailScreen(
    emiId: Long,
    viewModel: TrackerViewModel,
    onNavigate: (NavKey) -> Unit,
    onBack: () -> Unit
) {
    val emiState by viewModel.getEmiById(emiId).collectAsState(initial = null)
    val payments by viewModel.getPayments(emiId, "EMI").collectAsState(initial = emptyList())

    var showPaymentDialog by remember { mutableStateOf(false) }

    val emi = emiState ?: return

    val totalPaid = payments.sumOf { it.amount }
    val remaining = (emi.totalAmount - totalPaid).coerceAtLeast(0.0)
    val progress = if (emi.totalAmount > 0) (totalPaid / emi.totalAmount).toFloat() else 1f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(emi.purchaseItem, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigate(AddEditEmi(id = emi.id)) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = {
                        viewModel.deleteEmi(emi)
                        onBack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorDebt)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (!emi.isCompleted && emi.monthsPaid < emi.totalMonths) {
                FloatingActionButton(
                    onClick = { showPaymentDialog = true },
                    containerColor = ColorEmi,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Pay EMI")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // EMI Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CREDIT CARD EMI", fontSize = 11.sp, color = ColorEmi, fontWeight = FontWeight.Bold)
                            Text(emi.cardName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(ColorEmi.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CreditCard, contentDescription = null, tint = ColorEmi, modifier = Modifier.size(24.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Amount", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatCurrency(emi.totalAmount), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Monthly payment", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatCurrency(emi.monthlyPayment), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorEmi)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Remaining", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatCurrency(remaining), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = ColorEmi)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Progress", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${emi.monthsPaid} / ${emi.totalMonths} months", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ColorEmi,
                        trackColor = ColorEmi.copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Start: ${formatDate(emi.startDate)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Statement Day: ${emi.statementDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    if (emi.notes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Notes: ${emi.notes}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("EMI Payments Log", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (payments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No EMI payments recorded yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(payments) { payment ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        formatCurrency(payment.amount),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = ColorEmi
                                    )
                                    Text(
                                        formatDate(payment.date),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (payment.notes.isNotEmpty()) {
                                        Text(payment.notes, fontSize = 11.sp)
                                    }
                                }
                                IconButton(onClick = { viewModel.deletePayment(payment) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorDebt)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showPaymentDialog) {
            AddPaymentDialog(
                parentId = emiId,
                parentType = "EMI",
                onDismiss = { showPaymentDialog = false },
                defaultAmount = emi.monthlyPayment,
                onAdd = { amount, date, notes ->
                    viewModel.insertPayment(
                        PaymentEntity(
                            parentId = emiId,
                            parentType = "EMI",
                            amount = amount,
                            date = date,
                            notes = notes
                        )
                    )
                    showPaymentDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentDialog(
    parentId: Long,
    parentType: String,
    onDismiss: () -> Unit,
    defaultAmount: Double = 0.0,
    onAdd: (Double, Long, String) -> Unit
) {
    var amountText by remember { mutableStateOf(if (defaultAmount > 0) defaultAmount.toString() else "") }
    var notesText by remember { mutableStateOf("") }
    var paymentDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Record Payment",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        error = false
                    },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = error,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Date: ${formatDate(paymentDate)}")
                }

                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    label = { Text("Notes (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amount = amountText.toDoubleOrNull()
                            if (amount == null || amount <= 0) {
                                error = true
                            } else {
                                onAdd(amount, paymentDate, notesText)
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = paymentDate)
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { paymentDate = it }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecordScreen(
    recordId: Long,
    initialType: String,
    viewModel: TrackerViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(initialType) }
    var counterparty by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("0") }
    var notes by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var isPhysicalItem by remember { mutableStateOf(false) }

    var isEditMode by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }

    // Dropdown state
    var dropdownExpanded by remember { mutableStateOf(false) }
    val typeOptions = if (isPhysicalItem) listOf("DEBT", "LEND") else listOf("DEBT", "LEND", "LOAN", "KGOA_LOAN")

    // Date Dialog flags
    var showStartDateDialog by remember { mutableStateOf(false) }
    var showDueDateDialog by remember { mutableStateOf(false) }

    if (recordId != -1L && !loaded) {
        val recordFlow = viewModel.getRecordById(recordId)
        val recordState by recordFlow.collectAsState(initial = null)
        recordState?.let { r ->
            title = r.title
            type = r.type
            counterparty = r.counterparty
            amount = r.principalAmount.toString()
            interestRate = r.interestRate.toString()
            notes = r.notes
            startDate = r.startDate
            dueDate = r.dueDate
            isPhysicalItem = r.isPhysicalItem
            isEditMode = true
            loaded = true
        }
    } else {
        loaded = true
    }

    val themeColor = when (type) {
        "LEND" -> ColorLend
        "DEBT" -> ColorDebt
        else -> ColorLoan
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Record" else "Add Record", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Track Physical Item",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Track tools, books, etc. instead of money",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isPhysicalItem,
                        onCheckedChange = { checked ->
                            isPhysicalItem = checked
                            if (checked && (type == "LOAN" || type == "KGOA_LOAN")) {
                                type = "DEBT"
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = themeColor,
                            checkedTrackColor = themeColor.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = type,
                        onValueChange = {},
                        label = { Text("Record Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = themeColor,
                            focusedLabelColor = themeColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        typeOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    type = selectionOption
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isPhysicalItem) "Item Name (e.g. Lawn Mower, Book)" else "Title (e.g. Dinner, Car Loan)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeColor,
                        focusedLabelColor = themeColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = counterparty,
                    onValueChange = { counterparty = it },
                    label = { Text(if (type == "LEND") "Borrower Name" else "Lender / Bank Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeColor,
                        focusedLabelColor = themeColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text(if (isPhysicalItem) "Quantity" else "Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = themeColor,
                            focusedLabelColor = themeColor
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    if (!isPhysicalItem) {
                        OutlinedTextField(
                            value = interestRate,
                            onValueChange = { interestRate = it },
                            label = { Text("Interest (% p.a.)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = themeColor,
                                focusedLabelColor = themeColor
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (type == "KGOA_LOAN") {
                item {
                    Text(
                        text = "Note: KGOA Loan uses daily compounding interest based on days elapsed between transactions.",
                        fontSize = 11.sp,
                        color = ColorLoan,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Dates Picker
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { showStartDateDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start: ${formatDate(startDate)}", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { showDueDateDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = dueDate?.let { "Due: ${formatDate(it)}" } ?: "Set Due Date",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = themeColor,
                        focusedLabelColor = themeColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val principal = amount.toDoubleOrNull() ?: 0.0
                        val interest = if (isPhysicalItem) 0.0 else (interestRate.toDoubleOrNull() ?: 0.0)
                        if (title.isNotEmpty() && principal > 0) {
                            val r = RecordEntity(
                                id = if (isEditMode) recordId else 0,
                                type = type,
                                title = title,
                                counterparty = counterparty,
                                principalAmount = principal,
                                interestRate = interest,
                                startDate = startDate,
                                dueDate = dueDate,
                                notes = notes,
                                isCompleted = false,
                                isPhysicalItem = isPhysicalItem
                            )
                            if (isEditMode) {
                                viewModel.updateRecord(r)
                            } else {
                                viewModel.insertRecord(r)
                            }
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isEditMode) "Save Changes" else "Create Record", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showStartDateDialog) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startDate)
            DatePickerDialog(
                onDismissRequest = { showStartDateDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { startDate = it }
                        showStartDateDialog = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDateDialog = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showDueDateDialog) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate ?: System.currentTimeMillis())
            DatePickerDialog(
                onDismissRequest = { showDueDateDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        dueDate = datePickerState.selectedDateMillis
                        showDueDateDialog = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        dueDate = null
                        showDueDateDialog = false
                    }) { Text("Clear") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEmiScreen(
    emiId: Long,
    viewModel: TrackerViewModel,
    onBack: () -> Unit
) {
    var cardName by remember { mutableStateOf("") }
    var purchaseItem by remember { mutableStateOf("") }
    var totalAmount by remember { mutableStateOf("") }
    var monthlyPayment by remember { mutableStateOf("") }
    var totalMonths by remember { mutableStateOf("") }
    var monthsPaid by remember { mutableStateOf("0") }
    var statementDate by remember { mutableStateOf("15") }
    var notes by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(System.currentTimeMillis()) }

    var isEditMode by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }
    var showStartDateDialog by remember { mutableStateOf(false) }

    if (emiId != -1L && !loaded) {
        val emiFlow = viewModel.getEmiById(emiId)
        val emiState by emiFlow.collectAsState(initial = null)
        emiState?.let { e ->
            cardName = e.cardName
            purchaseItem = e.purchaseItem
            totalAmount = e.totalAmount.toString()
            monthlyPayment = e.monthlyPayment.toString()
            totalMonths = e.totalMonths.toString()
            monthsPaid = e.monthsPaid.toString()
            statementDate = e.statementDate.toString()
            notes = e.notes
            startDate = e.startDate
            isEditMode = true
            loaded = true
        }
    } else {
        loaded = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit EMI" else "Add EMI", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = cardName,
                    onValueChange = { cardName = it },
                    label = { Text("Credit Card Name (e.g. Chase Sapphire)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorEmi,
                        focusedLabelColor = ColorEmi
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = purchaseItem,
                    onValueChange = { purchaseItem = it },
                    label = { Text("Item Purchased (e.g. MacBook Pro)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorEmi,
                        focusedLabelColor = ColorEmi
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = totalAmount,
                        onValueChange = { totalAmount = it },
                        label = { Text("Total Cost (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorEmi,
                            focusedLabelColor = ColorEmi
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = monthlyPayment,
                        onValueChange = { monthlyPayment = it },
                        label = { Text("Monthly payment (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorEmi,
                            focusedLabelColor = ColorEmi
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = totalMonths,
                        onValueChange = { totalMonths = it },
                        label = { Text("Total Months") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorEmi,
                            focusedLabelColor = ColorEmi
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = monthsPaid,
                        onValueChange = { monthsPaid = it },
                        label = { Text("Months Already Paid") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorEmi,
                            focusedLabelColor = ColorEmi
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = statementDate,
                        onValueChange = { statementDate = it },
                        label = { Text("Statement Day (1-31)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorEmi,
                            focusedLabelColor = ColorEmi
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedButton(
                        onClick = { showStartDateDialog = true },
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start: ${formatDate(startDate)}", fontSize = 11.sp)
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorEmi,
                        focusedLabelColor = ColorEmi
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val total = totalAmount.toDoubleOrNull() ?: 0.0
                        val monthly = monthlyPayment.toDoubleOrNull() ?: 0.0
                        val months = totalMonths.toIntOrNull() ?: 0
                        val paid = monthsPaid.toIntOrNull() ?: 0
                        val sDate = statementDate.toIntOrNull() ?: 15

                        if (cardName.isNotEmpty() && purchaseItem.isNotEmpty() && total > 0 && monthly > 0 && months > 0) {
                            val emi = EmiEntity(
                                id = if (isEditMode) emiId else 0,
                                cardName = cardName,
                                purchaseItem = purchaseItem,
                                totalAmount = total,
                                monthlyPayment = monthly,
                                totalMonths = months,
                                monthsPaid = paid,
                                startDate = startDate,
                                statementDate = sDate,
                                notes = notes,
                                isCompleted = paid >= months
                            )
                            if (isEditMode) {
                                viewModel.updateEmi(emi)
                            } else {
                                viewModel.insertEmi(emi)
                            }
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorEmi),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isEditMode) "Save Changes" else "Create EMI", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showStartDateDialog) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startDate)
            DatePickerDialog(
                onDismissRequest = { showStartDateDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { startDate = it }
                        showStartDateDialog = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDateDialog = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
