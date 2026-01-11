# Kotlin Cache Implementation
This project is an education exploration of cache design and key-value stores, implemented in Kotlin.
The goal is to progressively build a cache system while learning common real-world caching concepts.
---
## Project Structure
The project is organized into incremental levels of complexity:
### Level 1 - Simple Cache
- In-memory key-value store
- Operations:
  - `put`
  - `get`
  - `remove`
  - `clear`
- Single-threaded

### Level 2 - Enhanced Cache
- Builds on Level 1
- Adds:
  - Capacity Limits
  - TTL (time-to-live) support
  - Eviction Policies
    - FIFO (First-In, First-Out)
    - LRU (Least Recently Used)
- Eviction policies are pluggable and tested independently

## Testing
Tests are written using kotlin.test
Run test with:
```bash
./gradle test
```