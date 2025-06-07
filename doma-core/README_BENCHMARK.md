# Performance Benchmarks

This module contains JMH benchmarks comparing the performance of optimized implementations
with their classic counterparts:

1. **SqlTokenUtil** (`org.seasar.doma.internal.util`) - Character classification utilities
2. **SqlTokenizer** (`org.seasar.doma.internal.jdbc.sql`) - SQL tokenization and parsing

## Benchmark Locations

- `src/test/java/org/seasar/doma/internal/util/SqlTokenUtilBenchmark.java`
- `src/test/java/org/seasar/doma/internal/jdbc/sql/SqlTokenizerBenchmark.java`

## Running the Benchmarks

From the project root directory:

```bash
./gradlew :doma-core:jmh
```

## Benchmark Parameters

The actual benchmark parameters used are defined in the benchmark classes (annotations override Gradle task settings):

- **Forks**: 2 (number of JVM instances)
- **Warmup**: 5 iterations, 1 second each
- **Measurement**: 10 iterations, 1 second each
- **Time Unit**: Nanoseconds (average time per operation)
- **JVM Args**: `-Xms2G -Xmx2G`

## Expected Results

### SqlTokenUtil
The optimized implementation using bitmasks should show significant performance improvements:
- `isWordPart`: ~30-50% faster for ASCII characters
- `isWhitespace`: ~20-40% faster for common whitespace

### SqlTokenizer
The optimized tokenizer should show improvements across different SQL types:
- Simple SQL: ~15-25% faster
- Complex SQL: ~20-30% faster
- Comment-heavy SQL: ~25-35% faster (due to optimized comment parsing)
- Keyword-heavy SQL: ~20-30% faster (due to optimized keyword detection)
- Directive-heavy SQL: ~30-40% faster (due to streamlined directive parsing)

## Understanding the Results

- Lower values are better (measured in nanoseconds per operation)
- The `optimized*` benchmarks test the current implementations
- The `classic*` benchmarks test the preserved original implementations
- Different SQL types test various aspects of tokenizer performance