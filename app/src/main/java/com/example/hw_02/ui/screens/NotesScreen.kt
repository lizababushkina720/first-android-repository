package com.example.hw_02.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.hw_02.R
import com.example.hw_02.models.Note
import com.example.hw_02.ui.theme.ThemeType

@Composable
fun NotesScreen(
    userEmail: String,
    notes: List<Note>,
    onAddNoteClick: () -> Unit,
    currentTheme: ThemeType,
    onThemeChange: (ThemeType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(R.string.notes_title) + " $userEmail",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                top = dimensionResource(R.dimen.padding_large),
                bottom = dimensionResource(R.dimen.padding_medium)
            )
        )
        ThemeDropdown(
            currentTheme = currentTheme,
            onThemeSelected = onThemeChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_medium))
        )
        Button(
            onClick = onAddNoteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_medium))
        ) {
            Text(stringResource(R.string.add_note_button))
        }
        if (notes.isEmpty()) {
            Text(
                text = stringResource(R.string.no_notes),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_xlarge))
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.padding_small)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                items(notes) { note ->
                    NoteItem(note = note)
                }
            }
        }
    }
}

@Composable
private fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_small))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (note.content.isNotBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
private fun ThemeDropdown(
    currentTheme: ThemeType,
    onThemeSelected: (ThemeType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val themeNames = mapOf(
        ThemeType.DEFAULT to stringResource(R.string.theme_default),
        ThemeType.RED to stringResource(R.string.theme_red),
        ThemeType.GREEN to stringResource(R.string.theme_green),
        ThemeType.BLUE to stringResource(R.string.theme_blue)
    )

    Column(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.theme_label) + " " + themeNames[currentTheme])
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ThemeType.values().forEach { theme ->
                DropdownMenuItem(
                    text = { Text(themeNames[theme] ?: "") },
                    onClick = {
                        onThemeSelected(theme)
                        expanded = false
                    }
                )
            }
        }
    }
}
