package practice

fun main() {
    val isKotlinFun: Boolean = true  // Boolean type

    val byteValue: Byte = 127         // Byte type (8-bit signed integer)
    val shortValue: Short = 32767     // Short type (16-bit signed integer)
    val intValue: Int = 2147483647   // Int type (32-bit signed integer)
    val longValue: Long = 9223372036854775807L // Long type (64-bit signed integer)

    val unsignedByteValue: UByte = 255u // Unsigned Byte type (8-bit unsigned integer)
    val unsignedShortValue: UShort = 32767u   // Unsigned Short type (16-bit unsigned integer)
    val unsignedIntValue: UInt = 4294967295u // Unsigned Int type (32-bit unsigned integer)
    val unsignedLongValue: ULong = 18446744073709551615uL // Unsigned Long type (64-bit unsigned integer)

    val floatValue: Float = 3.14f   // Float type (32-bit floating point)
    val doubleValue: Double = 3.141592653589793 // Double type (64-bit floating point)

    val charValue: Char = 'A'        // Char type (16-bit Unicode character)
    val stringValue: String = "Hello, Kotlin!" // String type

    // Declare variable without initializing it immediately
    val uninitializedInt: Int
    uninitializedInt = 42 // Now we can assign a value to it

    val uninitializedLong: Long
    // It's usage will cause a compile-time error because it's not initialized yet
    // println(uninitializedLong)

}