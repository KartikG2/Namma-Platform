package com.namma.platform.domain.usecase

import com.namma.platform.domain.model.Train
import com.namma.platform.domain.repository.TrainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNextTrainsUseCase @Inject constructor(
    private val repo: TrainRepository
) {
    suspend operator fun invoke(fromCode: String, toCode: String? = null): Flow<Result<List<Train>>> {
        return repo.getNextTrains(fromCode, toCode)
    }
}
