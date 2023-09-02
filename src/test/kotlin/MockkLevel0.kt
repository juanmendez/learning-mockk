import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class MockkLevel0 : MockkBaseTest() {
    @MockK
    lateinit var mockkBasics: MockkBasics

    @SpyK
    var spyMockkBasics = MockkBasics()

    @Test
    fun `mock return value`() {
        every { mockkBasics.hello() } returns "hola"
        assertEquals(mockkBasics.hello(), "hola")
    }

    @Test
    fun `answer based on parameters`() {
        every { mockkBasics.goodBye(any()) } answers {
            val person = firstArg<String>()

            "adios, $person"
        }

        assertEquals(mockkBasics.goodBye("Juan"), "adios, Juan")
    }

    @Test
    fun `answer having null as parameter`() {
        // Interesting, in Mockito we have been forced to include anyOrNull() in any param which welcomes null
        // but not here
        every { mockkBasics.greet(any(), any()) } answers {
            val message = firstArg<String>()
            val person = secondArg<String?>() ?: "who?"

            "test $message $person"
        }

        assertEquals(mockkBasics.greet("good evening", null), "test good evening who?")
        assertEquals(mockkBasics.greet("good evening", "Anuj"), "test good evening Anuj")
    }

    @Test
    fun `play around spying`() {
        // Interesting, in Mockito we have been forced to include anyOrNull() in any param which welcomes null
        // but not here
        every { spyMockkBasics.greet(any(), any()) } answers {
            val message = firstArg<String>()
            val person = secondArg<String?>() ?: "who?"

            "test $message $person"
        }

        assertEquals(spyMockkBasics.hello(), "hello")
        assertEquals(spyMockkBasics.greet("good evening", null), "test good evening who?")
        assertEquals(spyMockkBasics.greet("good evening", "Anuj"), "test good evening Anuj")
        assertEquals(spyMockkBasics.goodBye("Anuj"), "good bye, Anuj")
    }

    @Test
    fun `mock composition`() {
        val mockedComposition = mockk<MockkBasics> {
            every { hello() } returns "hola"

            every { greet(any(), any()) } answers {
                val message = firstArg<String>()
                val person = secondArg<String?>() ?: "who?"

                "test $message $person"
            }

            every { goodBye(any()) } answers {
                val person = firstArg<String>()

                "adios, $person"
            }
        }

        assertEquals(mockedComposition.hello(), "hola")
        assertEquals(mockedComposition.goodBye("Abigail"), "adios, Abigail")
        assertEquals(mockedComposition.greet("hello", "Abigail"), "test hello Abigail")
    }
}