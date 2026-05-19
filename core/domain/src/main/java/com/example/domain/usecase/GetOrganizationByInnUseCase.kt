package com.example.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.domain.model.OrganizationModel
import com.example.domain.repository.OrganizationRepository
import javax.inject.Inject

class GetOrganizationByInnUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) {
    suspend operator fun invoke(inn: String): OrganizationModel? {
        return withContext(Dispatchers.IO) {
            organizationRepository.getOrganizationByInn(inn)
        }
    }
}