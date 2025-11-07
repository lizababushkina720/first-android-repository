package com.example.hw_03.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.hw_03.Dimens
import com.example.hw_03.Keys
import com.example.hw_03.R
import com.example.hw_03.receiver.NotificationReplyReceiver
import kotlinx.coroutines.launch

@Composable
fun MessagesScreen() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var inputText by remember { mutableStateOf(Keys.EMPTY_STRING) }
    var messages by remember { mutableStateOf<List<String>>(emptyList()) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                messages = NotificationReplyReceiver.getMessages(context)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium)
    ) {
        Text(
            text = stringResource(R.string.messages_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = Dimens.paddingMedium)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(Dimens.WEIGHT_FULL)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.spacingSmall)
        ) {
            items(messages) { message ->
                MessageCard(message = message)
            }
        }

        Spacer(modifier = Modifier.height(Dimens.spacingMedium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(stringResource(R.string.messages_input_hint)) },
                modifier = Modifier.weight(Dimens.WEIGHT_FULL),
                maxLines = Dimens.textFieldMinLines
            )

            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        NotificationReplyReceiver.saveMessagePublic(context, inputText)

                        messages = NotificationReplyReceiver.getMessages(context)

                        inputText = Keys.EMPTY_STRING

                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - Dimens.LAST_ITEM_OFFSET)
                        }
                    }
                },
                enabled = inputText.isNotBlank()
            ) {
                Text(stringResource(R.string.messages_send_button))
            }
        }
    }
}

@Composable
private fun MessageCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(Dimens.paddingMedium),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

