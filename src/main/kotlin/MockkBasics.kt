import java.util.Date

class MockkBasics {
    fun hello(): String = "hello"
    fun greet(message: String, person: String?) = "$message ${person ?: "unknown"}"
    fun goodBye(person: String) = "good bye, $person"
}

class BadOperations {
    fun sum(a: Int, b: Int) = a + b

    @Throws(Exception::class)
    fun throwAnError() {
        throw Exception("intended error")
    }
}

data class DefaultAttributes(
    val name: String,
    val age: Int,
    val date: Date,
    val nullability: String?,
)