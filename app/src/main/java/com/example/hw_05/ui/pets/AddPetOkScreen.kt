package com.example.hw_05.ui.pets
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import com.example.hw_05.R
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import com.example.hw_05.ui.nav.Routes
@Composable
fun AddPetOkScreen(navController: NavHostController) {
    val screenPadding = dimensionResource(R.dimen.screen_padding)
    val vSpace12 = dimensionResource(R.dimen.v_space_12)
    val btnPaddingV = dimensionResource(R.dimen.btn_padding_vertical)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(vSpace12)
        ) {
            Text(stringResource(R.string.add_pet_ok_title), style = MaterialTheme.typography.headlineMedium)
            Text(stringResource(R.string.add_pet_ok_body), style = MaterialTheme.typography.bodyMedium)

            Button(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = btnPaddingV),
                onClick = {
                    navController.navigate(Routes.PETS) {
                        popUpTo(Routes.PETS) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            ) {
                Text(stringResource(R.string.add_pet_ok_btn_to_list))
            }
        }
    }
}
