@api
Feature: Genderize API tests

  Scenario: Predict the gender of a single name
    Given I send a request with name "Maria"
    Then The gender should be "female"

  Scenario: Predict the gender of a name within a specific country
    Given I send a request with name "Ahmet" and country code "TR"
    Then The gender should be "male"

  Scenario: Predict the gender of multiple names
    Given I send a request with names "Andrea, Judith, John"
    Then I should get 3 results

  Scenario Outline: Predict gender for various names
    Given I send a request with name "<name>"
    Then The gender should be "<gender>"

    Examples:
      | name  | gender |
      | Maria | female |
      | John  | male   |
      | Fatma | female |

  Scenario: Handle edge case - empty name
    Given I send a request with name ""
    Then The API should return status code 200
