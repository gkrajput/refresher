package practice

fun main() {
    // Lists store items in the order that they are added, and allow for duplicate items.
    println("--- Lists Example ---")

    // Immutable list
    val fruits = listOf("Apple", "Banana", "Orange", "Apple")
    println("Fruits: $fruits")
    println("First fruit: ${fruits[0]}")
    println("Number of fruits: ${fruits.size}")

    // Mutable list
    val vegetables = mutableListOf("Carrot", "Broccoli", "Spinach")
    vegetables.add("Cabbage")
    println("Vegetables: $vegetables")

    // Explicit type declaration
    val marks: List<Int> = listOf(85, 90, 78, 92)
    println("Marks: $marks")
    val students: MutableList<String> = mutableListOf("Alice", "Bob", "Charlie")
    println("Students: $students")

    // Casting mutable list to immutable list
    val vegetablesLocked: List<String> = vegetables
    println("First vegetable: ${vegetablesLocked.first()}")
    println("Last vegetable: ${vegetablesLocked.last()}")
    println("Vegetables count: ${vegetablesLocked.count()}")
    println("Vegetables starting with C: ${vegetablesLocked.count { it.startsWith("C")} }")
    println("Is Tomato in the list? ${vegetablesLocked.contains("Tomato")}")
    println("Is Potato in the list? ${"Potato" in vegetablesLocked}")

    vegetables.remove("Carrot")
    println("Vegetables starting with C: ${vegetablesLocked.count { it.startsWith("C")} }")


    // Sets store unique items and do not maintain any order.
    println("--- Sets Example ---")

    // Immutable set
    val uniqueFruits = setOf("Apple", "Banana", "Orange")
    println("Unique Fruits: $uniqueFruits")

    // Mutable set
    val uniqueVegetables = mutableSetOf("Carrot", "Broccoli", "Spinach")
    uniqueVegetables.add("Broccoli")
    uniqueVegetables.remove("Chilli")
    println("Unique Vegetables: $uniqueVegetables")

    println("Is Carrot in the set? ${"Carrot" in uniqueVegetables}")
    println("How many unique vegetables? ${uniqueVegetables.size}")
    println("Count of vegetables starting with S: ${uniqueVegetables.count { it.startsWith("S") }}")


    // Maps store key-value pairs, where each key is unique and maps to a single value.
    println("--- Maps Example ---")

    // Immutable map
    val countryCapitals = mapOf("USA" to "Washington D.C.", "France" to "Paris", "Japan" to "Tokyo")
    println("Country Capitals: $countryCapitals")
    println("Capital of France: ${countryCapitals["France"]}")

    // Mutable map
    val studentGrades = mutableMapOf("Alice" to 85, "Bob" to 90)
    studentGrades["Charlie"] = 78
    println("Student Grades: $studentGrades")
    studentGrades["Alice"] = 88
    println("Updated Student Grades: $studentGrades")
    studentGrades.remove("Alice")
    println("Alice grades: ${studentGrades["Alice"]}")
    println("Is Alice in the map? ${"Alice" in studentGrades}")
    println("Student count: ${studentGrades.size}")
    println("Count of students with grade above 80: ${studentGrades.count { it.value > 80 }}")
    println("Students with grade above 80: ${studentGrades.filter { it.value > 80 }.keys}")
    println("Students: ${studentGrades.keys}")
    println("Grades: ${studentGrades.values}")
}