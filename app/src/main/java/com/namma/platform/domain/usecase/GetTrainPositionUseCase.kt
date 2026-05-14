package com.namma.platform.domain.usecase

import com.namma.platform.domain.model.TrainPosition
import com.namma.platform.domain.repository.TrainRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class GetTrainPositionUseCase @Inject constructor(
    private val repo: TrainRepository
) {
    operator fun invoke(trainNo: String, date: String): Flow<Result<TrainPosition>> = flow {
        while (true) {
            repo.getTrainPosition(trainNo, date).collect { result ->
                emit(result)
            }
            delay(5.minutes)
        }
    }
}
