package com.example.hw_02.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.hw_02.R
import com.example.hw_02.utils.ValidationUtils
import androidx.compose.ui.platform.LocalContext


@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_xlarge))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_medium))
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail ->
                    email = newEmail
                    if (emailError != null) {
                        emailError = null
                    }
                },
                label = { Text(stringResource(R.string.email_label)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = emailError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            emailError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small)
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_large))
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { newPassword ->
                    password = newPassword
                    if (passwordError != null) {
                        passwordError = null
                    }
                },
                label = { Text(stringResource(R.string.password_label)) },
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = if (isPasswordVisible) {
                                stringResource(R.string.hide_password)
                            } else {
                                stringResource(R.string.show_password)
                            }
                        )
                    }
                },
                isError = passwordError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            passwordError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_small)
                    )
                )
            }
        }
        Button(
            onClick = {
                emailError = when {
                    email.isBlank() -> context.getString(R.string.email_empty_error)
                    !ValidationUtils.isValidEmail(email) -> context.getString(R.string.email_invalid_error)
                    else -> null
                }
                passwordError = when {
                    password.isBlank() -> context.getString(R.string.password_empty_error)
                    !ValidationUtils.isValidPassword(password) -> context.getString(R.string.password_short_error)
                    else -> null
                }
                if (emailError == null && passwordError == null) {
                    onLoginSuccess(email)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(R.string.login_button),
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
            )
        }
    }
}
