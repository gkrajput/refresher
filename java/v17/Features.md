# Java 17 Features

Java 17 is a **Long-Term Support (LTS)** release that brings major improvements in performance, security, language features, and JVM enhancements.

---

## 🚀 Key Features Introduced in Java 17

### 1. **Sealed Classes (Standard Feature)**

Restrict which classes can extend or implement a class/interface.

```java
public sealed class Vehicle permits Car, Bike {}
```

### 2. **Pattern Matching for `instanceof` (Standard Feature)**

Cleaner type-checking.

```java
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}
```

### 3. **Records (Standard Feature)**

Immutable data-carrier classes with minimal syntax.

```java
public record User(String name, int age){}
```

### 4. **Text Blocks (from Java 13/15 → widely used in 17)**

Multi-line string literals.

```java
String json = """
{
  "name": "John",
  "age": 30
}
""";
```

### 5. **Switch Expressions (Standard Feature)**

More concise switch.

```java
int num = switch (day) {
    case MONDAY -> 1;
    case TUESDAY -> 2;
    default -> 0;
};
```

### 6. **Enhanced Pseudo-random Number Generators (PRNG)**

New algorithm families like LXM.

```java
RandomGenerator g = RandomGenerator.of("L64X128MixRandom");
```

### 7. **Strong Encapsulation of JDK Internals**

Prevents reflective access to internal APIs.

### 8. **Removed / Deprecated Features**

* Applets removed
* Nashorn JS engine removed
* Security manager deprecated

### 9. **New macOS Rendering Pipeline (Metal)**

Improved performance on macOS.

### 10. **Foreign Function & Memory API (Incubator in 17)**

Interacting with native libraries without JNI.

---

## 🆚 Comparison: Java 8 vs Java 17

| Feature            | Java 8  | Java 17               |
| ------------------ | ------- | --------------------- |
| LTS Release        | Yes     | Yes                   |
| Lambdas            | ✅       | ✅                     |
| Streams API        | Basic   | Enhanced              |
| Records            | ❌       | ✅                     |
| Sealed Classes     | ❌       | ✅                     |
| Pattern Matching   | ❌       | Partial support       |
| Text Blocks        | ❌       | ✅                     |
| Switch Expressions | ❌       | ✅                     |
| Performance        | Good    | Much Faster           |
| G1 GC              | Default | Improved              |
| ZGC                | ❌       | Experimental/Improved |
| PRNG API           | ❌       | New modern API        |
| Security           | Basic   | Strong Encapsulation  |

Java 17 essentially modernizes Java with concise syntax, faster performance, and better memory management.

---

## 🔥 Java 17 Interview Questions

### 1. What are sealed classes and why are they useful?

They provide controlled inheritance, improving security and design clarity.

### 2. Difference between Records vs Classes?

* Records: immutable, auto-generated boilerplate
* Classes: fully customizable

### 3. How do switch expressions differ from old switch statements?

Switch expressions return values and avoid fall-through.

### 4. What is pattern matching in Java 17?

It simplifies `instanceof` with automatic casting.

### 5. What problems do text blocks solve?

They allow multi-line strings without escape characters.

### 6. What is the Foreign Function & Memory API?

A replacement for JNI, more efficient and safer.

### 7. What JDK internals were strongly encapsulated?

The `sun.*` APIs are no longer accessible.

### 8. What are the new PRNG algorithms?

LXM, SplitMix, Xoshiro, etc. offering reproducible randomness.

### 9. Why is Java 17 recommended for migration from Java 8?

* Large syntax upgrades
* Better garbage collectors
* Faster performance
* Long-term support

---

## 📌 Why Java 17 Matters

* Cleaner syntax (records, text blocks, switch expressions)
* High-performance garbage collection
* Stronger security
* Modern tooling and JVM improvements
* LTS stability

