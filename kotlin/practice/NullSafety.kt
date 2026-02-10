package practice

// Null safety detects potential problems with null values at compile time, rather than at run time.
// In Kotlin, we can declare a variable as nullable by appending a question mark `?` to the type. This means that the variable can hold either a non-null value or a null value.

// notNull doesn't accept null values, so we can safely call methods on it without worrying about null pointer exceptions.
fun strLength(notNull: String): Int {
    return notNull.length
}

fun describeString(mayBeString: String?): String {
    if (mayBeString != null && mayBeString.isNotEmpty()) {
        return "String of length ${mayBeString.length}"
    } else {
        return "Empty or null string"
    }
}

// uses a safe call to return either the length of the string or null
fun lengthString(mayBeString: String?): Int? = mayBeString?.length

fun main() {
    var neverNull: String = "This can't be null"
    // neverNull = null // Compilation error: Null can not be a value of a non-null type String

    var nullable: String? = "You can keep a null here"
    nullable = null // OK, nullable can hold a null value

    var inferredNonNull = "The compiler infers this as non-null"
    // inferredNonNull = null // Compilation error: Null can not be a value of a non-null type String

    println(strLength(inferredNonNull))
    // println(strLength(nullable)) // Compilation error: Type mismatch: inferred type is String? but String was expected

    val nullString: String? = null
    println(describeString(nullString)) // Output: Empty or null string
    println(describeString("Hello, Kotlin!")) // Output: String of length 14

    // To safely access properties of an object that might contain a null value, use the safe call operator ?.
    // The safe call operator returns null if either the object or one of its accessed properties is null.
    println(lengthString(nullString)) // Output: null
    println(lengthString("Hello, Kotlin!")) // Output: 14

    // Safe calls can be chained so that if any property of an object contains a null value, then null is returned without an error being thrown
    // person.company?.address?.country

    // The safe call operator can also be used to safely call an extension or member function. In this case, a null check
    // is performed before the function is called. If the check detects a null value, then the call is skipped and null is returned.
    println(nullString?.uppercase())

    // Elvis operator `?:` is used to provide a default value when the expression to the left of it is null. It is often used in conjunction with the safe call operator to handle null values gracefully.
    val length = nullString?.length ?: 0 // If nullString is null, length will be assigned the value 0 instead of null
    println(length) // Output: 0

}