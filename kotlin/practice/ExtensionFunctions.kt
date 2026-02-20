package practice

// Extension functions allow you to add new functions to existing classes without modifying their source code.
// This is particularly useful when you want to add functionality to classes from third-party libraries or the standard library.

// Example of an extension function for the String class
fun String.isPalindrome(): Boolean {
    // this refers to the String instance on which the function is called
    val cleanedString = this.replace("\\s".toRegex(), "").lowercase()
    return cleanedString == cleanedString.reversed()
}

class Emp(val name: String, var age: Int) {
    fun isAdult(): Boolean {
        return this.age >= 18
    }
}

// Extension function for the Emp class to check if the employee is a senior
fun Emp.isSenior(): Boolean {
    return this.age >= 60
}

fun main() {
    val word1 = "A man a plan a canal Panama"
    val word2 = "Hello"
    println("$word1 is a palindrome: ${word1.isPalindrome()}") // Output: true
    println("$word2 is a palindrome: ${word2.isPalindrome()}") // Output: false

    val employee1 = Emp("Alice", 30)
    val employee2 = Emp("Bob", 65)
    println("${employee1.name} is an adult: ${employee1.isAdult()}") // Output: true
    println("${employee1.name} is a senior: ${employee1.isSenior()}") // Output: false
    println("${employee2.name} is an adult: ${employee2.isAdult()}") // Output: true
    println("${employee2.name} is a senior: ${employee2.isSenior()}") // Output: true
}