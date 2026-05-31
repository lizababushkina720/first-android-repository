package com.example.hw_01_sem2.ui.screen.search

import com.example.api.OrganizationRequestAnalytics
import com.example.domain.model.OrganizationModel
import com.example.domain.usecase.GetOrganizationsByQueryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val useCase: GetOrganizationsByQueryUseCase = mockk()
    private val analytics: OrganizationRequestAnalytics = mockk(relaxed = true)

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(useCase, analytics)
    }

    @Test
    fun `new_list_uspeshno_peredaetca_v_state_and_otobrashaetca_on_screen`() = runTest {
        val query = "Yandex"
        val mockModel = OrganizationModel(
            inn = "111", kpp = "", ogrn = "", shortName = "Yandex",
            fullName = "", address = "", managementName = "",
            managementPost = "", status = "", type = ""
        )
        coEvery { useCase(query) } returns Pair(listOf(mockModel), false)

        viewModel.onQueryChanged(query)

        viewModel.performSearch()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Success)

        val items = (state as SearchUiState.Success).organizations.items
        assertEquals(1, items.size)
        assertEquals("111", items[0].inn)
    }

    @Test
    fun `with_exceptionsh_should_update_state_to_error`() = runTest {
        val query = "Unknown"
        val errorMessage = "Network Error"
        coEvery { useCase(query) } throws Exception(errorMessage)

        viewModel.onQueryChanged(query)

        viewModel.performSearch()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SearchUiState.Error)
        assertEquals(errorMessage, (state as SearchUiState.Error).messageKey)
    }
}