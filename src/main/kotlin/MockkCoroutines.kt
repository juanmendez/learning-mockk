import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class MockkCoroutines {
    suspend fun getList(): Flow<Int> {
        return flowOf(0, 1, 2, 3, 4)
    }

    suspend fun getLuckyNumber(): Int = 1
}