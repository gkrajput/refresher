# Java 21 Features

Java 21 is a **Long-Term Support (LTS)** release and one of the biggest updates in modern Java history. It includes finalized Project Loom features, major language enhancements, performance boosts, and new APIs.

---

## 🚀 Key Features Introduced in Java 21 (LTS)

### 1. **Virtual Threads (Final)**

Part of Project Loom — lightweight threads to handle massive concurrency.

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> System.out.println(Thread.currentThread()));
}
```

**Benefits:**

* Handles thousands/millions of concurrent requests
* Replaces complex reactive frameworks

---

### 2. **Record Patterns (Final)**

Pattern matching for destructuring objects.

```java
record Point(int x, int y) {}

static void print(Point p) {
    if (p instanceof Point(int x, int y)) {
        System.out.println(x + ", " + y);
    }
}
```

---

### 3. **Pattern Matching for `switch` (Final)**

Cleaner and safer switch logic.

```java
String format(Object obj) {
    return switch (obj) {
        case Integer i -> "int: " + i;
        case String s -> "string: " + s;
        default -> "unknown";
    };
}
```

---

### 4. **Sequenced Collections (New API)**

Provides consistent ordering for collections.

```java
SequencedCollection<String> seq = new ArrayList<>();
seq.addFirst("A");
seq.addLast("B");
```

---

### 5. **String Templates (Preview)**

Inline expressions inside string templates.

```java
String name = "John";
int age = 30;
String msg = STR."Hello \{name}, age \{age}";
```

---

### 6. **Scoped Values (Preview)**

A lightweight alternative to ThreadLocal.

```java
static final ScopedValue<String> USER = ScopedValue.newInstance();
```

Improves context passing across virtual threads.

---

### 7. **Foreign Function & Memory API (Final)**

High-performance native interop without JNI.

```java
try (Arena arena = Arena.ofConfined()) {
    MemorySegment segment = arena.allocate(100);
}
```

---

### 8. **Generational ZGC (Final)**

Ultra-low pause garbage collector.

* Great for high-throughput systems
* Sub-millisecond GC pauses

---

## 🆚 Comparison: Java 17 vs Java 21

| Feature                     | Java 17          | Java 21                |
| --------------------------- | ---------------- | ---------------------- |
| LTS Support                 | Yes              | Yes                    |
| Virtual Threads             | ❌ No             | ✅ Final                |
| Pattern Matching for switch | Partial          | Final                  |
| Record Patterns             | ❌ No             | ✅ Final                |
| String Templates            | ❌ No             | Preview                |
| Sequenced Collections       | ❌ No             | ✅ Yes                  |
| FFM API                     | Incubator        | Final                  |
| ZGC                         | Improved         | Generational ZGC       |
| Concurrency Model           | Platform threads | Virtual threads (Loom) |

Java 21 is essentially the **future of Java’s concurrency model**.

---

## 🔥 Java 21 Interview Questions

### 1. What are virtual threads?

Lightweight threads designed for massive concurrency, replacing the need for reactive frameworks.

### 2. Difference between platform and virtual threads?

* Platform threads = OS threads
* Virtual threads = JVM-managed lightweight threads

### 3. What are record patterns?

A destructuring mechanism for pattern matching.

### 4. What is pattern matching for switch?

A safer and more expressive switch with type patterns.

### 5. What are sequenced collections?

Interfaces guaranteeing first/last element access across lists, sets, and maps.

### 6. What problem do scoped values solve?

Safe, fast context propagation without ThreadLocal.

### 7. What is the Foreign Function & Memory API?

A modern API for accessing native memory and calling native libraries.

### 8. How do virtual threads impact scalability?

They enable handling millions of concurrent tasks with minimal overhead.

---

## 📌 Why Java 21 Is a Big Deal

* Project Loom (virtual threads) completes
* Modern pattern matching everywhere
* Native interop gets easier and faster
* Massive performance improvements
* Simplifies codebases drastically

