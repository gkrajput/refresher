# **Java Garbage Collection (GC) – Complete Guide**

A comprehensive overview of Java’s Garbage Collection mechanisms: how they work, how to tune them, and when to use which collector. This guide is designed for both real-world engineering and high-level interview preparation.

---

## Java Memory Model

Understanding GC begins with understanding the heap’s structure. Java uses a generational hypothesis: most objects die young.

### Heap Layout

* **Young Generation**
* **Eden:** Where new objects are initially allocated.
* **Survivor Spaces (S0/S1):** Where objects that survive a Young GC are moved.
* **Old Generation (Tenured):** Where long-lived objects reside after surviving multiple rounds of Young GC.
* **Metaspace:** Stores class metadata (replaces the older PermGen).

```
+-------------------+
|     Young Gen     |
|  +----+ +-------+ |
|  |Eden| |Survivor| |
|  +----+ +-------+ |
+-------------------+
|     Old Gen        |
+-------------------+
|    Metaspace       |
+-------------------+
```

---

## Why GC Exists

Java Garbage Collection automates memory management to:

1. **Reclaim memory** from unreachable objects.
2. **Prevent memory leaks** by clearing unreferenced data.
3. **Reduce complexity** by removing the need for manual `delete` calls (common in C++).

---

## Types of Garbage Collectors

### 1. Serial GC

A single-threaded collector that triggers a "Stop-The-World" (STW) pause for all phases.

* **Best for:** Small heaps (< 2GB), single-core systems, or embedded devices.
* **Flag:** `-XX:+UseSerialGC`

### 2. Parallel GC (Throughput Collector)

Uses multiple threads for both minor and major collections to maximize CPU usage for the application.

* **Best for:** Batch processing or background tasks where throughput is more important than pause times.
* **Flag:** `-XX:+UseParallelGC`

### 3. CMS (Concurrent Mark-Sweep) `Deprecated`

Designed to reduce pause times by performing most work concurrently with application threads.

* **Cons:** Causes memory fragmentation and has high CPU overhead.
* **Flag:** `-XX:+UseConcMarkSweepGC`

### 4. G1GC (Garbage-First GC)

The default collector since Java 9. It divides the heap into many small regions.

* **Key Features:** Predictable pauses, incremental compaction, and "Garbage-First" prioritization.
* **Flag:** `-XX:+UseG1GC`

### 5. ZGC (Z Garbage Collector)

An ultra-low latency collector (< 2ms pauses) regardless of heap size.

* **Mechanisms:** Colored pointers, Load barriers, and fully concurrent relocation.
* **Flag:** `-XX:+UseZGC`

### 6. Shenandoah GC

Developed by Red Hat, it provides low-pause times by performing compaction concurrently with application threads.

* **Flag:** `-XX:+UseShenandoahGC`

---

## GC Phases Explained

| Phase | Description |
| --- | --- |
| **Young GC** | Moves objects from Eden → Survivor → Old. Fast and frequent. |
| **Old GC** | Reclaims space in the Old Gen. Usually concurrent in G1/ZGC. |
| **Compaction** | Re-arranges memory to eliminate fragmentation (ZGC/Shenandoah do this concurrently). |

---

## GC Tuning Basics

### Common JVM Flags

```bash
-Xms4g                 # Initial heap size
-Xmx4g                 # Max heap size
-XX:MaxGCPauseMillis=200 # Target pause time

```

### Collector-Specific Tuning

* **G1GC:** `-XX:MaxGCPauseMillis=100` (Lowering this value makes GC work harder to stay under the limit).
* **ZGC:** `-Xms16g -Xmx16g` (ZGC performs best when the heap is large and fixed).

---

## GC Logs & Monitoring

### Enable Logging (Java 9+)

```bash
-Xlog:gc*,safepoint:file=gc.log:time,uptime,level,tags

```

### Recommended Tools

* **CLI:** `jcmd`, `jstat`
* **GUI:** VisualVM, GCViewer
* **Production:** JFR (Java Flight Recorder), Grafana + Prometheus

---

## G1GC vs ZGC

| Feature | G1GC | ZGC |
| --- | --- | --- |
| **Pause Time** | Low (Targeted) | Ultra-low (< 2ms) |
| **Concurrency** | Mostly concurrent | Fully concurrent |
| **Max Heap Support** | ~16 TB | Many TBs |
| **Default?** | Yes (Java 9+) | No |
| **Ideal for** | Latency + Throughput | Extreme Latency Sensitivity |

---

## Common GC Interview Questions

> **1. What is Stop-The-World (STW)?**
> A pause where the JVM suspends all application threads to perform critical GC work safely.

> **2. Difference between Minor GC and Major GC?**
> Minor GC targets the Young Generation (fast). Major GC targets the Old Generation (slower, may involve compaction).

> **3. Why was CMS deprecated?**
> It suffered from memory fragmentation and required complex tuning; G1GC is more efficient for most use cases.

> **4. How does G1 decide what to collect first?**
> It tracks how much "garbage" is in each region and collects the regions with the most reclaimable space first.

> **5. What are GC Roots?**
> The starting points for reachability analysis: Active thread stacks, Static fields, JNI references, and System class loaders.

> **6. Does Java GC use reference counting?**
> No, it uses **Tracing GC**. Reference counting fails to handle cyclic references (Object A points to B, and B points to A).

> **7. When should you use Parallel GC?**
> On systems with high CPU availability where the goal is maximum throughput (like batch jobs) and long pauses aren't a concern.

> **8. What is fragmentation?**
> When free memory is split across non-contiguous regions. Fixed by *compaction*.

> **9. Why is ZGC almost pause-less?**
>* Colored pointers
>* Concurrent relocation
>* Load barriers
>* Region-based heap

> **10. How to detect memory leaks?**
> * Heap dumps
> * JFR
> * jmap
> * MAT (Eclipse Memory Analyzer Tool)

> **11. What causes "OutOfMemoryError"?**
> * Heap full
> * Metaspace full
> * Direct memory exhausted
> * GC overhead limit exceeded

---

