# Genderize.io Test Automation Project

Project is a simple end-to-end test automation project to test the [Genderize.io](https://genderize.io/) API using Cucumber (BDD), Selenium WebDriver, and Java.

## Technologies Used
- Java (JDK 21)
- Maven
- Selenium WebDriver
- Cucumber BDD
- JUnit
- WebDriverManager
- Git

## How It Works
The test simulates a user entering a name into a sample web interface using the Genderize.io API, and verifies the gender predicted.

## Project Directory Structure

- `pages/` → Page Object Model (POM) directory
- `step_definitions/` → Step definitions using Gherkin and Java
- `runners/` → Test runner configurations
- `utilities/` → Helper functions and configurations that can be reused
- `features/` → Gherkin feature files

## Example Scenario

```gherkin
Scenario: Gender prediction validation
  Given Navigate to the home page
  When I enter the name "Alex" into inputbox
  And Click the search button
  Then I should see the predicted gender as "male"