# Bear Robotics Code Test Assignment

This assignment is implemented a simple ATM controller.

## Before you go
This project skips implementing CURD operation.

## Prerequisites
- Java 11
- IntelliJ IDEA

## Stack
- IDE
  - IntelliJ IDEA
- Langauge
  - Kotlin
- Framework
  - Spring Boot
  - Spring JPA
- Database
  - H2 Database
- Test Util
  - JUnit 5
  - Mockk
  - Fixtures
  - Kotest
- Build Tool
  - Gradle

## Controller
- [ATMController](./src/main/kotlin/io/pemassi/bearroboticsassignment/interface/AtmController.kt)
  - insertCard(...): Insert card to ATM
  - enterPinNumber(...): Enter pin number to verify card
  - selectAccount(...): Select account to look through
  - getAccountBalance(...): Get account balance
  - withdraw(...): Withdraw money from account
  - deposit(...): Deposit money to account

## How to run test
```bash
chmod 755 ./gradlew 
./gradlew test
```
> Please make sure env `JAVA_HOME` is correctly set for `Java 11`.

> You cloud find test codes in [here](./src/test/kotlin/io/pemassi/bearroboticsassignment)
