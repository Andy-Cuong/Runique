@file:OptIn(ExperimentalMaterial3Api::class)

package com.andyc.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andyc.core.presentation.designsystem.RuniqueTheme
import com.andyc.core.presentation.designsystem.StartIcon
import com.andyc.core.presentation.designsystem.StopIcon
import com.andyc.core.presentation.designsystem.components.RuniqueActionButton
import com.andyc.core.presentation.designsystem.components.RuniqueDialog
import com.andyc.core.presentation.designsystem.components.RuniqueFab
import com.andyc.core.presentation.designsystem.components.RuniqueOutlinedActionButton
import com.andyc.core.presentation.designsystem.components.RuniqueScaffold
import com.andyc.core.presentation.designsystem.components.RuniqueToolbar
import com.andyc.run.presentation.R
import com.andyc.run.presentation.active_run.components.RunDataCard
import com.andyc.run.presentation.active_run.maps.TrackerMap
import com.andyc.run.presentation.util.hasLocationPermission
import com.andyc.run.presentation.util.hasNotificationPermission
import com.andyc.run.presentation.util.shouldShowLocationPermissionRationale
import com.andyc.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ActiveRunScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasCoarseLocationPermission = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            perms[Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasCoarseLocationPermission && hasFineLocationPermission,
                showLocationPermissionRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationPermissionRationale = showNotificationRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationPermissionRationale = showLocationRationale
            )
        )
        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = showNotificationRationale
            )
        )

        if (!showLocationRationale && !showNotificationRationale) {
            permissionLauncher.requestRuniquePermissions(context = context)
        }
    }

    RuniqueScaffold(
        withGradient = false,
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(ActiveRunAction.OnBackClick)
                }
            )
        },
        floatingActionButton = {
            RuniqueFab(
                icon = if (state.shouldTrack) StopIcon else StartIcon,
                onClick = {
                    onAction(ActiveRunAction.OnToggleRunClick)
                },
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                },
                iconSize = 20.dp
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TrackerMap(
                isRunFinished = state.isRunFinished,
                currentLocation = state.currentLocation,
                locations = state.runData.locations,
                onSnapshot = {  },
                modifier = Modifier.fillMaxSize()
            )

            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }

        if (!state.shouldTrack && state.hasStartedRunning) {
            RuniqueDialog(
                title = stringResource(id = R.string.running_is_paused),
                onDismiss = {
                    onAction(ActiveRunAction.OnResumeRunClick)
                },
                description = stringResource(id = R.string.resume_or_finish_run),
                primaryButton = {
                    RuniqueActionButton(
                        text = stringResource(id = R.string.resume),
                        isLoading = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        onAction(ActiveRunAction.OnResumeRunClick)
                    }
                },
                secondaryButton = { 
                    RuniqueOutlinedActionButton(
                        text = stringResource(id = R.string.finish),
                        isLoading = state.isSavingRun,
                        modifier = Modifier.weight(1f)
                    ) {
                        onAction(ActiveRunAction.OnFinishRunClick)
                    }
                }
            )
        }

        if (state.showLocationRationale || state.showNotificationRationale) {
            RuniqueDialog(
                title = stringResource(id = R.string.permission_required),
                onDismiss = { /* Normal dismissal not allowed for permission request */ },
                description = when {
                    state.showLocationRationale && state.showNotificationRationale -> {
                        stringResource(id = R.string.location_notification_rationale)
                    }
                    state.showLocationRationale -> {
                        stringResource(id = R.string.location_rationale)
                    }
                    else -> {
                        stringResource(id = R.string.notification_rationale)
                    }
                },
                primaryButton = {
                    RuniqueOutlinedActionButton(
                        text = stringResource(id = R.string.okay),
                        isLoading = false,
                    ) {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestRuniquePermissions(context = context)
                    }
                }
            )
        }
    }
}

private fun ActivityResultLauncher<Array<String>>.requestRuniquePermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else emptyArray()

    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermission)
        }
        !hasLocationPermission -> {
            launch(locationPermissions)
        }
        !hasNotificationPermission -> {
            launch(notificationPermission)
        }
    }
}

@Preview
@Composable
private fun ActiveRunScreenPrev() {
    RuniqueTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onAction = {}
        )
    }
}