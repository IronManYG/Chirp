# Contributing

Thank you for your interest in contributing to Chirp! This guide will help you get started.

## Table of Contents

- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Code Style](#code-style)
- [Pull Request Process](#pull-request-process)
- [Build Health Checks](#build-health-checks)

## Getting Started

### Prerequisites

Before contributing, ensure you have:

| Requirement    | Version | Notes                           |
|----------------|---------|---------------------------------|
| JDK            | 17      | Required for Gradle and IDE     |
| Android Studio | Latest  | With Android SDK Platform 36    |
| Xcode          | Latest  | macOS only, for iOS development |

### Setup

1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/Chirp.git
   ```
3. Open in Android Studio or IntelliJ IDEA with KMP support
4. Let Gradle sync and download dependencies
5. Read `.junie/guidelines.md` for detailed build and testing notes

## Development Workflow

### Branch Naming

Use descriptive branch names:

| Type     | Pattern                | Example                      |
|----------|------------------------|------------------------------|
| Feature  | `feature/description`  | `feature/add-voice-messages` |
| Bug Fix  | `fix/description`      | `fix/login-crash`            |
| Refactor | `refactor/description` | `refactor/auth-module`       |
| Docs     | `docs/description`     | `docs/update-readme`         |

### Commit Messages

Write clear, descriptive commit messages:

```
type(scope): brief description

- Detail 1
- Detail 2
```

**Types:** `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

**Examples:**

```
feat(auth): add forgot password flow
fix(chat): resolve message ordering issue
docs(readme): update installation instructions
```

## Code Style

### General Guidelines

- Follow existing code patterns in the codebase
- Use Kotlin idioms and best practices
- Keep functions focused and small
- Write self-documenting code with clear naming

### Kotlin Style

- Use `camelCase` for functions and variables
- Use `PascalCase` for classes and interfaces
- Use `SCREAMING_SNAKE_CASE` for constants
- Prefer immutability (`val` over `var`)

### Compose Guidelines

- Keep Composables small and focused
- Extract reusable components to the design system
- Use `remember` and `derivedStateOf` appropriately
- Follow Material Design 3 guidelines

### Resource Naming

| Resource Type | Convention   | Example             |
|---------------|--------------|---------------------|
| Strings       | `snake_case` | `login_button_text` |
| Drawables     | `snake_case` | `ic_send_message`   |
| Colors        | `snake_case` | `primary_container` |

## Pull Request Process

### Before Opening a PR

1. **Sync with main:**
   ```bash
   git fetch origin
   git rebase origin/main
   ```

2. **Run build health checks** (see below)

3. **Ensure tests pass:**
   ```bash
   .\gradlew.bat test
   ```

4. **Run lint:**
   ```bash
   .\gradlew.bat :composeApp:lint
   ```

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Manual testing performed

## Checklist
- [ ] Code follows project style
- [ ] Self-review completed
- [ ] Documentation updated (if needed)
- [ ] No new warnings introduced
```

### Review Process

1. Open a PR against `main`
2. Fill out the PR template
3. Request review from maintainers
4. Address feedback and update as needed
5. Squash and merge when approved

## Build Health Checks

Run these commands before opening a PR:

### Full Build

```bash
# Windows
.\gradlew.bat build

# macOS/Linux
./gradlew build
```

### Lint (Android)

```bash
# Windows
.\gradlew.bat :composeApp:lint

# With auto-fix
.\gradlew.bat :composeApp:lintFix
```

### Unit Tests

```bash
# All modules
.\gradlew.bat test

# Specific module (debug)
.\gradlew.bat :core:domain:testDebugUnitTest
```

### Quick Validation

```bash
# Windows - build + test + lint
.\gradlew.bat build; .\gradlew.bat :composeApp:lint
```

## Additional Resources

| Resource                          | Description          |
|-----------------------------------|----------------------|
| [Installation](installation.md)   | Environment setup    |
| [Architecture](architecture.md)   | Project architecture |
| [Testing](testing.md)             | Testing guidelines   |
| [Configuration](configuration.md) | Build configuration  |

## Questions?

If you have questions or need help:

1. Check existing documentation in `docs/`
2. Review `.junie/guidelines.md` for detailed technical notes
3. Open an issue for discussion

---

**Thank you for contributing to Chirp!** 🎉
