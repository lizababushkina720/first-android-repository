package com.example.domain.usecase

import com.example.domain.model.OrganizationModel
import com.example.domain.repository.OrganizationRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class GetOrganizationsByQueryUseCaseTest {
    @MockK
    lateinit var organizationRepository: OrganizationRepository
    private lateinit var useCase: GetOrganizationsByQueryUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetOrganizationsByQueryUseCase(organizationRepository)
    }

    @Test
    fun `poluchenie_list_organization_by_query`() = runTest {
        val query = "Сбер"
        val expectedModel = OrganizationModel(
            inn = "7707083893",
            kpp = "", ogrn = "", shortName = "Сбер", fullName = "",
            address = "", managementName = "", managementPost = "",
            status = "", type = ""
        )
        val expectedResult = Pair(listOf(expectedModel), false)

        coEvery { organizationRepository.getOrganizationsByQuery(query) } returns expectedResult

        val actualResult = useCase(query)

        assertEquals(expectedResult, actualResult)

        coVerify(exactly = 1) { organizationRepository.getOrganizationsByQuery(query) }
    }

    @Test
    fun `poluchenie_empty_list_pri_empty_query`() = runTest {
        val emptyQuery = ""
        val expectedResult = Pair(emptyList<OrganizationModel>(), false)

        coEvery { organizationRepository.getOrganizationsByQuery(emptyQuery) } returns expectedResult

        val actualResult = useCase(emptyQuery)

        assertEquals(expectedResult, actualResult)

        coVerify(exactly = 1) { organizationRepository.getOrganizationsByQuery(emptyQuery) }
    }



}