package com.namma.platform.domain.usecase

import com.namma.platform.domain.model.CoachLayout
import com.namma.platform.domain.repository.TrainRepository
import javax.inject.Inject

class GetCoachLayoutUseCase @Inject constructor(
    private val repo: TrainRepository
) {
    suspend operator fun invoke(trainNo: String): CoachLayout? {
        return repo.getCoachLayout(trainNo)
    }
}
