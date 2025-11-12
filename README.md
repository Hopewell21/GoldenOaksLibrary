# Golden Oaks Library System (GOLS)

A comprehensive Android library management application built with Kotlin, Jetpack Compose, and modern Android architecture patterns.

## 📱 Overview

Golden Oaks Library System is a full-featured library management application designed to replace manual cataloging, lending, returns, fines, and reporting with a digital system. The app provides real-time book availability, automated fine calculation, and comprehensive reporting capabilities.

## ✨ Features

### 🔐 Authentication & Authorization
- Secure login with email/password
- Role-based access control (Patron, Librarian, Admin)
- BCrypt password hashing
- User registration support

### 📚 Catalog Management
- Search books by title, author, genre, or ISBN
- Add, update, and delete books
- View real-time availability (available/total copies)
- Book details with comprehensive information

### 👥 Member Management
- Register, update, and remove library members
- Search members by name or email
- Member status tracking (Active, Inactive, Suspended)
- Contact information management

### 📖 Transaction Management
- Issue books with automatic due date calculation (default: 14 days)
- Return books with automatic status updates
- View active loans and loan history
- Overdue loan tracking

### 💰 Fines Management
- Automatic fine calculation (R5 per day overdue)
- View fines by member
- Pay or waive fines
- Fine status tracking (Pending, Paid, Waived)

### 📊 Reporting
- **Activity Report**: Total loans, active loans, returned loans, overdue loans
- **Inventory Report**: Books by genre, copy status, availability statistics
- Date range filtering support

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture with the following components:

- **UI Layer**: Jetpack Compose with Material 3
- **ViewModel Layer**: State management with StateFlow
- **Repository Layer**: Data abstraction and business logic
- **Data Layer**: Room database with DAOs
- **Dependency Injection**: Hilt (Dagger)

### Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM
- **Dependency Injection**: Hilt
- **Database**: Room (SQLite)
- **Navigation**: Navigation Compose
- **Async Operations**: Kotlin Coroutines & Flow
- **Build System**: Gradle with KSP

## 📋 Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or higher
- Android SDK 24 (minimum) to 36 (target)
- Gradle 8.13.1 or compatible

## 🚀 Getting Started

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/GoldenOaksLibrary.git
cd GoldenOaksLibrary
```

2. Open the project in Android Studio

3. Sync Gradle files and wait for dependencies to download

4. Run the app on an emulator or physical device

### Default Credentials

The app comes with pre-configured users for testing:

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@golden-oaks.org` | `admin123` |
| Librarian | `librarian@golden-oaks.org` | `librarian123` |
| Patron | `patron@golden-oaks.org` | `patron123` |

### Sample Data

The app automatically initializes with:
- 3 sample books with 2 copies each
- 1 sample member (John Dlamini)
- Pre-configured user accounts

## 📁 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/goldenoaks/library/
│   │   │   ├── data/
│   │   │   │   ├── dao/              # Data Access Objects
│   │   │   │   ├── database/         # Room database & converters
│   │   │   │   ├── model/            # Entity classes
│   │   │   │   └── repository/      # Repository implementations
│   │   │   ├── di/                   # Hilt dependency injection modules
│   │   │   ├── navigation/           # Navigation graph
│   │   │   ├── ui/
│   │   │   │   ├── screen/          # Compose screens
│   │   │   │   │   ├── auth/         # Login screen
│   │   │   │   │   ├── catalog/      # Catalog management
│   │   │   │   │   ├── fines/        # Fines management
│   │   │   │   │   ├── home/         # Home dashboard
│   │   │   │   │   ├── members/      # Member management
│   │   │   │   │   ├── reports/      # Reports
│   │   │   │   │   └── transactions/  # Transaction management
│   │   │   │   ├── theme/            # Material 3 theme
│   │   │   │   └── viewmodel/        # ViewModels
│   │   │   └── util/                # Utility classes
│   │   └── res/                      # Resources
│   └── test/                         # Unit tests
└── build.gradle.kts
```

## 🧪 Testing

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run tests with coverage
./gradlew testDebugUnitTestCoverage
```

### Test Structure

- **Unit Tests**: ViewModel logic, repository methods, utility functions
- **Instrumented Tests**: UI tests, database operations, integration tests

## 🔧 Configuration

### Database

The app uses Room database with the following tables:
- `users` - User accounts and authentication
- `members` - Library members
- `books` - Book catalog
- `copies` - Physical book copies
- `loans` - Book lending transactions
- `fines` - Fine records

### Fine Calculation

Default fine rate: **R5.00 per day** (configurable in `FineCalculator.kt`)

### Loan Duration

Default loan period: **14 days** (configurable in `TransactionViewModel.kt`)

## 📦 Dependencies

Key dependencies include:
- **Jetpack Compose**: UI framework
- **Room**: Local database
- **Hilt**: Dependency injection
- **Navigation Compose**: Navigation
- **Coroutines & Flow**: Asynchronous operations
- **Material 3**: Design system
- **BCrypt**: Password hashing

See `gradle/libs.versions.toml` for complete dependency list.

## 🚢 Building for Production

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

The release APK will be located at:
```
app/build/outputs/apk/release/app-release.apk
```

### Signing

Configure signing in `app/build.gradle.kts`:
```kotlin
signingConfigs {
    create("release") {
        storeFile = file("path/to/keystore.jks")
        storePassword = "your-store-password"
        keyAlias = "your-key-alias"
        keyPassword = "your-key-password"
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **Development Team** - Golden Oaks Library System

## 🙏 Acknowledgments

- Material Design 3 for the design system
- Android Jetpack team for excellent libraries
- Room database for local data persistence
- Hilt team for dependency injection framework

## 📞 Support

For issues, questions, or contributions, please open an issue on the GitHub repository.

## 🔄 Version History

- **v1.0.0** (Current)
  - Initial release
  - Complete library management features
  - Authentication and authorization
  - Reporting capabilities
  - Fine calculation system

---

**Built with ❤️ using Kotlin and Jetpack Compose**

