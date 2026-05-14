package com.namma.platform.domain.usecase

import com.namma.platform.domain.model.Station
import com.namma.platform.domain.repository.TrainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchStationsUseCase @Inject constructor(
    private val repo: TrainRepository
) {
    operator fun invoke(query: String): Flow<List<Station>> {
        return repo.getStations(query)
    }
}
