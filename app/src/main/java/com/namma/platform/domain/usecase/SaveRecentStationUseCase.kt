package com.namma.platform.domain.usecase

import com.namma.platform.domain.model.Station
import com.namma.platform.domain.repository.TrainRepository
import javax.inject.Inject

class SaveRecentStationUseCase @Inject constructor(
    private val repo: TrainRepository
) {
    suspend operator fun invoke(station: Station) {
        repo.saveRecentStation(station)
    }
}
