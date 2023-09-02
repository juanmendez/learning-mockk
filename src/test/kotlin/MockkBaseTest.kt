import io.mockk.MockKAnnotations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class MockkBaseTest(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) {
    @BeforeEach
    fun onSetUp() {
        MockKAnnotations.init(this)

        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun onTearDown() {
        Dispatchers.resetMain()
    }
}