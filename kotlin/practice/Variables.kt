package practice

const val name: String = "Alice"    // Immutable constant variable

fun main() {
    println("Hello world!")

    var age: Int = 30   // Mutable variable

    println("Name: $name, Age: $age")   // String interpolation to print variables

    println("After 2 years...")

    age += 2
    println("Name: $name, Age: $age")

    val customers = 10  // Type inference allows us to omit the type declaration
    println("There are $customers customers")
    println("There are ${customers + 1} customers") // We can also use expressions inside string interpolation
}