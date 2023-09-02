import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlin.test.assertEquals
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
class MockkLevel3 : MockkBaseTest() {

    @MockK
    lateinit var mockkCoroutines: MockkCoroutines

    @Test
    fun `test a flow with Mockk`() = runTest {
        coEvery { mockkCoroutines.getList() } returns flowOf(5, 6, 7, 8)

        mockkCoroutines.getList().collect {
            println("value $it")
        }
    }

    @Test
    fun `colelct all`() = runTest {
        coEvery { mockkCoroutines.getList() } returns flowOf(5, 6, 7, 8)

        val list = mutableListOf<Int>()
        val job = launch(dispatcher) {
            mockkCoroutines.getList().collect(list::add)
        }

        coVerify { mockkCoroutines.getList() }

        assertEquals(list, listOf(5, 6, 7, 8))

        job.cancelAndJoin()
    }

    @Test
    fun `wait for result`() = runTest {
        coEvery { mockkCoroutines.getLuckyNumber() } coAnswers {
            delay(10000)
            34
        }

        assertEquals(mockkCoroutines.getLuckyNumber(), 34)
    }

}