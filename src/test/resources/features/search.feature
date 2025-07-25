@ui
Feature: Genderize Name Prediction

  Scenario Outline: Predict gender for given names
    Given Navigate to the home page
    When I enter the name "<name>" into inputbox
    And Click the search button
    Then I should see the predicted gender as "<gender>"

    Examples:
      | name  | gender |
      | Peter | male   |
      | Maria | female |
      | Alex  | male   |