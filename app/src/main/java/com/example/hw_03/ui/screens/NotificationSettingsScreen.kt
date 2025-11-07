package com.example.hw_03.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.hw_03.Dimens
import com.example.hw_03.R
import com.example.hw_03.model.NotificationModel
import com.example.hw_03.model.NotificationPriority
import com.example.hw_03.utils.NotificationHandler
import kotlin.random.Random



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    notificationHandler: NotificationHandler
) {

    val context = LocalContext.current


    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showTitleError by remember { mutableStateOf(false) }

    var isExpandable by remember { mutableStateOf(false) }
    var openOnClick by remember { mutableStateOf(false) }
    var hasReplyAction by remember { mutableStateOf(false) }

    var selectedPriority by remember { mutableStateOf(NotificationPriority.DEFAULT) }
    var isPriorityExpanded by remember { mutableStateOf(false) }

    val priorities = listOf(
        NotificationPriority.MIN to stringResource(R.string.priority_min),
        NotificationPriority.LOW to stringResource(R.string.priority_low),
        NotificationPriority.DEFAULT to stringResource(R.string.priority_default),
        NotificationPriority.HIGH to stringResource(R.string.priority_high)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                showTitleError = false
            },
            label = { Text(stringResource(R.string.settings_notification_title)) },
            isError = showTitleError,
            supportingText = {
                if (showTitleError) {
                    Text(
                        text = stringResource(R.string.settings_title_error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it
                if (it.isEmpty()) {
                    isExpandable = false
                }
            },
            label = { Text(stringResource(R.string.settings_notification_text)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = Dimens.textFieldMinLines,
            maxLines = Dimens.textFieldMaxLines
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_expandable),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isExpandable,
                onCheckedChange = { isExpandable = it },
                enabled = content.isNotEmpty()
            )
        }
        ExposedDropdownMenuBox(
            expanded = isPriorityExpanded,
            onExpandedChange = { isPriorityExpanded = it }
        ) {
            OutlinedTextField(
                value = priorities.find { it.first == selectedPriority }?.second ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.settings_priority)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPriorityExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isPriorityExpanded,
                onDismissRequest = { isPriorityExpanded = false }
            ) {
                priorities.forEach { (priority, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            selectedPriority = priority
                            isPriorityExpanded = false
                        }
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_open_on_click),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = openOnClick,
                onCheckedChange = { openOnClick = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings_reply_action),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = hasReplyAction,
                onCheckedChange = { hasReplyAction = it }
            )
        }

        Button(
            onClick = {
                if (title.isBlank()) {
                    showTitleError = true
                    return@Button
                }

                val notification = NotificationModel(
                    id = Random.nextInt(Dimens.notificationIdMin, Dimens.notificationIdMax),
                    title = title,
                    content = content.ifBlank { null },
                    icon = R.drawable.ic_notification,
                    priority = selectedPriority,
                    isExpandable = isExpandable && content.isNotEmpty(),
                    openOnClick = openOnClick,
                    hasReplyAction = hasReplyAction
                )

                val success = notificationHandler.showNotification(notification)

                val messageResId = if (success) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.notification_sent, notification.id),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                } else {
                    R.string.notification_error_permission
                }

                Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.settings_send_notification))
        }
    }
}
