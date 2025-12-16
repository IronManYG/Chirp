# Testing

This document covers testing strategies, commands, and best practices for Chirp across all
platforms.

## Table of Contents

- [Overview](#overview)
- [Test Source Locations](#test-source-locations)
- [Running Tests](#running-tests)
- [Test Dependencies](#test-dependencies)
- [Writing Tests](#writing-tests)
- [Troubleshooting](#troubleshooting)

## Overview

Chirp uses Kotlin Multiplatform testing with platform-specific test runners:

| Platform | Test Type          | Runner        |
|----------|--------------------|---------------|
| Android  | Unit Tests (JVM)   | JUnit via AGP |
| Android  | Instrumented Tests | AndroidX Test |
| iOS      | Unit Tests         | Kotlin/Native |
| Desktop  | Unit Tests         | JUnit         |
| Common   | Shared Tests       | kotlin.test   |

## Test Source Locations

### KMP Android Source Set Layout V2 (Recommended)

| Test Type            | Source Path                                  |
|----------------------|----------------------------------------------|
| Local JVM Unit Tests | `<module>/src/androidUnitTest/kotlin/...`    |
| Instrumented Tests   | `<module>/src/androidAndroidTest/kotlin/...` |
| Common Tests         | `<module>/src/commonTest/kotlin/...`         |

> **Note:** Legacy `src/test/kotlin` is still recognized but emits a deprecation warning. Migrate to
`src/androidUnitTest/kotlin` for new tests.

### Example Paths

```
core/domain/
├── src/
│   ├── commonMain/kotlin/...
│   ├── commonTest/kotlin/...          # Shared tests
│   ├── androidMain/kotlin/...
│   ├── androidUnitTest/kotlin/...     # Android JVM unit tests
│   └── androidAndroidTest/kotlin/...  # Instrumented tests
```

## Running Tests

### Global Test Commands

| Command               | Description                        |
|-----------------------|------------------------------------|
| `.\gradlew.bat test`  | Run all unit tests across modules  |
| `.\gradlew.bat check` | Run tests + lint + static analysis |

### Module-Specific Tests

#### Android JVM Unit Tests

```bash
# Debug variant
.\gradlew.bat :<module>:testDebugUnitTest

# Release variant
.\gradlew.bat :<module>:testReleaseUnitTest

# All variants
.\gradlew.bat :<module>:test
```

**Examples:**

```bash
# Core domain tests
.\gradlew.bat :core:domain:testDebugUnitTest

# Auth presentation tests
.\gradlew.bat :feature:auth:presentation:testDebugUnitTest

# Chat data tests
.\gradlew.bat :feature:chat:data:testDebugUnitTest
```

#### Instrumented Tests (Device/Emulator)

```bash
# Requires connected device or running emulator
.\gradlew.bat :<module>:connectedDebugAndroidTest

# Aggregate for all variants
.\gradlew.bat :<module>:connectedAndroidTest
```

#### iOS Tests (macOS Only)

```bash
# Simulator tests
.\gradlew.bat :<module>:iosSimulatorArm64Test
.\gradlew.bat :<module>:iosX64Test
```

> **Note:** iOS tests only run on macOS with proper Xcode toolchains. On Windows/Linux, these
> targets are disabled at configuration time.

### Running Specific Tests

```bash
# Single test class
.\gradlew.bat :core:domain:testDebugUnitTest --tests "dev.gaddal.core.domain.MyTestClass"

# Single test method
.\gradlew.bat :core:domain:testDebugUnitTest --tests "dev.gaddal.core.domain.MyTestClass.myTestMethod"

# Pattern matching
.\gradlew.bat :core:domain:testDebugUnitTest --tests "*MyTest*"
```

## Test Dependencies

### Provided by KMP Library Convention

The `dev.gaddal.convention.kmp.library` plugin automatically adds:

```kotlin
commonTestImplementation(kotlin("test"))
```

This provides:

- `kotlin.test` assertions (`assertEquals`, `assertTrue`, etc.)
- Platform-specific test runners (JUnit on Android/JVM)

### Additional Dependencies (When Needed)

| Dependency                | Purpose                   | Catalog Alias                  |
|---------------------------|---------------------------|--------------------------------|
| `junit:junit`             | Pure JUnit features       | `libs.junit`                   |
| `androidx.test:core`      | Android test utilities    | `libs.androidx.test.core`      |
| `androidx.test:runner`    | Android test runner       | `libs.androidx.runner`         |
| `androidx.test.ext:junit` | AndroidX JUnit extensions | `libs.androidx.junit`          |
| `kotlinx-coroutines-test` | Coroutine testing         | `libs.kotlinx.coroutines.test` |

## Writing Tests

### Basic Unit Test Example

```kotlin
package dev.gaddal.core.domain

import org.junit.Test
import kotlin.test.assertEquals

class MathTestJvm {
    @Test
    fun `addition works correctly`() {
        assertEquals(4, 2 + 2)
    }
}
```

**Location:** `core/domain/src/androidUnitTest/kotlin/dev/gaddal/core/domain/MathTestJvm.kt`

### Common Test Example (Shared)

```kotlin
package dev.gaddal.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SharedMathTest {
    @Test
    fun additionWorksCorrectly() {
        assertEquals(4, 2 + 2)
    }
}
```

**Location:** `core/domain/src/commonTest/kotlin/dev/gaddal/core/domain/SharedMathTest.kt`

### Coroutine Test Example

```kotlin
package dev.gaddal.feature.chat.data

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RepositoryTest {
    @Test
    fun `fetch data returns expected result`() = runTest {
        val repository = MyRepository()
        val result = repository.fetchData()
        assertEquals(expected, result)
    }
}
```

## Troubleshooting

### Common Issues

| Issue                                                            | Cause              | Solution                                         |
|------------------------------------------------------------------|--------------------|--------------------------------------------------|
| `Cannot locate tasks that match ':<module>:testAndroidHostTest'` | Wrong task name    | Use `testDebugUnitTest` or `testReleaseUnitTest` |
| Deprecation warning for `src/test/kotlin`                        | Legacy source set  | Move tests to `src/androidUnitTest/kotlin`       |
| iOS tests not running                                            | Non-macOS host     | Expected behavior; iOS tests require macOS       |
| `kotlinx.coroutines.ExperimentalCoroutinesApi is unresolved`     | Missing dependency | Add `kotlinx-coroutines-test` dependency         |

### Suppressing Deprecation Warning

To temporarily suppress the source set layout warning, add to `gradle.properties`:

```properties
kotlin.mpp.androidSourceSetLayoutV2AndroidStyleDirs.nowarn=true
```

> **Recommendation:** Migrate to the new layout instead of suppressing.

### Test Reports

Test reports are generated at:

```
<module>/build/reports/tests/testDebugUnitTest/index.html
```

### Debugging Tests

Add logging to tests for debugging:

```kotlin
@Test
fun myTest() {
    println("[DEBUG_LOG] Starting test...")
    // test code
    println("[DEBUG_LOG] Result: $result")
}
```

## Best Practices

1. **Use `commonTest` for shared logic** — Write platform-agnostic tests in `commonTest` when
   possible
2. **Prefer `androidUnitTest` over `test`** — Align with KMP Android source set layout V2
3. **Keep tests focused** — One assertion per test when practical
4. **Use descriptive names** — Test names should describe the scenario and expected outcome
5. **Mock external dependencies** — Use fakes or mocks for network, database, and platform services
6. **Run tests before PRs** — Ensure all tests pass locally before opening pull requests
