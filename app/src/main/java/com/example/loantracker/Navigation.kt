package com.example.loantracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.loantracker.data.DefaultDataRepository
import com.example.loantracker.data.TrackerDatabase
import com.example.loantracker.ui.TrackerViewModel
import com.example.loantracker.ui.screens.AddEditEmiScreen
import com.example.loantracker.ui.screens.AddEditRecordScreen
import com.example.loantracker.ui.screens.DashboardScreen
import com.example.loantracker.ui.screens.DebtsLendingListScreen
import com.example.loantracker.ui.screens.EmiDetailScreen
import com.example.loantracker.ui.screens.RecordDetailScreen

@Composable
fun MainNavigation() {
    val context = LocalContext.current.applicationContext
    val db = TrackerDatabase.getDatabase(context)
    val repository = DefaultDataRepository(db.dao)
    val trackerViewModel: TrackerViewModel = viewModel { TrackerViewModel(repository) }

    val backStack = rememberNavBackStack(Main)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Main> {
                DashboardScreen(
                    viewModel = trackerViewModel,
                    onNavigate = { key -> backStack.add(key) },
                    modifier = Modifier.safeDrawingPadding()
                )
            }

            entry<DebtsLendingList> {
                DebtsLendingListScreen(
                    viewModel = trackerViewModel,
                    onNavigate = { key -> backStack.add(key) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<RecordDetail> { key ->
                RecordDetailScreen(
                    recordId = key.id,
                    viewModel = trackerViewModel,
                    onNavigate = { navKey -> backStack.add(navKey) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<EmiDetail> { key ->
                EmiDetailScreen(
                    emiId = key.id,
                    viewModel = trackerViewModel,
                    onNavigate = { navKey -> backStack.add(navKey) },
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<AddEditRecord> { key ->
                AddEditRecordScreen(
                    recordId = key.id,
                    initialType = key.type,
                    viewModel = trackerViewModel,
                    onBack = { backStack.removeLastOrNull() }
                )
            }

            entry<AddEditEmi> { key ->
                AddEditEmiScreen(
                    emiId = key.id,
                    viewModel = trackerViewModel,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
