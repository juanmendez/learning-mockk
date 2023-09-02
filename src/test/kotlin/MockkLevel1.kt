import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MockkLevel1 : MockkBaseTest() {
    @MockK
    lateinit var mockkBasics: MockkBasics

    @MockK
    lateinit var badOperations: BadOperations

    @Test
    fun `verify call`() {
        val mockedComposition = mockk<MockkBasics> {
            // general cases must be set before specific ones; otherwise, they are replaced
            every { hello() } returns "hola"

            every { goodBye(any()) } answers {
                val person = firstArg<String>()

                "adios, $person"
            }

            // specification has to override the stub above
            every { goodBye(eq("")) } returns "adios"
        }

        assertEquals(mockedComposition.goodBye(""), "adios")
    }

    @Test
    fun `verify argument conditions`() {
        // general cases must be set before specific ones; otherwise, they are replaced
        every { badOperations.sum(any(), any()) } answers {
            val a = firstArg<Int>()
            val b = secondArg<Int>()

            a * b
        }
        every { badOperations.sum(less(0), less(0)) } returns 0
        every { badOperations.sum(eq(0), eq(0)) } returns -1

        assertEquals(badOperations.sum(-1, -1), 0)
        assertEquals(badOperations.sum(0, 0), -1)
        assertEquals(badOperations.sum(1, 1), 1)
    }

    @Test
    fun `handle exceptions`() {
        // general cases must be set before specific ones; otherwise, they are replaced
        every { badOperations.sum(any(), any()) } answers {
            val a = firstArg<Int>()
            val b = secondArg<Int>()

            a * b
        }

        every { badOperations.sum(more(100), more(100)) } throws RuntimeException("error happened")

        assertEquals(badOperations.sum(1, 1), 1)

        assertThrows<RuntimeException> {
            badOperations.sum(101, 101)
        }
    }

    @Test
    fun `verify calls made`() {
        // general cases must be set before specific ones; otherwise, they are replaced
        every { badOperations.sum(any(), any()) } answers {
            val a = firstArg<Int>()
            val b = secondArg<Int>()

            a * b
        }

        every { badOperations.sum(more(100), more(100)) } throws RuntimeException("error happened")

        assertEquals(badOperations.sum(1, 1), 1)

        assertThrows<RuntimeException> {
            badOperations.sum(101, 101)
        }

        verify {
            badOperations.sum(1, 1)
            badOperations.sum(more(100), more(100))
        }
    }

    @Test
    fun `return many`() {
        every { badOperations.sum(any(), any()) } returnsMany listOf(0, 1, 2, 3, 4)

        assertEquals(badOperations.sum(0,0), 0)
        assertEquals(badOperations.sum(0,0), 1)
        assertEquals(badOperations.sum(0,0), 2)
        assertEquals(badOperations.sum(0,0), 3)
        assertEquals(badOperations.sum(0,0), 4)
    }

    @Test
    fun `return each`() {
        every { badOperations.sum(any(), any()) } returns 0 andThen 1 andThen 2 andThen 3 andThen 4

        assertEquals(badOperations.sum(0,0), 0)
        assertEquals(badOperations.sum(0,0), 1)
        assertEquals(badOperations.sum(0,0), 2)
        assertEquals(badOperations.sum(0,0), 3)
        assertEquals(badOperations.sum(0,0), 4)
    }

    @Test
    fun `do not act upon`() {
        every { badOperations.throwAnError() } just runs
        badOperations.throwAnError()
    }

    @Test
    fun `verify sequence`() {
        // general cases must be set before specific ones; otherwise, they are replaced
        val mockedComposition = mockk<MockkBasics> {
            // general cases must be set before specific ones; otherwise, they are replaced
            every { hello() } returns "hola"

            every { goodBye(any()) } answers {
                val person = firstArg<String>()

                "adios, $person"
            }

            // specification has to override the stub above
            every { goodBye(eq("")) } returns "adios"
        }

        mockedComposition.goodBye("a")
        mockedComposition.goodBye("b")
        mockedComposition.goodBye("c")
        mockedComposition.goodBye("d")


        // all calls must be checked, and in the given order
        // I removed one of the expected calls, and the test failed!
        verifySequence {
            mockedComposition.goodBye("a")
            mockedComposition.goodBye("b")
            mockedComposition.goodBye("c")
            mockedComposition.goodBye("d")
        }
    }

    @Test
    fun `verify order`() {
        // general cases must be set before specific ones; otherwise, they are replaced
        val mockedComposition = mockk<MockkBasics> {
            // general cases must be set before specific ones; otherwise, they are replaced
            every { hello() } returns "hola"

            every { goodBye(any()) } answers {
                val person = firstArg<String>()

                "adios, $person"
            }

            // specification has to override the stub above
            every { goodBye(eq("")) } returns "adios"
        }

        mockedComposition.goodBye("a")
        mockedComposition.goodBye("b")
        mockedComposition.goodBye("c")
        mockedComposition.goodBye("d")


        // here we check the order of the expected calls.
        // it is ok, if there are calls not included in the verification
        verifyOrder {
            mockedComposition.goodBye("a")
            mockedComposition.goodBye("c")
        }
    }
}