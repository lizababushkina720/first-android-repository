package com.example.hw_04.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.Dispatchers
import com.example.hw_04.R

data class UiCoroutineSettings(
    val count: Int,
    val dispatcher: kotlinx.coroutines.CoroutineDispatcher,
    val isSequential: Boolean,
    val isParallel: Boolean,
    val isLazy: Boolean,
    val isBackgroundWork: Boolean
)

@Composable
fun CoroutinesScreen(
    isLoading: Boolean,
    completed: Int,
    total: Int,
    onStartClicked: (UiCoroutineSettings) -> Unit,
    onCancelClicked: () -> Unit,
    onResetRequested: () -> Unit
) {
    var sliderValue by remember { mutableStateOf(10f) }
    var dispatcher by remember { mutableStateOf(Dispatchers.Default) }
    var isSequential by remember { mutableStateOf(true) }
    var isParallel by remember { mutableStateOf(false) }
    var isLazy by remember { mutableStateOf(false) }
    var isBackgroundWork by remember { mutableStateOf(true) }
    var showDispatcherMenu by remember { mutableStateOf(false) }

    val progress = if (total == 0) 0f else completed.toFloat() / total.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_screen)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = R.string.coroutines_count_label,
                sliderValue.toInt()
            )
        )

        Slider(
            value = sliderValue,
            onValueChange = { newValue ->
                val snapped = (newValue / 5f).toInt() * 5f
                sliderValue = snapped.coerceIn(10f, 100f)
            },
            valueRange = 10f..100f,
            steps = 0,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))

        Text(text = stringResource(id = R.string.dispatcher_label))

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { showDispatcherMenu = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (dispatcher) {
                        Dispatchers.Default -> stringResource(id = R.string.dispatcher_default)
                        Dispatchers.IO -> stringResource(id = R.string.dispatcher_io)
                        Dispatchers.Main -> stringResource(id = R.string.dispatcher_main)
                        else -> stringResource(id = R.string.dispatcher_unknown)
                    }
                )
            }

            DropdownMenu(
                expanded = showDispatcherMenu,
                onDismissRequest = { showDispatcherMenu = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.dispatcher_default_full)) },
                    onClick = {
                        dispatcher = Dispatchers.Default
                        showDispatcherMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.dispatcher_io_full)) },
                    onClick = {
                        dispatcher = Dispatchers.IO
                        showDispatcherMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.dispatcher_main_full)) },
                    onClick = {
                        dispatcher = Dispatchers.Main
                        showDispatcherMenu = false
                    }
                )
            }
        }


        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.sequential_label),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isSequential,
                onCheckedChange = { checked ->
                    isSequential = checked
                    isParallel = !checked
                }
            )
        }
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.parallel_label),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isParallel,
                onCheckedChange = { checked ->
                    isParallel = checked
                    isSequential = !checked
                }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.lazy_label),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isLazy,
                onCheckedChange = { isLazy = it }
            )
        }
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.background_label),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isBackgroundWork,
                onCheckedChange = { isBackgroundWork = it }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_extra_large)))

        if (isLoading) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.progress_height))
            )
            Text(
                text = stringResource(
                    id = R.string.progress_text,
                    completed,
                    total
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_large)))
        }
        if (!isLoading) {
            Button(
                onClick = {
                    val uiSettings = UiCoroutineSettings(
                        count = sliderValue.toInt(),
                        dispatcher = dispatcher,
                        isSequential = isSequential,
                        isParallel = isParallel,
                        isLazy = isLazy,
                        isBackgroundWork = isBackgroundWork
                    )
                    onStartClicked(uiSettings)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.button_start))
            }
        } else {
            Button(
                onClick = { onCancelClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.button_cancel))
            }
        }
    }
}
