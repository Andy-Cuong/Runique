@file:OptIn(ExperimentalMaterial3Api::class)

package com.andyc.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andyc.analytics.presentation.components.AnalyticsCard
import com.andyc.core.presentation.designsystem.RuniqueTheme
import com.andyc.core.presentation.designsystem.components.RuniqueScaffold
import com.andyc.core.presentation.designsystem.components.RuniqueToolbar
import com.example.analytics.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsScreenRoot(
    onBackClick: () -> Unit,
    viewModel: AnalyticsDashboardViewModel = koinViewModel()
) {
    AnalyticsScreenRootScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                AnalyticsAction.OnBackClick -> onBackClick()
            }
        },
    )
}

@Composable
fun AnalyticsScreenRootScreen(
    state: AnalyticsDashboardState,
    onAction: (AnalyticsAction) -> Unit
) {
    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(R.string.analytics),
                onBackClick = { onAction(AnalyticsAction.OnBackClick) }
            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                AnalyticsCard(
                    title = stringResource(id = R.string.total_distance_run),
                    value = state.totalDistanceRun,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                AnalyticsCard(
                    title = stringResource(id = R.string.total_time_run),
                    value = state.totalTimeRun,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                AnalyticsCard(
                    title = stringResource(id = R.string.fastest_ever_run),
                    value = state.fastestEverRun,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                AnalyticsCard(
                    title = stringResource(id = R.string.avg_distance_per_run),
                    value = state.avgDistance,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                AnalyticsCard(
                    title = stringResource(id = R.string.avg_pace_per_run),
                    value = state.avgPace,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnalyticsScreenRootScreenPrev() {
    RuniqueTheme {
        AnalyticsScreenRootScreen(
            state = AnalyticsDashboardState(
                totalDistanceRun = "0.2 km",
                totalTimeRun = "0d 14m 02s",
                fastestEverRun = "30 km/h",
                avgDistance = "0.1 km",
                avgPace = "7:10"
            ),
            onAction = {}
        )
    }
}