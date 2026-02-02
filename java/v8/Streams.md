# **Streams API**

Streams API provides a functional, declarative way to process collections, arrays, and I/O data.

## **📌 What is a Stream?**

A Stream is a sequence of data elements supporting sequential and parallel operations.

* Not a data structure
* Does **not** store elements
* Does **not** modify the underlying collection
* Operations can be lazy, pipelined, and parallelizable

**Analogy**: Think of a stream as a conveyor belt on which elements flow and operations process them.

## **📌 Stream Pipeline**

A pipeline consists of three parts: Source, Intermediate Operations, and Terminal Operation.

**1. Source:**
```java
// Source
List<Integer> list = Arrays.asList(1, 2, 3);
Stream<Integer> stream = list.stream();
```

**2. Intermediate Operations (return a stream):**

* Lazy evaluation
* Don't execute until a terminal operation is called
* Examples: map(), filter(), sorted(), distinct(), limit(), skip()

**3. Terminal Operations (produce a result):**

* Triggers execution
* Returns non-stream value
* Examples: collect(), forEach(), reduce(), count(), findFirst(), findAny()


## **Examples**

```java
List<String> result = list.stream()
                         .filter(s -> s.startsWith("A"))
                         .collect(Collectors.toList());

// Sorting employees by salary
employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary))
        .toList();

// Paginating a list
List<Integer> page2 = nums.stream()
        .skip(5)
        .limit(5)
        .toList();

// Creating a Map from a List
Map<String, Integer> map = employees.stream()
        .collect(Collectors.toMap(
                Employee::getName,
                Employee::getSalary
        ));

// Reducing to a single value
int sum = nums.stream()
        .reduce(0, (a, b) -> a + b);

// Finding the top 3 highest paid employees
List<Employee> top3 = employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary).reversed())
        .limit(3)
        .toList();

// Counting employees in each department
Map<String, Long> count =
        employees.stream().collect(
                Collectors.groupingBy(Employee::getDepartment, Collectors.counting())
        );

// Parsing CSV string to List
String csv = "apple, banana, cherry, date";
List<String> list = Arrays.stream(csv.split(","))
        .map(String::trim)
        .toList();

// Sum of squares of even numbers
int result = nums.stream()
        .filter(n -> n % 2 == 0)
        .map(n -> n * n)
        .reduce(0, Integer::sum);
```

## **📌 Collectors — Very Important**

Collectors are used to accumulate elements into collections or other data structures.

**🧬 How a Collector Works Internally**

A collector has 4 components:

* **Supplier** – creates the result container
* **Accumulator** – adds an element
* **Combiner** – merges two result containers (parallel streams)
* **Finisher** – final transformation (optional)

```java
Collector<T, List<T>, List<T>> toList = new Collector<>() {
    Supplier<List<T>> supplier = ArrayList::new;
    BiConsumer<List<T>, T> accumulator = List::add;
    BinaryOperator<List<T>> combiner = (l1, l2) -> { l1.addAll(l2); return l1; };
    Function<List<T>, List<T>> finisher = Function.identity();
};
```

**📌 Collector Characteristics**

A Collector can have flags:

* UNORDERED – order doesn’t matter
* CONCURRENT – safe for parallel operations
* IDENTITY_FINISH – finisher is identity
* toList() is not concurrent.
* groupingByConcurrent() is concurrent.

**Examples**

```java
List<String> names = stream.collect(Collectors.toList());
Set<String> set = list.stream().collect(Collectors.toSet());

// Collect into a specific collection
LinkedList<String> result = list.stream()
        .collect(Collectors.toCollection(LinkedList::new));

// Join strings with delimiter
String res = names.stream()
        .collect(Collectors.joining(", "));

// Counting elements
long count = list.stream().collect(Collectors.counting());

// Summing and averaging
int sum = list.stream().collect(Collectors.summingInt(x -> x));
double avg = list.stream().collect(Collectors.averagingInt(x -> x));
IntSummaryStatistics stats = list.stream().collect(Collectors.summarizingInt(x -> x));

// Grouping by department
Map<String, List<Employee>> byDept = employees.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment));

// Grouping by with downstream collector
Map<String, Long> countByDept =
        employees.stream().collect(
                Collectors.groupingBy(Employee::getDepartment, Collectors.counting())
        );

// Mapping salaries by department
Map<String, List<Integer>> salaries =
        employees.stream().collect(
                Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.mapping(Employee::getSalary, Collectors.toList())
                )
        );
// Partitioning even and odd numbers
Map<Boolean, List<Integer>> evenOdd =
        nums.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));

// Creating a Map from a List
Map<Integer, String> idName =
        people.stream().collect(Collectors.toMap(Person::getId, Person::getName));

// Counting word frequencies or Handling duplicates
Map<String, Long> freq =
        words.stream().collect(Collectors.toMap(
                w -> w,
                w -> 1L,
                Long::sum
        ));

// Reducing with Collectors
int sum = list.stream()
        .collect(Collectors.reducing(0, Integer::sum));

// Custom Collector: Collecting to a TreeSet
Collector<String, ?, TreeSet<String>> toTreeSet = Collectors.toCollection(TreeSet::new);
TreeSet<String> treeSet = stream.collect(toTreeSet);

// FlatMapping example: Grouping skills by department
Map<String, List<String>> map =
        employees.stream().collect(
                Collectors.groupingBy(
                        Employee::getDept,
                        Collectors.flatMapping(e -> e.getSkills().stream(), Collectors.toList())
                )
        );

// Post-processing: Collecting and then Unmodifiable List
List<String> names =
        employees.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                )
        );

// Teeing Collector: Summarizing sum and average (Java 12+)
var result = list.stream().collect(
        Collectors.teeing(
                Collectors.summingInt(x -> x),
                Collectors.averagingInt(x -> x),
                (sum, avg) -> new Summary(sum, avg)
        )
);

// Nested Grouping: Employees by Department and Role
Map<String, Map<String, Long>> result =
        employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.groupingBy(
                                Employee::getRole,
                                Collectors.counting()
                        )
                ));
```

**⚠️ Common Pitfalls With Collectors**

```java
// 1. Using toMap with duplicates
Collectors.toMap(k -> k, v -> v); // throws IllegalStateException
// Fix with merge function
Collectors.toMap(k -> k, v -> v, (a, b) -> a);

// 2. Using parallel streams with non-concurrent collectors
parallelStream().collect(Collectors.toList());  // Not thread-safe
// Fix
parallelStream().collect(Collectors.toCollection(CopyOnWriteArrayList::new));
// OR
parallelStream().collect(Collectors.groupingByConcurrent(...));

// 3. Using reducing() when simpler collectors exist
stream.collect(Collectors.reducing(0, Integer::sum)); // verbose
// Fix
stream.collect(Collectors.summingInt(x -> x));

// 4. Forgetting that groupingBy returns sorted maps only if specified
Collectors.groupingBy(...);        // produces HashMap
Collectors.groupingBy(..., TreeMap::new, ...); // sorted
```

## **📌 Parallel Streams**

Used for data that can be processed independently.

* Do NOT use with shared mutable state
* Overhead may make sequential faster
* Great for CPU-heavy computations

**⚠ But parallel streams are NOT always faster.**

They are best when:

* Data set is large
* CPU-bound operations
* No shared mutable state
* Multi-core CPU available
* No complex pipeline overhead

```java
nums.parallelStream()
    .map(n -> n * 2)
    .forEach(System.out::println);
```

## **📌 Infinite Streams**

Created using `Stream.generate()` or `Stream.iterate()`.

```java
Stream.iterate(0, n -> n + 1)
        .limit(10)
        .forEach(System.out::println);

Stream.generate(Math::random)
        .limit(5)
        .forEach(System.out::println);
```

## **📌 Short-Circuiting Operations**

TShort-circuiting operations can terminate the stream pipeline early, without processing all elements.
This improves performance, especially with large or infinite streams.

**📌Short-Circuiting Intermediate Operations**

These can reduce or limit the number of elements passed to the next stage.

| Operation               | What it does                         | Short-circuit behavior              |
| ----------------------- | ------------------------------------ | ----------------------------------- |
| `limit(n)`              | Takes first n elements               | Stops after n elements are produced |
| `skip(n)`               | Skips first n elements               | Stops skipping after n are removed  |
| `takeWhile()` (Java 9+) | Takes elements until predicate fails | Stops once predicate returns false  |
| `dropWhile()` (Java 9+) | Drops elements until predicate fails | Stops once predicate returns false  |

**📌Short-Circuiting Terminal Operations**

These can return early without processing the entire stream.

| Operation     | Purpose                                | Short-circuit behavior                   |
| ------------- | -------------------------------------- | ---------------------------------------- |
| `anyMatch()`  | Checks if any element matches          | Stops when first match is found          |
| `allMatch()`  | Checks if all elements match           | Stops at first non-match                 |
| `noneMatch()` | Checks if no element matches           | Stops at first match                     |
| `findFirst()` | Returns first element                  | Stream ends immediately                  |
| `findAny()`   | Returns any element (fast in parallel) | Ends as soon as one element is available |


## **📌 Primitive Streams**

Primitive streams avoid boxing/unboxing overhead. IntStream, LongStream, DoubleStream

```java
int sum = IntStream.range(1, 5).sum();

// Ranges only available in primitive streams
IntStream.range(1, 5);      // 1,2,3,4
IntStream.rangeClosed(1, 5); // 1,2,3,4,5
```

## **✅ Streams for Arrays**

Java provides dedicated utilities to convert arrays into Streams.
This is especially important because arrays are not collections, so they don't have .stream() method.

You typically use:

* Arrays.stream(...)
* Stream.of(...)
* Primitive streams: IntStream, LongStream, DoubleStream

```java
String[] names = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(names);

Integer[] arr = {1, 2, 3};
Stream<Integer> s = Stream.of(arr);
```

**🚨 Primitive Arrays DO NOT create element-wise Streams with Stream.of().
Stream.of() behaves differently for primitive arrays**

```java
int[] arr = {1, 2, 3};
Stream<int[]> s = Stream.of(arr);   // ❌ Stream of ONE element (the array object)
IntStream s = Arrays.stream(arr);   // ✅ Correct: IntStream of elements

// Converting streams back to arrays
String[] arr = stream.toArray(String[]::new);
int[] arr = intStream.toArray();   // returns primitive int[]
```

**Examples**

```java
// Convert to upper case
String[] names = {"ram", "shyam", "mohan"};
String[] upper =
    Arrays.stream(names)
          .map(String::toUpperCase)
          .toArray(String[]::new);

// Sum of int array
int[] nums = {10, 20, 30};
int sum = Arrays.stream(nums).sum();   // IntStream has sum()

// Filter even numbers
int[] arr = {1, 2, 3, 4, 5};
int[] evens =
        Arrays.stream(arr)
                .filter(n -> n % 2 == 0)
                .toArray();

// Flattening a 2D int array
int[][] matrix = { {1, 2}, {3, 4}, {5, 6} };
int[] flat =
        Arrays.stream(matrix)
                .flatMapToInt(Arrays::stream)
                .toArray();

// Sorting an int array
int[] sorted =
        Arrays.stream(arr)
                .sorted()
                .toArray();

// Sorting a String array in reverse order
String[] sorted =
        Arrays.stream(names)
                .sorted(Comparator.reverseOrder())
                .toArray(String[]::new);

// For object arrays
Arrays.asList(arr).stream()   // works same as Arrays.stream(arr)

// For primitive arrays
Arrays.asList(int[])   // ❌ Produces a list with ONE element (the whole array). Reason: primitive arrays cannot be auto-boxed.
```

| Task                      | Best way                                      |
| ------------------------- | --------------------------------------------- |
| Stream of object array    | `Arrays.stream(T[])` or `Stream.of(T[])`      |
| Stream of primitive array | `Arrays.stream(int[])`                        |
| Avoid boxing/unboxing     | Use `IntStream`, `LongStream`, `DoubleStream` |
| Processing 2D arrays      | `flatMapToInt(Arrays::stream)`                |
| Range operations          | `IntStream.range()` & `rangeClosed()`         |


## **Exception Handling**

Streams encourage functional programming, but Java lambdas cannot throw checked exceptions directly.
This makes exception handling inside streams tricky. You can wrap the lambda in a try-catch block or create a utility method to handle exceptions.

### **Unchecked exceptions** (e.g., NullPointerException, ArithmeticException) behave normally in Streams:

```java
Stream.of(1, 0, 2)
      .map(n -> 10 / n)       // <-- division by zero
      .forEach(System.out::println);
```

➡ Will throw ArithmeticException immediately and terminate the pipeline.

**Behavior**:

* Stream will stop immediately
* No more elements are processed
* Exception propagates to the caller

### Java lambdas cannot throw **checked exceptions**:

#### **Wrap** checked exceptions into RuntimeException

* Pros: simple
* Cons: stack trace makes debugging harder

```java
list.stream()
    .map(n -> {
        try {
            return methodThatThrows(n);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    })
    .forEach(System.out::println);
```

#### Create a **generic wrapper** (Industry standard)

Reusable functional interface for lambdas with checked exceptions:

```java
@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}

// Wrapper
public static <T, R> Function<T, R> wrap(CheckedFunction<T, R> fn) {
    return t -> {
        try {
            return fn.apply(t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
}

// Usage
static String readFile(String file) throws IOException {
    return Files.readString(Path.of(file));
}

public static void main(String[] args) {

    List<String> files = List.of("a.txt", "b.txt", "c.txt");
    files.stream()
            .map(wrap(f -> readFile(f))) // readFile throws IOException
            .forEach(System.out::println);
}
```

#### Return **Optional** instead of failing. 

Useful when you want to skip failed elements.

```java
list.stream()
    .map(n -> {
        try {
            return Optional.of(methodThatThrows(n));
        } catch (Exception e) {
            return Optional.<ReturnType>empty();
        }
    })
    .filter(Optional::isPresent)
    .map(Optional::get)
    .forEach(System.out::println);
```

#### Other patterns

```java
// Continue stream on failure (skip failed elements)
list.stream()
    .flatMap(n -> {
        try {
            return Stream.of(method(n));
        } catch (Exception e) {
            return Stream.empty(); // skip
        }
    });

// Collect failures separately
Map<Boolean, List<String>> result =
        list.stream()
                .collect(
                        Collectors.partitioningBy(
                                s -> {
                                    try {
                                        method(s);
                                        return true;
                                    } catch (Exception e) {
                                        return false;
                                    }
                                }
                        ));
```

## **Streams vs Loops**

For many cases: **Loops are slightly faster** because:

* no lambda overhead
* no creation of pipeline objects
* no boxing/unboxing (loops can use primitive vars)

But the difference is often micro-seconds and negligible.

| Aspect               | Streams                                | Loops                                   |
|----------------------|----------------------------------------|-----------------------------------------|
| Style                | Declarative (“What to do”), functional | Imperative (“How to do it”), procedural |
| Readability          | Higher for complex operations          | Can become cluttered with nested loops  |
| Performance          | Can be optimized, parallelized         | Faster, generally sequential            |
| Mutability           | Encourages immutability                | Often involves mutable state            |
| Debugging            | Can be harder due to lazy evaluation. Must use peek() or logs | Easier to step through                  |
| Parallelism          | Built-in support with parallelStream() | Requires manual thread management       |
| Memory Usage        | May use more memory for intermediate objects | Generally lower memory footprint        |
| GC Overhead         | More frequent GC due to temporary objects | Less frequent GC                        |

### ✔ Use Streams when:

* You want concise, readable code
* Transforming/processing data
* Filtering, mapping, grouping
* Code readability matters
* You want immutability
* You want easy parallelism
* Working with collections or I/O
* You need short-circuiting (anyMatch etc.)

### ✔ Use Loops When:

* Mutating a data structure
* Performance is critical
* You need index-based access
* You need break/continue logic
* You need checked exceptions
* You must reuse iteration logic
* You're dealing with small/critical code paths

### **❌Limitations:**

* Cannot modify the source during processing
* Streams can be consumed only once
* Debugging can be tricky due to lazy evaluation
* Avoid modifying external variables inside streams
* Don’t use parallel streams with Shared Mutability

## **🎯Questions**

### **1. Can streams be reused? Why not?**

Stream is a consumable, one-time-use pipeline. A stream represents:

* A pipeline of computations
* That can be executed only one time
* And is closed after its terminal operation

Once a terminal operation runs (e.g., forEach, collect, reduce), the underlying stream is marked consumed.

Trying to reuse it throws: `IllegalStateException: stream has already been operated upon or closed`

**Why not?**

Streams use internal iterators, not external ones. When the pipeline executes:

* It binds the data source
* Iterates through it
* Uses spliterators internally
* And invalidates the pipeline afterward

Reusing would require rebuilding the entire pipeline, which violates the stream design: immutable, functional, non-replayable data flow

### **2. When should parallel streams be avoided?**

1. Tasks involve shared mutable state
```java
list.parallelStream().forEach(n -> sharedList.add(n));  // ❌ unsafe
```

2. Data set is small (overhead outweighs benefits)
3. Operations are I/O-bound rather than CPU-bound

   Parallel streams work best for heavy CPU computations, not:
    * DB calls
    * File operations
    * Network calls

   These block threads → parallelism wasted.
4. Order matters and ordering is expensive. Example: `sorted().parallel()` can be slower than sequential.
5. You are in a server environment

   Parallel streams use `ForkJoinPool.commonPool()`, which:
   * Affects all parallel stream operations across your app
   * Can lead to thread starvation in server apps
   * Interferes with other parallel tasks makes performance unpredictable
6. Complex pipelines with many intermediate operations
7. The operations are not independent. Parallelism requires pure functions (no side effects).
8. You need deterministic results. Parallelism may change: Order of execution, Timing, Thread usage

### **3. Why do Streams cause boxing/unboxing overhead?**

Stream API operates on objects, not primitives. So `Stream<Integer>` involves:

* Integer → int (unboxing)
* int → Integer (boxing)

This has CPU cost + GC pressure. Java collections store objects, not primitives. Streams over a collection therefore produce Stream<T> = object stream.

Primitive streams (**IntStream**, **LongStream**…) were added to avoid boxing:

* They have specialized methods (sum, average, max)
* They use primitive arrays internally
* They reduce GC and CPU overhead

```java
Stream<Integer> s = List.of(1, 2, 3).stream();

int sum = s.map(n -> n * 2)   // n is Integer → needs unboxing
          .reduce(0, Integer::sum);  // arguments boxed/unboxed

// Optimized
int sum = IntStream.of(1, 2, 3)
        .map(n -> n * 2)
        .sum();
```

### **4. Why not use parallel streams with shared mutable state?**

Parallel streams run your operations on multiple threads simultaneously (from the ForkJoinPool).
If these threads modify the same shared object, you get:

* Race conditions
* Data corruption
* Unexpected results
* Lost updates
* Thread-safety issues

Parallel streams assume your operations are stateless and side-effect-free.

**Shared Mutable State:**

* Shared → accessed by multiple threads
* Mutable → can be changed
* State → variables, lists, maps, counters, objects

If each thread modifies the same object:

* No synchronization
* No ordering
* No visibility guarantee
* No atomicity

So state gets corrupted.

**Parallel streams=multiple threads → touching the same variable at the same time → unpredictability.**

#### Examples:

```java
List<Integer> result = new ArrayList<>(); // shared mutable state

numbers.parallelStream()
       .forEach(n -> result.add(n));    // ❌ unsafe
```

**🔥 Problem**

* Multiple threads call add() simultaneously
* ArrayList is not thread-safe
* Internal array resizing can corrupt the array
* Final list may contain missing or duplicate values
* Program may even crash

```java
int[] counter = {0};  // shared mutable array

list.parallelStream()
    .forEach(e -> counter[0]++);  // ❌ race condition
```

**🔥 Problem**

counter[0]++ is not atomic (read → increment → write). Threads overwrite each other → incorrect final count.

```java
class Stats {
    int sum = 0;
}

Stats stats = new Stats(); // shared mutable

list.parallelStream()
    .forEach(n -> stats.sum += n);  // ❌ unsafe
```

**🔥 Problem**

Many threads writing to sum simultaneously → corrupted results.

```java
// Use concurrent collectors
Map<String, Long> map =
        list.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        s -> s,
                        Collectors.counting()
                ));
```

### **5. What are Internal Iterators?**

Internal iterators mean that the iteration logic is controlled by the API, not by you.

In simple words:

    👉 The library decides how and when to iterate over the elements.
    👉 You only describe what to do, not how to loop.

**External Iterator (loop):**
You drive the car → control steering, brakes, acceleration.

**Internal Iterator (stream):**
You sit in a cab → the driver (API) takes you where you need.



