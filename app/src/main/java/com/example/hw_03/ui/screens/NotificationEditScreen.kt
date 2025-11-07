package com.example.hw_03.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.hw_03.Dimens
import com.example.hw_03.R
import com.example.hw_03.utils.NotificationHandler


@Composable
fun NotificationEditScreen(
    notificationHandler: NotificationHandler
) {
    val context = LocalContext.current


    var notificationId by remember { mutableStateOf("") }
    var newText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
    ) {
        Text(
            text = stringResource(R.string.edit_title),
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = notificationId,
            onValueChange = { notificationId = it },
            label = { Text(stringResource(R.string.edit_notification_id)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = newText,
            onValueChange = { newText = it },
            label = { Text(stringResource(R.string.edit_new_text)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = Dimens.textFieldMinLines,
            maxLines = Dimens.textFieldMaxLines
        )
        Button(
            onClick = {
                val id = notificationId.toIntOrNull()
                if (id == null) {
                    Toast.makeText(
                        context,
                        R.string.edit_invalid_id,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                val success = notificationHandler.updateNotification(id, newText)

                val messageResId = if (success) {
                    R.string.edit_success
                } else {
                    R.string.edit_not_found
                }

                Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = notificationId.isNotBlank() && newText.isNotBlank()
        ) {
            Text(stringResource(R.string.edit_update_button))
        }

        Spacer(modifier = Modifier.height(Dimens.spacingLarge))

        OutlinedButton(
            onClick = {
                val hadNotifications = notificationHandler.cancelAllNotifications()

                val messageResId = if (hadNotifications) {
                    R.string.edit_all_cleared
                } else {
                    R.string.edit_no_notifications
                }

                Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.edit_clear_all_button))
        }
    }
}
