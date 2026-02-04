# API Test Automation - PetStore

Automated API tests for the [Swagger PetStore](https://petstore.swagger.io/) using **RestAssured** and **TestNG**.

---

## Project Structure

```text
api-test-automation/
├── .idea/
├── src/
│   └── test/java/com/diliana/api
│       ├── base/
│       │   └── BaseTest.java
│       ├── enums/
│       │   ├── OrderStatus.java
│       │   └── PetStatus.java
│       ├── tests/
│       │   ├── OrderTests.java
│       │   ├── PetTests.java
│       │   └── UserTests.java
│       └── utils/
│           ├── OrderUtils.java
│           ├── PetUtils.java
│           └── UserUtils.java
├── target/
├── .gitignore
├── setup
└── pom.xml


## Technologies & Dependencies

- **Java 21**
- **Maven**
- **RestAssured** (API automation)
- **TestNG** (testing framework)
- **Log4j2** (logging) (to be used, currently not in use)

---

## Enums

- `PetStatus` – available, sold, pending  
- `OrderStatus` – placed, approved, delivered  

---

## How to Run

1. Clone the repository:
```bash
git clone https://github.com/yourusername/api-test-automation.git
cd api-test-automation


---

## Run all tests with Maven
```bash
mvn clean test

---


## Notes

- The tests use **random available pets** for testing CRUD operations.
- TestNG groups are used:
  - `positive` – valid scenarios
  - `negative` – invalid/error scenarios
- All request payloads are handled via utility classes (`PetUtils`, `OrderUtils`, `UserUtils`) for reusability.
- Enums (`PetStatus`, `OrderStatus`) are used for consistent status values.

---

## Example Test Cases

- **PetTests.java**
  - Create, get, update, and delete pets
  - Fetch random available pet
  - Validate negative scenarios (deleted or invalid pets)

- **OrderTests.java**
  - Create, get, and delete orders
  - Validate order status
  - Handle non-existing orders

- **UserTests.java**
  - Create, get, and delete users
  - Validate negative scenarios (non-existing users)

---

## Logging (not currently, to be used)

- All test activities are logged using **Log4j2**. 
- Logs include created IDs, API responses, and test results.

---

## Contact

Diliana Todorova – [detcheva2812@gmail.com]
