import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MockkLevel2 : MockkBaseTest() {
    @MockK
    lateinit var mockkBasics: MockkBasics

    @MockK
    lateinit var badOperations: BadOperations

    @RelaxedMockK
    lateinit var defaultRelaxedAttributes: DefaultAttributes

    @Test
    fun `capture arguments`() {

        val firstSlot = slot<Int>()
        val secondSlot = slot<Int>()

        every { badOperations.sum(capture(firstSlot), capture(secondSlot)) } answers {
            val a = firstArg<Int>()
            val b = secondArg<Int>()

            a * b
        }

        assertEquals(badOperations.sum(1, 2), 2)
        assertEquals(1, firstSlot.captured)
        assertEquals(2, secondSlot.captured)
    }
    @Test
    fun `mock relaxed object`() {
        val mockedRelaxed = mockk<MockkBasics>(name = "relaxed", relaxed = true)

        assertEquals(mockedRelaxed.hello(), "")
        assertEquals(mockedRelaxed.goodBye("Abigail"), "")
        assertEquals(mockedRelaxed.greet("hello", "Abigail"), "")
    }

    @Test
    fun `mock relaxed data class`() {
        // let's use the annotation instead of the following line.
        // val defaultAttributes = mockk<DefaultAttributes>(relaxed = true)

        assertEquals(defaultRelaxedAttributes.name, "")
        assertEquals(defaultRelaxedAttributes.age, 0)
        assertEquals(defaultRelaxedAttributes.date.time, 0)
        assertEquals(defaultRelaxedAttributes.nullability, "", "very interesting nullables still get default value")
    }
}