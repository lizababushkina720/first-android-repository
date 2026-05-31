package com.example.domain.usecase

import com.example.domain.model.OrganizationModel
import com.example.domain.repository.OrganizationRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest


class GetOrganizationByInnUseCaseTest {

    @MockK
    lateinit var organizationRepository: OrganizationRepository

    private lateinit var useCase: GetOrganizationByInnUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetOrganizationByInnUseCase(organizationRepository)
    }

    @Test
    fun `poluchenie_organizacii_po_inn`()= runTest {

        val inn = "7707083893"
        val expectedOrg = OrganizationModel(
            inn = inn, kpp = "", ogrn = "", shortName = "Сбер",
            fullName = "", address = "", managementName = "",
            managementPost = "", status = "", type = ""
        )
        coEvery { organizationRepository.getOrganizationByInn(inn) } returns expectedOrg

        val result = useCase(inn)

        assertEquals(expectedOrg, result)
        coVerify(exactly = 1) { organizationRepository.getOrganizationByInn(inn) }

    }

}