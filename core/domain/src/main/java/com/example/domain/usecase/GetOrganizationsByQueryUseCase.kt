package com.example.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.domain.model.OrganizationModel
import com.example.domain.repository.OrganizationRepository
import javax.inject.Inject

class GetOrganizationsByQueryUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) {
    suspend operator fun invoke(query: String): Pair<List<OrganizationModel>, Boolean> {
        return withContext(Dispatchers.IO) {
            organizationRepository.getOrganizationsByQuery(query)
        }
    }
}
