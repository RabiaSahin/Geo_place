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

## What This Project Covers

The project consists of *two main tasks*:

### Task 1: UI Automation

- *Test tool*: Selenium WebDriver
- *BDD Framework*: Cucumber
- *Test goal*: Input a name into the search bar on the UI and verify that the predicted gender appears correctly.
- *Browser*: Chrome
- *Utilities*:
    - Reusable Driver class
    - Custom Hooks for setup and teardown
    - Dynamic waits and assertions

### ✅ Task 2: API Automation

- *API*: https://api.genderize.io
- *Test tool*: RestAssured
- *Test cases include*:
    - Sending GET requests with names and country codes
    - Verifying response status codes
    - Asserting predicted genders
    - Handling multiple name queries


## How It Works
The test simulates a user entering a name into a sample web interface using the Genderize.io API, and verifies the gender predicted.

## Running Specific Features
- This project contains two separate feature sets: **search** and **api**.

To run these features, you need to check and update the tag in the Cukes Runner class accordingly:

- To run the **search** feature, set the tag in the Runner class to:

    @CucumberOptions(tags = "@search")

- To run the **api** feature, set the tag to:

  @CucumberOptions(tags = "@api")

## Note on API Feature
This API returns HTTP status code 200 for all requests, including those with invalid inputs that would typically return 4xx errors.
Therefore, scenarios expecting 4xx errors are implemented by checking warning or error messages in the response body instead of status codes.

Note: Genderize.io API supports optional API keys. Since the task did not require testing
for API authentication and the API functions correctly without a key, no tests were included
for missing or invalid API keys.


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

