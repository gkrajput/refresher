# G1GC vs ZGC — Complete Guide

## Overview
Modern JVMs provide advanced garbage collectors designed to deliver low pause times and predictable latency.  
Two of the most important GC algorithms introduced in recent Java versions are:

- **G1GC (Garbage First Garbage Collector)** — Default since Java 9
- **ZGC (Z Garbage Collector)** — Ultra-low latency collector (sub-millisecond pauses)

This document explains how each GC works and compares their strengths, weaknesses, and usage scenarios.

---

# 1. G1GC (Garbage First GC)

## 1.1 What is G1GC?
G1GC is a **server-style**, **low-pause**, **region-based**, and **parallel** garbage collector designed to replace CMS.

It attempts to meet user-defined pause-time goals (default: 200ms) while maintaining high throughput.

---

## 1.2 How G1GC Works

### ✔ Region-based Heap Layout
- Heap is divided into **hundreds of fixed-size regions** (1–32 MB each)
- Regions are dynamically assigned roles:
    - Eden
    - Survivor
    - Old
    - Humongous

### ✔ Evacuating Collector
G1GC uses **copying/evacuation** for cleanup:
- Live objects are moved to empty regions
- Fragmentation is eliminated automatically

### ✔ Garbage-First Strategy
- Finds regions with the **most garbage ("garbage-first")**
- Cleans those first to maximize benefit per pause

### ✔ Concurrent + Incremental
G1 performs many operations concurrently:
- Marking live objects
- Remembered sets updates
- Region refinements

### ✔ Pause-Time Goals

You can tell G1GC: `-XX:MaxGCPauseMillis=200`

G1 attempts to stay within that.

---

## 1.3 When to Use G1GC
- Default GC for modern Java
- Balanced latency + throughput workloads
- Large heaps (8 GB – 64 GB)
- General server applications
- SaaS, API servers, microservices

---

# 2. ZGC (Z Garbage Collector)

## 2.1 What is ZGC?
ZGC is a **scalable**, **region-based**, **concurrent**, **low-latency** GC targeting **<1ms pause times**, even on **multi-terabyte heaps**.

Designed for:
- Ultra-low latency
- Very large memory applications
- High-traffic services

---

## 2.2 How ZGC Works

### ✔ Colored Pointers
ZGC uses **load barriers** + **colored object pointers** to track object states:
- Marked
- Relocated
- Remapped

This allows almost all GC phases to run concurrently with the application.

### ✔ Region-based Heap
Heap split into **huge, small, and medium** regions:
- Small: 2 MB
- Medium: 32 MB
- Large: >32 MB

### ✔ Concurrent Relocation
Objects are moved (relocated) concurrently **without stopping application threads**.

### ✔ Pause Time Guarantee
ZGC guarantees: Pauses < 1ms (sub-millisecond)

### ✔ Extremely Scalable
Supports heap sizes from: 8 MB → 16 TB


---

## 2.3 When to Use ZGC
- Real-time systems
- High-frequency trading (HFT)
- Latency-sensitive applications
- Gaming servers
- ML inference services
- Microservices needing consistent sub-millisecond latency
- Very large heap usage (>64 GB)

---

# 3. G1GC vs ZGC — Comparison

| Feature | G1GC | ZGC |
|--------|------|------|
| **Introduction** | Java 7u4, default since Java 9 | Java 11 (experimental), PROD since Java 15 |
| **Goal** | Low pauses + high throughput | Ultra-low latency (<1 ms pauses) |
| **Heap Size Support** | Best for 8 GB – 64 GB | 8 MB – 16 TB |
| **Pause Times** | 10–200 ms | <1 ms (sub-millisecond) |
| **Compaction** | Mixed phases, incremental | Fully concurrent relocation |
| **Fragmentation** | Eliminated through region evacuation | Eliminated through concurrent relocation |
| **Algorithm Type** | Mostly concurrent + region copying | Fully concurrent + colored pointers |
| **Throughput** | High | High, but slightly lower than G1GC |
| **Latency** | Medium-low | Ultra-low |
| **Tuning Required** | Moderate | Minimal tuning |
| **Best For** | General server workloads | Real-time, ultra-low-latency workloads |
| **Default JVM GC?** | Yes (Java 9–20) | Not default, must enable manually |

---

# 4. How to Enable Each GC

## G1GC (default)
Enable explicitly:
```bash
-XX:+UseG1GC
```

## ZGC (Java 15+)
Enable with:
```bash
-XX:+UseZGC
```

# 5. When to Choose Which?
## Choose G1GC if:

* You want good performance without many constraints
* Application heap < 64 GB
* Occasional 50–100 ms pauses are acceptable
* Standard enterprise server/API workloads

## Choose ZGC if:

* Latency must be extremely predictable
* Pauses > 1ms are unacceptable
* You use large heaps (100 GB+)
* You run real-time workloads
* High throughput + low latency is critical simultaneously

# 6. Summary

* G1GC → Balanced GC with predictable low pauses (10–200 ms)
* ZGC → Ultra-low latency GC with sub-millisecond pauses
* ZGC is the future for latency-sensitive and large-memory workloads
* G1GC remains a good default for general applications

---

# G1GC vs ZGC: Comprehensive Comparison
## High-level summary

- **G1GC (Garbage-First)**: **Generational**, region-based collector optimized for **good throughput with predictable pause-time goals** (typically tens of ms) on multi‑GB heaps; it’s the long-time **general-purpose default**. 
- **ZGC**: **Low-latency** collector designed to keep **GC pauses extremely small (typically sub‑millisecond to a few milliseconds)** even with very large heaps, by doing most work concurrently and using load barriers/colored pointers. 

## Core differences

### 1) Latency (pause times)
- **G1GC** targets a user-specified pause goal (e.g., `-XX:MaxGCPauseMillis=200`) and tries to meet it, but pauses can still grow under pressure (allocation bursts, fragmentation, humongous allocations, mixed collections). 
- **ZGC** is built to keep **stop-the-world phases very short**, moving most marking/relocation concurrently; long pauses are much rarer, making it a strong choice when tail latency matters. 

### 2) Throughput / CPU overhead
- **G1GC** often provides **better raw throughput** for many “normal server” workloads because it relies less on per-access barriers and concurrent relocation, though tuning and workload shape matter. 
- **ZGC** typically spends **more CPU** doing concurrent GC work and uses additional barriers, so you may trade some throughput for much lower pauses (often a good trade for latency-sensitive services).

### 3) Heap size scaling
- **G1GC** scales well into multi‑GB heaps and can handle large heaps, but very large heaps with strict pause SLOs can be difficult depending on object lifetime distribution and promotion rates. 
- **ZGC** is explicitly designed to handle **very large heaps** while keeping pauses low, making it attractive for heaps in the tens/hundreds of GB where tail latency is critical. 

### 4) Generational behavior
- **G1GC is generational** (young/old) and relies on the generational hypothesis to collect most garbage cheaply in the young generation, then occasionally do mixed collections of old regions. 
- **ZGC historically was non-generational**, focusing on concurrent collection across the heap; newer JDKs have introduced **Generational ZGC** as an option/feature line to improve throughput for typical generational workloads while maintaining low pauses. 

## Key features

### G1GC key features
- **Region-based heap** (many equal-sized regions) with incremental evacuation. 
- **Predictable pause-time targeting** via `-XX:MaxGCPauseMillis`. 
- **Concurrent marking** of old generation and “mixed” collections (young + selected old regions). 

### ZGC key features
- **Mostly concurrent** marking, relocation, and remapping; **short STW phases**.
- Uses **colored pointers / load barriers** (implementation technique) to enable concurrent relocation with minimal pauses. 
- **Designed for large heaps and low latency**, with tuning generally simpler (fewer knobs typically needed than G1 for latency). 

## Advantages and disadvantages

### G1GC advantages
- Strong **all-around default**: good throughput, good latency, mature behavior across many workloads. 
- Works well when you can tolerate **tens of ms** pauses and want robust throughput.
- Generally a safe pick for “unknown workload” server apps.

### G1GC downsides
- Can see **pause outliers** under fragmentation, heavy promotion, or humongous object patterns (workload-dependent).
- May require more tuning to consistently hit strict tail-latency SLOs at large heap sizes.

### ZGC advantages
- Excellent for **tail-latency-sensitive** workloads (interactive services, trading, real-time-ish APIs) because pauses are extremely short. 
- Handles **very large heaps** with low pause impact.
- Often simpler to operate for latency goals (less “pause-goal chasing”).

### ZGC downsides
- Can cost more **CPU** and sometimes reduce peak throughput vs G1 (workload dependent).
- Requires a reasonably modern JDK and benefits from modern CPUs due to barrier costs.

## When to choose which (rule of thumb)

**Choose G1GC when:**
- You want a **general-purpose** collector with good throughput and reasonable pauses.
- Your latency budget allows **~10–200ms** occasional pauses (depending on heap/workload).
- You prefer a very widely deployed default with extensive operational familiarity.

**Choose ZGC when:**
- You have **strict tail latency SLOs** (p99/p999) and GC pauses are hurting you.
- You run **large heaps** and want to avoid long stop-the-world compaction pauses.
- You’re willing to trade some CPU/throughput for consistently low pauses.

## Enabling (quick flags)

- **G1GC** (often default on modern JDKs, but explicit): `-XX:+UseG1GC`.
- **ZGC**: `-XX:+UseZGC`.
